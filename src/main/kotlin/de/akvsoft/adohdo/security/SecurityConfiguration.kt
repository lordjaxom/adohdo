package de.akvsoft.adohdo.security

import de.akvsoft.adohdo.security.oauth2.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.endpoint.RestClientAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain
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
    private val clientRegistrationRepository: ClientRegistrationRepository,
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
            it.anyRequest().authenticated()
        }

        http.oauth2Login {
            it.authorizationEndpoint { endpoint ->
                endpoint.baseUri(OAUTH2_BASE_URI)
                endpoint.authorizationRequestRepository(oauth2AuthorizationRequestRepository)
                endpoint.authorizationRequestResolver(CustomAuthorizationRequestResolver(clientRegistrationRepository, OAUTH2_BASE_URI))
            }
            it.redirectionEndpoint { endpoint -> endpoint.baseUri(OAUTH2_REDIRECTION_ENDPOINT) }
            it.userInfoEndpoint { endpoint -> endpoint.userService(customOAuth2UserService) }
            it.tokenEndpoint { endpoint -> endpoint.accessTokenResponseClient(RestClientAuthorizationCodeTokenResponseClient()) }
            it.successHandler(oAuth2AuthenticationSuccessHandler)
            it.failureHandler(oAuth2AuthenticationFailureHandler)
        }

        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}