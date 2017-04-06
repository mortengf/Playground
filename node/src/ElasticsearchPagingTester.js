var _ = require('lodash');
var request = require('request');
var Q = require('q');

const preference = '2378278'; // TODO: generate a random UUID

// TODO: get all values below from CLI options
// Usage: node ElasticsearchPagingTester.js <esUrl> <indexId> <startIndex> <endIndex> <pageSize>

var esBaseUrl = '<baseUrl>';

const esIndexId = '<indexId>';

var esUrl = esBaseUrl + '/' + esIndexId + '/_search?preference=' + preference;

const startIndex = 9399; // TODO: works with start=9499 even without "preference"???
const endIndex = 9999;
const pageSize = 10;

function searchElasticsearch(offset) {
    /*
    TODO: add the following?
        - types = "primary",
        - searchType = "QUERY_THEN_FETCH",
     */
    var esRequest = {
        "from": offset,
        "size": pageSize,
        "query":{"match_all":{}}
    };

    return Q.Promise(function(resolve, reject) {
        request({
            method: 'POST',
            url: esUrl,
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

var offsets = [];

for (var i=startIndex; i<endIndex; i+=pageSize) {
    offsets.push(i);
}

var results = [];

// TODO: is this a home-made version of sequences in Q? Made by pair-programming mate.
function doCall() {
    if (offsets.length < 1) {
        return Q();
    }

	return searchElasticsearch(offsets.shift()).then(function(result) {
		results.push(result);
	}).then(doCall);

}

console.log("Searching " + esIndexId + " on server " + esUrl + " from index " + startIndex + " to index " + endIndex + " with a page size of " + pageSize);

doCall().then(function () {
    var rowIdsFlattened = _.flatten(results);
    console.log('# row IDs: ' + rowIdsFlattened.length);
    console.log('# row IDs - after duplicates removed: ' + _.uniq(rowIdsFlattened).length);
}).fail(function (error) {
    console.log(error);
});

