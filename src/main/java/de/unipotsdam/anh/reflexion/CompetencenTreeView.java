package de.unipotsdam.anh.reflexion;

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
import javax.faces.bean.SessionScoped;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import uzuzjmd.competence.shared.dto.Graph;
import uzuzjmd.competence.shared.dto.GraphTriple;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;

@ManagedBean(name = "competencenTreeView")
@SessionScoped
public class CompetencenTreeView implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final String LABELNAME = "SuggestedCompetencePrerequisite";
	
	private Map<String, List<TreeNode>> treeNodeMap;
	private List<String> catchWords;
	
	private String selectedCompetenceToNode;
	private String selectedCompetenceFromNode;
	private TreeNode selectedNode;
	private String selectedCatchword;
	
	@PostConstruct
	public void init() {
		treeNodeMap = new HashMap<String, List<TreeNode>>();
		catchWords = new ArrayList<String>();
	}
	
	public void onNodeSelect(String catchword) {
        this.selectedCompetenceFromNode = (String) selectedNode.getParent().getData();
        this.selectedCatchword = catchword;
        System.out.println("catchword: " + selectedCatchword);
        System.out.println("parent from node: " + selectedCompetenceFromNode);
    }
	
	//TODO LearningTemplateResultSet have only GraphRoot
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

	private List<TreeNode> getTreeForCatchword(Map<GraphTriple, String[]> catchwordMap,String catchword) {
		Graph graph = getGraphForCatchword(catchwordMap, catchword);
		return getListTreeRootForGraph(graph);
	}

	private List<TreeNode> getListTreeRootForGraph(Graph graph) {
		if(graph == null) {
			return new ArrayList<TreeNode>();
		}
		
		final Map<String, TreeNode> nodes = new HashMap<String, TreeNode>();
		for(Entry<Integer, String> e: graph.getNodeIdValues().entrySet()) {
			nodes.put(e.getValue(), createTreeNode(e.getValue()));
		}
		
		final List<TreeNode> roots = new ArrayList<TreeNode>();
		for(GraphTriple t : graph.triples) {
			TreeNode fromNode = nodes.get(t.fromNode);
			TreeNode toNode = nodes.get(t.toNode);
			fromNode.getChildren().add((fromNode.getChildCount() - 1),toNode);
		}
		
		for(Entry<String, TreeNode> n : nodes.entrySet()) {
			if(n.getValue().getParent() == null) {
				roots.add(n.getValue());
			}
		}
		
		return roots;
	}

	private Graph getGraphForCatchword(Map<GraphTriple, String[]> catchwordMap, String catchword) {
		final Graph graph = new Graph();
		for(Entry<GraphTriple, String[]> entry : catchwordMap.entrySet()) {
			if(Arrays.asList(entry.getValue()).contains(catchword)) {
				GraphTriple triple = entry.getKey();
				graph.addTriple(triple.fromNode, triple.toNode, triple.label, triple.directed);
			}
		}
		return graph;
	}
	
	private TreeNode createTreeNode(String label) {
		final TreeNode node = new DefaultTreeNode(label);
		node.setExpanded(true);
		node.getChildren().add( new DefaultTreeNode("+"));
		return node;
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
}
