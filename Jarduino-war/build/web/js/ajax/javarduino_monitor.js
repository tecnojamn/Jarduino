/*
 * 3 states: 
 * sensors fecthed, sensors ready, sensors loop
 * 
 */
var Monitor = function () {
    var _this = this;
    var MAX_VALS = 5;
    var sensors = [];
    this.SENSORS_FETCH_URL = '';
    this.SENSOR_FETCH_DATA_URL = '';
    var prepareSensorsUI = function () {
        //prepare UI
        for (var n = 0; n < sensors.length; n++) {
            var html = "<div class = 'col-lg-6'><div class = 'panel panel-default' ><div class = 'panel-heading'>" + sensors[n].s_name + "</div><div class ='panel-body'> <div class='registry sensor-" + sensors[n].s_id + "'></div></div></div>";
            console.log($("#sensors_container").length);
            $("#sensors_container").append(html);
        }
        onSensorsReady();
    };
    var updateSensorsUI = function (data) {
        //prepare UI
        console.log(data);
        if (data !== "null" && data !== null) {
            html = '<ul class="registry-item"><li>' + data.r_id + '</li><li>' + data.r_date + '</li><li>' + data.r_value + '</li></ul>';
            $(".registry.sensor-" + data.r_sensor_id).prepend(html);
            var total = $(".registry.sensor-" + data.r_sensor_id).find(".registry-item").length;
            $(".registry.sensor-" + data.r_sensor_id).find(".registry-item").eq(0).addClass("new");
            $(".registry.sensor-" + data.r_sensor_id).find(".registry-item").eq(0).delay(1000).queue(function () {
                $(this).removeClass("new");
                ;
            });


            if (total > MAX_VALS) {
                $(".registry.sensor-" + data.r_sensor_id).find(".registry-item").eq(total - 1).remove();
            }


        }

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

$(document).ready(function () {
    console.log("asd");
    var m = new Monitor();
    m.SENSORS_FETCH_URL = 'http://localhost:8080/Jarduino/Monitor?action=index&returnType=json';
    m.SENSOR_FETCH_DATA_URL = 'http://localhost:8080/Jarduino/Monitor?action=getSensorReg&returnType=json&sId=';
    m.init();
});
