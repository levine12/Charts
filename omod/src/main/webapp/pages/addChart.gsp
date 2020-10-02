
<% ui.decorateWith("appui", "standardEmrPage") 
ui.includeJavascript("uicommons", "datatables/jquery.dataTables.min.js")
ui.includeCss("uicommons", "datatables/dataTables_jui.css")

%>
<script type="text/javascript">
            //var OPENMRS_CONTEXT_PATH = 'openmrs';
            window.sessionContext = window.sessionContext || {
                locale: "en_GB"
            };
            window.translations = window.translations || {};
</script>

<script type="text/javascript">
   var breadcrumbs = [

       { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "Add Remove Charts", link: '/' + OPENMRS_CONTEXT_PATH + '/graphing/addRemoveCharts.page' },
        { label: "Add Chart"}
    ];

</script>
<script type="text/javascript">
jq=jQuery;
jq(function() { 
     
     jq('#chartTable').dataTable({
            "aaSorting": [],
            "sPaginationType": "full_numbers",
            "bPaginate": true,
            "bAutoWidth": false,
            "bLengthChange": true,
            "bSort": true,
            "bJQueryUI": true
        });
});
</script>

<h1>CHARTS</h1>
<table id="chartTable"  border="1" class="display" cellspacing="0" width="50%">
<thead>
<th>id</th><th>Title</th><th>Chart Type</th><th>X Axis Label</th><th>Y Axis Label</th><th>Concepts</th>
</thead>
<tbody >
  <% if (chartInfos) { %>
     <% chartInfos.each { %>
      <tr>
        <td>${it.id}</td><td>${it.title}</td><td>${it.chartTypeName}</td><td>${it.xaxisLabel}</td><td>${it.yaxisLabel}</td>
            <td>${it.conceptNames}</td>
      </tr>
     <% } %>
   <% } %>
</tbody>
</table>
<br><br>

<form id="addNewChart" method="post">
You can provide colors to associate with each line when creating a line graph<br>
The list of colors are: <i>red, green, blue, purple, black, pink, brown, cyan, turquoise, violet</i> <br>
<h1>Add New Chart</h1>
Chart Title: <input name="chartTitle"/> 

   <select name="chartTypeIdStr" id="chartTypeIdStr" onchange="selectChartType()">
   <option value="0">Select Chart Type</option>
    <% charttypes.each { %>
     <option value="${it.id}">${it.name}</option>
     <% } %>
    </select>
<div id="conceptInfo" style="display:none">
List of Associated Concept Ids/Colors <br>
(e.g. 5089/blue,5090/red for <i>graphing</i> else <br>5089,5090 for <i>health trends</i>): <br>
<input name="conceptIdsColors">
</div>

<div id="graphInfo" style="display:none">
X Axis Label: <input name="xAxisLabel"/> Y Axis Label: <input name="yAxisLabel"/><br>

</div>
<div id="bmiInfo" style="display:none">
X Axis Label: <input name="xAxisLabel"/> Y Axis Label: <input name="yAxisLabel"/><br>

</div>
<input type="submit">
</form>

<script type="text/javascript">
function selectChartType() {
    //var a = jq( "#chartTypeIdStr" ).val();
    let chartType = jq( "#chartTypeIdStr option:selected" ).text();
    chartType = chartType.toLowerCase();
    if (chartType.includes('recent visits')) {
        jq("#conceptInfo").hide();
        jq("#graphInfo").hide();
        jq("#bmiInfo").hide();
        return;
    }
    
   if (chartType.includes('bmi')) {
        jq("#bmiInfo").show();
        jq("#conceptInfo").hide();
        jq("#graphInfo").hide();
        return;
    }

    jq("#conceptInfo").show();
    jq("#bmiInfo").hide();
    if (chartType.includes('graph')) {
        jq("#graphInfo").show();
    } else {
        jq("#graphInfo").hide();
    }
}
</script>

<img src="${ ui.resourceLink("graphing", "images/HealthTrendExample.png") }"/>
<br><br>
Encounters Grouped by Visits:<br>
<img src="${ ui.resourceLink("graphing", "images/Visits.png") }"/>


<script type="text/javascript">
            //var OPENMRS_CONTEXT_PATH = 'openmrs';
            window.sessionContext = window.sessionContext || {
                locale: "en_GB"
            };
            window.translations = window.translations || {};
</script>



