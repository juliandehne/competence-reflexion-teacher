package uzuzjmd.competence.liferay.reflexion;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.beanutils.BeanUtils;

import uzuzjmd.competence.shared.ReflectiveAssessmentsListHolder;
import uzuzjmd.competence.shared.SuggestedCompetenceColumn;

@ManagedBean(name = "GridViewController")
@ViewScoped
public class GridViewController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 44L;
	
	@ManagedProperty("#{ReflectiveAssessmentsListHolder}")
	private ReflectiveAssessmentsListHolder holder;

	public ReflectiveAssessmentsListHolder getHolder() {
		return holder;
	}

	public void setHolder(ReflectiveAssessmentsListHolder holder) {
		this.holder = holder;
	}

	public void update(SuggestedCompetenceColumn column) {
		System.out.println("updating column:" + column.getTestOutput());
//		try {
//			BeanUtils.copyProperties(holder,
//					column.getReflectiveAssessmentListHolder());
			setHolder(column.getReflectiveAssessmentListHolder());
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void handleChange() {
		SuggestedCompetenceGridDAO.updateReflexion(getHolder());
	}

	@PostConstruct
	public void init() {
		holder = new ReflectiveAssessmentsListHolder();
		holder.init();
	}

}
