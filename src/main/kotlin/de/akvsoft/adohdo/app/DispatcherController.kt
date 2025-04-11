package de.akvsoft.adohdo.app

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

private const val APP_ROOT_PATH = "/browser"
private const val APP_INDEX_PATH = "$APP_ROOT_PATH/index.html"

@Controller
class DispatcherController {

    @GetMapping("/", APP_ROOT_PATH, "$APP_ROOT_PATH/")
    fun redirectToApp() = "redirect:$APP_INDEX_PATH"

    @GetMapping("$APP_ROOT_PATH/**/{path:[^.]+}")
    fun forwardToRouter(@PathVariable path: String) = "forward:$APP_INDEX_PATH"
}