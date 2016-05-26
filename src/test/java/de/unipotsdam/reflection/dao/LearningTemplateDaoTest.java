package de.unipotsdam.reflection.dao;

import java.util.Arrays;

import org.junit.Test;

import de.unipotsdam.anh.dao.LearningTemplateDao;

public class LearningTemplateDaoTest {

	@Test
	public void createOneCompetenceTest() {
		int status = LearningTemplateDao.createOneCompetence("creating api", "op", "TestLernprojekt",
				Arrays.asList("th1", "th2"), null, Arrays.asList("super competence"));
		
		System.out.println(status);
		
		final String subCompetencen = LearningTemplateDao.getSubCompetence("creating api");
		
		System.out.println(subCompetencen);
	}
}
