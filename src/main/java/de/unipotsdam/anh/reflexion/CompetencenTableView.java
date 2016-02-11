package de.unipotsdam.anh.reflexion;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;

import uzuzjmd.competence.shared.SuggestedCompetenceGrid;
import de.unipotsdam.anh.dao.LearningTemplateDao;

@ManagedBean(name = "competencenTableView")
@ViewScoped
public class CompetencenTableView implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private SuggestedCompetenceGrid suggestedCompetenceGrid;
	
	private String selectedLearningTemplate;
	
	@PostConstruct
	public void init() {
		selectedLearningTemplate = "new 1";
		setGridData();
	}

	public void update(String selectedLearningTemplate) {
		setSelectedLearningTemplate(selectedLearningTemplate);
		setGridData();
	}

	public SuggestedCompetenceGrid getSuggestedCompetenceGrid() {
		return suggestedCompetenceGrid;
	}

	public void setSuggestedCompetenceGrid(
			SuggestedCompetenceGrid suggestedCompetenceGrid) {
		this.suggestedCompetenceGrid = suggestedCompetenceGrid;
	}

	public String getSelectedLearningTemplate() {
		return selectedLearningTemplate;
	}

	public void setSelectedLearningTemplate(String selectedLearningTemplate) {
		this.selectedLearningTemplate = selectedLearningTemplate;
	}
	
	private void setGridData() {
		if (!StringUtils.isEmpty(selectedLearningTemplate)) {	
			final SuggestedCompetenceGrid data = LearningTemplateDao.getGridviewFromLearningTemplate(selectedLearningTemplate);	
			setSuggestedCompetenceGrid(data);
		}
	}
}
