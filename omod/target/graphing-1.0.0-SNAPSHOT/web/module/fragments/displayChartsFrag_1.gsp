
<%
ui.includeCss("graphing", "Chart.min.css")
ui.includeJavascript("graphing", "Chart.bundle.min.js")
%>

<!-- script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.css" /script
     script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.bundle.min.js" /script
-->

<style>
.s1 {
  width: 400px;
  height: 400px;
}
.s2 {
  max-width: 400px;
  height: 400px;
}
h2 {
display: none;
}
.clear {
    clear: both;
}
</style>

<h1>DISPLAY CHARTS</h1>

<% ui.includeJavascript("graphing", "datepicker.js") %>
     
<script>
var startDate, endDate;
var chartsDrawn = false;
jq = jQuery;
jq(document).ready(function() {
    jq("#updateCharts").click(function(){
        dates = jq("#dates").val().split("-");
        startDate = new Date(jq.trim(dates[0]));
        endDate = new Date(jq.trim(dates[1]));
        refreshCharts();
    }); 


});

</script>




<p>
  <label for="dates">Date range:</label>
  <input type="text" id="dates" style="border: 0; color: #f6931f; font-weight: bold;" size="100"/>
</p>
 
<br><br>
<div id="slider-range" class="c"></div> 

<br><br>
<button id="updateCharts">Update Charts With New Date Range</button>


<% for (int i = 0; i < chartInfos.size; i++) {%>
<div id="i${i}" class="s1">
             <% if (chartInfos.get(i).chartTypeName.equals("graph")){ %>
                <br><br><h2>${chartInfos.get(i).title}</h2>
                <canvas id="graphChart${i}" class="display" width="400" height="400"></canvas>
            <% } else if (chartInfos.get(i).chartTypeName.equals("bmi")){ %>
                <br><br><h2>${chartInfos.get(i).title}</h2>
                <canvas id="bmiChart${i}" class="display" width="400" height="400"></canvas>
            <% } else if (chartInfos.get(i).chartTypeName.equals("visit")){ %>
                <br><br><h2>${chartInfos.get(i).title}</h2>
                <table  id="visitTable" class="display" style="word-break:break-all;" width="600"></table>
            <% } else if (chartInfos.get(i).chartTypeName.equals("trend")){ %>
                <br><br><h2>${chartInfos.get(i).title}</h2>
                <table  id="trendTable${i}"  class="display" width="100%"></table>
            <% } %>
            
</div>
<br><br>
<div class="clear"></div>
<% } %> 


   

<script>

function dateInRange(dateString) {
    let date = new Date(dateString);

    let sDate = new Date(startDate);
    sDate = new Date(sDate.setHours(24,0,0,0));
    let eDate = new Date(endDate);
    eDate = new Date(eDate.setHours(24,0,0,0));
    let oneDayBeforeStartDate = new Date(sDate.setDate(sDate.getDate() - 1));
    let oneDayAfterEndDate = new Date(eDate.setDate(eDate.getDate() + 1));
    if ( (date >= oneDayBeforeStartDate) && (date <= oneDayAfterEndDate) ) {
        return true;
    }
    return false;
}

function refreshCharts() {
jq( "h2:hidden" ).show();
    refreshAllGraphCharts();
    refreshAllVisitCharts();
    refreshAllTrendCharts();
    refreshAllBmiCharts();
    chartsDrawn = true;
}


function refreshAllGraphCharts() {
    chartNum = -1;
    <% chartInfos.each { chartInfo ->%>
        chartNum++;
        <% if (chartInfo.chartTypeName.equals("graph")){ %>



var ctx = document.getElementById("graphChart" + chartNum).getContext("2d");

linebreak = document.createElement("br");
//let graphDoc = document.getElementById("graphChart" + chartNum);
graphDoc = document.getElementById("graphChart" + chartNum);
graphDoc.appendChild(linebreak);


var graphChart = new Chart(ctx, {
  type: 'line',
  data: {
    labels: [],
    datasets: []
  },
  options: {
    title: {
            display: true,
            text: '',
            fontSize: 18},
    scales: {
      xAxes: [{
        type: 'time',
        scaleLabel: {
            display: true,
            labelString: ''
         }
      }],
      yAxes: [{
        scaleLabel: {
            display: true,
            labelString: ''
         },
        ticks: {
            beginAtZero: true
         }
       }]
    }
  }
}); 

            graphChart.options.title.text = "${chartInfo.title}";
            graphChart.options.scales.xAxes[0].scaleLabel.labelString = "${chartInfo.graphChart.xaxisLabel}";
            graphChart.options.scales.yAxes[0].scaleLabel.labelString = "${chartInfo.graphChart.yaxisLabel}";
            graphChart.data.labels = [startDate, endDate]
            
            <% chartInfo.graphChart.allChartConceptInfos.each { ChartConceptInfo ->%>
                    data = [];
                    dataset = {
                        label: '',
                        fill: 'false',
                        lineTension: '0.1',
                        data: [],
                        backgroundColor: '',
                        borderColor: '',
                        borderWidth: 1
                    }
                    <% ChartConceptInfo.allObs.each { obs ->%>
                        obsDate = "${obs.obsDatetime}";
                        obsValue = "${obs.valueNumeric}";
                        if (dateInRange(obsDate)) {
                            data.push({t: obsDate, y: obsValue})
                        }
                    <% } %>
                    dataset.data = data;
                    dataset.backgroundColor = "${ChartConceptInfo.color}";
                    dataset.borderColor     = "${ChartConceptInfo.color}";

                    dataset.label = "${ChartConceptInfo.conceptName}";                    
                    graphChart.data.datasets.push(dataset);
            <% } %>




graphChart.update();  

<% } %> 
<% } %> 
 
}

function refreshAllBmiCharts() {
    chartNum = -1;
    <% chartInfos.each { chartInfo ->%>
        chartNum++;
        <% if (chartInfo.chartTypeName.equals("bmi")){ %>



var ctx = document.getElementById("bmiChart" + chartNum).getContext("2d");

linebreak = document.createElement("br");
//let graphDoc = document.getElementById("graphChart" + chartNum);
bmiDoc = document.getElementById("bmiChart" + chartNum);
bmiDoc.appendChild(linebreak);


var bmiChart = new Chart(ctx, {
  type: 'line',
  data: {
    labels: [],
    datasets: []
  },
  options: {
    title: {
            display: true,
            text: '',
            fontSize: 18},
    scales: {
      xAxes: [{
        type: 'time',
        scaleLabel: {
            display: true,
            labelString: ''
         }
      }],
      yAxes: [{
        scaleLabel: {
            display: true,
            labelString: ''
         },
        ticks: {
            beginAtZero: true
         }
       }]
    }
  }
}); 

            bmiChart.options.title.text = "${chartInfo.title}";
            bmiChart.options.scales.xAxes[0].scaleLabel.labelString = "${chartInfo.bmiChart.xaxisLabel}";
            bmiChart.options.scales.yAxes[0].scaleLabel.labelString = "${chartInfo.bmiChart.yaxisLabel}";
            bmiChart.data.labels = [startDate, endDate]
                    data = [];
                    dataset = {
                        label: '',
                        fill: 'false',
                        lineTension: '0.1',
                        data: [],
                        backgroundColor: '',
                        borderColor: '',
                        borderWidth: 1
                    }
                     <% chartInfo.bmiChart.allBmi.each { bmi ->%>
                        bmiDate = "${bmi.date}";
                        bmiValue = "${bmi.value}";
                        if (dateInRange(bmiDate)) {
                            data.push({t: bmiDate, y: bmiValue})
                        }
                    dataset.data = data;
                    dataset.backgroundColor = "${bmiChart.color}";
                    dataset.borderColor     = "${bmiChart.color}";

                    dataset.label = "BMI";                    
                    bmiChart.data.datasets.push(dataset);
                    <% } %>




bmiChart.update();  

<% } %> 
<% } %> 
 
}

function refreshAllVisitCharts() {
    chartNum = -1;
    <% chartInfos.each { chartInfo ->%>
        chartNum++;
        <%if (chartInfo.chartTypeName.equals("visit")) { %>
            visitDataset = [];
            <% chartInfo.visitChart.allVisitInfos.each { visitInfo ->%>
                if (dateInRange("${visitInfo.visitDate}")) {
                    visitDataset.push([ "${visitInfo.visitDateString}" , "${visitInfo.visitType}" , "${visitInfo.encounterTypes}" ]); 
                }
            <% } %>
            if (!chartsDrawn) {
                jq('#visitTable').dataTable({
                    "aaSorting": [],
                    "sPaginationType": "full_numbers",
                    "bPaginate": true,
                    "bAutoWidth": false,
                    "bLengthChange": true,
                    "bSort": true,
                    "bJQueryUI": true,
                    "aaData": visitDataset,
                    "aoColumns": [
                        { "sTitle": "Visit Start Date" },
                        { "sTitle": "Visit Type" },
                        { "sTitle": "Encounters" }
                    ]
                }); 
                visitTable = jq('#visitTable').dataTable();
            } else {
                visitTable = jq('#visitTable').dataTable();
                visitTable.fnClearTable();
                visitTable.fnAddData( visitDataset);
            }
            <% if (chartInfo.visitChart.allVisitInfos) { %>   
                <% chartInfo.visitChart.allVisitInfos.each { visitInfo ->%>
                    "${visitInfo.visitType}";
                    "${visitInfo.encounterTypes}";
                <% } %> 
            <% } %> 
        <% } %>
    <% } %> 
}

function getDate(dateTimeString) {
    let d = dateTimeString.split(" ");
    return d[0];
}

function refreshAllTrendCharts() {
    chartNum = -1;
    <% chartInfos.each { chartInfo ->%>
        chartNum++;
        <%if (chartInfo.chartTypeName.equals("trend")) { %>
            encounterDataset = [];
            <% chartInfo.healthTrendChart.allEncounterInfo.each { encounterInfo ->%>
                if (dateInRange("${encounterInfo.encounter.encounterDatetime}")) {
                    dataSetRow = [];
                    dataSetRow.push(getDate("${encounterInfo.encounter.encounterDatetime}"));
                    <% if (encounterInfo.allObsSortedByConceptId) { %>
                        <% encounterInfo.allObsStringValues.each { obsStringValue ->%>
                            dataSetRow.push("${obsStringValue}");
                        <% } %>
                        encounterDataset.push(dataSetRow);
                    <% } %>
                }
            <% } %>
            if (!chartsDrawn) {
                jq('#' + 'trendTable' + chartNum).dataTable({
                    "aaSorting": [],
                    "sPaginationType": "full_numbers",
                    "bPaginate": true,
                    "bAutoWidth": false,
                    "bLengthChange": true,
                    "bSort": true,
                    "bJQueryUI": true,
                    "aaData": encounterDataset,
                    "aoColumns": [
                        { "sTitle": "Encounter"},
                        <% chartInfo.healthTrendChart.concepts.each { concept ->%>
                            { "sTitle": "${concept.name}" },
                        <% } %>
                    ]
                }); 
                trendTable = jq('#' + 'trendTable' + chartNum).dataTable();
            } else {
                trendTable = jq('#' + 'trendTable' + chartNum).dataTable();
                trendTable.fnClearTable();
                trendTable.fnAddData( encounterDataset);
            }
        <% } %>
    <% } %> 
}

 </script> 

