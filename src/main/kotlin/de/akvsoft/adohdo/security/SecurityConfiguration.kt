package de.akvsoft.adohdo.security

import de.akvsoft.adohdo.security.oauth2.CustomOAuth2UserService
import de.akvsoft.adohdo.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import de.akvsoft.adohdo.security.oauth2.OAuth2AuthenticationFailureHandler
import de.akvsoft.adohdo.security.oauth2.OAuth2AuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

private const val OAUTH2_BASE_URI = "/oauth2/authorize"
private const val OAUTH2_REDIRECTION_ENDPOINT = "/oauth2/callback/*"

@Configuration
class SecurityConfiguration(
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
    private val oauth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
    private val tokenAuthenticationFilter: TokenAuthenticationFilter,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
        http.cors {}
        http.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.formLogin { it.disable() }
        http.httpBasic { it.disable() }

        http.authorizeHttpRequests {
            it.requestMatchers("/browser/**").permitAll()
            it.requestMatchers("/token/refresh/**").permitAll()
            it.requestMatchers("/", "/error").permitAll()
            it.requestMatchers("/auth/**", "/oauth2/**").permitAll()
            it.requestMatchers("/api/login").permitAll()
            it.anyRequest().authenticated()
        }

        http.oauth2Login {
            it.authorizationEndpoint { endpoint ->
                endpoint.baseUri(OAUTH2_BASE_URI)
                endpoint.authorizationRequestRepository(oauth2AuthorizationRequestRepository)
            }
            it.redirectionEndpoint { endpoint -> endpoint.baseUri(OAUTH2_REDIRECTION_ENDPOINT) }
            it.userInfoEndpoint { endpoint -> endpoint.userService(customOAuth2UserService) }
            it.successHandler(oAuth2AuthenticationSuccessHandler)
            it.failureHandler(oAuth2AuthenticationFailureHandler)
        }

        http.exceptionHandling {
            it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        }

        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager

    @Bean
    fun passwordEncoder(): PasswordEncoder =
        PasswordEncoderFactories.createDelegatingPasswordEncoder()
}