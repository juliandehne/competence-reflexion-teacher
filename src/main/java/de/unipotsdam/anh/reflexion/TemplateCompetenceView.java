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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.model.TreeNode;

import uzuzjmd.competence.shared.dto.Graph;
import uzuzjmd.competence.shared.dto.GraphTriple;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

@ManagedBean(name = "templateCompetenceView")
@SessionScoped
public class TemplateCompetenceView implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final String LABELNAME = "SuggestedCompetencePrerequisit";
	
	@ManagedProperty(value = "#{competencenTreeView}")
	private CompetencenTreeView competencenTreeView;
	
	private Map<String, List<String>> competencen;

	private String newCompetence;
	
	private String selectedCatchword;
	
	private String selectedCompetenceToNode;
	private String selectedCompetenceFromNode;
	private TreeNode selectedNode;
	
	private LearningTemplateResultSet learningTemplateResultSet;

	@PostConstruct
	public void init() {
		learningTemplateResultSet = null;
		competencen = new HashMap<String, List<String>>();
		
		competencenTreeView.update(learningTemplateResultSet);
	}

	//TODO delete test method "getLearningTemplateTestData"
	public void update(String learnProject) {
//		learningTemplateResultSet = LearningTemplateDao.getLearningProjectTemplate(learnProject);
		learningTemplateResultSet = getLearningTemplateTestData();
		competencen.clear();
		
		if(learningTemplateResultSet == null) {
			learningTemplateResultSet = new LearningTemplateResultSet();
		}
		
		competencenTreeView.update(learningTemplateResultSet);
	}

	public void onNodeSelect(String catchword) {
        this.selectedCompetenceFromNode = (String) selectedNode.getParent().getData();
        this.selectedCatchword = catchword;
        System.out.println("catchword: " + selectedCatchword);
        System.out.println("parent from node: " + selectedCompetenceFromNode);
    }
	
	//TODO create learningTemplateResultSet for one competence and update server
	public void addNewCompetence(ActionEvent e) {
		System.out.println("add new Competence for Catchword: " + selectedCatchword + ": " + newCompetence);	
		competencenTreeView.addNewRootTreeNode(selectedCatchword, newCompetence);
	}
	
	//TODO follow list is changed and need to update server
	public void branchCompetenceAction(ActionEvent e) {
		System.out.println("From node:" + selectedCompetenceFromNode);
		System.out.println("To node:" + selectedCompetenceToNode);
		
		learningTemplateResultSet.getResultGraph().addTriple(selectedCompetenceFromNode, selectedCompetenceToNode, LABELNAME, true);
		final GraphTriple triple = new GraphTriple(selectedCompetenceFromNode, selectedCompetenceToNode, LABELNAME, true);
		learningTemplateResultSet.getCatchwordMap().put(triple, (String[]) Arrays.asList(selectedCatchword).toArray());

		competencenTreeView.update(learningTemplateResultSet);
		
		System.out.println(learningTemplateResultSet.getResultGraph());
	}
	
	public void selecteCatchWord(String catchWord) {
		this.selectedCatchword = catchWord;
		System.out.println(selectedCatchword);
	}
	
	//TODO get all competence from database
	public List<String> complete(String query) {
		final List<String> results = new ArrayList<String>();
        final List<String> dbCompetencen = getDbCompetenceTestData();
        
        for(Entry<String, List<String>> entry : competencen.entrySet()) {
        	dbCompetencen.addAll(entry.getValue());
        }
        
		final Collection<String> tmp = Collections2.filter(dbCompetencen, Predicates.containsPattern(query));	
		results.addAll(tmp);
		
        return results;
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

	public LearningTemplateResultSet getLearningTemplateResultSet() {
		return learningTemplateResultSet;
	}

	public void setLearningTemplateResultSet(
			LearningTemplateResultSet learningTemplateResultSet) {
		this.learningTemplateResultSet = learningTemplateResultSet;
	}

	public CompetencenTreeView getCompetencenTreeView() {
		return competencenTreeView;
	}

	public void setCompetencenTreeView(CompetencenTreeView competencenTreeView) {
		this.competencenTreeView = competencenTreeView;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}
	
	//TODO Test data, to delete
	private LearningTemplateResultSet getLearningTemplateTestData() {
		final Graph graph = new Graph();
		graph.addTriple("using tags", "using JSP tags", LABELNAME, true);
		graph.addTriple("using JSP tags", "using primfaces tags", LABELNAME,
				true);
		graph.addTriple("using JSP tags", "using faces tags", LABELNAME, true);
		graph.addTriple("programming", "creating api", LABELNAME, true);
		graph.addTriple("creating api",
				"being the first person to generate a universal api",
				LABELNAME, true);

		GraphTriple first = new GraphTriple("using tags", "using JSP tags",
				LABELNAME, true);
		GraphTriple second = new GraphTriple("using JSP tags",
				"using primfaces tags", LABELNAME, true);
		GraphTriple third = new GraphTriple("programming", "creating api",
				LABELNAME, true);
		GraphTriple fourth = new GraphTriple("using JSP tags",
				"using faces tags", LABELNAME, true);
		GraphTriple fifth = new GraphTriple("creating api",
				"being the first person to generate a universal api",
				LABELNAME, true);

		HashMap<GraphTriple, String[]> map = new HashMap<GraphTriple, String[]>();
		map.put(first, new String[] { "programming", "jsp" });
		map.put(second, new String[] { "programming", "jsp" });
		map.put(fourth, new String[] { "programming", "jsp" });
		map.put(third, new String[] { "programming", "api" });
		map.put(fifth, new String[] { "programming", "api", "universality" });

		LearningTemplateResultSet learningTemplateResultSet = new LearningTemplateResultSet(graph, map,
				"TestLernprojekt");
		
		return learningTemplateResultSet;
	}
	
	private List<String> getDbCompetenceTestData() {
		final List<String> competencen = new ArrayList<String>();
		competencen.add("using tags");
		competencen.add("using JSP tags");
		competencen.add("creating api");
		competencen.add("programming");
		competencen.add("being the first person to generate a universal api");
		return competencen;
	}
}
