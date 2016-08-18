package uzuzjmd.competence.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import uzuzjmd.competence.reflexion.dao.CourseDao;
import uzuzjmd.competence.reflexion.util.AppUtil;
import uzuzjmd.competence.reflexion.util.GraphUtil;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSet;
import uzuzjmd.competence.shared.moodle.UserCourseListItem;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

import datastructures.graph.Graph;
import datastructures.graph.GraphNode;
import datastructures.graph.GraphTriple;

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

//	@ManagedProperty("#{userLoginView}")
//	private UserLoginView userLoginView;
//
//	public UserLoginView getUserLoginView() {
//		return userLoginView;
//	}
//
//	public void setUserLoginView(UserLoginView userLoginView) {
//		this.userLoginView = userLoginView;
//	}

	@PostConstruct
	public void init() {
		//update();
		root = new DefaultTreeNode("Root", null);
		courses = new ArrayList<UserCourseListItem>();
		courseKeyMap = new HashMap<UserCourseListItem, List<String>>();
	}

	public void update(String username, String password) {									
			courses.addAll(CourseDao.getCourseFromUser(MOODLE,
					username, null,
					password));
			getCourseCompetenceMap();			
	}

	public Map<UserCourseListItem, List<String>> getCourseKeyMap() {
		return courseKeyMap;
	}

	public void setCourseKeyMap(
			Map<UserCourseListItem, List<String>> courseKeyMap) {
		this.courseKeyMap = courseKeyMap;
	}

	public void deleteCompetenceFromCourse(String competence, String course) {
		int status = CourseDao.deleteSuggestedCourseForCompetence(competence,
				course);
		// System.out.println("delete competence " + competence + "with status "
		// + status);
		// if(status == 200) {
		// for(Entry<UserCourseListItem, List<String>> e :
		// courseKeyMap.entrySet()) {
		// if(StringUtils.equals(String.valueOf(e.getKey().getCourseid()),
		// course)) {
		// e.getValue().remove(competence);
		// }
		// }
		// }
		courseKeyMap.clear();
		getCourseCompetenceMap();
	}

	public String requirementFromCourse(String course) {
		final String requirement = CourseDao.getRequirementFromCourse(course);
		// System.out.println("requirement from Course with id " + course + " "
		// + requirement);
		return requirement;
	}

	public void courseCompetenceCollate(ActionEvent e) {
		final List<String> competences = new ArrayList<String>();
		for (TreeNode node : selectedNodes) {
			final String competence = node.getData().toString();
			if (CourseDao.addSuggestedCourseForCompetence(competence,
					String.valueOf(selectedCourse.getCourseid())) == 200) {
				competences.add(competence);
			}
			node.setSelected(false);
		}

		addCompetencenIntoCourseMap(selectedCourse, competences);
		selectedCourse = null;
		requirement = null;
		selectedNodes = new TreeNode[0];
	}

	private void addCompetencenIntoCourseMap(UserCourseListItem course,
			List<String> competences) {
		if (courseKeyMap.get(course) != null) {
			for (String c : courseKeyMap.get(course)) {
				if (!competences.contains(c)) {
					competences.add(c);
				}
			}
		}
		courseKeyMap.put(selectedCourse, competences);
	}

	public void update(LearningTemplateResultSet learningTemplateResultSet) {
		createTreeNode(learningTemplateResultSet);
	}

	public List<UserCourseListItem> complete(final String query) {
		final List<UserCourseListItem> results = new ArrayList<UserCourseListItem>();
		final Collection<UserCourseListItem> tmp = Collections2.filter(courses,
				new Predicate<UserCourseListItem>() {

					@Override
					public boolean apply(UserCourseListItem arg0) {
						final String courseName = arg0.getName();
						if (StringUtils.isEmpty(query)) {
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

	private void createTreeNode(
			LearningTemplateResultSet learningTemplateResultSet) {
		final Set<String> catchWords = GraphUtil
				.getAllCatchword(learningTemplateResultSet);

		for (String catchword : catchWords) {
			root.getChildren().add(
					createTreeNodeForCatchword(
							learningTemplateResultSet.getCatchwordMap(),
							catchword));
		}
	}

	private TreeNode createTreeNodeForCatchword(
			Map<GraphTriple, String[]> catchwordMap, String catchword) {
		final Graph graph = GraphUtil.getGraphForCatchword(catchwordMap,
				catchword);

		final TreeNode catchwordNode = new DefaultTreeNode(catchword);
		catchwordNode.setExpanded(true);

		for (GraphNode gn : graph.nodes) {
			catchwordNode.getChildren().add(new DefaultTreeNode(gn.getLabel()));
		}
		return catchwordNode;
	}

	private void getCourseCompetenceMap() {
		for (UserCourseListItem course : courses) {
			final List<String> competences = CourseDao
					.getCompetenceFormSuggestedCourse(String.valueOf((course
							.getCourseid())));
			courseKeyMap.put(course, competences);
		}
	}

	
}
