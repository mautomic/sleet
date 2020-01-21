# sleet

TD-Ameritrade's API contains useful **free** live option data. This repository houses a java client to retrieve this data, among other tools. 

`sleet-api-service` contains the interface to the TD-Ameritrade API. A client can call the methods in this package to query the endpoint over REST for option data.

`sleet-core-tools` contains option strategies and tools that utilize data from the TD-Ameritrade API via the sleet-api-service package. *This module may be refactored to be a REST service at a future date*

In order to use this API client, you must first have an API key. This requires both an account with the [TDAmeritrade](https://www.tdameritrade.com/home.page), and an account setup on the [developer page](https://developer.tdameritrade.com). Once you have the accounts setup, you can create an API key. 

Clone repository and add a resources.cfg file in the `sleet-core-tools` resources folder. That file should contain the apiKey and logFile properties:

apiKey=test123
<br />
logFile=/path/to/location/logFile.txt
