package de.unipotsdam.anh.reflexion;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;

import uzuzjmd.competence.shared.DESCRIPTORType;

@ManagedBean(name = "descriptorTypeView")
@RequestScoped
public class DescriptorTypeView implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String competenceHeader;
	private DESCRIPTORType newDescriptorType;

	@PostConstruct
	public void init() {
		newDescriptorType = new DESCRIPTORType();
	}
	
	public DESCRIPTORType getNewDescriptorType() {
		return newDescriptorType;
	}

	public void setNewDescriptorType(DESCRIPTORType newDescriptorType) {
		this.newDescriptorType = newDescriptorType;
	}

	public String getCompetenceHeader() {
		return competenceHeader;
	}

	public void setCompetenceHeader(String competenceHeader) {
		this.competenceHeader = competenceHeader;
	}
	
	public void createDescriptorType(ActionEvent e) {
		System.out.println("### create DescriptorType: ###");
		System.out.println(newDescriptorType.toString());
	}
}
