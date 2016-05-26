package uzuzjmd.competence.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import uzuzjmd.competence.reflexion.util.GraphUtil;
import uzuzjmd.competence.shared.dto.Graph;
import uzuzjmd.competence.shared.dto.GraphTriple;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;

@ManagedBean(name = "competencenTreeView")
@ViewScoped
public class CompetencenTreeView implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final String ADD_LABEL = "+";
	
	private Map<String, List<TreeNode>> treeNodeMap;
	private List<String> catchWords;
	
	private String selectedCompetenceToNode;
	private String selectedCompetenceFromNode;
	private String newLevelCompetence;
	private TreeNode selectedNode;
	private String selectedCatchword;
	
	@ManagedProperty(value = "#{competencenTableView}")
	private CompetencenTableView competencenTableView;
	
	@ManagedProperty(value = "#{courseCompetenceView}")
	private CourseCompetenceView courseCompetenceView;
	
	@ManagedProperty(value = "#{activityCompetenceView}")
	private ActivityCompetenceView activityCompetenceView;
	
	@PostConstruct
	public void init() {
		treeNodeMap = new HashMap<String, List<TreeNode>>();
		catchWords = new ArrayList<String>();
	}
	
	public void onNodeSelect(String catchword) {
        if(ADD_LABEL.equals(selectedNode.getData())) {
        	this.selectedCompetenceFromNode = (String) selectedNode.getParent().getData();
            this.selectedCatchword = catchword;
            
        	if(selectedNode.getParent().getChildCount() > 1) {
        		RequestContext.getCurrentInstance().execute("PF('branchCompetenceDialog').show();");
        	} else {
        		RequestContext.getCurrentInstance().execute("PF('newLevelCompetenceDialog').show();");
        	}
        } else {
        	RequestContext.getCurrentInstance().execute("PF('editCompetenceDialog').show();");
        }
        
    }

	public void update(LearningTemplateResultSet learningTemplateResultSet) {
		if(learningTemplateResultSet == null || learningTemplateResultSet.getResultGraph() == null) {
			return;
		}
		
		treeNodeMap.clear();
		catchWords.clear();
		final Set<String> tmp = new HashSet<String>();
		for(Entry<GraphTriple, String[]> entry : learningTemplateResultSet.getCatchwordMap().entrySet()) {
			tmp.addAll(Arrays.asList(entry.getValue()));
		}
		
		catchWords.addAll(tmp);
		for(String catchword : catchWords) {
			treeNodeMap.put(catchword, getTreeForCatchword(learningTemplateResultSet.getCatchwordMap(), catchword));
		}
		
		courseCompetenceView.update(learningTemplateResultSet);
		competencenTableView.update(learningTemplateResultSet, courseCompetenceView.getCourses());
		activityCompetenceView.update(learningTemplateResultSet);
	}

	public void addNewRootTreeNode(String catchword, String newCompetence) {
		TreeNode node = createTreeNode(newCompetence);
		final List<TreeNode> nodes = treeNodeMap.get(catchword);
		if(nodes == null) {
			treeNodeMap.put(catchword, Arrays.asList(node));
		} else {
			treeNodeMap.get(catchword).add(node);
		}
	}
	
	public Map<String, List<TreeNode>> getTreeNodeMap() {
		return treeNodeMap;
	}

	public void setTreeNodeMap(Map<String, List<TreeNode>> treeNodeMap) {
		this.treeNodeMap = treeNodeMap;
	}

	public List<String> getCatchWords() {
		return catchWords;
	}

	public void setCatchWords(List<String> catchWords) {
		this.catchWords = catchWords;
	}

	public String getSelectedCompetenceToNode() {
		return selectedCompetenceToNode;
	}

	public void setSelectedCompetenceToNode(String selectedCompetenceToNode) {
		this.selectedCompetenceToNode = selectedCompetenceToNode;
	}

	public String getSelectedCompetenceFromNode() {
		return selectedCompetenceFromNode;
	}

	public void setSelectedCompetenceFromNode(String selectedCompetenceFromNode) {
		this.selectedCompetenceFromNode = selectedCompetenceFromNode;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public String getSelectedCatchword() {
		return selectedCatchword;
	}

	public void setSelectedCatchword(String selectedCatchword) {
		this.selectedCatchword = selectedCatchword;
	}

	public String getNewLevelCompetence() {
		return newLevelCompetence;
	}

	public void setNewLevelCompetence(String newLevelCompetence) {
		this.newLevelCompetence = newLevelCompetence;
	}
	
	public CompetencenTableView getCompetencenTableView() {
		return competencenTableView;
	}

	public void setCompetencenTableView(CompetencenTableView competencenTableView) {
		this.competencenTableView = competencenTableView;
	}
	
	public CourseCompetenceView getCourseCompetenceView() {
		return courseCompetenceView;
	}

	public void setCourseCompetenceView(CourseCompetenceView courseCompetenceView) {
		this.courseCompetenceView = courseCompetenceView;
	}
	
	public ActivityCompetenceView getActivityCompetenceView() {
		return activityCompetenceView;
	}

	public void setActivityCompetenceView(
			ActivityCompetenceView activityCompetenceView) {
		this.activityCompetenceView = activityCompetenceView;
	}
	
	private List<TreeNode> getTreeForCatchword(Map<GraphTriple, String[]> catchwordMap,String catchword) {
		Graph graph = GraphUtil.getGraphForCatchword(catchwordMap, catchword);
		return getListTreeRootForGraph(graph);
	}

	private List<TreeNode> getListTreeRootForGraph(Graph graph) {
		if(graph == null) {
			return new ArrayList<TreeNode>();
		}
		System.out.println(graph.toString());
		final Map<String, TreeNode> nodes = new HashMap<String, TreeNode>();
		
		for(Entry<Integer, String> e: graph.nodeIdValues.entrySet()) {
			nodes.put(e.getValue(), createTreeNode(e.getValue()));
		}
		
		final List<TreeNode> roots = new ArrayList<TreeNode>();
		for(GraphTriple t : graph.triples) {
			TreeNode fromNode = nodes.get(t.fromNode);
			TreeNode toNode = nodes.get(t.toNode);
			if(toNode.getParent() != null) {
				fromNode.getChildren().add((fromNode.getChildCount() - 1),new DefaultTreeNode(t.toNode));
			} else {
				fromNode.getChildren().add((fromNode.getChildCount() - 1),toNode);
			}
		}
		
		for(Entry<String, TreeNode> n : nodes.entrySet()) {
			if(n.getValue().getParent() == null) {
				roots.add(n.getValue());
			}
		}
		
		return roots;
	}
	
	private TreeNode createTreeNode(String label) {
		final TreeNode node = new DefaultTreeNode(label);
		node.setExpanded(true);
		node.getChildren().add( new DefaultTreeNode(ADD_LABEL));
		return node;
	}
}
