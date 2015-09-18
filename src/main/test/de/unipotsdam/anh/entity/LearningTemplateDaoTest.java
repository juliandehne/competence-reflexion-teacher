package de.unipotsdam.anh.entity;

import java.util.Arrays;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import uzuzjmd.competence.shared.dto.GraphTriple;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;
import de.unipotsdam.anh.dao.LearningTemplateDao;

public class LearningTemplateDaoTest {

//	@Test
	public void testGetLearningProjectTemplate() {
		System.out.println("##### Test getLearningProjectTemplate #####");
		LearningTemplateResultSet result = LearningTemplateDao.getLearningProjectTemplate("TestLernprojekt");
		
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
	public void testCreateOneCompetence() {
		int result1 = LearningTemplateDao.createOneCompetence("Java 1", "analyse", "TestLernprojekt", "java");
		int result2 = LearningTemplateDao.createOneCompetence("Java 2", "analyse", "TestLernprojekt", "java");
		int result3 = LearningTemplateDao.createOneCompetence("Java 3", "analyse", "TestLernprojekt", "java");
		
		Assert.assertEquals(200, result1);
		Assert.assertEquals(200, result2);
		Assert.assertEquals(200, result3);
	}

//	@Test
	public void testFindAll() {
		System.out.println("##### Test findAll LearningTemplate #####");
		for(String s : LearningTemplateDao.findAll().getData()) {
			System.out.println("\t" + s);
		}
	}
}
