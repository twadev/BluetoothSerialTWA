/*global cordova*/
module.exports = {

    connect: function (macAddress, success, failure) {
        cordova.exec(success, failure, "BluetoothSerialTWA", "connect", [macAddress]);
    },

    // Android only - see http://goo.gl/1mFjZY
    connectInsecure: function (macAddress, success, failure) {
        cordova.exec(success, failure, "BluetoothSerialTWA", "connectInsecure", [macAddress]);
    },

    disconnect: function (success, failure) {
        cordova.exec(success, failure, "BluetoothSerialTWA", "disconnect", []);
    },

    // list bound devices
    list: function (success, failure) {
        console.log('listing?');
        cordova.exec(success, failure, "BluetoothSerialTWA", "list", []);
    },

    isEnabled: function (success, failure) {
        cordova.exec(success, failure, "BluetoothSerialTWA", "isEnabled", []);
    },

    isConnected: function (success, failure) {
        cordova.exec(success, failure, "BluetoothSerialTWA", "isConnected", []);
    },

    // the number of bytes of data available to read is passed to the success function
    available: function (success, failure) {
        cordova.exec(success, failure, "BluetoothSerialTWA", "available", []);
    },

    // read all the data in the buffer
    read: function (success, failure) {
        cordova.exec(success, failure, "BluetoothSerialTWA", "read", []);
    },

    // reads the data in the buffer up to and including the delimiter
    readUntil: function (delimiter, success, failure) {
        cordova.exec(success, failure, "BluetoothSerialTWA", "readUntil", [delimiter]);
    },

    // writes data to the bluetooth serial port - data must be a string
    write: function (data, success, failure) {
        cordova.exec(success, failure, "BluetoothSerialTWA", "write", [data]);
    },

    // calls the success callback when new data is available
    subscribe: function (delimiter, success, failure) {
        cordova.exec(success, failure, "BluetoothSerialTWA", "subscribe", [delimiter]);
    },

    // removes data subscription
    unsubscribe: function (success, failure) {
        cordova.exec(success, failure, "BluetoothSerialTWA", "unsubscribe", []);
    },

    // clears the data buffer
    clear: function (success, failure) {
        cordova.exec(success, failure, "BluetoothSerialTWA", "clear", []);
    }

};
