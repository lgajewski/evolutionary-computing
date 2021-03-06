var bestFitness = 0.0;
var bestSerie;
var workerSeries = [];
var bestOfAllPoints = [];
var biggerFitnessIsBetter = true;

var visualizations = ["chartAllInTime", "chartBestInTime", "chartBestOfAll", "tableOutput_wrapper", "ws-output"];

function formatDate(timestamp) {
    var date = new Date(timestamp);
    var s =
        new Intl.NumberFormat("en", { minimumIntegerDigits: 2 }).format(date.getDay()) + "." +
        new Intl.NumberFormat("en", { minimumIntegerDigits: 2 }).format(date.getMonth()) + "." +
        date.getFullYear() + " " +
        new Intl.NumberFormat("en", { minimumIntegerDigits: 2 }).format(date.getHours()) + ":" +
        new Intl.NumberFormat("en", { minimumIntegerDigits: 2 }).format(date.getMinutes()) + ":" +
        new Intl.NumberFormat("en", { minimumIntegerDigits: 2 }).format(date.getSeconds()) + "." +
        new Intl.NumberFormat("pl-PL", { style: "decimal", minimumIntegerDigits: 3 }).format(date.getMilliseconds());
    return s;
}

function checkWorkerSeries(workerId, data) {
    var dat = (data === undefined) ? [] : data;
    if (workerSeries[workerId] === undefined) {
        workerSeries[workerId] = chartAllInTime.addSeries({
            name: "Worker " + workerId,
            data: dat,
            marker: {
                enabled: true,
                radius: 3
            }
        });
    }
}

function setInitialPointsToCharts(points) {
    // messages[i].workerId, messages[i].timestamp, messages[i].fitness)
    var dataBestInTime = [];
    var dataAllInTime = [];
    var dataBestOfAll = [];
    var dataForTable = [];

    for (var i = 0; i < points.length; i++) {
        var point = {
            category: points[i].workerId,
            x: points[i].timestamp,
            y: points[i].fitness
        };

        // best in time
        if ((biggerFitnessIsBetter && point.y > bestFitness) || (!biggerFitnessIsBetter && point.y < bestFitness)) {
            dataBestInTime.push(point);
            bestFitness = point.y;
        }

        // all in time
        if (dataAllInTime[point.category] === undefined) {
            dataAllInTime[point.category] = [];
        }

        dataAllInTime[point.category].push(point);

        // table
        dataForTable.push([
            point.category,
            formatDate(point.x),
            point.y]);
    }

    // best in time
    setupChartBestInTime(dataBestInTime);

    // all in time
    var series = [];

    for (var serie in dataAllInTime) {
        var s = {
            name: serie,
            data: dataAllInTime[serie],
            marker: {
                enabled: true,
                radius: 3
            }
        };
        series.push(s);
    }
    setupChartAllInTime(series);
    for (var serieNum in chartAllInTime.series) {
        workerSeries[chartAllInTime.series[serieNum].name] = chartAllInTime.series[serieNum];
    }

    // table
    dataTable.rows.add(dataForTable);
    dataTable.columns.adjust().draw();
}

function addPointToCharts(workerId, xVal, value) {
    var point = {
        category: workerId,
        x: xVal,
        y: value
    };

    // best in time
    if ((biggerFitnessIsBetter && value > bestFitness) || (!biggerFitnessIsBetter && value < bestFitness)) {
        chartBestInTime.series[0].addPoint(point);
        bestFitness = value;
    }

    // all in time
    checkWorkerSeries(workerId);

    workerSeries[workerId].addPoint(point, true, false);

    // best of all
    if (bestOfAllPoints[workerId] === undefined) {
        chartBestOfAll.series[0].addPoint({
            category: "Worker " + workerId,
            x: workerId,
            y: value
        });
        bestOfAllPoints[workerId] = chartBestOfAll.series[0].data[chartBestOfAll.series[0].data.length - 1];
    } else {
        if (value > bestOfAllPoints[workerId].y) {
            bestOfAllPoints[workerId].update(value);
        }
    }

    // table
    dataTable.row.add([
        workerId,
        formatDate(xVal),
        value
    ]);
}

Highcharts.setOptions({
    lang: {
        noData: "Waiting for data from server"
    },

    global: {
        useUTC: false
    }
});

var chartSettings = {
    rangeSelector: {
        allButtonsEnabled: true,
        buttons: [{
            type: 'second',
            count: 30,
            text: '30 secs'
        }, {
            type: 'minute',
            count: 1,
            text: '1 min'
        }, {
            type: 'minute',
            count: 2,
            text: '2 min'
        }, {
            type: 'minute',
            count: 5,
            text: '5 min'
        }, {
            type: 'minute',
            count: 10,
            text: '10 min'
        }, {
            type: 'minute',
            count: 30,
            text: '30 min'
        }, {
            type: 'hour',
            count: 1,
            text: '1 h'
        }],
        buttonTheme: {
            width: 60
        },
        selected: 2
    }
};