package de.unipotsdam.anh.util;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.util.portlet.PortletProps;

public class AppUtil {
	
	public static String[] getTestUser() {
		final String[] user = {"xunguyen@uni-potsdam.de", "xa8641933"};
		return user;
	}
	
	public static String getBaseUrl() {
//		try {
//			String competenceRestServerUrl = GetterUtil.getString(PortletProps.get("competenceRestServerUrl"));
//			return competenceRestServerUrl;
//		} catch (Exception ex) {
//			System.err.println(ex);
//			return "http://fleckenroller.cs.uni-potsdam.de/app/competence-database/competence";
//		}
		
		return "http://localhost:8084";
	}
	
	public static User getUserLoggedIn() throws PortalException, SystemException {

		User user = UserLocalServiceUtil.getUser(PrincipalThreadLocal.getUserId());
		
		if (user == null) {			
			FacesContext fc = FacesContext.getCurrentInstance();
			try {
				fc.getExternalContext().redirect("/");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		return user;
	}
	
	public static boolean validateNotEmptyString(String message, String ... validStrings) {
		boolean result = true;
		for(String validString : validStrings) {
			if(StringUtils.isEmpty(validString)) {
				result = false;
			}
		}
		if(!result) {
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anfrage nicht ausgeführt!", message));
		}
		return result;
	}
	
	public static boolean validateNotEquals(String message, String str1, String str2) {
		boolean result = true;
		if(StringUtils.equals(str1, str2)) {
			result = false;
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anfrage nicht ausgeführt!", message));
		}
		return result;
	}
	
	public static void showInfo(String title, String message) {
		FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_INFO, title, message));
	}
	
	public static void showError(String title, String message) {
		FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, message));
	}
}
