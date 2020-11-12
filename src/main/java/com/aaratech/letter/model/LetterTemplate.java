package com.aaratech.letter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LetterTemplate {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	long id;
	
	String name;
	
	@Column(name="BODY",columnDefinition="ntext")
	String body;	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body; 
	}

}
