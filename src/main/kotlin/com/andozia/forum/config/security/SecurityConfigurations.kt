package com.andozia.forum.config.security

import com.andozia.forum.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@EnableWebSecurity
@Configuration
class SecurityConfigurations(
        private val authService: AutenticacaoService,
        private val tokenService: TokenService,
        private val userRepository: UserRepository
) : WebSecurityConfigurerAdapter() {

    //isso precisa ser injetado em uma classe que extende WebSecurityConfigurerAdapter antes
    //de poder ser injetado no controller. O controller nao sabe criar esta classe
    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    //autenticacao
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(authService)?.passwordEncoder(BCryptPasswordEncoder())
    }

    //autorizacao
    override fun configure(http: HttpSecurity?) {
        http?.run {
            authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/topicos").permitAll()
                    .antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
                    .antMatchers(HttpMethod.POST, "/auth").permitAll()
                    .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                    .anyRequest().authenticated()
                    .and().csrf().disable() //nececssario para usar o jwt. Se nao o spring vai tentar validar os tokens
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().addFilterBefore(AutenticacaoViaTokenFilter(tokenService, userRepository), UsernamePasswordAuthenticationFilter::class.java)

        }
    }

    //arquivos estaticos
    override fun configure(web: WebSecurity?) {

    }

}
