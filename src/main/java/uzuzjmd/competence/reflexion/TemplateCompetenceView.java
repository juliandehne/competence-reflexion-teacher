package uzuzjmd.competence.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang3.StringUtils;

import uzuzjmd.competence.reflexion.dao.CompetenceDao;
import uzuzjmd.competence.reflexion.dao.LearningTemplateDao;
import uzuzjmd.competence.reflexion.util.AppUtil;
import uzuzjmd.competence.reflexion.util.Label;
import datastructures.graph.GraphNode;
import datastructures.graph.GraphTriple;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSet;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

@ManagedBean(name = "templateCompetenceView")
@ViewScoped
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
	
	private String tmpName;
	
	private LearningTemplateResultSet learningTemplateResultSet;
	
	private String newSubCompetence;

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
	
	public void renameCatchword(String oldName) {
		//System.out.println("rename: " + oldName);
	}
	
	public void addSubCompetence(String selectedComptence) {
		String learningTemplate = learningTemplateResultSet.getNameOfTheLearningTemplate();
		Collection<String[]> catchwords = learningTemplateResultSet.getCatchwordMap().values();
		ArrayList<String> catchwords2 = new ArrayList<String>();
		for (String[] list : catchwords) {
			catchwords2.addAll(Arrays.asList(list));
		}
		CompetenceDao.addCompetenceStringGiven(newSubCompetence,learningTemplate, catchwords2, "nicht angegeben");		
		CompetenceDao.addSubCompetence(selectedComptence, newSubCompetence);		
	}
	
	public void renameCompetence(ActionEvent e) {
		if(!AppUtil.validateNotEmptyString("Sie müssen einen Name eingeben!",tmpName)) {
			return;
		}
	    int respone = LearningTemplateDao.rename((String) competencenTreeView.getSelectedNode().getData(), tmpName, Label.COMPETENCE);
	    if(respone == 200) {
	    	learningTemplateResultSet = LearningTemplateDao.getLearningProjectTemplate(learningTemplateResultSet.getNameOfTheLearningTemplate());
	    	tmpName = null;
	    	competencenTreeView.update(learningTemplateResultSet);
	    }
	}

	public void addNewCatchWord(ActionEvent e) {
		catchwordCreater.addNewCatchWord(learningTemplateResultSet);
		competencenTreeView.update(learningTemplateResultSet);
	}
	
	public void addNewCompetence(ActionEvent e) {
		if(addBranchCompetenceAction(competencenTreeView.getSelectedCatchword(), firstNewCompetence, secondNewCompetence)) {
			firstNewCompetence = "";
			secondNewCompetence = "";
		}
	}
	
	public void addNewCompetenceLevel(ActionEvent e) {
		final String selectedCatchword = competencenTreeView.getSelectedCatchword();
		final String selectedCompetenceFromNode = competencenTreeView.getSelectedCompetenceFromNode();
		final String selectedCompetenceToNode = competencenTreeView.getNewLevelCompetence();
		if(addBranchCompetenceAction(selectedCatchword, selectedCompetenceFromNode, selectedCompetenceToNode)) {
			competencenTreeView.setNewLevelCompetence("");
		}
	}
	
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

	public String getTmpName() {
		return tmpName;
	}

	public void setTmpName(String tmpName) {
		this.tmpName = tmpName;
	}
	
	private boolean addBranchCompetenceAction(String selectedCatchword, String selectedCompetenceFromNode, String selectedCompetenceToNode) {
		if(AppUtil.validateNotEmptyString("Sie müssen alle Felder ausführen!", selectedCompetenceFromNode, selectedCompetenceToNode, selectedCatchword) && 
				AppUtil.validateNotEquals("Die beide Kompetenzen müssen unterschiedlich sein!", selectedCompetenceFromNode, selectedCompetenceToNode)) {
		
			if(validExistTriple(selectedCompetenceFromNode, selectedCompetenceToNode)) {
				AppUtil.showInfo("Kompetenzen hinzufügen:", "Dieses Lernpfad ist existiert!! Bitte versuchen Sie mit anderem Lernpfad!");
				return false;
			}
			
			final GraphTriple triple = new GraphTriple(selectedCompetenceFromNode, selectedCompetenceToNode, LABELNAME, true);
			
			learningTemplateResultSet.addTriple(triple, (String[]) Arrays.asList(selectedCatchword).toArray());
	
			int status = LearningTemplateDao.createTemplate(learningTemplateResultSet);
			if( status == 200) {
				competencenTreeView.update(learningTemplateResultSet);
				AppUtil.showInfo("Kompetenzen hinzufügen:", "Die Kompetenzen wurden erfolgreich hinzugefügt!!");
				return true;
			} else {
				learningTemplateResultSet = LearningTemplateDao.getLearningProjectTemplate(learningTemplateResultSet.getNameOfTheLearningTemplate());
				AppUtil.showError("Kompetenzen hinzufügen:", "Die Kompetenzen wurden nicht erfolgreich hinzugefügt! Bitte prüfen Sie noch mal die Eingabe!");
			}
		}
		
		return false;
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
	
	private boolean validExistTriple(String competenceFromNode, String competenceToNode) {
		for(GraphTriple triple : learningTemplateResultSet.getResultGraph().triples) {
			if(StringUtils.equals(triple.fromNode, competenceFromNode) && StringUtils.equals(triple.toNode, competenceToNode)) {
				return true;
			}
		}
		return false;
	}

	public String getNewSubCompetence() {
		return newSubCompetence;
	}

	public void setNewSubCompetence(String newSubCompetence) {
		this.newSubCompetence = newSubCompetence;
	}
}
