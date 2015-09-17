package de.unipotsdam.anh.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import uzuzjmd.competence.shared.dto.GraphNode;
import uzuzjmd.competence.shared.dto.GraphTriple;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

@ManagedBean(name = "templateCompetenceView")
@SessionScoped
public class TemplateCompetenceView implements Serializable{

	private static final long serialVersionUID = 1L;
	private String learnProject;
	
	private Map<String, List<String>> competencen;
	
	private List<String> catchWords;
	
	private String newCatchWord;
	private String newCompetence;
	
	private String selectedCatchword;
	
	private String selectedCompetenceToNode;
	private String selectedCompetenceFromNode;
	
	private Graph graph;
	
	private TreeNode root;

	@PostConstruct
	public void init() {
		graph = new Graph();
		root = new DefaultTreeNode("Root", null);
		
		competencen = new HashMap<String, List<String>>();
		catchWords = new ArrayList<String>();
		
		String[] test = {"java","pascal","C++"};
		catchWords.addAll(Arrays.asList(test));
	}
	
	public void update(String learnProject) {
		this.learnProject = learnProject;
	}
	
	public void addNewCatchWord(ActionEvent e) {
		catchWords.add(newCatchWord);
	}
	
	public void addNewCompetence(ActionEvent e) {
		final List<String> newCompetencen = new ArrayList<String>();
		newCompetencen.add(newCompetence);
		
		List<String> competencenFromCatchWord = competencen.get(selectedCatchword);
		if(competencenFromCatchWord == null) {
			competencen.put(selectedCatchword, newCompetencen);
		} else {
			competencenFromCatchWord.addAll(newCompetencen);
		}
	}
	
	public void selecteCatchWord(String catchWord) {
		this.selectedCatchword = catchWord;
		System.out.println(selectedCatchword);
	}
	
	public void selecteCompetence(String competence) {
		this.selectedCompetenceFromNode = competence;
		System.out.println(selectedCompetenceFromNode);
	}
	
	public List<String> complete(String query) {
		final List<String> results = new ArrayList<String>();
        final List<String> dbCompetencen = new ArrayList<String>();
        
        for(Entry<String, List<String>> entry : competencen.entrySet()) {
        	dbCompetencen.addAll(entry.getValue());
        }
        
		final Collection<String> tmp = Collections2.filter(dbCompetencen, Predicates.containsPattern(query));	
		results.addAll(tmp);
		
        return results;
	}
	
	public void branchCompetenceAction(ActionEvent e) {
		System.out.println("From node: " + selectedCompetenceFromNode);
		System.out.println("To Node: " + selectedCompetenceToNode);
		
		graph.addTriple(selectedCompetenceFromNode, selectedCompetenceToNode, "", true);
		
		convertGraphToTree();
	}
	
	private void convertGraphToTree() {
		Map<Integer, DefaultTreeNode> nodes = new HashMap<Integer, DefaultTreeNode>();
		for(GraphNode n : graph.nodes) {
			nodes.put(n.getId(), new DefaultTreeNode(n.getLabel()));
		}
		int n = 0;
		root.getChildren().clear();
		for(GraphTriple t : graph.triples) {
			if(n == 0) {
				root.getChildren().add(nodes.get(t.getNode1id()));
				n++;
			}
			nodes.get(t.getNode1id()).getChildren().add(nodes.get(t.getNode2id()));
		}
	}

	public String getLearnProject() {
		return learnProject;
	}

	public void setLearnProject(String learnProject) {
		this.learnProject = learnProject;
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

	public String getNewCompetence() {
		return newCompetence;
	}

	public void setNewCompetence(String newCompetence) {
		this.newCompetence = newCompetence;
	}

	public Map<String, List<String>> getCompetencen() {
		return competencen;
	}

	public void setCompetencen(Map<String, List<String>> competencen) {
		this.competencen = competencen;
	}

	public String getSelectedCatchword() {
		return selectedCatchword;
	}

	public void setSelectedCatchword(String selectedCatchword) {
		this.selectedCatchword = selectedCatchword;
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

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}
	
}
