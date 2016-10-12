package com.mm.rest.gainchange.repository;

import org.springframework.data.repository.CrudRepository;

import com.mm.rest.gainchange.entity.GainChange;

public interface GainChangeRepository extends CrudRepository<GainChange, Long> {

	GainChange findByStockNameIgnoreCase(String stockName);

	GainChange findByUserNameAndStockNameAllIgnoreCase(String userName, String stockName);

}
