package com.project.service;

import com.project.dto.UserDto;

public interface UserService {

	public boolean add(UserDto user);
	public UserDto existUsername( String username);
	
	public UserDto exist(String username, String password);
}
