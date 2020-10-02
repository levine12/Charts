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
        { label: "Add Chart Type"}
    ];

</script>
<script type="text/javascript">
jq=jQuery;
jq(function() { 
     jq('#chartTypeTable').dataTable({
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
<h1>CHART TYPES</h1>
<table id="chartTypeTable"  border="1" class="display" cellspacing="0" width="50%">
<thead>
<th>id</th><th>Names</th>
</thead>
<tbody >
  <% if (charttypes) { %>
     <% charttypes.each { %>
      <tr>
        <td>${it.id}</td><td>${it.name}</td>
      </tr>
     <% } %>
   <% } %>
</tbody>
</table>

<br>

<table>
<thead>
<th>Chart Type Includes This String</th><th>Comments</th>
</thead>
<tbody>
<tr><td>graph</td><td>Yields graph(s) of obs with numeric data</td></tr>
<tr><td>bmi</td><td>Yields graph(s) of body mass index numeric data: The formula for BMI is weight in kilograms divided by height in meters squared</td></tr>
<tr><td>recent visits</td><td>Encounter Dates/Types Grouped by Visits</td></tr>
<tr><td>health trend summary</td><td>Provide one or more concepts to display for each encounter</td></tr>
</tbody>
</table>
<div>
<br><br>
<form id="addNewChartType" method="post">
Add New Chart Type: <input name="newChartType"/>
<input type="submit">
</form>
</div>

<script type="text/javascript">
            //var OPENMRS_CONTEXT_PATH = 'openmrs';
            window.sessionContext = window.sessionContext || {
                locale: "en_GB"
            };
            window.translations = window.translations || {};
</script>



