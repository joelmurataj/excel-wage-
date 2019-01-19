package com.project.utility;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Message {
	private Message() {}

	public final static ResourceBundle bundle = ResourceBundle.getBundle("com.project.message");

	 public static void addMessage(String summary, String type) {

			FacesMessage message = new FacesMessage(summary, null);
			setSeverity(message, type);
			requestContext().addMessage(null, message);
		}

		public static void addFlushMessage(String summary, String type) {
			addMessage(summary, type);
			requestContext().getExternalContext().getFlash().setKeepMessages(true);
		}

		private static FacesMessage setSeverity(FacesMessage message, String type) {
			if (type == "info") {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
			} else if (type == "error") {
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
			} else if (type == "warn") {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
			} else if (type == "fatal") {
				message.setSeverity(FacesMessage.SEVERITY_FATAL);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
			}
			return message;
		}

		private static FacesContext requestContext() {
			return FacesContext.getCurrentInstance();
		}
}
