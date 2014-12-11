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
	 * TODO 暂时禁用 防止csrf 的安全措施，寻求更好的解决方案
	 * 
	 * 有更好的解决方案的原因是，当我没有更改 Spring Boot 的默认配置时，我没有遇到 在移动端因为防止csrf而不让我访问
	 * 
	 * 当 后台与移动终端 启用双向认证后，basic auth的安全性就可以得到保障，而且csrf的问题
	 * 也可以得到解决，在开发过程中，这个双向认证不启用
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/user/add")
				.permitAll().anyRequest().authenticated().and().httpBasic();
	}
}
