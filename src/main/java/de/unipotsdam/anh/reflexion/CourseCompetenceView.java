package de.unipotsdam.anh.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import uzuzjmd.competence.shared.dto.Graph;
import uzuzjmd.competence.shared.dto.GraphNode;
import uzuzjmd.competence.shared.dto.GraphTriple;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;
import uzuzjmd.competence.shared.dto.UserCourseListItem;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import de.unipotsdam.anh.dao.CourseDao;
import de.unipotsdam.anh.util.AppUtil;
import de.unipotsdam.anh.util.GraphUtil;

@ManagedBean(name = "courseCompetenceView")
@ViewScoped
public class CourseCompetenceView implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String MOODLE = "moodle";

	private Map<UserCourseListItem, List<String>> courseKeyMap;
	private List<UserCourseListItem> courses;

	private UserCourseListItem selectedCourse;
	private TreeNode root;
	private TreeNode[] selectedNodes;
	private String requirement;
	
	@PostConstruct
	public void init() {
		root = new DefaultTreeNode("Root", null);
		courseKeyMap = new HashMap<UserCourseListItem, List<String>>();
		courses = CourseDao.getCourseFromUser(MOODLE, AppUtil.getTestUser()[0], 
											null, AppUtil.getTestUser()[1]);
		
		getCourseCompetenceMap();
	}
	
	public Map<UserCourseListItem, List<String>> getCourseKeyMap() {
		return courseKeyMap;
	}
	
	public void setCourseKeyMap(Map<UserCourseListItem, List<String>> courseKeyMap) {
		this.courseKeyMap = courseKeyMap;
	}
	
	public String requirementFromCourse(String course) {
		final String requirement = CourseDao.getRequirementFromCourse(course);
		System.out.println("requirement from Course with id " + course + " " + requirement);
		return requirement;
	}
	
	public void courseCompetenceCollate(ActionEvent e) {
		final List<String> competences = new ArrayList<String>();
		for(TreeNode node : selectedNodes) {
			final String competence = node.getData().toString();
			if(CourseDao.addSuggestedCourseForCompetence(competence, String.valueOf(selectedCourse.getCourseid())) == 200) {
				competences.add(competence);
			}
		}
		
		if(courseKeyMap.get(selectedCourse) != null) {
			competences.addAll(courseKeyMap.get(selectedCourse));
		}
		courseKeyMap.put(selectedCourse, competences);
		selectedCourse = null;
		requirement = null;
		selectedNodes = new TreeNode[0];
	}

	public void update(LearningTemplateResultSet learningTemplateResultSet) {
		createTreeNode(learningTemplateResultSet);
	}
	
	public List<UserCourseListItem> complete(final String query) {
		final List<UserCourseListItem> results = new ArrayList<UserCourseListItem>();
        final Collection<UserCourseListItem> tmp = Collections2.filter(courses, new Predicate<UserCourseListItem>() {

			@Override
			public boolean apply(UserCourseListItem arg0) {
				final String courseName = arg0.getName();
				if(StringUtils.isEmpty(query)) {
					return true;
				}
				return StringUtils.equals(courseName, query);
			}
		});	
		results.addAll(tmp == null ? new ArrayList<UserCourseListItem>() : tmp);
		
        return results;
    }

	public List<UserCourseListItem> getCourses() {
		return courses;
	}

	public void setCourses(List<UserCourseListItem> courses) {
		this.courses = courses;
	}

	public UserCourseListItem getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(UserCourseListItem selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}
	
	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}
	
	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	private void createTreeNode(LearningTemplateResultSet learningTemplateResultSet) {
		final Set<String> catchWords = GraphUtil.getAllCatchword(learningTemplateResultSet);

		for(String catchword : catchWords) {
			root.getChildren().add(createTreeNodeForCatchword(learningTemplateResultSet.getCatchwordMap(), catchword));
		}
	}
	
	private TreeNode createTreeNodeForCatchword(Map<GraphTriple, String[]> catchwordMap,String catchword) {
		final Graph graph = GraphUtil.getGraphForCatchword(catchwordMap, catchword);
		
		final TreeNode catchwordNode = new DefaultTreeNode(catchword);
		catchwordNode.setExpanded(true);
		
		for(GraphNode gn : graph.nodes) {
			catchwordNode.getChildren().add(new DefaultTreeNode(gn.getLabel()));
		}
		return catchwordNode;
	}
	
	private void getCourseCompetenceMap() {
		for(UserCourseListItem course : courses) {
			final List<String> competences = CourseDao.getCompetenceFormSuggestedCourse(String.valueOf((course.getCourseid())));
			courseKeyMap.put(course, competences);
		}
	}
}
