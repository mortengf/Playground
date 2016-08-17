var request = require("request");
var async = require("async");

var options = { method: 'POST',
  url: 'http://staging.dexi.io:8991/api/runs/a93795ea-737c-4f1e-93a7-a1707b36f012/execute/bulk/wait',
  headers: 
   { 'postman-token': '9b217331-6075-bf21-abf3-dede985a3cfe',
     'cache-control': 'no-cache',
     'content-type': 'application/json',
     accept: 'application/json',
     'x-dexiio-account': '59e86a67-d035-4e4a-bc56-7c2da5e8c908',
     'x-dexiio-access': 'fa2171640322611eb59e2d958d93b062' },
  body: [ { hest: '3343343' }, { hest: '11' }, { hest: '77777' } ],
  json: true };

async.parallel([

  function(callback) {
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    }), 
    callback(null, 'request 1 done');
  },
  function(callback) {
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    }),  
    callback(null, 'request 2 done');
  },
  function(callback) {
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    }),  
    callback(null, 'request 3 done');
  },
  function(callback) {
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    }),  
    callback(null, 'request 4 done');
  },
  function(callback) {
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    }),  
    callback(null, 'request 5 done');
  },
  function(callback) {
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    }),  
    callback(null, 'request 6 done');
  },
  function(callback) {
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    }),  
    callback(null, 'request 7 done');
  },
  function(callback) {
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    }),  
    callback(null, 'request 8 done');
  },
  function(callback) {
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    }),  
    callback(null, 'request 9 done');
  },
  function(callback) {
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    }),
    callback(null, 'request 10 done');
  },
  function(callback) {
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    }),
    callback(null, 'request 11 done');
  },
  function(callback) {
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    }),
    callback(null, 'request 12 done');
  },
  function(callback) {
    request(options, function (error, response, body) {
      if (error) throw new Error(error);
      console.log(body);
    }),
    callback(null, 'request 13 done');
  }


  ],
  function (err, res) {
    console.log('err: ' + JSON.stringify(err));	
    console.log('res: ' + JSON.stringify(res));
  }
);

