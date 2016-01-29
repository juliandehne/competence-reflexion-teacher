package de.unipotsdam.anh.util;

public enum Label {

	CATCHWORD("Catchword"),
	COMPETENCE("Competence"),
	LEARNING_PROJECT_TEMPLATE("LearningProjectTemplate");
	
	private final String labelString;
	
	private Label(String labelString) {
		this.labelString = labelString;
	}

	public String getLabelString() {
		return labelString;
	}
}
