package com.newt.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.newt.model.ProductDetail;
@Transactional
public interface ProductDetailRepository extends CrudRepository <ProductDetail, String>{
	
	@Modifying
	@Query("update  #{#entityName}  e set e.productName = ?1 where e.productId = ?2")
	int updateByName(String productName, String productId);
}
