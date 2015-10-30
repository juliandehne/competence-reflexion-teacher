package de.unipotsdam.anh.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.StringUtils;

import uzuzjmd.competence.shared.dto.GraphNode;
import uzuzjmd.competence.shared.dto.GraphTriple;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import de.unipotsdam.anh.dao.LearningTemplateDao;

@ManagedBean(name = "templateCompetenceView")
@SessionScoped
public class TemplateCompetenceView implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final String LABELNAME = "SuggestedCompetencePrerequisit";
	
	@ManagedProperty(value = "#{competencenTreeView}")
	private CompetencenTreeView competencenTreeView;
	
	@ManagedProperty(value = "#{catchwordCreater}")
	private CatchwordCreater catchwordCreater;

	private String firstNewCompetence;
	private String secondNewCompetence;
	
	private String selectedDbCompetence;
	
	private LearningTemplateResultSet learningTemplateResultSet;

	@PostConstruct
	public void init() {
		learningTemplateResultSet = null;
		
		competencenTreeView.update(learningTemplateResultSet);
	}

	public void update(String learnProject) {
		learningTemplateResultSet = LearningTemplateDao.getLearningProjectTemplate(learnProject);
		
		if(learningTemplateResultSet == null) {
			learningTemplateResultSet = new LearningTemplateResultSet();
		}
		
		competencenTreeView.update(learningTemplateResultSet);
	}

	public void addNewCatchWord(ActionEvent e) {
		catchwordCreater.addNewCatchWord(learningTemplateResultSet);
		competencenTreeView.update(learningTemplateResultSet);
	}
	
	public void addNewCompetence(ActionEvent e) {
		System.out.println("add new Competence for Catchword: " + competencenTreeView.getSelectedCatchword() + ": " + firstNewCompetence + "--" + secondNewCompetence);	
		
		addBranchCompetenceAction(competencenTreeView.getSelectedCatchword(), firstNewCompetence, secondNewCompetence);
	}
	
	public void addNewCompetenceLevel(ActionEvent e) {
		final String selectedCatchword = competencenTreeView.getSelectedCatchword();
		final String selectedCompetenceFromNode = competencenTreeView.getSelectedCompetenceFromNode();
		final String selectedCompetenceToNode = competencenTreeView.getNewLevelCompetence();
		addBranchCompetenceAction(selectedCatchword, selectedCompetenceFromNode, selectedCompetenceToNode);
	}
	
	//TODO follow list is changed
	public void branchCompetenceAction(ActionEvent e) {
		final String selectedCatchword = competencenTreeView.getSelectedCatchword();
		final String selectedCompetenceFromNode = competencenTreeView.getSelectedCompetenceFromNode();
		final String selectedCompetenceToNode = competencenTreeView.getSelectedCompetenceToNode();
		
		addBranchCompetenceAction(selectedCatchword, selectedCompetenceFromNode, selectedCompetenceToNode);
	}

	public void branchCompetenceFromDbAction(ActionEvent e) {
		final String selectedCatchword = competencenTreeView.getSelectedCatchword();
		final String selectedCompetenceFromNode = competencenTreeView.getSelectedCompetenceFromNode();
		
		addBranchCompetenceAction(selectedCatchword, selectedCompetenceFromNode, selectedDbCompetence);
	}
	
	public void selecteCatchWord(String catchWord) {
		competencenTreeView.setSelectedCatchword(catchWord);
		System.out.println(competencenTreeView.getSelectedCatchword());
	}
	
	public List<String> complete(String query) {
		final List<String> results = new ArrayList<String>();
        final List<String> dbCompetencen = getDbCompetences();
        
		final Collection<String> tmp = Collections2.filter(dbCompetencen, Predicates.containsPattern(query));	
		results.addAll(tmp);
		
        return results;
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

	public String getFirstNewCompetence() {
		return firstNewCompetence;
	}

	public void setFirstNewCompetence(String firstNewCompetence) {
		this.firstNewCompetence = firstNewCompetence;
	}

	public String getSecondNewCompetence() {
		return secondNewCompetence;
	}

	public void setSecondNewCompetence(String secondNewCompetence) {
		this.secondNewCompetence = secondNewCompetence;
	}

	public CatchwordCreater getCatchwordCreater() {
		return catchwordCreater;
	}

	public void setCatchwordCreater(CatchwordCreater catchwordCreater) {
		this.catchwordCreater = catchwordCreater;
	}

	public String getSelectedDbCompetence() {
		return selectedDbCompetence;
	}

	public void setSelectedDbCompetence(String selectedDbCompetence) {
		this.selectedDbCompetence = selectedDbCompetence;
	}
	
	private void addBranchCompetenceAction(String selectedCatchword,
			String selectedCompetenceFromNode, String selectedCompetenceToNode) {
		if(StringUtils.isEmpty(selectedCompetenceFromNode) || StringUtils.isEmpty(selectedCompetenceToNode)) {
			FacesContext.getCurrentInstance().addMessage("templateHandleMessages", new FacesMessage(FacesMessage.SEVERITY_WARN, "Anfrage wird nicht ausgeführt", "Sie müssen alle Felder ausführen!"));
			return;
		}
		
		if(StringUtils.equals(selectedCompetenceFromNode, selectedCompetenceToNode)) {
			FacesContext.getCurrentInstance().addMessage("templateHandleMessages", new FacesMessage(FacesMessage.SEVERITY_WARN, "Anfrage wird nicht ausgeführt", "Die beide Kompetenzen müssen unterschiedlicht sein!"));
			return;
		}
		
		learningTemplateResultSet.getResultGraph().addTriple(selectedCompetenceFromNode, selectedCompetenceToNode, LABELNAME, true);
		final GraphTriple triple = new GraphTriple(selectedCompetenceFromNode, selectedCompetenceToNode, LABELNAME, true);
		learningTemplateResultSet.getCatchwordMap().put(triple, (String[]) Arrays.asList(selectedCatchword).toArray());

		LearningTemplateDao.createTemplate(learningTemplateResultSet);
		competencenTreeView.update(learningTemplateResultSet);
	}
	
	private List<String> getDbCompetences() {
		final List<String> competencen = new ArrayList<String>();
		for(GraphNode node : learningTemplateResultSet.getResultGraph().nodes) {
			if(!competencen.contains(node.getLabel())) {
				competencen.add(node.getLabel());
			}
		}
		return competencen;
	}
}
