package de.unipotsdam.anh.dto;

import java.util.List;

public class TableRow {

	private String catchWord;
	
	private List<String> competencen;

	public String getCatchWord() {
		return catchWord;
	}

	public void setCatchWord(String catchWord) {
		this.catchWord = catchWord;
	}

	public List<String> getCompetencen() {
		return competencen;
	}

	public void setCompetencen(List<String> competencen) {
		this.competencen = competencen;
	}
}
