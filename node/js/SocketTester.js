var s = require('net').Socket();
s.connect(81, 'google.com');
s.write('GET http://www.google.com/ HTTP/1.1\n\n');

s.on('data', function(d){
    console.log(d.toString());
});

s.end();