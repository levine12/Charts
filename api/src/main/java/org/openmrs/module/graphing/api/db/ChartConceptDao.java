package org.openmrs.module.graphing.api.db;

import java.util.List;
import org.openmrs.api.APIException;
import org.openmrs.module.graphing.ChartConcept;

/**
 * This is the DAO interface. This is never used by the developer, but instead the
 * {@link ChartConceptService} calls it (and developers call the NoteService)
 */

public interface ChartConceptDao {
	
	public List<ChartConcept> getAllChartConcepts();
	
	public List<ChartConcept> getAllChartConceptsForChart(int chartId);
	
	public ChartConcept saveChartConcept(ChartConcept item) throws APIException;
	
}
