/*
 * 3 states: 
 * sensors fecthed, sensors ready, sensors loop
 * 
 */
var Monitor = function () {
    var _this = this;
    var sensors = [];
    this.SENSORS_FETCH_URL = '';
    this.SENSOR_FETCH_DATA_URL = '';

    var prepareSensorsUI = function () {
        //prepare UI
        for (var n = 0; n < sensors.length; n++) {

        }
        onSensorsReady();
    };
    var updateSensorsUI = function (data) {
        //prepare UI
        console.log(data);
    };
    var startFetchingData = function () {
        for (var n = 0; n < sensors.length; n++) {

            fetchSensorData(sensors[n].s_id, null);
        }
    };
    var fetchSensorData = function (sensorId, axReq) {
        //ajax call
        if (axReq !== null)
            axReq.abort();
        axReq = $.ajax({
            url: _this.SENSOR_FETCH_DATA_URL + sensorId
        }).done(function (data) {
            //allocate sensors
            updateSensorsUI(data);
            //callback
            fetchSensorData(sensorId, axReq);
        });
    };
    _this.init = function () {
        console.log("Monitor says: Fetching Sensors...");
        fetchSensors();
    };
    var onSensorsFetched = function (data) {
        console.log("Monitor says: Sensors Fetched.");
        prepareSensorsUI(data);
    };
    var onSensorsReady = function () {
        startFetchingData();
    };
    var fetchSensors = function () {
        //ajax call
        $.ajax({
            url: _this.SENSORS_FETCH_URL
        }).done(function (data) {
            //allocate sensors
            sensors = data;
            //callback
            onSensorsFetched();
        });

    };
};
var m = new Monitor();
m.SENSORS_FETCH_URL = 'http://localhost:8080/Jarduino/Monitor?action=index&returnType=json';
m.SENSOR_FETCH_DATA_URL = 'http://localhost:8080/Jarduino/Monitor?action=getSensorReg&returnType=json&sId=';
m.init();