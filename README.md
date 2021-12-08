# nosto_demo
Please send request to https://nosto-demo-pengyu.herokuapp.com/demo/v1/currency/convert?from=USD&to=CNY&amount=1 via GET 

The currency of 'from','to' and amount can be specified by caller, e.g: https://nosto-demo-pengyu.herokuapp.com/demo/v1/currency/convert?from=EUR&to=USD&amount=100

### Currency is case-insensitive

Please request http://localhost:8086/demo/v1/currency/convert?from=USD&to=CNY&amount=1 if test in local

### Note:
The api key to the exchangeratesapi was removed from properties file for security concern. You can config your own key as environment variable to make it works in your own environment.
