# intuit

#GainChangeService

- Uses GainChange table to store id, userName, stockName and changeValue
- Endpoint to list gain change values for all stock names stored in GainChange table
- Endpoint to fetch gain change value for given stock name
- Endpoint to add new gain change value for given stock name
  - Calls PriceCheckerService to add a new entry to track
- Endpoint to update gain change value for given stock name

#PriceCheckerService
- Uses PriceCheck table to store id, userName, stockName
- Endpoint to fetch stock quote of given stock symbol from a public api
  - Log each call to the stock API service by notifying the APIChargesService
- Endpoint to receive and store stock name data in PriceCheck table
- Scheduler job that does the following:
  - fetch stock quote of each stock symbol
  - compare with changeValue from GainChangeService and
  - call SMS Notifier service to notify of gain if change is greater
