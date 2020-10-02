package org.openmrs.module.graphing.api.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.graphing.ChartType;
import org.openmrs.module.graphing.api.ChartTypeService;
import org.openmrs.module.graphing.api.db.ChartTypeDao;
import org.springframework.transaction.annotation.Transactional;

public class ChartTypeServiceImpl extends BaseOpenmrsService implements ChartTypeService {
	
	ChartTypeDao dao;
	
	private static final Log log = LogFactory.getLog(ChartTypeServiceImpl.class);
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(ChartTypeDao dao) {
		this.dao = dao;
	}
	
	public ChartTypeDao getDao() {
		return dao;
	}
	
	public static Log getLog() {
		return log;
	}
	
	@Transactional(readOnly = true)
	public List<ChartType> getAllChartTypes() {
		return dao.getAllChartTypes();
	}
	
	@Transactional
	public ChartType saveChartType(ChartType item) throws APIException {
		return dao.saveChartType(item);
	}
	
}
