package de.unipotsdam.anh.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.util.portlet.PortletProps;

public class AppUtil {
	
	public static String getBaseUrl() {
		try {
			String competenceRestServerUrl = GetterUtil.getString(PortletProps.get("competenceRestServerUrl"));
			return competenceRestServerUrl;
		} catch (Exception ex) {
			System.err.println(ex);
			return "http://fleckenroller.cs.uni-potsdam.de/app/competence-servlet/competence";
		}
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
}
