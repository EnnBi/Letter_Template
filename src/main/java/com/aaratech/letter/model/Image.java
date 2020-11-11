package com.aaratech.letter.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Image{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	long id;
	String path;
	
	Integer width;
	
	Integer height;	
	
	public Image(String path, Integer width, Integer height) {
		super();
		this.path=path;
		this.width = width!=null?width:0;
		this.height = height!=null?height:0;
	}

	public Image() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}
	
}
