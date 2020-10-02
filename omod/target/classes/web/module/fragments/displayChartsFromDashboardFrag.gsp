
<div class="info-section drugs">    
    <div class="info-header">
        <i class="icon-medical"></i>
        <h3>Display Charts</h3>
        <i class="icon-pencil edit-action right" title="${ ui.message("coreapps.edit") }" onclick="location.href='${ui.pageLink("allergyui", "allergies", [patientId: patient.patient.id])}';"></i>
    </div>

    <div class="info-body">
<a href="/openmrs/graphing/displayCharts.page?patientId=${patientId}">Display Charts</a>
    </div>

</div>