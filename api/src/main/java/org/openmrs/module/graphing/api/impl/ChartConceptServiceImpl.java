package org.openmrs.module.graphing.api.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.graphing.ChartConcept;
import org.openmrs.module.graphing.api.ChartConceptService;
import org.openmrs.module.graphing.api.db.ChartConceptDao;
import org.springframework.transaction.annotation.Transactional;

public class ChartConceptServiceImpl extends BaseOpenmrsService implements ChartConceptService {
	
	ChartConceptDao dao;
	
	private static final Log log = LogFactory.getLog(ChartConceptServiceImpl.class);
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(ChartConceptDao dao) {
		this.dao = dao;
	}
	
	public ChartConceptDao getDao() {
		return dao;
	}
	
	public static Log getLog() {
		return log;
	}
	
	@Transactional(readOnly = true)
	public List<ChartConcept> getAllChartConcepts() {
		return dao.getAllChartConcepts();
	}
	
	@Transactional(readOnly = true)
	public List<ChartConcept> getAllChartConceptsForChart(int chartId) {
		return dao.getAllChartConceptsForChart(chartId);
	}
	
	@Transactional
	public ChartConcept saveChartConcept(ChartConcept item) throws APIException {
		return dao.saveChartConcept(item);
	}
	
}
