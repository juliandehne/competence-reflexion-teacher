package de.unipotsdam.anh.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import uzuzjmd.competence.shared.dto.Graph;
import uzuzjmd.competence.shared.dto.GraphTriple;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;

@ManagedBean(name = "competencenTreeView")
@SessionScoped
public class CompetencenTreeView implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Map<String, List<TreeNode>> treeNodeMap;
	private List<String> catchWords;
	
	private String newCatchWord;
	
	@PostConstruct
	public void init() {
		treeNodeMap = new HashMap<String, List<TreeNode>>();
		catchWords = new ArrayList<String>();
	}
	
	public void addNewCatchWord(ActionEvent e) {
		catchWords.add(newCatchWord);
	}
	
	public void update(LearningTemplateResultSet learningTemplateResultSet) {
		treeNodeMap.clear();
		catchWords.clear();
		
		if(learningTemplateResultSet == null) {
			return;
		}
		updateListAndMap(learningTemplateResultSet);
		
		// test all graph:
		catchWords.add("all");
		treeNodeMap.put("all", getListTreeRootForGraph(learningTemplateResultSet.getResultGraph()));
	}

	private void updateListAndMap(LearningTemplateResultSet learningTemplateResultSet) {
		for(Entry<GraphTriple, String[]> entry : learningTemplateResultSet.getCatchwordMap().entrySet()) {
			for(String catchword : entry.getValue()) {
				if(!catchWords.contains(catchword) && !"all".equals(catchword)) {
					catchWords.add(catchword);
					treeNodeMap.put(catchword, getTreeForCatchword(learningTemplateResultSet.getCatchwordMap(), catchword));
				}	
			}
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

	public String getNewCatchWord() {
		return newCatchWord;
	}

	public void setNewCatchWord(String newCatchWord) {
		this.newCatchWord = newCatchWord;
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
//			roots.remove(nodes.get(t.toNode));
//			if(!roots.contains(nodes.get(t.fromNode)))
//				roots.add(nodes.get(t.fromNode));
			nodes.get(t.fromNode).getChildren().add(0,nodes.get(t.toNode));
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
		TreeNode node = new DefaultTreeNode(label);
		node.setExpanded(true);
		node.getChildren().add(new DefaultTreeNode("+"));
		
		return node;
	}
}
