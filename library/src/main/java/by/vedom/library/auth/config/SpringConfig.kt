package by.vedom.library.auth.config

import by.vedom.library.auth.filter.AuthTokenFilter
import by.vedom.library.auth.filter.ExceptionHandlerFilter
import by.vedom.library.auth.service.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.session.SessionManagementFilter

@Configuration
@EnableWebSecurity //(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = ["by.vedom*"])
@EnableAsync
open class SpringConfig {

    private lateinit var exceptionHandlerFilter: ExceptionHandlerFilter

    private lateinit var authTokenFilter: AuthTokenFilter

    private lateinit var userDetailsService: UserDetailsServiceImpl

    @Autowired
    open fun setExceptionHandlerFilter(exceptionHandlerFilter: ExceptionHandlerFilter) {
        this.exceptionHandlerFilter = exceptionHandlerFilter
    }

    @Autowired
    open fun setAuthTokenFilter(authTokenFilter: AuthTokenFilter) {
        this.authTokenFilter = authTokenFilter
    }

    @Autowired
    open fun setUserDetailsService(userDetailsService: UserDetailsServiceImpl) {
        this.userDetailsService = userDetailsService
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    open fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    open fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    open fun userDetailsService(): UserDetailsService {
        return UserDetailsServiceImpl()
    }

    @Bean
    open fun registration(filter: AuthTokenFilter): FilterRegistrationBean<*> {
        val registration = FilterRegistrationBean(filter)
        registration.isEnabled = false
        return registration
    }

    @Bean
    @Throws(Exception::class)
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.csrf().disable()
        http.formLogin().disable()
        http.httpBasic().disable()
        http.requiresChannel().anyRequest().requiresSecure()

        http.addFilterBefore(
            authTokenFilter,
            SessionManagementFilter::class.java
        )

        http.addFilterBefore(
            exceptionHandlerFilter,
            AuthTokenFilter::class.java
        )
        return http.build()
    }
}