package uzuzjmd.competence.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

import uzuzjmd.competence.reflexion.dao.ActivityDao;
import uzuzjmd.competence.reflexion.util.AppUtil;
import uzuzjmd.competence.reflexion.util.GraphUtil;
import datastructures.trees.AbstractTreeEntry;
import datastructures.trees.ActivityEntry;
import uzuzjmd.competence.shared.activity.ActivityTyp;
import datastructures.graph.Graph;
import datastructures.graph.GraphNode;
import datastructures.graph.GraphTriple;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSet;
import uzuzjmd.competence.shared.moodle.UserTree;

@ManagedBean(name = "activityCompetenceView")
@ViewScoped
public class ActivityCompetenceView  implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private static final String MOODLE = "moodle";
	
	private List<UserTree> activities;
	private Map<AbstractTreeEntry, List<String>> activityMap;
	
	private TreeNode activityTreeRoot;
	private TreeNode competenceTreeRoot;
	
	private TreeNode[] selectedKompetenceNodes;
	private TreeNode selectedActivityNode;
	
	
	@ManagedProperty("#{userLoginView}")
	private UserLoginView userLoginView;

	public UserLoginView getUserLoginView() {
		return userLoginView;
	}
	
	public void setUserLoginView(UserLoginView userLoginView) {
		this.userLoginView = userLoginView;
	}
	
	
	
	@PostConstruct
	public void init() throws PortalException, SystemException {
		activityTreeRoot = new DefaultTreeNode("Root", null);
		competenceTreeRoot = new DefaultTreeNode("Root", null);
		
		activityMap = new HashMap<AbstractTreeEntry, List<String>>();
		activities = ActivityDao.getActivityFromCourse("15", MOODLE, 
				userLoginView.getUsername(), null, userLoginView.getPassword(), false);		
//		createActivityTree();
	}
	
	public void update(LearningTemplateResultSet learningTemplateResultSet) {
		createCompetenceTreeNode(learningTemplateResultSet);
	}
	
	public void activityCompetenceCollate(ActionEvent e) {
		final List<String> competences = new ArrayList<String>();
		for(TreeNode node : selectedKompetenceNodes) {
			competences.add(node.getData().toString());
			
			node.setSelected(false);
		}
		activityMap.put((AbstractTreeEntry) selectedActivityNode.getData(), competences);
		
		selectedActivityNode.setSelected(false);
		selectedActivityNode = null;
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
		for(UserTree userTree : activities) {
			final TreeNode userNode = new DefaultTreeNode(userTree);
			userNode.setExpanded(true);
			activityTreeRoot.getChildren().add(userNode);
			for(ActivityTyp activityTyp : userTree == null ? new ArrayList<ActivityTyp>() : userTree.getActivityTypes()) {
				final TreeNode activityTypNode = new DefaultTreeNode(activityTyp);
				activityTypNode.setExpanded(true);
				userNode.getChildren().add(activityTypNode);
				for(ActivityEntry activityEntry : (CollectionUtils.isEmpty(activityTyp.getActivities()) ? new ArrayList<ActivityEntry>() : activityTyp.getActivities())) {
					final TreeNode activityEntryNode = new DefaultTreeNode(activityEntry);
					activityTypNode.getChildren().add(activityEntryNode);
				}
			}
		}
	}
	
	private void createCompetenceTreeNode(LearningTemplateResultSet learningTemplateResultSet) {
		try {
		final Set<String> catchWords = GraphUtil.getAllCatchword(learningTemplateResultSet);
		for(String catchword : catchWords) {
			competenceTreeRoot.getChildren().add(createTreeNodeForCatchword(learningTemplateResultSet.getCatchwordMap(), catchword));
		}
		} catch (NullPointerException e ) {
			System.err.println(e.getMessage());
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
}
