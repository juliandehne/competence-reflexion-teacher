package uzuzjmd.competence.reflexion;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import uzuzjmd.competence.reflexion.dao.LearningTemplateDao;
import uzuzjmd.competence.reflexion.util.AppUtil;
import datastructures.graph.Graph;
import datastructures.graph.GraphTriple;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSet;

@ManagedBean(name = "catchwordCreater")
@ViewScoped
public class CatchwordCreater implements Serializable{

	private static final long serialVersionUID = 1L;

	public static final String LABELNAME = "SuggestedCompetencePrerequisite";
	
	private String newCatchWord;
	private String newOperation;
	private String firstCompetence;
	private String secondCompetence;

	public void addNewCatchWord(LearningTemplateResultSet learningTemplateResultSet) {
		if(AppUtil.validateNotEmptyString("Sie müssen alle Felder ausfühlen!", newCatchWord, newOperation,firstCompetence, secondCompetence)
				&& AppUtil.validateNotEquals("Die beide Kompetenzen müssen unterschiedlich sein!", firstCompetence, secondCompetence)) {
			if(learningTemplateResultSet != null) {
				final GraphTriple graphTriple = new GraphTriple(firstCompetence, secondCompetence, LABELNAME, true);
				final Graph graph = learningTemplateResultSet.getResultGraph() == null ? new Graph() : learningTemplateResultSet.getResultGraph();
				graph.addTriple(firstCompetence, secondCompetence, LABELNAME, true);
				learningTemplateResultSet.getCatchwordMap().put(graphTriple, new String[]{newCatchWord});
				
				if(LearningTemplateDao.createTemplate(learningTemplateResultSet) == 200) {
					AppUtil.showInfo("Thema hinzufügen: ", "Thema ''" + newCatchWord + "'' wird erfolgreich hinzugefügt!");
					resetValue();
				} else {
					AppUtil.showError("Thema hinzufügen:", "Thema ''" + newCatchWord + "'' wird erfolgreich hinzugefügt! Bitte prüfen Sie noch mal die Eingabe!");
				}
				
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry unknown error!", "Contact admin."));
			}
		}
	}

	public String getNewCatchWord() {
		return newCatchWord;
	}

	public void setNewCatchWord(String newCatchWord) {
		this.newCatchWord = newCatchWord;
	}

	public String getNewOperation() {
		return newOperation;
	}

	public void setNewOperation(String newOperation) {
		this.newOperation = newOperation;
	}

	public String getFirstCompetence() {
		return firstCompetence;
	}

	public void setFirstCompetence(String firstCompetence) {
		this.firstCompetence = firstCompetence;
	}

	public String getSecondCompetence() {
		return secondCompetence;
	}

	public void setSecondCompetence(String secondCompetence) {
		this.secondCompetence = secondCompetence;
	}

	private void resetValue() {
		newCatchWord = "";
		newOperation = "";
		firstCompetence = "";
		secondCompetence = "";
	}
	
}
