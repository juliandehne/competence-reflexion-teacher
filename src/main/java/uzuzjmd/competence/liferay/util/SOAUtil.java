package uzuzjmd.competence.liferay.util;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.util.portlet.PortletProps;

public class SOAUtil {
	/**
	 * This is a utility class to get the endpoint of the competence server api calls.
	 * The endpoint should be changed in portlet.properties
	 * 
	 * @return
	 */
	public static String getRestserverUrl() {

		try {
			String competenceRestServerUrl = GetterUtil.getString(PortletProps
					.get("competenceRestServerUrl"));
			return competenceRestServerUrl;
		} catch (Exception ex) {
			System.err.println(ex);
			return "http://fleckenroller.cs.uni-potsdam.de/app/competence-servlet/competence";
		}
	}
}
