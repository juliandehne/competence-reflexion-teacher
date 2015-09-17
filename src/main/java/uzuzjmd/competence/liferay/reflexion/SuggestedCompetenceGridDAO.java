package uzuzjmd.competence.liferay.reflexion;

import javax.ws.rs.core.MediaType;

import uzuzjmd.competence.liferay.util.ContextUtil;
import uzuzjmd.competence.liferay.util.SOAUtil;
import uzuzjmd.competence.shared.ReflectiveAssessmentsListHolder;
import uzuzjmd.competence.shared.SuggestedCompetenceGrid;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

public class SuggestedCompetenceGridDAO {


	/**
	 * Given a learningTemplate this returns a paintable competence grid format.
	 * TODO extract API Paths Strings in static variables
	 * 
	 * @param selectedLearningTemplate
	 * @return
	 */
	public static synchronized SuggestedCompetenceGrid getGrid(
			String selectedLearningTemplate) {
		
		Client client = com.sun.jersey.api.client.Client.create();
		WebResource webResource = client.resource(SOAUtil.getRestserverUrl()
				+ "/competences/xml/learningtemplates/gridview");
		SuggestedCompetenceGrid result = null;
		try {
			result = webResource
					.queryParam("userId",
							ContextUtil.getUserLoggedIn().getLogin() + "")
					.queryParam("groupId",
							ContextUtil.getGroup().getGroupId() + "")
					.queryParam("selectedTemplate", selectedLearningTemplate)
					.accept(MediaType.APPLICATION_XML)
					.get(SuggestedCompetenceGrid.class);
		} catch (UniformInterfaceException e) {
			e.printStackTrace();
		} catch (ClientHandlerException e) {
			e.printStackTrace();
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
		return result;
	}

	/**
	 * Sends user interaction to the server
	 * @param holder
	 */
	public static synchronized void updateReflexion(
			ReflectiveAssessmentsListHolder holder) {
		System.out.println("updating: " + holder.getSuggestedMetaCompetence());

		Client client = com.sun.jersey.api.client.Client.create();
		WebResource webResource = client.resource(SOAUtil.getRestserverUrl()
				+ "/competences/xml/learningtemplates/gridview/update");
		try {
			webResource
					.queryParam("userId",
							ContextUtil.getUserLoggedIn().getLogin() + "")
					.queryParam("groupId",
							ContextUtil.getGroup().getGroupId() + "")
					.type(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
					.post(ReflectiveAssessmentsListHolder.class, holder);
		} catch (UniformInterfaceException e) {
			e.printStackTrace();
		} catch (ClientHandlerException e) {
			e.printStackTrace();
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		} finally {
			client.destroy();
		}
	}
}
