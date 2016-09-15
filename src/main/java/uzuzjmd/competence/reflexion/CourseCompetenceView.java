package uzuzjmd.competence.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import uzuzjmd.competence.reflexion.dao.CompetenceDao;
import uzuzjmd.competence.reflexion.dao.CourseDao;
import uzuzjmd.competence.reflexion.util.GraphUtil;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSet;
import uzuzjmd.competence.shared.moodle.UserCourseListItem;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

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

	//
	private UserCourseListItem selectedCourse;
	private TreeNode root;
	private TreeNode[] selectedNodes;
	private String requirement;
	private Boolean showCompetenceForCourse;
	//
	private List<String> selectedCompetences;

	public CourseCompetenceView() {		
	}

	@PostConstruct
	public void init() {
		System.err.println("initializing coursecompetenceview bean");
		root = new DefaultTreeNode("Root", null);
		courses = new ArrayList<UserCourseListItem>();
		courseKeyMap = new HashMap<UserCourseListItem, List<String>>();
		showCompetenceForCourse = false;
	}

//	public void showCompetenceSelected() {
//		System.err.println(selectedCompetences.iterator().next());
//		System.err.println(selectedCourse.getName() + " is selected");
//	}
//
//	public void showCourseSelected() {
//		System.err.println(selectedCourse.getName() + " is selected");
//		if (selectedCompetences != null) {
//			System.err.println(selectedCompetences.iterator().next());
//		}
//	}

	public void connectCourseAndCompetences(ActionEvent e) {
		for (String selectedCompetence : selectedCompetences) {
			CourseDao.addSuggestedCourseForCompetence(selectedCompetence,
					selectedCourse.getCourseid() + "");
		}
		showLinkMessage();
	}

	public void showLinkMessage() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"It worked", "It maybe worked");
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	public void update(String username, String password) {
		courses.addAll(CourseDao.getCourseFromUser(MOODLE, username, null,
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

	public List<String> completeCompetence(final String query) {
		List<String> tmp = CompetenceDao.getSubCompetences("Kompetenz", query)
				.getData();
		Collection<String> result = Collections2.filter(tmp,
				new Predicate<String>() {

					@Override
					public boolean apply(String arg0) {
						if (arg0.contains(query)) {
							return true;
						} else {
							return false;
						}
					}
				});

		return new LinkedList<String>(result);
	}

	public List<UserCourseListItem> complete(final String query) {		
		final List<UserCourseListItem> results = new ArrayList<UserCourseListItem>();
		final Collection<UserCourseListItem> tmp = Collections2.filter(courses,
				new Predicate<UserCourseListItem>() {
					@Override
					public boolean apply(UserCourseListItem arg0) {
						final String courseName = arg0.getName();
						return courseName.contains(query);
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

	public Boolean getShowCompetenceForCourse() {
		return showCompetenceForCourse;
	}

	public void setShowCompetenceForCourse(Boolean showCompetenceForCourse) {
		this.showCompetenceForCourse = showCompetenceForCourse;
	}

	public List<String> getSelectedCompetences() {
		return selectedCompetences;
	}

	public void setSelectedCompetences(List<String> selectedCompetences) {
		this.selectedCompetences = selectedCompetences;
	}

}
