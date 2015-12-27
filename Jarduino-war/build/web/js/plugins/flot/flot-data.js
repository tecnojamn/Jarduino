// Flot Line Charts - Multiple Axes - With Data
var flot_data = [
        [1167692400000, 61.05],
        [1167778800000, 58.32],
        [1167865200000, 57.35],
        [1167951600000, 56.31],
        [1168210800000, 55.55]
    ];
$(function () {
    
    function euroFormatter(v, axis) {
        return v.toFixed(axis.tickDecimals) + "€";
    }
    function doPlot(position) {
        $.plot($("#flot-multiple-axes-chart"), [{
                data: flot_data,
                label: "Valor"
            }], {
            xaxes: [{
                    mode: 'time'
                }],
            yaxes: [{
                    min: 0
                }, {
                    // align if we are to the right
                    alignTicksWithAxis: position == "right" ? 1 : null,
                    position: position,
                    tickFormatter: euroFormatter
                }],
            legend: {
                position: 'sw'
            },
            grid: {
                hoverable: true //IMPORTANT! this is needed for tooltip to work
            },
            tooltip: true,
            tooltipOpts: {
                content: "Date: %x Value: %y",
                xDateFormat: "%y-%0m-%0d",
                onHover: function (flotItem, $tooltipEl) {
                    // console.log(flotItem, $tooltipEl);
                }
            }

        });
    }
    doPlot("right");
    $("button").click(function () {
        doPlot($(this).text());
    });
});
