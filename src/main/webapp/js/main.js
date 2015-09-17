// implements client site events for lernprojekte tab

$("[id$='createLearningGoalPanel']").hide();

$("[id$='addLearningGoalButton']").click(function() {
	$("[id$='addLearningGoalButton']").hide();
	$("[id$='createLearningGoalPanel']").show();
});

$("[id$='cancelCreatingLearningGoal']").click(function() {	
	$("[id$='createLearningGoalPanel']").hide();
	$("[id$='addLearningGoalButton']").show();
});

$("[id$='saveLearningGoal']").click(function() {
	setTimeout(function() {
		location.reload();
	}, 300);
});

$( "span:contains('löschen')").click(function() {
	setTimeout(function() {
		location.reload();
	}, 300);
});


//$("[id$='bearbeiten_button']").click(openTab2);

function openTab2() {		
	PrimeFaces.widgets.widget__CompetenceReflexionPortlet_WAR_competencereflexionportlet__tabViewWidget.select(1);	
}


//$('[id$="selectLearningTemplateMenu"] > option[value="'+ element  +'"]').attr("selected", "selected")




