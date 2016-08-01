package uzuzjmd.competence.reflexion.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.filter.LoggingFilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uzuzjmd.competence.reflexion.util.AppUtil;
import uzuzjmd.competence.reflexion.util.Label;
import uzuzjmd.competence.reflexion.util.RequestBuilder;
import datastructures.lists.StringList;
import uzuzjmd.competence.shared.learningtemplate.SuggestedCompetenceGrid;
import datastructures.graph.Graph;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSet;

public class LearningTemplateDao {

	private static final LoggingFilter loggingFilter = new LoggingFilter(
			Logger.getLogger(LearningTemplateDao.class.getName()), true);

	public static synchronized LearningTemplateResultSet getLearningProjectTemplate(String learningTemplateName) {
		final Client client = ClientBuilder.newClient();

		try {
			final WebTarget webResource = client.target(AppUtil.getBaseUrl()
					+ "/competences/learningtemplate/get/"
					+ learningTemplateName);
			LearningTemplateResultSet result = webResource
					.register(loggingFilter).request(MediaType.APPLICATION_XML)
					.get(LearningTemplateResultSet.class);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}

		return null;
	}

	/**
	 * send new Template to the server
	 * 
	 * @param learningTemplateName
	 */
	public static synchronized int createTemplate(String learningTemplateName) {
		final Client client = ClientBuilder.newClient();
		final WebTarget webResource = client.target(AppUtil.getBaseUrl()
				+ "/competences/learningtemplate/add");
		try {
			LearningTemplateResultSet learningTemplateResultSet = new LearningTemplateResultSet();
			learningTemplateResultSet.setNameOfTheLearningTemplate(learningTemplateName);
			Response response = webResource.register(loggingFilter)
					.queryParam("learningTemplateName", learningTemplateResultSet)
					.request(MediaType.APPLICATION_XML).post(Entity.entity(learningTemplateResultSet,
							MediaType.APPLICATION_XML));
			return response.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}

		return -1;
	}
	
	public static synchronized int deleteTemplate(String learningTemplateName) {
		final Client client = ClientBuilder.newClient();
		final WebTarget webResource = client.target(AppUtil.getBaseUrl()
				+ "/competences/learningtemplate/delete/"+learningTemplateName);
		try {
			Response response = webResource.register(loggingFilter)
					.request(MediaType.APPLICATION_XML).post(null);
			return response.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}

		return -1;
	}

	public static synchronized int createTemplate(LearningTemplateResultSet learningTemplateResultSet) {
		final Client client = ClientBuilder.newClient();

		try {
			final WebTarget webResource = client
					.target(AppUtil.getBaseUrl() + "/competences/learningtemplate/add")
					.register(loggingFilter)
					.queryParam("learningTemplateResultSet",
							learningTemplateResultSet);

			Response result = webResource.request(MediaType.APPLICATION_XML)
					.post(Entity.entity(learningTemplateResultSet,
							MediaType.APPLICATION_XML));

			return result.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}

		return -1;
	}

	/**
	 * add a competence to the model
	 * 
	 * @param competence
	 * @param operator
	 * @param learningTemplate
	 * @param catchWords
	 * @return
	 */
	public static synchronized int createOneCompetence(String competence, String operator, String learningTemplate, String catchWords) {
		final Client client = ClientBuilder.newClient();
		final WebTarget webResource = client.target(AppUtil.getBaseUrl()
				+ "/competences/addOne/");
		Response response = null;
		try {
			response = webResource.register(loggingFilter)
					.queryParam("competence", competence)
					.queryParam("operator", operator)
					.queryParam("catchwords", catchWords)
					.queryParam("learningTemplateName", learningTemplate)
					.request().post(null);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return response.getStatus();

	}
	
	public static synchronized int createOneCompetence(String competence, String operator, String learningTemplate, List<String> catchWords, List<String> superCompetences, List<String> subCompetences) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/addOne/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		return requestBuilder.withUrl(url.toString())
										.withMedienType(MediaType.APPLICATION_JSON_TYPE)
										.addQueryParam("competence", competence)
										.addQueryParam("operator", operator)
										.addQueryParam("catchwords", catchWords)
										.addQueryParam("superCompetences", superCompetences)
										.addQueryParam("subCompetences", subCompetences)
										.addQueryParam("learningTemplateName", learningTemplate)
										.postWithStatus(null);
	}
	
	public static synchronized String getSubCompetence(String rootCompetence) {
		final String context = "university";
		
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/competencetree/")
			.append(context)
			.append("/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		
		return requestBuilder.withUrl(url.toString())
				.withMedienType(MediaType.APPLICATION_XML_TYPE)
				.addQueryParam("rootCompetence", rootCompetence)
				.get(String.class);
	}

	/**
	 * get all Learning template from database
	 * 
	 * @return
	 */
	public static synchronized StringList findAll() {
		final Client client = ClientBuilder.newClient();
		final WebTarget webResource = client.target(AppUtil.getBaseUrl()
				+ "/competences/learningtemplates");
		try {
			StringList response = webResource.register(loggingFilter)
					.request(MediaType.APPLICATION_XML).get(StringList.class);

			return response;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}

		return null;
	}
	
	public static synchronized int rename(String oldName, String newName, Label label) {
		final StringBuilder pathUrl = new StringBuilder("/competences/update/");
		pathUrl.append(label.getLabelString()).append("/")
				.append(oldName).append("/")
				.append(newName).append("/");
		final Client client = ClientBuilder.newClient();
		final WebTarget webResource = client.target(AppUtil.getBaseUrl()
				+ pathUrl.toString());
		try {
			Response response = webResource.register(loggingFilter)
					.request(MediaType.APPLICATION_JSON).post(null);

			return response.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}

		return -1;
	}

	public static synchronized SuggestedCompetenceGrid getGridviewFromLearningTemplate(String selectedTemplate) {
		final Client client = ClientBuilder.newClient();
		final WebTarget webResource = client.target(AppUtil.getBaseUrl() +
								"/competences/learningtemplates/gridview/");
		try {
			final SuggestedCompetenceGrid response = webResource.register(loggingFilter)
					.queryParam("userId", AppUtil.getUserLoggedIn().getLogin() + "")
					.queryParam("groupId", AppUtil.getUserLoggedIn().getGroupId() + "")
					.queryParam("selectedTemplate", selectedTemplate)
					.request(MediaType.APPLICATION_XML).get(SuggestedCompetenceGrid.class);

			return response;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		
		return null;
	}

	public static Object getGraphFromCourse(String course) {
		final Client client = ClientBuilder.newClient();
		final WebTarget webResource = client.target(
				AppUtil.getBaseUrl() + "/competences/prerequisite/graph/")
				.path(course);
		final Gson gson = new GsonBuilder().create();
		try {
			Response response = webResource.register(loggingFilter)
					.request(MediaType.APPLICATION_JSON).get();
			return gson
					.fromJson(response.readEntity(String.class), Graph.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}

		return null;
	}
}
