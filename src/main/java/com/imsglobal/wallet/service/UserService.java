package com.imsglobal.wallet.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.imsglobal.wallet.dto.JwtResponse;
import com.imsglobal.wallet.model.Account;
import com.imsglobal.wallet.model.Role;
import com.imsglobal.wallet.model.RoleType;
import com.imsglobal.wallet.model.User;
import com.imsglobal.wallet.repository.RoleRepository;
import com.imsglobal.wallet.repository.UserRepository;
import com.imsglobal.wallet.security.JwtUtils;

@Service
public class UserService {
	private UserRepository userRepository;
	private AuthenticationManager authenticationManager;
	private RoleRepository roleRepository;
	private PasswordEncoder encoder;
	private JwtUtils jwtUtils;
	private WalletService walletService;

	UserService(UserRepository userRepository, AuthenticationManager authenticationManager,
			RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, WalletService walletService) {
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.roleRepository = roleRepository;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
		this.walletService = walletService;
	}

//	public List<User> getUser(String username) {
////		return userRepository;
//	}
	@Transactional(propagation = Propagation.REQUIRED)
	public User addUser(User user) {
		User created = null;
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new IllegalArgumentException("User already exists");
		} else {
			user.setPassword(encoder.encode(user.getPassword()));
			Optional<Role> userOption = roleRepository.findByType(RoleType.ROLE_USER);
			if (userOption.isEmpty()) {
				Role role = roleRepository.save(new Role(RoleType.ROLE_USER));
				user.setRoles(Collections.singleton(role));

			} else {
				user.setRoles(Collections.singleton(userOption.get()));
			}
			Account account = new Account(user, 0.0);
			user.setAccount(account);
			created = userRepository.save(user);
		}
		return created;

	}

	
	public JwtResponse login(User user) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(),
				user.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		User userDetails = (User) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);
	}

	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

}
