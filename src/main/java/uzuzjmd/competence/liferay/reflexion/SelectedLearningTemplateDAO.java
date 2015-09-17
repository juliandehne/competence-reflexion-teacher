package uzuzjmd.competence.liferay.reflexion;

import javax.ws.rs.core.MediaType;

import uzuzjmd.competence.liferay.util.ContextUtil;
import uzuzjmd.competence.liferay.util.SOAUtil;
import uzuzjmd.competence.shared.StringList;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

public class SelectedLearningTemplateDAO {
	
	/**
	 * Send the information that the user has selected a learning template for personal
	 * competence reflection to the server
	 * 
	 * @param selectedLearningTemplate
	 */
	public static synchronized void persist(String selectedLearningTemplate) {

		Client client = com.sun.jersey.api.client.Client.create();
		WebResource webResource = client.resource(SOAUtil.getRestserverUrl()
				+ "/competences/xml/learningtemplates/add");
		try {
			try {
				webResource
						.queryParam("userId",
								ContextUtil.getUserLoggedIn().getLogin() + "")
						.queryParam("groupId",
								ContextUtil.getGroup().getGroupId() + "")
						.queryParam("selectedTemplate",
								selectedLearningTemplate)
						.accept(MediaType.APPLICATION_XML).post();
			} catch (PortalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UniformInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.destroy();
		}
	}

	public static synchronized StringList findAll()
			throws UniformInterfaceException, ClientHandlerException,
			PortalException, SystemException {
		Client client = com.sun.jersey.api.client.Client.create();
		WebResource webResource = client.resource(SOAUtil.getRestserverUrl()
				+ "/competences/xml/learningtemplates/selected");
		
		System.out.println("FETCHING FROM_: "+webResource.getURI());

		StringList result = null;
		String user = ContextUtil.getUserLoggedIn().getLogin();
		
		try {

			result = webResource
					.queryParam("userId",
							 user + "")
					.queryParam("groupId",
							ContextUtil.getGroup().getGroupId() + "")
					.accept(MediaType.APPLICATION_XML).get(StringList.class);
		} finally {
			client.destroy();
		}

		return result;
	}

	public static synchronized void delete(String selectedLearningTemplate) {
		Client client = com.sun.jersey.api.client.Client.create();
		WebResource webResource = client.resource(SOAUtil.getRestserverUrl()
				+ "/competences/xml/learningtemplates/delete");
		try {
			try {
				webResource
						.queryParam("userId",
								ContextUtil.getUserLoggedIn().getLogin() + "")
						.queryParam("groupId",
								ContextUtil.getGroup().getGroupId() + "")
						.queryParam("selectedTemplate",
								selectedLearningTemplate)
						.accept(MediaType.APPLICATION_XML).post();
			} catch (PortalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UniformInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.destroy();
		}

	}
}
