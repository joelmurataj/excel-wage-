package com.project.converters;

import java.util.ArrayList;
import java.util.List;

import org.jasypt.util.password.BasicPasswordEncryptor;

import com.project.dto.UserDto;
import com.project.entity.User;

public class UserConverter {
	private UserConverter() {
	}

	public static User toUser(UserDto userDto) {
		if (userDto != null) {
			User user = new User();
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setUsername(userDto.getUsername());
			BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
			user.setPassword(encryptor.encryptPassword(userDto.getPassword()));
			return user;
		} else {
			return null;
		}

	}

	public static User toEditUser(UserDto userDto) {
		if (userDto != null) {
			User user = new User();
			user.setId(userDto.getId());
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setUsername(userDto.getUsername());
			BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
			user.setPassword(encryptor.encryptPassword(userDto.getPassword()));
			return user;
		} else {
			return null;
		}

	}


	public static UserDto toUserDto(User user) {
		if (user != null) {
			UserDto userDto = new UserDto();
			userDto.setId(user.getId());
			userDto.setFirstName(user.getFirstName());
			userDto.setLastName(user.getLastName());
			userDto.setUsername(user.getUsername());
			userDto.setPassword(user.getPassword());
			return userDto;
		} else {
			return null;
		}

	}

	public static List<UserDto> toUserListDto(List<User> list) {
		ArrayList<UserDto> userDto = new ArrayList<>();
		if (list != null) {
			for (User user : list) {
				userDto.add(toUserDto(user));
			}
			return userDto;
		} else {
			return userDto;
		}
	}

}
