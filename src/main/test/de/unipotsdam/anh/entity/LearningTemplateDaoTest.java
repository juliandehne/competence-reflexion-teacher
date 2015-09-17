package de.unipotsdam.anh.entity;

import org.junit.Test;

import uzuzjmd.competence.shared.dto.GraphNode;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;
import de.unipotsdam.anh.dao.LearningTemplateDao;

public class LearningTemplateDaoTest {

	@Test
	public void testGetLearningProjectTemplate() {
		System.out.println("##### Test getLearningProjectTemplate #####");
		LearningTemplateResultSet result = LearningTemplateDao.getLearningProjectTemplate("TestLernprojekt");
		
		System.out.println("result LearningTemplateName: " + result.getNameOfTheLearningTemplate());
		System.out.println("Root graph: " + result.getRoot());
		System.out.println("Result graph: " + result.getResultGraph());
	}
	
//	@Test
	public void testCreateOneCompetence() {
		System.out.println("##### Test createOneCompetence #####");
		
//		GraphNode node = new GraphNode("Java 1");
//		LearningTemplateResultSet learningTemplateResultSet = new LearningTemplateResultSet(node);

		int result = LearningTemplateDao.createOneCompetence("Java 1", "analyse", "TestLernprojekt", "java");
		System.out.println(result);
	}

//	@Test
	public void testFindAll() {
		System.out.println("##### Test findAll #####");
		for(String s : LearningTemplateDao.findAll().getData()) {
			System.out.println(s);
		}
	}
}
