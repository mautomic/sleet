# sleet

Sleet is a high performance java client for retrieving useful **free** live quote and option data from TD-Ameritrade. 
It was built in mind for high frequency trading (either generating alerts or placing orders) and therefore optimized 
in terms of memory consumption (limiting garbage collection time), thread safety, and overall request speed from the API. 

Option chain calls can be quite large, (think getting 100 OTM and ITM strikes for every expiration date for a single ticker) 
and can take a few seconds to get using the naive implementation of a synchronous HTTP call. Sleet can retrieve this data by 
breaking down the API calls into async batches running in parallel (abstracted away from caller). If there is a need to 
retrieve option prices for any sort of strategy even at a sub second interval, doing this will reduce the API response time 
to half or even quarter of what it was, into the millisecond range. Most often, the over-the-wire time is the bottleneck, 
and should be reduced as much as possible. 

`sleet-api-service` contains the interface to the TD-Ameritrade API. A client can call the methods in this package to 
query the endpoint over REST for option data.

In order to use this API client, you must first have an API key. This requires both an account with the 
[TDAmeritrade](https://www.tdameritrade.com/home.page), and an account setup on the [developer page](https://developer.tdameritrade.com). 
Once you have the accounts setup, you can create an API key, and pass it into the service classes. 
