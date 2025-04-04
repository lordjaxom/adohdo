package de.akvsoft.adohdo.npm

import org.springframework.boot.json.JsonParserFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StreamUtils
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.net.URI
import java.nio.charset.StandardCharsets
import java.util.Properties

/**
 * A `Controller` that redirects to a more precise webjars path that can
 * be handled by the resource resolver.
 */
@RestController
class NpmVersionResolver {

    @GetMapping("/npm/{webjar}")
    fun module(@PathVariable webjar: String): ResponseEntity<Void> {
        val path = findWebJarResourcePath(webjar, "/")
        if (path == null) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/webjars/$path")).build()
    }

    @GetMapping("/npm/{webjar}/{*remainder}")
    fun remainder(@PathVariable webjar: String, @PathVariable remainder: String): ResponseEntity<Void> {
        var webjar = webjar
        var remainder = remainder
        if (webjar.startsWith("@")) {
            val index = remainder.indexOf("/", 1)
            val path = if (index < 0) remainder.substring(1) else remainder.substring(1, index)
            webjar = webjar.substring(1) + "__" + path
            if (index < 0 || index == remainder.length - 1) {
                return module(webjar)
            }
            remainder = remainder.substring(index)
        }
        val path = findWebJarResourcePath(webjar, remainder)
        if (path == null) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/webjars/$path")).build()
    }

    private fun findWebJarResourcePath(webjar: String, path: String): String? {
        if (webjar.isNotEmpty()) {
            val version = version(webjar)
            if (version != null) {
                val partialPath = path(webjar, version, path)
                if (partialPath != null) {
                    val webJarPath = "$webjar/$version$partialPath"
                    return webJarPath
                }
            }
        }
        return null
    }

    private fun path(webjar: String?, version: String?, path: String): String? {
        if (path == "/") {
            val module = module(webjar, version, path)
            if (module != null) {
                return module
            } else {
                return null
            }
        }
        if (path == "/main.js") {
            val module = module(webjar, version, path)
            if (module != null) {
                return module
            }
        }
        if (ClassPathResource("$RESOURCE_ROOT$webjar/$version$path").isReadable) {
            return path
        }
        return null
    }

    private fun module(webjar: String?, version: String?, path: String): String? {
        val resource: Resource = ClassPathResource("$RESOURCE_ROOT$webjar/$version$PACKAGE_JSON")
        if (resource.isReadable) {
            try {
                val parser = JsonParserFactory.getJsonParser()
                val map = parser
                    .parseMap(StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8))
                if (path != "/main.js" && map.containsKey("module")) {
                    return "/" + map.get("module") as String?
                }
                if (!map.containsKey("main") && map.containsKey("jspm")) {
                    val stem = resolve(map, "jspm.directories.lib", "dist")
                    val main = resolve(map, "jspm.main", "index.js")
                    return "/" + stem + "/" + main + (if (main.endsWith(".js")) "" else ".js")
                }
                return "/" + map["main"] as String?
            } catch (_: IOException) {
            }
        }
        return null
    }

    private fun version(webjar: String?): String? {
        var resource: Resource = ClassPathResource(PROPERTIES_ROOT + NPM + webjar + POM_PROPERTIES)
        if (!resource.isReadable) {
            resource = ClassPathResource(PROPERTIES_ROOT + PLAIN + webjar + POM_PROPERTIES)
        }
        if (resource.isReadable) {
            val properties: Properties?
            try {
                properties = PropertiesLoaderUtils.loadProperties(resource)
                return properties.getProperty("version")
            } catch (_: IOException) {
            }
        }
        return null
    }

    companion object {

        private const val PROPERTIES_ROOT = "META-INF/maven/"
        private const val RESOURCE_ROOT = "META-INF/resources/webjars/"
        private const val NPM = "org.webjars.npm/"
        private const val PLAIN = "org.webjars/"
        private const val POM_PROPERTIES = "/pom.properties"
        private const val PACKAGE_JSON = "/package.json"

        private fun resolve(map: MutableMap<String?, Any?>?, path: String?, defaultValue: String): String {
            var sub: MutableMap<*, *>? = map
            val elements = StringUtils.delimitedListToStringArray(path, ".")
            for (i in 0..<elements.size - 1) {
                val tmp = sub!![elements[i]] as MutableMap<*, *>?
                sub = tmp
                if (sub == null) {
                    return defaultValue
                }
            }
            return sub!!.getOrDefault(elements[elements.size - 1], defaultValue) as String
        }
    }
}