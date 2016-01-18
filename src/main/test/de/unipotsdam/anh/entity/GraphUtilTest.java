package de.unipotsdam.anh.entity;

import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uzuzjmd.competence.shared.dto.GraphTriple;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;
import de.unipotsdam.anh.util.GraphUtil;

public class GraphUtilTest {
	
	private static final LoggingFilter logginFilter = new LoggingFilter(
			Logger.getLogger(GraphUtilTest.class.getName()), true);
	
	public static final String LABELNAME = "SuggestedCompetencePrerequisite";
	private static String learningTemplateName = "Graphen sortieren";
	private static String catchword = "TopoSort";
	private static LearningTemplateResultSet learningTemplateResultSet;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		learningTemplateResultSet = new LearningTemplateResultSet();
		learningTemplateResultSet.setNameOfTheLearningTemplate(learningTemplateName);

		final GraphTriple triple1 = new GraphTriple("a1", "a2", LABELNAME, true);
		final GraphTriple triple2 = new GraphTriple("a1", "a7", LABELNAME, true);
		final GraphTriple triple3 = new GraphTriple("a2", "a3", LABELNAME, true);
		final GraphTriple triple4 = new GraphTriple("a3", "a4", LABELNAME, true);
		final GraphTriple triple5 = new GraphTriple("a2", "a5", LABELNAME, true);
		final GraphTriple triple6 = new GraphTriple("a5", "a6", LABELNAME, true);
		
		learningTemplateResultSet.addTriple(triple1, new String[] {catchword});
		learningTemplateResultSet.addTriple(triple2, new String[] {catchword});
		learningTemplateResultSet.addTriple(triple3, new String[] {catchword});
		learningTemplateResultSet.addTriple(triple4, new String[] {catchword});
		learningTemplateResultSet.addTriple(triple5, new String[] {catchword});
		learningTemplateResultSet.addTriple(triple6, new String[] {catchword});
		
	}

	@AfterClass
	public static void tearDown() {
//		learningTemplateResultSet = null;
	}
	
	@Test
	public void testCreateGraphFromServer() {
		final Client client = ClientBuilder.newClient();

		try {
			final WebTarget webResource = client
					.target("http://localhost:8084" + "/competences/xml/learningtemplate/add/"
							+ learningTemplateResultSet.getNameOfTheLearningTemplate())
					.register(logginFilter)
					.queryParam("learningTemplateResultSet",
							learningTemplateResultSet);

			Response result = webResource.request(MediaType.APPLICATION_XML)
					.post(Entity.entity(learningTemplateResultSet,
							MediaType.APPLICATION_XML));
			Assert.assertEquals(200, result.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}
	
	@Test
	public void testGraphFromServer() {
		
		
		final Client client = ClientBuilder.newClient();

		final WebTarget webResource = client.target("http://localhost:8084"
				+ "/competences/xml/learningtemplate/get/" + learningTemplateName).register(logginFilter);
		LearningTemplateResultSet result = webResource.request(
				MediaType.APPLICATION_XML).get(LearningTemplateResultSet.class);

		System.out.println("##########  From local #############");
		System.out.println(learningTemplateResultSet.getResultGraph());

		System.out.println("##########  From Server #############");
		System.out.println(result.getResultGraph());
		
		List<Entry<String, Integer>> list = GraphUtil.topoSort(GraphUtil.getGraphForCatchword(
				result.getCatchwordMap(), catchword));
		Assert.assertEquals("a1", ((Entry<String, Integer>)list.get(0)).getKey());
		Assert.assertEquals("a2", ((Entry<String, Integer>)list.get(1)).getKey());
		Assert.assertEquals("a7", ((Entry<String, Integer>)list.get(2)).getKey());
		Assert.assertEquals("a3", ((Entry<String, Integer>)list.get(3)).getKey());
		Assert.assertEquals("a5", ((Entry<String, Integer>)list.get(4)).getKey());
		Assert.assertEquals("a4", ((Entry<String, Integer>)list.get(5)).getKey());
		Assert.assertEquals("a6", ((Entry<String, Integer>)list.get(6)).getKey());
	}
	
	@Test
	public void testTopoSort() {
		List<Entry<String, Integer>> list = GraphUtil.topoSort(learningTemplateResultSet.getResultGraph());
		Assert.assertNotNull(list);
		Assert.assertEquals("a1", ((Entry<String, Integer>)list.get(0)).getKey());
		Assert.assertEquals("a2", ((Entry<String, Integer>)list.get(1)).getKey());
		Assert.assertEquals("a7", ((Entry<String, Integer>)list.get(2)).getKey());
		Assert.assertEquals("a3", ((Entry<String, Integer>)list.get(3)).getKey());
		Assert.assertEquals("a5", ((Entry<String, Integer>)list.get(4)).getKey());
		Assert.assertEquals("a4", ((Entry<String, Integer>)list.get(5)).getKey());
		Assert.assertEquals("a6", ((Entry<String, Integer>)list.get(6)).getKey());
	}
}
