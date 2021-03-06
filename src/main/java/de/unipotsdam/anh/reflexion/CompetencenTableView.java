package de.unipotsdam.anh.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;

import uzuzjmd.competence.shared.SuggestedCompetenceGrid;
import uzuzjmd.competence.shared.dto.AbstractTreeEntry;
import uzuzjmd.competence.shared.dto.ActivityTyp;
import uzuzjmd.competence.shared.dto.UserCourseListItem;
import de.unipotsdam.anh.dao.LearningTemplateDao;

@ManagedBean(name = "competencenTableView")
@ViewScoped
public class CompetencenTableView implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private SuggestedCompetenceGrid suggestedCompetenceGrid;
	private Map<String, List<UserCourseListItem>> courseCompetenceMap;
	private Map<String, List<AbstractTreeEntry>> activityCompetenceMap;
	
	private String selectedLearningTemplate;
	private String selectedCompetence;
	
	private String selectedCourse;
	
	@PostConstruct
	public void init() {
		courseCompetenceMap = new HashMap<String, List<UserCourseListItem>>();
		activityCompetenceMap = new HashMap<String, List<AbstractTreeEntry>>();
		setGridData();
	}

	public void update(String selectedLearningTemplate) {
		setSelectedLearningTemplate(selectedLearningTemplate);
		setGridData();
	}
	
	public List<UserCourseListItem> getCourseFromCompetence(String competence) {
		List<UserCourseListItem> course = courseCompetenceMap.get(competence);
		return course;
	}
	
	public List<AbstractTreeEntry> getActivityFromCourse() {
//		final List<UserTree> userTrees = ActivityDao.getActivityFromCourse("15", "moodle", 
//				AppUtil.getTestUser()[0], null, AppUtil.getTestUser()[1], false);
//		final List<AbstractTreeEntry> tmp = new ArrayList<AbstractTreeEntry>();
//		tmp.addAll(userTrees.get(0).getActivityTypes());
		
		return new ArrayList<AbstractTreeEntry>();
	}
	
	public List<AbstractTreeEntry> getActivityFromCompetence() {
		if (!StringUtils.isEmpty(selectedLearningTemplate)) {
			final String competence = suggestedCompetenceGrid.getSuggestedCompetenceRows().get(0).
					getSuggestedCompetenceColumns().get(0).getTestOutput();
			return activityCompetenceMap.get(competence);
		}
		return activityCompetenceMap.get(selectedCompetence);
	}
	
	public void selecteCompetence(String competence) {
		selectedCompetence = competence;
	}
	
	public void selecteCourse(String course) {
		selectedCourse = course;
		
		System.out.println("selected Course from Table-View: " + selectedCourse);
	}

	public SuggestedCompetenceGrid getSuggestedCompetenceGrid() {
		return suggestedCompetenceGrid;
	}

	public void setSuggestedCompetenceGrid(
			SuggestedCompetenceGrid suggestedCompetenceGrid) {
		this.suggestedCompetenceGrid = suggestedCompetenceGrid;
	}

	public String getSelectedLearningTemplate() {
		return selectedLearningTemplate;
	}

	public void setSelectedLearningTemplate(String selectedLearningTemplate) {
		this.selectedLearningTemplate = selectedLearningTemplate;
	}

	public Map<String, List<UserCourseListItem>> getCourseCompetenceMap() {
		return courseCompetenceMap;
	}

	public void setCourseCompetenceMap(
			Map<String, List<UserCourseListItem>> courseCompetenceMap) {
		this.courseCompetenceMap = courseCompetenceMap;
	}
	
	public Map<String, List<AbstractTreeEntry>> getActivityCompetenceMap() {
		return activityCompetenceMap;
	}

	public void setActivityCompetenceMap(
			Map<String, List<AbstractTreeEntry>> activityCompetenceMap) {
		this.activityCompetenceMap = activityCompetenceMap;
	}

	public String getSelectedCompetence() {
		return selectedCompetence;
	}

	public void setSelectedCompetence(String selectedCompetence) {
		this.selectedCompetence = selectedCompetence;
	}
	
	public String getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(String selectedCourse) {
		this.selectedCourse = selectedCourse;
	}
	
	private void setGridData() {
		if (!StringUtils.isEmpty(selectedLearningTemplate)) {	
			final SuggestedCompetenceGrid data = LearningTemplateDao.getGridviewFromLearningTemplate(selectedLearningTemplate);	
			setSuggestedCompetenceGrid(data);
			getTestData();
		}
	}
	
	private void getTestData() {
		final String competence = suggestedCompetenceGrid.getSuggestedCompetenceRows().get(0).
										getSuggestedCompetenceColumns().get(0).getTestOutput();
		final List<UserCourseListItem> courses = new ArrayList<UserCourseListItem>();
		final UserCourseListItem course1 = new UserCourseListItem();
		course1.setName("course 1");
		course1.setCourseid(15l);
		courses.add(course1);
		final UserCourseListItem course2 = new UserCourseListItem();
		course2.setName("course 2");
		course2.setCourseid(15l);
		courses.add(course2);
		courseCompetenceMap.put(competence, courses);
		
		final List<AbstractTreeEntry> activities = new ArrayList<AbstractTreeEntry>();
		final ActivityTyp activityTyp1 = new ActivityTyp();
		final ActivityTyp activityTyp2 = new ActivityTyp();
		activityTyp1.setName("aktivity 1");
		activityTyp2.setName("aktivity 2");
		
		activityTyp1.setIcon("https://eportfolio.uni-potsdam.de/moodle/theme/image.php/standard/chat/1448549337/icon");
		activityTyp2.setIcon("https://eportfolio.uni-potsdam.de/moodle/theme/image.php/standard/chat/1448549337/icon");
		
		activities.add(activityTyp1);
		activities.add(activityTyp2);
		
		activityCompetenceMap.put(competence, activities);
	}
}
