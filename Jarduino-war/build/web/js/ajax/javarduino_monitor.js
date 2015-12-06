/*
 * 4 states: 
 * sensors fecthed,configs fetched,sensors ready,sensors loop
 * 
 */
var Monitor = function () {
    var _this = this;
    var MAX_VALS = 5;
    var jQueryDebugOutputElement = null;
    var kill_count = 0;
    var sensors = [];
    this.SENSORS_FETCH_URL = '';
    this.SENSOR_FETCH_DATA_URL = '';
    var FETCH_FLAG = true;
    _this.setDebugOutputElement = function (element) {
        jQueryDebugOutputElement = element;
    };
    var log = function (debugString) {
        if (jQueryDebugOutputElement !== null) {
            jQueryDebugOutputElement.html("<p>" + debugString + "</p>");
        } else {
            console.log(debugString);
        }
    }
    ;
    var prepareSensorsUI = function () {
        //prepare UI
        for (var n = 0; n < sensors.length; n++) {
            var html = "<div class = 'col-lg-6'><div class = 'panel panel-default' ><div class = 'panel-heading'>" + sensors[n].s_name + "</div><div class ='panel-body'> <div class='registry sensor-" + sensors[n].s_id + "'></div></div></div>";
            $("#sensors_container").append(html);
        }
        onSensorsReady();
    };
    var updateSensorsUI = function (data) {
        //prepare UI
        if (data !== "null" && data !== null) {
            html = '<ul class="registry-item"><li>' + data.r_id + '</li><li>' + data.r_date + '</li><li>' + data.r_value + '</li></ul>';
            $(".registry.sensor-" + data.r_sensor_id).prepend(html);
            var total = $(".registry.sensor-" + data.r_sensor_id).find(".registry-item").length;
            $(".registry.sensor-" + data.r_sensor_id).find(".registry-item").eq(0).addClass("new");
            $(".registry.sensor-" + data.r_sensor_id).find(".registry-item").eq(0).delay(1000).queue(function () {
                $(this).removeClass("new");
                //si se supera el valor en config...TODO IMPORTANT!
                if (data.r_value > 10) {
                    $(this).addClass("j-alert");
                }
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
        if (axReq !== null)
            axReq.abort();
        if (!FETCH_FLAG) {
            log("Monitor : killing fetching process " + kill_count + "...");
            kill_count--;
            if (kill_count === 0)
                log("Monitor  : Monitor is down.");
            return;
        }
        //ajax call

        axReq = $.ajax({
            url: _this.SENSOR_FETCH_DATA_URL + sensorId
        }).done(function (data) {
            //allocate sensors
            updateSensorsUI(data);
            //callback
            log("Monitor : working...");
            fetchSensorData(sensorId, axReq);
        });
    };
    _this.init = function () {
        log("Monitor : Fetching Sensors...");
        fetchSensors();
    };
    var onSensorsFetched = function (data) {
        log("Monitor : Sensors Fetched.");
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
    _this.stop = function () {
        if (FETCH_FLAG) {
            kill_count = sensors.length;
            log("Monitor : stoping " + kill_count + " requests...");
            FETCH_FLAG = false;
        }
    };

    _this.restart = function () {

        if (!FETCH_FLAG) {
            log("Monitor : restarting...");
            FETCH_FLAG = true;
            startFetchingData();
        }
    };
};
var m = new Monitor();
$(document).ready(function () {

    m.SENSORS_FETCH_URL = 'http://localhost:8080/Jarduino/Monitor?action=index&returnType=json';
    m.SENSOR_FETCH_DATA_URL = 'http://localhost:8080/Jarduino/Monitor?action=getSensorReg&returnType=json&sId=';
    m.init();
    m.setDebugOutputElement($("#monitor-log"));
    $("#monitor-power").click(function () {
        if ($(this).hasClass("off")) {
            m.restart();
            $(this).removeClass("off");
        } else {
            m.stop();
            $(this).addClass("off");
        }
    });

});
