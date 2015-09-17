package de.unipotsdam.anh.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import de.unipotsdam.anh.dao.LearningTemplateDao;

@ManagedBean(name = "learningTemplateView")
public class LearningTemplateView implements Serializable, Validator{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{templateCompetenceView}")
	private TemplateCompetenceView templateCompetenceView;
	
	private String newLearningTemplate;
	private String selectedLearningTemplate;
	
	private List<String> learningTemplates;
	
	@PostConstruct
	public void init() {
		learningTemplates = LearningTemplateDao.findAll().getData();
	}
	
	public List<String> complete(String query) {
        final List<String> results = new ArrayList<String>();
        final Collection<String> tmp = Collections2.filter(learningTemplates, Predicates.containsPattern(query));	
		results.addAll(tmp);
		
        return results;
    }
	
	public void createLearningTemplate(ActionEvent e) {
		LearningTemplateDao.createTemplate(newLearningTemplate);
		templateCompetenceView.update(newLearningTemplate);
	}
	
	public void selectLearningTemplate(ActionEvent e) {
		System.out.println(selectedLearningTemplate);
		templateCompetenceView.update(selectedLearningTemplate);
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
}
