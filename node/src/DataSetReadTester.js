var _ = require('lodash');
var request = require('request');
var Q = require('q');

function searchElasticsearch(offset) {
    var esRequest = {
        "from": offset,
        "size": 10,
        "query":{"match_all":{}}
    };

    return Q.Promise(function(resolve, reject) {
        request({
            method: 'POST',
            url: 'http://<host>:<port>/<index>/_search?preference=_primary',
            body: JSON.stringify(esRequest)
        }, function (error, response, body) {
            if (error) {
                console.log(error);
                return reject(error);
            }

            //console.log('error:', error);
            //console.log('statusCode:', response && response.statusCode);
            //console.log('body:', JSON.parse(body));
            var parsedBody = JSON.parse(body);

            var rowIds = [];
            if (parsedBody.hits && parsedBody.hits.hits) {
                parsedBody.hits.hits.forEach(function (hit) {
                    //console.log(JSON.stringify(hit));
                    rowIds.push(hit._id);
                });
            }

            return resolve(rowIds);
        });
    });

}

function searchDataSet(offset) {
    var options = {
        "indexId": 'eb0b824c-60ba-4f94-9805-48b268bb072f', //'4f979900-2c58-4e15-b70e-72d753b0fc95'
        "offset": offset,
        "limit": 10,
        "query": {},
        "sorting": { "_id": "ASC" }
    };

    return Q.Promise(function(resolve, reject) {
        request({
            // TODO: add required 'X-' headers
            method: 'POST',
            url: 'http://<host>:<port>/rest/datasets/rows/search',
            body: JSON.stringify(options)
        }, function (error, response, body) {
            if (error) {
                console.log(error);
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

//var promises = [];
var offsets = [];
var results = [];

for (var i=0; i<10200; i+=10) {
    offsets.push(i);
    //var promise = callAPI(i);
    //promises.push(promise);
}

// TODO: is this a home-made version of sequences in Q? Made by pair-programming mate.
function doCall() {
    if (offsets.length < 1) {
        return Q();
    }

    /*
    return searchDataSet(offsets.shift()).then(function(result) {
        results.push(result);
    }).then(doCall);
    */

    return searchElasticsearch(offsets.shift()).then(function(result) {
        results.push(result);
    }).then(doCall);
}

doCall().then(function () {
//Q.allSettled(promises).then(function (results) {

    var rowIdsFlattened = _.flatten(results);
    console.log('# row IDs: ' + rowIdsFlattened.length);
    console.log('# row IDs - after duplicates removed: ' + _.uniq(rowIdsFlattened).length);

    //console.log(results.length + ' promises returned results...');
    /*
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
        */

    })
    .fail(function (error) {
        console.log(error);
    });

