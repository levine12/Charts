package org.openmrs.module.graphing.api;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.graphing.Chart;
import org.openmrs.module.graphing.ChartConfig;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ChartService extends OpenmrsService {
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<Chart> getAllCharts();
	
	@Authorized()
	@Transactional(readOnly = true)
	public Chart getChart(Integer id);
	
	@Authorized(ChartConfig.MODULE_PRIVILEGE)
	@Transactional
	public Chart saveChart(Chart item) throws APIException;
	
}
