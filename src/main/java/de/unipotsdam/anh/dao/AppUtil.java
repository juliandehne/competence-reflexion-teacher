package de.unipotsdam.anh.dao;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.util.portlet.PortletProps;

public class AppUtil {

	public static String getBaseUrl() {
		try {
			String competenceRestServerUrl = GetterUtil.getString(PortletProps
					.get("competenceRestServerUrl"));
			return competenceRestServerUrl;
		} catch (Exception ex) {
			System.err.println(ex);
			return "http://fleckenroller.cs.uni-potsdam.de/app/competence-servlet/competence";
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getBaseUrl());
	}
}
