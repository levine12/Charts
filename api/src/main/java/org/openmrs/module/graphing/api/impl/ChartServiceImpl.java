package org.openmrs.module.graphing.api.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.graphing.Chart;
import org.openmrs.module.graphing.api.ChartService;
import org.openmrs.module.graphing.api.db.ChartDao;
import org.springframework.transaction.annotation.Transactional;

public class ChartServiceImpl extends BaseOpenmrsService implements ChartService {
	
	ChartDao dao;
	
	private static final Log log = LogFactory.getLog(ChartServiceImpl.class);
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(ChartDao dao) {
		this.dao = dao;
	}
	
	public ChartDao getDao() {
		return dao;
	}
	
	public static Log getLog() {
		return log;
	}
	
	@Transactional(readOnly = true)
	public List<Chart> getAllCharts() {
		return dao.getAllCharts();
	}
	
	@Transactional(readOnly = true)
	public Chart getChart(Integer id) {
		return dao.getChart(id);
	}
	
	@Transactional
	public Chart saveChart(Chart item) throws APIException {
		return dao.saveChart(item);
	}
	
}
