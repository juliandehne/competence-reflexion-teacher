package de.unipotsdam.anh.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uzuzjmd.competence.shared.dto.ActivityEntry;
import uzuzjmd.competence.shared.dto.ActivityTyp;
import uzuzjmd.competence.shared.dto.Graph;
import uzuzjmd.competence.shared.dto.UserCourseListItem;
import uzuzjmd.competence.shared.dto.UserTree;
import de.unipotsdam.anh.dao.ActivityDao;
import de.unipotsdam.anh.dao.CourseDao;
import de.unipotsdam.anh.util.AppUtil;

public class CourseDaoTest {

	public static final LoggingFilter logginFilter = new LoggingFilter(
			Logger.getLogger(CourseDaoTest.class.getName()), true);
	
	private static final String course = "15";
	private static final String requirements = "requirements from course";

	private static final String MODDLE_SYSTEM = "moodle";
	
	private static List<String> competences;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		competences = new ArrayList<String>();
		competences.add("q1");
		competences.add("q2");
		competences.add("q3");
		competences.add("q4");
	}
	
	@AfterClass
	public static void tearDown() {
		competences = null;
	}
	
	@Test
	public void testGetCourseFromUser() {
		List<UserCourseListItem> courses = CourseDao.getCourseFromUser(MODDLE_SYSTEM, AppUtil.getTestUser()[0], null, AppUtil.getTestUser()[1]);
		
		for(UserCourseListItem c : courses) {
			System.out.println(c.toString());
		}
	}
	
	@Test
	public void testGetActivityFromCourse() {
		List<UserTree> userTrees = ActivityDao.getActivityFromCourse(course ,MODDLE_SYSTEM, AppUtil.getTestUser()[0], null, AppUtil.getTestUser()[1], false);
		
		for(UserTree usertree : userTrees) {
			System.out.println(usertree.toString());
			for(ActivityTyp a : usertree.getActivityTypes()) {
				System.out.println(a);
				for(ActivityEntry e : a.getActivities()) {
					System.out.println(e);
				}
			}
		}
	}

	@Test
	public void testGetRequirementFromCourse() {
		String response = CourseDao.getRequirementFromCourse(course);
		System.out.println(response);
		
		Assert.assertEquals(requirements, response);
	}
	
	@Test
	public void testGetPrerequisiteGraph() {
		int status = CourseDao.createPrerequisiteForCourse(course, "c5", Arrays.asList("r1", "r2"));
		System.out.println(status);
		Graph graph = CourseDao.getPrerequisiteGraph(course, Arrays.asList("r1", "r2"));
		
		System.out.println(graph);
		
		List<String> competence = CourseDao.getPrerequisiteForCompetence(course, "r1");
		
		System.out.println(competence);
	}
}
