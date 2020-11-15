package com.aaratech.letter.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.aaratech.letter.model.Attribute;

public interface AttributeRepo extends CrudRepository<Attribute, Long> {
	List<Attribute> findByKeyIn(List<String> keys);
	
	@Query("Select a.key from Attribute a where status = :status")
	List<String> findAllKeys(@Param("status") String status);
}
