package study.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import study.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepositories;

	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepositories) {
		this.userRepositories = userRepositories;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		if (userRepositories.findOne(username) == null)
			throw new UsernameNotFoundException("找不到该用户");

		return userRepositories.findOne(username);
	}

}