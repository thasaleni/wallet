package com.imsglobal.wallet.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.imsglobal.wallet.dto.JwtResponse;
import com.imsglobal.wallet.model.User;
import com.imsglobal.wallet.service.UserService;


@Controller
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
	
	
    @Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
    @PostMapping(value = "/register")
	public ResponseEntity<? extends Object> register(@RequestBody User user) {
		try {
			user = userService.addUser(user);
		}catch(IllegalArgumentException e) {
			return  new ResponseEntity<String> ("User already exists.", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<User> (user, HttpStatus.CREATED);
	}
    
    @PostMapping(value = "/login")
	public ResponseEntity<? extends Object> login(@RequestBody User user) {
    	if(user == null || (!StringUtils.hasLength(user.getUsername()) ||!StringUtils.hasLength(user.getPassword()))  ) {
    		throw new IllegalArgumentException("username and password are required.");
    	}
    	if(!userService.existsByUsername(user.getUsername())) {
    		return  new ResponseEntity<String> ("User does not exist.", HttpStatus.UNAUTHORIZED);
    	}
		return new ResponseEntity<JwtResponse> (userService.login(user), HttpStatus.CREATED);
	}
}
