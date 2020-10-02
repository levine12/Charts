package org.openmrs.module.graphing.api.db;

import java.util.List;
import org.openmrs.api.APIException;
import org.openmrs.module.graphing.ChartType;

/**
 * This is the DAO interface. This is never used by the developer, but instead the
 * {@link ChartTypeService} calls it (and developers call the NoteService)
 */

public interface ChartTypeDao {
	
	public List<ChartType> getAllChartTypes();
	
	public ChartType saveChartType(ChartType item) throws APIException;
	
}
