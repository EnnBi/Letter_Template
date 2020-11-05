package com.aaratech.letter.dao;

import org.springframework.data.repository.CrudRepository;

import com.aaratech.letter.model.LetterTemplate;

public interface LetterTemplateRepo extends CrudRepository<LetterTemplate, Long> {

	public LetterTemplate findByName(String name);
}
