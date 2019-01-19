package com.project.dao;

import com.project.entity.User;

public interface UserDao {

	public boolean add(User user);
	public User existUsername( String username);
	
	public User exist(String username, String password);
	
}
