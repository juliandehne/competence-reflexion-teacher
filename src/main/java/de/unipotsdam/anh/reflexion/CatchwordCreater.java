package de.unipotsdam.anh.reflexion;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import uzuzjmd.competence.shared.dto.Graph;
import uzuzjmd.competence.shared.dto.GraphTriple;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;
import de.unipotsdam.anh.dao.LearningTemplateDao;

@ManagedBean(name = "catchwordCreater")
@SessionScoped
public class CatchwordCreater implements Serializable{

	private static final long serialVersionUID = 1L;

	public static final String LABELNAME = "SuggestedCompetencePrerequisite";
	
	private String newCatchWord;
	private String newOperation;
	private String firstCompetence;
	private String secondCompetence;

	public void addNewCatchWord(LearningTemplateResultSet learningTemplateResultSet) {
		if(StringUtils.isEmpty(newCatchWord) || StringUtils.isEmpty(newOperation) || StringUtils.isEmpty(firstCompetence) || StringUtils.isEmpty(secondCompetence)) {
			FacesContext.getCurrentInstance().addMessage("NewCatchwordCreateMessages", new FacesMessage(FacesMessage.SEVERITY_WARN, "Vorsicht!", "Sie müssen alle Felder ausfühlen!"));
		} else if(StringUtils.equals(firstCompetence, secondCompetence)){
			FacesContext.getCurrentInstance().addMessage("NewCatchwordCreateMessages", new FacesMessage(FacesMessage.SEVERITY_WARN, "Vorsicht!", "Die beide Kompetenze müssen unterschiedlich sein!"));
		} else {
			if(learningTemplateResultSet != null) {
				final GraphTriple graphTriple = new GraphTriple(firstCompetence, secondCompetence, LABELNAME, true);
				final Graph graph = learningTemplateResultSet.getResultGraph() == null ? new Graph() : learningTemplateResultSet.getResultGraph();
				graph.addTriple(firstCompetence, secondCompetence, LABELNAME, true);
				learningTemplateResultSet.getCatchwordMap().put(graphTriple, new String[]{newCatchWord});
				
				System.out.println(learningTemplateResultSet.getResultGraph().toString());
				LearningTemplateDao.createTemplate(learningTemplateResultSet);
				
				resetValue();
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
