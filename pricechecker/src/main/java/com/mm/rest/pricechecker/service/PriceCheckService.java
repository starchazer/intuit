package com.mm.rest.pricechecker.service;

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

import com.mm.rest.pricechecker.entity.PriceCheck;
import com.mm.rest.pricechecker.entity.StockQuote;

/**
 * Service class for stock price check
 * 
 * @author madhesh
 *
 */
@RestController
public class PriceCheckService {

	private static final Logger logger = LoggerFactory.getLogger(PriceCheckService.class);

	@Autowired
	private PriceCheckRepository priceCheckRepository;

	/**
	 * http://localhost:8080/pricechecker/stockquote/INTU
	 * 
	 * @param stockName
	 * @return
	 */
	@RequestMapping(value = "/pricechecker/stockquote/{stockName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StockQuote> checkPrice(@PathVariable("stockName") String stockName) {
		logger.debug("stockName: " + stockName);

		StockQuote stockQuote = callExternalStockAPI(stockName);
		
		return new ResponseEntity<StockQuote>(stockQuote, HttpStatus.OK);
	}

	protected StockQuote callExternalStockAPI(String stockName) {
		RestTemplate restTemplate = new RestTemplate();
		StockQuote stockQuote = restTemplate.getForObject("http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol=" + stockName, StockQuote.class);
		// add call to apichargesservice
		logger.debug("call APIChargesService !!");

		return stockQuote;
	}
	
	/**
	 * http://localhost:8080/pricechecker
	 * 
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/pricechecker", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<PriceCheck>> fetchAllUserInfo() {
		Iterable<PriceCheck> priceChecks = priceCheckRepository.findAll();
		if (priceChecks == null) {
			return new ResponseEntity<Iterable<PriceCheck>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Iterable<PriceCheck>>(priceChecks, HttpStatus.OK);
	}
	
	/**
	 * http://localhost:8080/pricechecker/jane
	 * 
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/pricechecker/{userName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PriceCheck> fetchUserInfo(@PathVariable("userName") String userName) {
		logger.debug("userName: " + userName);
		
		PriceCheck persistedPriceCheck = priceCheckRepository.findByUserNameIgnoreCase(userName);
		if (persistedPriceCheck == null) {
			return new ResponseEntity<PriceCheck>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<PriceCheck>(persistedPriceCheck, HttpStatus.OK);
	}
	
	/**
	 * http://localhost:8080/pricechecker
	 * 
	 * 
	 * @param priceCheck
	 * @return
	 */
	@RequestMapping(value = "/pricechecker", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PriceCheck> checkPriceChanges(@Validated @RequestBody PriceCheck priceCheck) {
		logger.debug("priceCheck:" + priceCheck);
		
		PriceCheck persistedPriceCheck = priceCheckRepository.findByUserNameAndStockNameAllIgnoreCase(priceCheck.getUserName(), priceCheck.getStockName());
		PriceCheck priceCheckToSave = priceCheck;
		if (persistedPriceCheck != null) {
			priceCheckToSave = persistedPriceCheck;
		}
		persistedPriceCheck = priceCheckRepository.save(priceCheckToSave);
		return new ResponseEntity<PriceCheck>(priceCheck, HttpStatus.OK);
	}
	
}
