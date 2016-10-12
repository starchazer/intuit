package com.mm.rest.pricechecker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mm.rest.pricechecker.entity.GainChange;
import com.mm.rest.pricechecker.entity.PriceCheck;
import com.mm.rest.pricechecker.entity.StockQuote;

/**
 * Scheduler job to check stock price and notify jane if change is over gain preference.
 * 
 * @author madhesh
 *
 */
@Component
public class PriceCheckerJob {
	private static final Logger logger = LoggerFactory.getLogger(PriceCheckerJob.class);

	@Autowired
	private PriceCheckRepository priceCheckRepository;

	@Autowired
	private PriceCheckService priceCheckService;

	/**
	 * run priceChecker 3 times a week day and handle market holidays inside.
	 */
	@Scheduled(cron="0 0 11,13,15 * * MON-FRI", zone="US/Eastern")
	public void priceChecker() {
		Iterable<PriceCheck> allPriceCheckEntries = priceCheckRepository.findAll();
		for (PriceCheck priceCheck : allPriceCheckEntries) {
			// add market holiday checks to ignore processing
			logger.debug("stockName: " + priceCheck.getStockName());

			StockQuote stockQuote = priceCheckService.callExternalStockAPI(priceCheck.getStockName());
			logger.debug("stockQuote: " + stockQuote);

			//call the gainchangeservice for current gainvalue
			RestTemplate restTemplate = new RestTemplate();
			GainChange gainChange = restTemplate.getForObject("http://localhost:8080/gainchange/" + priceCheck.getStockName(), GainChange.class);

			if (stockQuote.getPriceChange() > gainChange.getChangeValue()) {
				// call smsnotifier service for user+stockname
				logger.debug("call SMSNotifierService for each user+stockname");
			}
		}
	}
}
