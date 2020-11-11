package com.aaratech.letter.dao;

import org.springframework.data.repository.CrudRepository;

import com.aaratech.letter.model.Image;

public interface ImageRepo extends CrudRepository<Image, Long> {
	
}
