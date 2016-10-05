var request = require("request");
var async = require("async");

var options = { method: 'POST',
  url: 'http://localhost:8991/api/runs/074c6ac9-bf34-40ab-9f01-d76fc417470d/execute/bulk/wait',
  headers: 
   { 'postman-token': '9b217331-6075-bf21-abf3-dede985a3cfe',
     'cache-control': 'no-cache',
     'content-type': 'application/json',
     accept: 'application/json',
     'x-dexiio-account': 'e7c33dd1-a7e4-44d4-885e-79728199953f',
     'x-dexiio-access': 'ae58c9c05d4f296b45487a8e69a2aa19' },
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

