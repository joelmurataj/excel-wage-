package com.project.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.stereotype.Repository;

import com.project.dao.UserDao;
import com.project.entity.User;

@Repository(value = "userDao")
public class UserDaoImpl implements UserDao {

	private static final Logger logger = LogManager.getLogger(UserDaoImpl.class.getName());

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public boolean add(User newUser) {
		try {
			logger.debug("adding user {}.", newUser.getUsername());
			entityManager.persist(newUser);
			logger.debug("user added succesfuly");
			return true;
		} catch (Exception e) {
			logger.error("Error adding user:" + e.getMessage());
			return false;
		}
	}

	@Override
	public User exist(String username, String password) {
		try {
			logger.debug("findind if user exist");
			User user = entityManager
					.createQuery("Select user From User user Where user.username=:username",
							User.class)
					.setParameter("username", username).getSingleResult();
			BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
			if (encryptor.checkPassword(password, user.getPassword())) {
				logger.debug("user exist");
				return user;
			} else {
				logger.debug("user dont exist");
				return null;
			}
		} catch (Exception e) {
			logger.error("error finding user:" + e.getMessage());
			return null;

		}
	}

	@Override
	public User existUsername(String username) {
		try {
			logger.debug("finding user by username");
			User user = entityManager
					.createQuery("Select user From User user Where user.username=:username", User.class)
					.setParameter("username", username).getSingleResult();
			logger.debug("user found");
			return user;
		} catch (Exception e) {
			logger.error("error finding user " + e.getMessage());
			return null;

		}
	}

}
