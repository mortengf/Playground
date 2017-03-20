var _ = require('lodash');
var request = require('request');
var Q = require('q');

function callAPI(offset) {
    var options = {
        "indexId": "4f979900-2c58-4e15-b70e-72d753b0fc95",
        "offset": offset,
        "limit": 10,
        "query": {},
        "sorting": {}
    };

    return Q.Promise(function(resolve, reject) {
        request({
            headers: {
                'X-DexiIO-Account': 'xxx',
                'X-DexiIO-User': 'xxx',
                'X-DexiIO-Access': 'xxx',
                'Content-Type': 'application/json'
            },
            method: 'POST',
            url: 'http://localhost:8080/rest/datasets/rows/search',
            body: JSON.stringify(options)
        }, function (error, response, body) {
            if (error) {
                return reject(error);
            }

            //console.log('error:', error);
            //console.log('statusCode:', response && response.statusCode);
            //console.log('body:', JSON.parse(body));
            var parsedBody = JSON.parse(body);

            var rowIds = [];
            if (parsedBody.rows) {
                parsedBody.rows.forEach(function (parsedRow) {
                    rowIds.push(parsedRow._id);
                });
            }

            return resolve(rowIds);
        });
    });
}

var promises = [];
for (var i=0; i<50; i+=10) {
    var promise = callAPI(0);
    promises.push(promise);
}

Q.allSettled(promises).then(function (results) {
        for (var currentIndex=0; currentIndex<results.length-1; currentIndex++) {
            var currentResult = results[currentIndex];
            var nextIndex = currentIndex + 1;

            if (currentResult.state === "fulfilled") {
                if (nextIndex <= results.length) {
                    var nextResult = results[nextIndex];
                    var intersection = _.intersection(currentResult.value, nextResult.value);
                    if (intersection.length > 0) {
                        console.log('Intersection between result[' + currentIndex + '] and result[' + nextIndex + '] contains: ' + intersection.length + ' elements');
                        intersection.forEach(function (element) {
                            console.log(element);
                        });
                    }
                }
            } else {
                var reason = currentResult.reason;
                throw new Error(reason);
            }
        }
    });

