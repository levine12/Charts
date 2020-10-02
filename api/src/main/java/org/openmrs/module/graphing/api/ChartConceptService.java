package org.openmrs.module.graphing.api;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.graphing.ChartConcept;
import org.openmrs.module.graphing.ChartConceptConfig;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ChartConceptService extends OpenmrsService {
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<ChartConcept> getAllChartConcepts();
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<ChartConcept> getAllChartConceptsForChart(int chartId);
	
	@Authorized(ChartConceptConfig.MODULE_PRIVILEGE)
	@Transactional
	public ChartConcept saveChartConcept(ChartConcept item) throws APIException;
	
}
