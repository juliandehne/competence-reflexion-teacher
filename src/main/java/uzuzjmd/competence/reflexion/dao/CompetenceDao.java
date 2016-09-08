package uzuzjmd.competence.reflexion.dao;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.primefaces.model.TreeNode;

import uzuzjmd.competence.reflexion.util.AppUtil;
import uzuzjmd.competence.shared.competence.CompetenceData;
import uzuzjmd.competence.shared.competence.CompetenceXMLTree;
import datastructures.lists.SortedList;
import datastructures.lists.StringList;
import datastructures.trees.HierarchyChange;
import datastructures.trees.HierarchyChangeSet;

public class CompetenceDao {

	public static void addCompetenceStringGiven(String competence,
			String learningTemplate, List<String> catchwords, String operator) {
		CompetenceData data = new CompetenceData();
		data.setCatchwords(catchwords);
		if (catchwords.isEmpty()) {
			catchwords.addAll(Arrays.asList(competence.split(" ")));
		}
		data.setForCompetence(competence);
		data.setLearningProjectName(learningTemplate);
		data.setOperator(catchwords.iterator().next());
		data.setOperator(operator);

		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl());
		url.append("/api1/competences/");
		url.append(competence);

		System.err.println(url.toString());

		Client client = ClientBuilder.newClient();
		Response result = client.target(url.toString())
				.request(MediaType.TEXT_PLAIN)
				.put(Entity.entity(data, MediaType.APPLICATION_XML));
		System.err.println(result.getStatus() + " for addSubCompetence");
	}

	public static void addSubCompetence(String competence, String subCompetence) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl()).append(
				"/api1/competences/hierarchy/update");

		HierarchyChangeSet set = new HierarchyChangeSet();
		HierarchyChange change = new HierarchyChange("Kompetenz", competence,
				subCompetence);
		set.getElements().add(change);
		Client client = ClientBuilder.newClient();
		Response result = client.target(url.toString())
				.request(MediaType.TEXT_PLAIN)
				.post(Entity.entity(set, MediaType.APPLICATION_XML));
		System.err.println(result.getStatus() + " for update hierarchy 2");
	}

	// public static HashSet<String> getSubCompetences(String superCompetence) {
	// HashSet<String> result1 = new HashSet<String>();
	// if (superCompetence != null && !superCompetence.trim().equals("")) {
	// final StringBuilder url = new StringBuilder();
	// url.append(AppUtil.getBaseUrl()).append("/api1/competences");
	// System.err.println("getting subcompetences 2 with url:" + url);
	// Client client = ClientBuilder.newClient();
	// try {
	// java.util.List<CompetenceXMLTree> result = client
	// .target(url.toString()).queryParam("asTree", true)
	// .request(MediaType.APPLICATION_XML)
	// .get(java.util.List.class);
	// // .queryParam("rootCompetence", superCompetence).
	// // queryParam("courseId", "university").
	// System.err.println("result is" + result.toString());
	// if (result1 != null) {
	// SortedList<CompetenceXMLTree> children = result.iterator()
	// .next().getChildren();
	// for (CompetenceXMLTree competenceXMLTree : children) {
	// result1.add(competenceXMLTree.getName());
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return result1;
	// }

	public static StringList getSubCompetences(String superCompetence) {
		return getSubCompetences(superCompetence, null);
	}

	public static StringList getSubCompetences(String superCompetence,
			String textfilter) {
		if (superCompetence != null && !superCompetence.trim().equals("")) {
			final StringBuilder url = new StringBuilder();
			url.append(AppUtil.getBaseUrl()).append("/api1/competences");
			System.err.println("getting subcompetences 3 with url:" + url);
			try {
				Client client = ClientBuilder.newClient();
				List<String> result = client.target(url.toString())
						.queryParam("asTree", false)
						.queryParam("textFilter", textfilter)
						.queryParam("rootCompetence", superCompetence)
						.request().get(List.class);
				System.err.println("result for subs is:" + result.toString());
				return new StringList(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new StringList();
	}

//	public static void linkCompetencesAndCourse(List<String> competences,
//			String course) {
//		final StringBuilder url = new StringBuilder();
//		url.append(AppUtil.getBaseUrl()).append(
//				"/SuggestedCourseForCompetence/create");
//		System.err.println("linking with url" + url.toString());
//		Client client = ClientBuilder.newClient();
//
//		for (String competence : competences) {
//			Response result = client.target(url.toString())
//					.queryParam("competence", competence)
//					.queryParam("course", "course")
//					.request(MediaType.APPLICATION_JSON).post(null);
//		}
//		
//	}
}
