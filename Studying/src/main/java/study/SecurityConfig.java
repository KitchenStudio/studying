package study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 安全相关的配置放在这里 这里修改了 Spring Boot 的默认配置，使用 DetailsService 提供的用户服务
 * 
 * @author seal
 *
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth,
			UserDetailsService detailsService, BCryptPasswordEncoder encoder)
			throws Exception {
		auth.userDetailsService(detailsService).passwordEncoder(encoder);
	}

	/**
	 * 
	 * 对匹配的 路径 禁用 csrf，启用httpbasic
	 * 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/**").authenticated()
				.and().httpBasic();
	}
}
