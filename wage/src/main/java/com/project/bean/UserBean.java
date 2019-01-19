package com.project.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.project.dto.UserDto;

@ManagedBean(name="userBean")
@SessionScoped
public class UserBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserDto userDto;
	
	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	public void logOut() {
		userDto = null;
	}
	public boolean isManager() {
		return userDto.getRoliId()==1;
	}

	public boolean isEmployee() {
		return userDto.getRoliId()==2;
	}
	
}
