package de.unipotsdam.anh.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uzuzjmd.competence.shared.dto.Graph;
import uzuzjmd.competence.shared.dto.GraphTriple;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;
import de.unipotsdam.anh.dao.LearningTemplateDao;

public class LearningTemplateDaoTest {

	public static final String LABELNAME = "SuggestedCompetencePrerequisit";
	private static final String course = "university";
	private static String learningTemplateName = "TestLernprojekt";
	private static LearningTemplateResultSet learningTemplateResultSet;
	private static Graph graph;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		graph = new Graph();
		graph.addTriple("using tags", "using JSP tags", LABELNAME, true);
		graph.addTriple("using JSP tags", "using primfaces tags", LABELNAME,
				true);
		graph.addTriple("using JSP tags", "using faces tags", LABELNAME, true);
		graph.addTriple("programming", "creating api", LABELNAME, true);
		graph.addTriple("creating api",
				"being the first person to generate a universal api",
				LABELNAME, true);

		GraphTriple first = new GraphTriple("using tags", "using JSP tags",
				LABELNAME, true);
		GraphTriple second = new GraphTriple("using JSP tags",
				"using primfaces tags", LABELNAME, true);
		GraphTriple third = new GraphTriple("programming", "creating api",
				LABELNAME, true);
		GraphTriple fourth = new GraphTriple("using JSP tags",
				"using faces tags", LABELNAME, true);
		GraphTriple fifth = new GraphTriple("creating api",
				"being the first person to generate a universal api",
				LABELNAME, true);

		HashMap<GraphTriple, String[]> map = new HashMap<GraphTriple, String[]>();
		map.put(first, new String[] { "programming", "jsp" });
		map.put(second, new String[] { "programming", "jsp" });
		map.put(fourth, new String[] { "programming", "jsp" });
		map.put(third, new String[] { "programming", "api" });
		map.put(fifth, new String[] { "programming", "api", "universality" });

		learningTemplateResultSet = new LearningTemplateResultSet(graph, map,
				learningTemplateName);
	}
	
	@Test
	public void testGetLearningProjectTemplate() {
		System.out.println("##### Test getLearningProjectTemplate #####");
		LearningTemplateResultSet result = LearningTemplateDao.getLearningProjectTemplate(learningTemplateName);
		
		System.out.println("Result LearningTemplateName: " + result.getNameOfTheLearningTemplate());
		System.out.println("Root graph: " + result.getRoot());
		System.out.println("Result graph: " + result.getResultGraph());
		System.out.println("Catchword Map: ");
		for(Entry<GraphTriple, String[]> e : result.getCatchwordMap().entrySet()) {
			System.out.println("\t Catchword" + Arrays.asList(e.getValue()).toString() + " for Graphtriple: ");
			System.out.println("\t" + e.getKey().toString());
		}
	}
	
	@Test
	public void testCreateTemplate() {
		System.out.println("##### Test CreateTemplate #####");
		Assert.assertEquals(200, LearningTemplateDao.createTemplate(learningTemplateName + "without Graph"));
	}
	
//	@Test
	public void testCreateOneCompetence() {
		System.out.println("##### Test CreateOneCompetence #####");
		int result1 = LearningTemplateDao.createOneCompetence("Java 1", "analyse", "TestLernprojekt", "java");
		int result2 = LearningTemplateDao.createOneCompetence("Java 2", "analyse", "TestLernprojekt", "java");
		int result3 = LearningTemplateDao.createOneCompetence("Java 3", "analyse", "TestLernprojekt", "java");
		
		Assert.assertEquals(200, result1);
		Assert.assertEquals(200, result2);
		Assert.assertEquals(200, result3);
		
		Assert.assertNotNull(LearningTemplateDao.getLearningProjectTemplate(learningTemplateName));
	}
	
//	@Test
	public void testCreateTemplateWithGraph() {
		System.out.println("##### Test CreateTemplateWithGraph #####");
		Assert.assertEquals(200, LearningTemplateDao.createTemplate(learningTemplateName));
		Assert.assertEquals(200, LearningTemplateDao.createTemplate(learningTemplateName, learningTemplateResultSet));
	}

	@Test
	public void testFindAll() {
		System.out.println("##### Test findAll LearningTemplate #####");
		for(String s : LearningTemplateDao.findAll().getData()) {
			System.out.println("\t" + s);
		}
	}
	
	@Test
	public void testGetGraphFromCourse() {
		System.out.println("##### Test GetGraphFromCourse #####");
		Assert.assertNotNull(LearningTemplateDao.getGraphFromCourse(course));
	}
}
