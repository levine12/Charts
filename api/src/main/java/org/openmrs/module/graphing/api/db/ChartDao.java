package org.openmrs.module.graphing.api.db;

import java.util.List;
import org.openmrs.api.APIException;
import org.openmrs.module.graphing.Chart;

/**
 * This is the DAO interface. This is never used by the developer, but instead the
 * {@link ChartService} calls it (and developers call the NoteService)
 */

public interface ChartDao {
	
	public List<Chart> getAllCharts();
	
	public Chart saveChart(Chart item) throws APIException;
	
	public Chart getChart(Integer id);
	
}
