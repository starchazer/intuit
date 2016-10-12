package com.mm.rest.pricechecker.service;

import org.springframework.data.repository.CrudRepository;

import com.mm.rest.pricechecker.entity.PriceCheck;

public interface PriceCheckRepository extends CrudRepository<PriceCheck, Long>{

	PriceCheck findByUserNameAndStockNameAllIgnoreCase(String userName, String stockName);

	PriceCheck findByUserNameIgnoreCase(String userName);

}
