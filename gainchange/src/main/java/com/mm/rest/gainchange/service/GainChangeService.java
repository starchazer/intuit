package com.mm.rest.gainchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mm.rest.gainchange.entity.GainChange;
import com.mm.rest.gainchange.entity.PriceCheck;
import com.mm.rest.gainchange.repository.GainChangeRepository;


/**
 * Service class for gain change
 * 
 * @author madhesh
 *
 */
@RestController
public class GainChangeService {
	private static final Logger logger = LoggerFactory.getLogger(GainChangeService.class);

	@Autowired
	private GainChangeRepository gainChangeRepository;
	
	/**
	 * fetches the gain changes for all stock names available
	 * 
	 * http://localhost:8080/gainChange
	 * 
	 * @return
	 */
	@RequestMapping(value = "/gainchange", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<GainChange>> fetchGainChanges() {
		
		Iterable<GainChange> gainChanges = gainChangeRepository.findAll();
		if (gainChanges == null) {
			return new ResponseEntity<Iterable<GainChange>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Iterable<GainChange>>(gainChanges, HttpStatus.OK);
		
	}
	
	/**
	 * fetches the gain changes for the given stock name
	 * 
	 * http://localhost:8080/gainChange/INTU
	 * 
	 * @param stockName
	 * @return
	 */
	@RequestMapping(value = "/gainchange/{stockName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GainChange> fetchGainChange(@PathVariable("stockName") String stockName) {
		
		logger.debug("stockName: " + stockName);
		
		GainChange persistedGainChange = gainChangeRepository.findByStockNameIgnoreCase(stockName);
		if (persistedGainChange == null) {
			return new ResponseEntity<GainChange>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<GainChange>(persistedGainChange, HttpStatus.OK);
		
	}
	
	/**
	 * add a gain change for the given userName and stockName
	 * 
	 * http://localhost:8080/gainChange
	 * 
	 * @param gainChange
	 * @return
	 */
	@RequestMapping(value = "/gainchange", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GainChange> addNewGainChange(@Validated @RequestBody GainChange gainChange) {

		logger.debug("stockName: " + gainChange.getStockName());
		GainChange persistedGainChange = gainChangeRepository.findByUserNameAndStockNameAllIgnoreCase(gainChange.getUserName(), gainChange.getStockName());
		GainChange gainChangeToSave = gainChange;
		if (persistedGainChange != null) {
			gainChangeToSave = persistedGainChange;
		}
		persistedGainChange = gainChangeRepository.save(gainChangeToSave);
		
		// call pricecheckservice to POST a new entry to track prices
		RestTemplate restTemplate = new RestTemplate();
		PriceCheck priceCheck = new PriceCheck();
		priceCheck.setUserName(gainChange.getUserName());
		priceCheck.setStockName(gainChange.getStockName());
		restTemplate.postForObject("http://localhost:8081/pricechecker", priceCheck, PriceCheck.class);

		return new ResponseEntity<GainChange>(gainChange, HttpStatus.OK);
		
	}
	
	/**
	 * update the gain change for the given stockName
	 * 
	 * http://localhost:8080/gainChange/INTU
	 * 
	 * @param stockName
	 * @param gainChange
	 * @return
	 */
	@RequestMapping(value = "/gainchange/{stockName}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GainChange> updateGainChange(@PathVariable("stockName") String stockName, @Validated @RequestBody GainChange gainChange) {
		
		logger.debug("stockName: " + stockName);
		logger.debug("changeValue: " + gainChange.getChangeValue());
		
		GainChange persistedGainChange = gainChangeRepository.findByStockNameIgnoreCase(stockName);
		if (persistedGainChange == null) {
			return new ResponseEntity<GainChange>(HttpStatus.NOT_FOUND);
		}
		persistedGainChange.setChangeValue(gainChange.getChangeValue());
		persistedGainChange = gainChangeRepository.save(persistedGainChange);
		return new ResponseEntity<GainChange>(persistedGainChange, HttpStatus.OK);
		
	}

}
