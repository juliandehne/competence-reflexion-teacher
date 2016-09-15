package uzuzjmd.competence.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import uzuzjmd.competence.reflexion.dao.ActivityDao;
import uzuzjmd.competence.reflexion.util.GraphUtil;
import uzuzjmd.competence.shared.activity.ActivityTyp;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSet;
import uzuzjmd.competence.shared.moodle.UserTree;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

import datastructures.graph.Graph;
import datastructures.graph.GraphNode;
import datastructures.graph.GraphTriple;
import datastructures.trees.AbstractTreeEntry;
import datastructures.trees.ActivityEntry;

@ManagedBean(name = "activityCompetenceView")
@ViewScoped
public class ActivityCompetenceView implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String MOODLE = "moodle";

	private List<UserTree> activities;
	private Map<AbstractTreeEntry, List<String>> activityMap;

	private TreeNode activityTreeRoot;
	private TreeNode competenceTreeRoot;

	private TreeNode[] selectedKompetenceNodes;
	private TreeNode selectedActivityNode;

	@ManagedProperty("#{courseCompetenceView}")
	private CourseCompetenceView courseCompetenceView;

	private String userName;

	private String password;

	public void setCourseCompetenceView(
			CourseCompetenceView courseCompetenceView) {
		this.courseCompetenceView = courseCompetenceView;
	}

	public CourseCompetenceView getCourseCompetenceView() {
		return courseCompetenceView;
	}

	@PostConstruct
	public void init() throws PortalException, SystemException {
		activityTreeRoot = new DefaultTreeNode("Root", null);
		competenceTreeRoot = new DefaultTreeNode("Root", null);
		activityMap = new HashMap<AbstractTreeEntry, List<String>>();
		activities = new LinkedList<UserTree>();
	}

	public void update(String userName, String password) {
		this.userName = userName;
		this.password = password;
		activityMap = new HashMap<AbstractTreeEntry, List<String>>();
		
	}

	public void update(LearningTemplateResultSet learningTemplateResultSet) {
		createCompetenceTreeNode(learningTemplateResultSet);
	}

	public void activityCompetenceCollate(SelectEvent e) {
		fillActivityTree();
	}
	
	public void activityCompetenceCollate(ActionEvent e) {
		fillActivityTree();
	}


	private void fillActivityTree() {
		activities = ActivityDao.getActivityFromCourse(courseCompetenceView.getSelectedCourse().getCourseid() +"", MOODLE, userName,
				null, password, false);
		createActivityTree();
		
		final List<String> competences = new ArrayList<String>();
		try {
			for (TreeNode node : selectedKompetenceNodes) {
				competences.add(node.getData().toString());
				node.setSelected(false);
			}
			activityMap.put((AbstractTreeEntry) selectedActivityNode.getData(),
					competences);
			selectedActivityNode.setSelected(false);
			selectedActivityNode = null;
		} catch (Exception ex) {
			// todo find out wtf
		}
	}

	public void writeConnection() {
		List<String> competencesSelected = courseCompetenceView
				.getSelectedCompetences();
		for (String selectedCompetence : competencesSelected) {
			int result = ActivityDao.addSuggestedActivityForCompetence(selectedCompetence,
					((AbstractTreeEntry)selectedActivityNode.getData()).getName());
			if (result != 200) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"It worked", "It maybe worked");				
				RequestContext.getCurrentInstance().showMessageInDialog(message);
			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"It worked", "It maybe worked");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
			}
		}
	}
	
	

	public List<UserTree> getActivities() {
		return activities;
	}

	public void setActivities(List<UserTree> activities) {
		this.activities = activities;
	}

	public TreeNode getActivityTreeRoot() {
		return activityTreeRoot;
	}

	public void setActivityTreeRoot(TreeNode activityTreeRoot) {
		this.activityTreeRoot = activityTreeRoot;
	}

	public TreeNode getCompetenceTreeRoot() {
		return competenceTreeRoot;
	}

	public void setCompetenceTreeRoot(TreeNode competenceTreeRoot) {
		this.competenceTreeRoot = competenceTreeRoot;
	}

	public TreeNode[] getSelectedKompetenceNodes() {
		return selectedKompetenceNodes;
	}

	public void setSelectedKompetenceNodes(TreeNode[] selectedKompetenceNodes) {
		this.selectedKompetenceNodes = selectedKompetenceNodes;
	}

	public TreeNode getSelectedActivityNode() {
		return selectedActivityNode;
	}

	public void setSelectedActivityNode(TreeNode selectedActivityNode) {
		this.selectedActivityNode = selectedActivityNode;
	}

	public Map<AbstractTreeEntry, List<String>> getActivityMap() {
		return activityMap;
	}

	public void setActivityMap(Map<AbstractTreeEntry, List<String>> activityMap) {
		this.activityMap = activityMap;
	}

	private void createActivityTree() {		
		for (UserTree userTree : activities) {
			final TreeNode userNode = new DefaultTreeNode(userTree);
			userNode.setExpanded(true);
			if (activityTreeRoot == null) {
				try {
					init();
				} catch (PortalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				activityTreeRoot.getChildren().add(userNode);
				try {
					for (ActivityTyp activityTyp : userTree == null ? new ArrayList<ActivityTyp>()
							: userTree.getActivityTypes()) {
						final TreeNode activityTypNode = new DefaultTreeNode(
								activityTyp);
						activityTypNode.setExpanded(true);
						userNode.getChildren().add(activityTypNode);
						for (ActivityEntry activityEntry : (CollectionUtils
								.isEmpty(activityTyp.getActivities()) ? new ArrayList<ActivityEntry>()
								: activityTyp.getActivities())) {
							final TreeNode activityEntryNode = new DefaultTreeNode(
									activityEntry);
							activityTypNode.getChildren()
									.add(activityEntryNode);
						}
					}
				} catch (Exception exception) {

				}
			}
		}
	}

	private void createCompetenceTreeNode(
			LearningTemplateResultSet learningTemplateResultSet) {
		try {
			final Set<String> catchWords = GraphUtil
					.getAllCatchword(learningTemplateResultSet);
			for (String catchword : catchWords) {
				competenceTreeRoot.getChildren().add(
						createTreeNodeForCatchword(
								learningTemplateResultSet.getCatchwordMap(),
								catchword));
			}
		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
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
}
