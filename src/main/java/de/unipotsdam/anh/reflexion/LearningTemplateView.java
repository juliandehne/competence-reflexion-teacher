package de.unipotsdam.anh.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import uzuzjmd.competence.shared.StringList;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

import de.unipotsdam.anh.dao.LearningTemplateDao;
import de.unipotsdam.anh.util.AppUtil;

@ManagedBean(name = "learningTemplateView")
@ViewScoped
public class LearningTemplateView implements Serializable, Validator{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{templateCompetenceView}")
	private TemplateCompetenceView templateCompetenceView;
	
	private String newLearningTemplate;
	private String selectedLearningTemplate;
	
	private List<String> learningTemplates;
	
	@PostConstruct
	public void init() {
		fetchAllLearningTemplate();
	}
	
	public List<String> complete(String query) {
        final List<String> results = new ArrayList<String>();
        final Collection<String> tmp = Collections2.filter(learningTemplates, Predicates.containsPattern(query));	
		results.addAll(tmp == null ? new ArrayList<String>() : tmp);
		
		if(results.size() == 0) {
			FacesContext.getCurrentInstance().addMessage( "autocompleteMessage", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info: ", "Es gibt kein Lernprojekt,den Sie auswählen können!"));
		}
        return results;
    }
	
	public void createLearningTemplate(ActionEvent e) {
		if(AppUtil.validateNotEmptyString("Sie müssen einen Lernprojektname eingeben!!", newLearningTemplate)) {
			LearningTemplateResultSet learningTemplateResultSet = new LearningTemplateResultSet();
			learningTemplateResultSet.setNameOfTheLearningTemplate(newLearningTemplate);
			LearningTemplateDao.createTemplate(learningTemplateResultSet);
			templateCompetenceView.update(newLearningTemplate);
			AppUtil.showInfo("Template erstellen", "Lernprojekt " + newLearningTemplate + " wird erfolgreich erstellt!");
			fetchAllLearningTemplate();
			newLearningTemplate = "";
		}
	}
	
	public void selectLearningTemplate(ActionEvent e) {
		if(StringUtils.isEmpty(selectedLearningTemplate)) {
			FacesContext.getCurrentInstance().addMessage("selecteCompetenceMessages", new FacesMessage(FacesMessage.SEVERITY_WARN, "Anfrage nicht ausgeführt!", "Sie müssen eine Template auswählen!"));
		} else {
			templateCompetenceView.update(selectedLearningTemplate);
			System.out.println(selectedLearningTemplate);
			selectedLearningTemplate = "";
		}
		
	}
	
	public void deleteLearningTemplate(String learningTemplate) {
		if(!StringUtils.isEmpty(learningTemplate)) {
			if(LearningTemplateDao.deleteTemplate(learningTemplate) == 200) {
				templateCompetenceView.setLearningTemplateResultSet(null);
				fetchAllLearningTemplate();
			}
		}
	}

	public String getNewLearningTemplate() {
		return newLearningTemplate;
	}

	public void setNewLearningTemplate(String newLearningTemplate) {
		this.newLearningTemplate = newLearningTemplate;
	}

	public String getSelectedLearningTemplate() {
		return selectedLearningTemplate;
	}

	public void setSelectedLearningTemplate(String selectedLearningTemplate) {
		this.selectedLearningTemplate = selectedLearningTemplate;
	}

	public List<String> getLearningTemplates() {
		return learningTemplates;
	}

	public void setLearningTemplates(List<String> learningTemplates) {
		this.learningTemplates = learningTemplates;
	}

	public TemplateCompetenceView getTemplateCompetenceView() {
		return templateCompetenceView;
	}

	public void setTemplateCompetenceView(
			TemplateCompetenceView templateCompetenceView) {
		this.templateCompetenceView = templateCompetenceView;
	}

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		if (!learningTemplates.contains(arg2)) {			
			throw new ValidatorException(new FacesMessage("EingabeFehler: Es gibt kein Lernziel mit diesem Namen"));
		}	
		
	}
	
	private void fetchAllLearningTemplate() {
		StringList result = LearningTemplateDao.findAll();
		final List<String> tmp = result == null ? new ArrayList<String>() : result.getData();
		learningTemplates = CollectionUtils.isEmpty(tmp) ? new ArrayList<String>() : tmp;
		Iterables.removeIf(learningTemplates, Predicates.isNull());
	}
}
