package net.bonsamigos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public AppUserDetailsService userDetailsService() {
		return new AppUserDetailsService();
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(new Md5PasswordEncoder());
		return authProvider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JsfLoginUrlAuthenticationEntryPoint jsfLoginEntry = new JsfLoginUrlAuthenticationEntryPoint();
		jsfLoginEntry.setLoginFormUrl("/login.xhtml");
		jsfLoginEntry.setRedirectStrategy(new JsfRedirectStrategy());

		JsfAccessDeniedHandler jsfDeniedEntry = new JsfAccessDeniedHandler();
		jsfDeniedEntry.setLoginPath("/error/access.xhtml");
		jsfDeniedEntry.setContextRelative(true);

		http
				.csrf().disable()
				.headers().frameOptions().sameOrigin()
				.and()

				.authorizeRequests()
				.antMatchers("/login.xhtml", "/error/**", "/javax.faces.resource/**").permitAll()
				.antMatchers("/conversationScoped/**").authenticated()
				.antMatchers("/pedido/**").hasRole("PEDIDO")
				.antMatchers("/usuario/**").hasRole("USUARIO")
				.antMatchers("/perfil/**").hasRole("PERFIL")
				.antMatchers("/unidade/**").hasRole("UNIDADE")
				.antMatchers("/produto/**").hasRole("PRODUTO")

				.and()
				
				.formLogin()
					.loginPage("/login.xhtml")
					.failureUrl("/login.xhtml?invalid=true")
					.and()

				.logout()
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.and()

				.exceptionHandling()
					.accessDeniedPage("/error/access")
					.authenticationEntryPoint(jsfLoginEntry)
					.accessDeniedHandler(jsfDeniedEntry);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.authenticationProvider(authProvider());

	}

}