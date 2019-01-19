package com.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.converters.UserConverter;
import com.project.dao.UserDao;
import com.project.dto.UserDto;
import com.project.entity.User;
import com.project.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	@Transactional
	public boolean add(UserDto userDto) {
		return userDao.add(UserConverter.toUser(userDto));
	}

	
	@Override
	public UserDto exist(String username, String password) {
		User user = userDao.exist(username, password);
		if (user != null) {

			return UserConverter.toUserDto(user);
		} else {
			return null;
		}

	}

	@Override
	public UserDto existUsername(String username) {
		return UserConverter.toUserDto(userDao.existUsername(username));
	}

}
