'use strict';

var fs = require('fs');

fs.stat('platforms/android', function (err, stat) {
    if (!err && stat.isDirectory()) {
        fs.stat('google-services.json', function (err, stat) {
            if (!err && stat.isFile()) {
                var readStream = fs.createReadStream('google-services.json');
                var writeStream = fs.createWriteStream('platforms/android/google-services.json');
                readStream.pipe(writeStream);
            }
        });
    }
});


fs.stat('platforms/ios', function (err, stat) {
    if (!err && stat.isDirectory()) {
        fs.stat('GoogleService-Info.plist', function (err, stat) {
            if (!err && stat.isFile()) {
                var readStream = fs.createReadStream('GoogleService-Info.plist');
                var writeStream = fs.createWriteStream('platforms/ios/GoogleService-Info.plist');
                readStream.pipe(writeStream);
            }
        });
    }
});
