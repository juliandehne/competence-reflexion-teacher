package de.unipotsdam.anh.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uzuzjmd.competence.shared.DESCRIPTORType;
import uzuzjmd.competence.shared.StringList;
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
	
	@AfterClass
	public static void tearDown() {
		graph = null;
		learningTemplateResultSet = null;
	}
	
	@Test
	public void testGetLearningProjectTemplate() {
		System.out.println("##### Test getLearningProjectTemplate #####");
		final LearningTemplateResultSet result = LearningTemplateDao.getLearningProjectTemplate(learningTemplateName);
		
		Assert.assertNotNull(result);
		Assert.assertEquals("TestLernprojekt", result.getNameOfTheLearningTemplate());
		
		final GraphTriple first = getGraphTriple(result.getResultGraph(),"using tags" , "using JSP tags");
		final GraphTriple second = getGraphTriple(result.getResultGraph(),"using JSP tags" , "using primfaces tags");
		final GraphTriple fourth = getGraphTriple(result.getResultGraph(),"using JSP tags" , "using faces tags");
		final GraphTriple third = getGraphTriple(result.getResultGraph(),"programming" , "creating api");
		final GraphTriple fifth = getGraphTriple(result.getResultGraph(),"creating api" , "being the first person to generate a universal api");
		
		Assert.assertNotNull(first);
		Assert.assertNotNull(second);
		Assert.assertNotNull(third);
		Assert.assertNotNull(fourth);
		Assert.assertNotNull(fifth);
		
		Assert.assertEquals("programming", result.getCatchwordMap().get(first)[0]);
		Assert.assertEquals("jsp", result.getCatchwordMap().get(first)[1]);
		
		Assert.assertEquals("programming", result.getCatchwordMap().get(first)[0]);
		Assert.assertEquals("jsp", result.getCatchwordMap().get(first)[1]);
		
		Assert.assertEquals("programming", result.getCatchwordMap().get(second)[0]);
		Assert.assertEquals("jsp", result.getCatchwordMap().get(second)[1]);
		
		Assert.assertEquals("programming", result.getCatchwordMap().get(third)[0]);
		Assert.assertEquals("api", result.getCatchwordMap().get(third)[1]);
		
		Assert.assertEquals("programming", result.getCatchwordMap().get(fourth)[0]);
		Assert.assertEquals("jsp", result.getCatchwordMap().get(fourth)[1]);
		
		Assert.assertEquals("programming", result.getCatchwordMap().get(fifth)[0]);
		Assert.assertEquals("api", result.getCatchwordMap().get(fifth)[1]);
		Assert.assertEquals("universality", result.getCatchwordMap().get(fifth)[2]);
		
		showLearningTemplateResultSet(learningTemplateResultSet);
	}

	@Test
	public void testCreateTemplate() {
		System.out.println("##### Test CreateTemplate #####");
		Assert.assertEquals(200, LearningTemplateDao.createTemplate(learningTemplateName));
	}
	
	@Test
	public void testCreateOneCompetence() {
		System.out.println("##### Test CreateOneCompetence #####");
		final int result1 = LearningTemplateDao.createOneCompetence("Java 1", "analyse", "TestLernprojekt2", "java");
		final int result2 = LearningTemplateDao.createOneCompetence("Java 2", "analyse", "TestLernprojekt2", "java");
		final int result3 = LearningTemplateDao.createOneCompetence("Java 3", "analyse", "TestLernprojekt2", "java");
		
		Assert.assertEquals(200, result1);
		Assert.assertEquals(200, result2);
		Assert.assertEquals(200, result3);
		
		final LearningTemplateResultSet result = LearningTemplateDao.getLearningProjectTemplate("TestLernprojekt2");
		showLearningTemplateResultSet(result);
		
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testCreateTemplateWithGraph() {
		System.out.println("##### Test CreateTemplateWithGraph #####");
		Assert.assertEquals(200, LearningTemplateDao.createTemplate(learningTemplateName));
		Assert.assertEquals(200, LearningTemplateDao.createTemplate(learningTemplateName, learningTemplateResultSet));
	}

	@Test
	public void testFindAll() {
		System.out.println("##### Test findAll LearningTemplate #####");
		final StringList result = LearningTemplateDao.findAll();
		
		Assert.assertNotNull(result);
		
		for(String s : result.getData()) {
			System.out.println("\t" + s);
		}
	}
	
	@Test
	public void testGetGraphFromCourse() {
		System.out.println("##### Test GetGraphFromCourse #####");
		Assert.assertNotNull(LearningTemplateDao.getGraphFromCourse(course));
	}
	
	private GraphTriple getGraphTriple(Graph resultGraph, String fromNode, String toNode) {
		GraphTriple tmp = null;
		for(GraphTriple triple : resultGraph.triples) {
			if(fromNode.equals(triple.fromNode) && toNode.equals(triple.toNode)) {
				tmp = triple;
			}
		}
		return tmp;
	}
	
	private void showLearningTemplateResultSet(LearningTemplateResultSet result) {
		System.out.println("Result LearningTemplateName: " + result.getNameOfTheLearningTemplate());
		System.out.println("Root graph: " + result.getRoot());
		System.out.println("Result graph: " + result.getResultGraph());
		System.out.println("Catchword Map: ");
		for(Entry<GraphTriple, String[]> e : result.getCatchwordMap().entrySet()) {
			System.out.println("\t Catchword" + Arrays.asList(e.getValue()).toString() + " for Graphtriple: ");
			System.out.println("\t" + e.getKey().toString());
		}
	}
	
	public void createDefaultDescriptorTypes() {
		List<DESCRIPTORType> descriptorTypes = new ArrayList<DESCRIPTORType>();
		
		DESCRIPTORType desType1 = new DESCRIPTORType();
		desType1.setNAME("ich kann Funktion schreiben");
		desType1.setCOMPETENCE("pascal");
		desType1.setEVALUATIONS("gar nicht; ausreichend; befriedigend; gut");
		desType1.setGOAL((byte) 1);
		desType1.setLEVEL("niedrig");
		
		DESCRIPTORType desType2 = new DESCRIPTORType();
		desType2.setNAME("Ich kann Klasse schreiben");
		desType2.setCOMPETENCE("java");
		desType2.setEVALUATIONS("gar nicht; ausreichend; befriedigend; gut");
		desType2.setGOAL((byte) 1);
		desType2.setLEVEL("hoch");
		
		descriptorTypes.add(desType1);
		descriptorTypes.add(desType2);
	}
}
