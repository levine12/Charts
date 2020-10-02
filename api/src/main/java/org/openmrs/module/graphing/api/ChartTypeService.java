package org.openmrs.module.graphing.api;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.graphing.ChartType;
import org.openmrs.module.graphing.ChartTypeConfig;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ChartTypeService extends OpenmrsService {
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<ChartType> getAllChartTypes();
	
	@Authorized(ChartTypeConfig.MODULE_PRIVILEGE)
	@Transactional
	public ChartType saveChartType(ChartType item) throws APIException;
	
}
