package uzuzjmd.competence.liferay.util;

import java.io.IOException;

import javax.faces.context.FacesContext;

import com.liferay.faces.portal.context.LiferayFacesContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * This class provides the necessary context information only liferay can provide.
 * It is a wrapper for some Liferay-API calls.
 * @author dehne
 *
 */
public class ContextUtil {
	/**
	 * Get the user currently logged in
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static User getUserLoggedIn() throws PortalException,
			SystemException {
		
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
	

	/**
	 * Get the scope group the portlet runs in.
	 * 
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static Group getGroup() throws PortalException, SystemException {
		LiferayFacesContext liferayFacesContext = LiferayFacesContext
				.getInstance();
		ServiceContext serviceContext = liferayFacesContext.getServiceContext();
		return serviceContext.getScopeGroup();

	}
}
