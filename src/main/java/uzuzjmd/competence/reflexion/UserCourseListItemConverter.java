package uzuzjmd.competence.reflexion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.commons.lang3.StringUtils;
import uzuzjmd.competence.shared.moodle.UserCourseListItem;

@FacesConverter(value = "userCourseListItemConverter")
public class UserCourseListItemConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (StringUtils.isEmpty(value)) {			
			return null;
		}
		final CourseCompetenceView view = (CourseCompetenceView) context
				.getViewRoot().getViewMap().get("courseCompetenceView");
		UserCourseListItem result = null;
		for (UserCourseListItem c : view.getCourses()) {	
			if (StringUtils.equals(value, c.getName())) {
				result = c;
			}
		}		
		return result;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null) {			
			return null;
		}
		String result = ((UserCourseListItem) value).getName();		
		return result;
	}

}
