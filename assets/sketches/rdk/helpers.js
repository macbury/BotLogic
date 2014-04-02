function repeat(times, callback) {
    for(var i = 0; i < times; i++) {
        callback();
    }
}