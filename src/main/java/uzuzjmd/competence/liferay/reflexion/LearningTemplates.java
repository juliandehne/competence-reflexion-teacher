package uzuzjmd.competence.liferay.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;

import uzuzjmd.competence.shared.SuggestedCompetenceGrid;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

@ManagedBean(name = "LearningTemplates")
@ViewScoped
public class LearningTemplates implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> learningTemplates;
	private String selectedCompetence; // the one to persist in lernprojekte tab
	private String selectedLearningTemplate; // the one to select the grid for
	
	private SuggestedCompetenceGrid suggestedCompetenceGrid;

	public List<String> getLearningTemplates() {
		return learningTemplates;
	}

	public void setLearningTemplates(List<String> learningTemplates) {
		this.learningTemplates = learningTemplates;
	}

	@PostConstruct
	public void init() {
		learningTemplates = new ArrayList<String>();
	}

	public void updateGrid(ComponentSystemEvent event) throws UniformInterfaceException, ClientHandlerException, PortalException, SystemException {
		if (selectedLearningTemplate != null) {
			SuggestedCompetenceGrid data = SuggestedCompetenceGridDAO
					.getGrid(selectedLearningTemplate);			
			setSuggestedCompetenceGrid(data);
		} else {
			List<String> selectedTemplates = SelectedLearningTemplateDAO.findAll()
					.getData();
			if (selectedTemplates != null) {
				learningTemplates.addAll(selectedTemplates);
				if (!selectedTemplates.isEmpty()) {
					SuggestedCompetenceGrid data = SuggestedCompetenceGridDAO
							.getGrid(selectedTemplates.get(0));					
					setSuggestedCompetenceGrid(data);
				}
			}
		}
	}

	public void setSuggestedCompetenceGrid(
			SuggestedCompetenceGrid suggestedCompetenceGrid) {
		this.suggestedCompetenceGrid = suggestedCompetenceGrid;
	}

	public SuggestedCompetenceGrid getSuggestedCompetenceGrid() {
		return suggestedCompetenceGrid;
	}

	public void addTemplate(ActionEvent e) {
		SelectedLearningTemplateDAO.persist(getSelectedCompetence());
//		while(learningTemplates.contains(selectedCompetence)) {
//			learningTemplates.remove(selectedCompetence);
//		}
//		learningTemplates.add(selectedCompetence);
		//learningTemplates = new ArrayList<String>(new LinkedHashSet<String>(learningTemplates));		
	}

	public void deleteTemplate1(String todelete) {
		SelectedLearningTemplateDAO.delete(todelete);
//		while(learningTemplates.contains(selectedCompetence)) {
//			learningTemplates.remove(todelete);
//		}				
	}

	public String getSelectedCompetence() {
		return selectedCompetence;
	}

	public void setSelectedCompetence(String selectedCompetence) {
		this.selectedCompetence = selectedCompetence;
	}

	public void setSelectedLearningTemplate(String selectedLearningTemplate) {
		this.selectedLearningTemplate = selectedLearningTemplate;
	}

	public String getSelectedLearningTemplate() {
		return selectedLearningTemplate;
	}

	// public void processValueChange(AjaxBehaviorEvent event)
	// throws AbortProcessingException {
	// System.out.println("value change listener called");
	// System.out.println("selectedItem: " + selectedLearningTemplate);
	// setSuggestedCompetenceGrid(SuggestedCompetenceGridDAO.getGrid(selectedLearningTemplate));
	// RequestContext.getCurrentInstance().update("gridView");
	// }

	// @Override
	// public void processValueChange(ValueChangeEvent event)
	// throws AbortProcessingException {
	// System.out.println("prozessing event change: " + event.getNewValue());
	// System.out.println("selectedItem: " + selectedLearningTemplate);
	// }
}
