package com.project.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import com.project.dto.UserDto;
import com.project.service.UserService;
import com.project.utility.Message;

@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {
	private String username;
	private String password;

	@ManagedProperty(value = "#{userService}")
	UserService userService;

	@ManagedProperty(value = "#{userBean}")
	UserBean userBean;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public String logIn() {
		if (username != null && password != null) {
			UserDto user = userService.exist(username, password);
			if (user != null) {
					userBean.setUserDto(user);
					return "upload?faces-redirect=true";
			}else {
				Message.addMessage(Message.bundle.getString("USER_NOTLOGGED"), "error");
				return "";
			}
		} 
		return"";
	}

	public String logOut() {
		userBean.logOut();
		return "/login.xhtml?faces-redirect=true";
	}

}
