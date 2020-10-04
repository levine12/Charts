<script type="text/javascript">
var chartIdForDelete;

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

jq("#editChartDiv").hide();

jq( "#dialog-confirm-deleteChart" ).dialog({
    autoOpen: false,
      resizable: false,
      height: "auto",
      width: 400,
      modal: true,
      buttons: {
        "Delete Chart": function() {
            jq(this).dialog("close");
            callbackDeleteChart(true);
        },
        Cancel: function() {
            jq(this).dialog("close");
            callbackDeleteChart(false);
        }
      }
    });
});

    function deleteChart(th,id) {
        chartIdForDelete = id;
        jq( "#dialog-confirm-deleteChart" ).dialog( "open" );
    };
           function callbackDeleteChart(value) {
                event.stopPropagation();
                if (!value) {
                    return;
                }
                jq.getJSON('${ ui.actionLink("deleteChart") }',
                    {
                        'chartIdForDelete': chartIdForDelete
                    })
                    .error(function(xhr, status, err) {
                        alert('AJAX error ' + err);
                    })
                    .success(function(ret) {
                        location.reload();
                        //jq("form").submit();
                    });
            }

    function editGraphChart(id, title, chartTypeId, chartTypeName, xaxisLabel, yaxisLabel, 
           conceptsColors) {
                    jq("#id").val(id);
                    jq("#title").val(title);
                    jq("#chartTypeId").val(chartTypeId);
                    jq("#chartTypeName").val(chartTypeName);
                    jq("#xAxisLabel").val(xaxisLabel);
                    jq("#yAxisLabel").val(yaxisLabel);
                    jq("#conceptsColors").val(conceptsColors);
                    jq("#editChartDiv").show();
                    jq("#editGraphDivBmiDiv").show();
                    jq("#editGraphDiv").show();
                    jq("#editHealthTrendDiv").hide();
                }

    function editBmiChart(id, title, chartTypeId, chartTypeName, xaxisLabel, yaxisLabel) {
                   jq("#id").val(id);
                    jq("#title").val(title);
                    jq("#chartTypeId").val(chartTypeId);
                    jq("#chartTypeName").val(chartTypeName);
                    jq("#xAxisLabel").val(xaxisLabel);
                    jq("#yAxisLabel").val(yaxisLabel);
                    jq("#editChartDiv").show();
                    jq("#editGraphDiv").hide();
                    jq("#editGraphDivBmiDiv").show();
                    jq("#editHealthTrendDiv").hide();
    }
    

    function editTrendChart(id, title, chartTypeId, chartTypeName, concepts) {
                    jq("#id").val(id);
                    jq("#title").val(title);
                    jq("#chartTypeId").val(chartTypeId);
                    jq("#chartTypeName").val(chartTypeName);
                    jq("#concepts").val(concepts);
                    jq("#editChartDiv").show();
                    jq("#editGraphDiv").hide();
                    jq("#editGraphDivBmiDiv").hide();
                    jq("#editHealthTrendDiv").show();
                }

    function editRecentVisitsChart(id, title, chartTypeId, chartTypeName) {
                    jq("#id").val(id);
                    jq("#title").val(title);
                    jq("#chartTypeId").val(chartTypeId);
                    jq("#chartTypeName").val(chartTypeName);
                    jq("#editChartDiv").show();
                    jq("#editGraphDiv").hide();
                    jq("#editGraphDivBmiDiv").hide();
                    jq("#editHealthTrendDiv").hide();
                }
</script>

<a href="addChartType.page">Chart Types</a><br><br><br>
<a href="addChart.page">Add Chart</a><br><br><br>

<h1>CHARTS</h1>
<table id="chartTable"  border="1" class="display" cellspacing="0" width="50%">
<thead>
<th>id</th><th>Title</th><th>Chart Type</th><th>X Axis Label</th><th>Y Axis Label</th><th>Concepts</th>
<th>Delete/Edit</th>
</thead>
<tbody >
  <% if (chartInfos) { %>
     <% chartInfos.each { chartInfo ->%>
     <tr>
        <td>${chartInfo.chart.id}</td><td>${chartInfo.title}</td><td>${chartInfo.chartTypeName}</td>
        <% if (chartInfo.graphChart != null) { %>
            <td>${chartInfo.graphChart.xaxisLabel}</td><td>${chartInfo.graphChart.yaxisLabel}</td>
            <td>

            <% if (chartInfo.graphChart.allChartConceptInfos) { %>
                <% chartInfo.graphChart.allChartConceptInfos.each { chartConcept ->%>
                    ${chartConcept.conceptId}/${chartConcept.conceptName}/${chartConcept.color},
                <% } %>
            <% } %>
            </td>


            <td>
            <i class="icon-pencil edit-action right" onclick="editGraphChart(
                ${chartInfo.chart.id}, '${chartInfo.title}', ${chartInfo.chartTypeId}, '${chartInfo.chartTypeName}', 
                    '${chartInfo.graphChart.xaxisLabel}', '${chartInfo.graphChart.yaxisLabel}', 
                    <% if (chartInfo.graphChart.allChartConceptInfos) { %>
                        <% chartInfo.graphChart.allChartConceptInfos.each { chartConcept ->%>
                            '${chartConcept.conceptId}/${chartConcept.conceptName}/${chartConcept.color},' +
                        <% } %> ' ')" >
                    <% } %>
            </i>

            <em class="icon-trash delete-action" onclick="deleteChart(this,${chartInfo.chart.id})"</em>
            </td>
        
        <% }  else if (chartInfo.healthTrendChart != null) {%>
                        <td></td><td></td>
                        <td>
                        <% chartInfo.healthTrendChart.allChartConceptInfos.each { chartConceptInfo ->%>
                            ${chartConceptInfo.conceptId}/${chartConceptInfo.conceptName},
                        <% } %>
                        </td>  
                        <td>
                        <i class="icon-pencil edit-action right" onclick="editTrendChart(
                            ${chartInfo.chart.id}, '${chartInfo.title}', ${chartInfo.chartTypeId}, '${chartInfo.chartTypeName}', 
                            <% chartInfo.healthTrendChart.allChartConceptInfos.each { chartConcept ->%>
                                '${chartConcept.conceptId}/${chartConcept.conceptName},' +
                            <% } %> ' ')">
                        </i>
                        <em class="icon-trash delete-action" onclick="deleteChart(this,${chartInfo.chart.id})"></em>
                        </td>
        <% } else {%>
                    <td></td><td></td><td></td>
                    <td>
                    <i class="icon-pencil edit-action right" onclick="editRecentVisitsChart(
                            ${chartInfo.chart.id}, '${chartInfo.title}', ${chartInfo.chartTypeId}, 
                            '${chartInfo.chartTypeName}')">
                        </i>
                    <em class="icon-trash delete-action" onclick="deleteChart(this,${chartInfo.chart.id})"></em>
                    </td>
                <% } %>
    </tr>            
        <% } %>
 

     <% } %>
</tbody>
</table>
<br><br>
 
<div id="dialog-confirm-deleteChart" title="Delete a Chart?">
  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:12px 12px 20px 0;"></span>This chart will be permanently deleted and cannot be recovered. Are you sure?</p>
</div>

<form id="editChartForm" method="post">

<div id="editChartDiv">
<label for="id">id</label> <input type="text" id="id" name="id">
<label for="title">title</label> <input type="text" id="title" name="title">
<label for="chartTypeId">chartTypeId</label><input id="chartTypeId" name="chartTypeId">
<label for="chartTypeName">chartTypeName</label><input id="chartTypeName" name="chartTypeName">


<div id="editGraphDivBmiDiv">
    <label for="xAxisLabel">xAxisLabel</label><input type="text" id="xAxisLabel" name="xAxisLabel">
    <label for="yAxisLabel">yAxisLabel</label><input type="text" id="yAxisLabel" name="yAxisLabel">

    <div id="editGraphDiv">
    <label for="conceptsColors">conceptsColors</label><input type="text" id="conceptsColors" name="conceptsColors">
    </div>

</div>


<div id="editHealthTrendDiv">
    <label for="concepts">concepts</label><input type="text" id="concepts" name="concepts">
</div>

</div>

<input type="submit" value="Submit">
</form>


<img src="<%= ui.resourceLink('/ConceptAndPatientData/IMG_3551-1.jpg') %>" id="myImg1" width="224" height="162"/>