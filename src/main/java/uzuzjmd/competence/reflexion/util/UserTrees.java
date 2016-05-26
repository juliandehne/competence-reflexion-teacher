package uzuzjmd.competence.reflexion.util;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import uzuzjmd.competence.shared.dto.UserTree;

@XmlRootElement
public class UserTrees {

	private List<UserTree> activity;

	public List<UserTree> getActivity() {
		return activity;
	}

	public void setActivity(List<UserTree> activity) {
		this.activity = activity;
	}
}
