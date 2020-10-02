package org.openmrs.module.graphing;

import java.io.Serializable;
import org.openmrs.BaseOpenmrsData;

/**
 * @author levine
 */
public class ChartConcept extends BaseOpenmrsData implements Serializable {
	
	private int id;
	
	private int conceptId;
	
	private int chartId;
	
	private String color;
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public int getConceptId() {
		return conceptId;
	}
	
	public void setConceptId(int conceptId) {
		this.conceptId = conceptId;
	}
	
	public int getChartId() {
		return chartId;
	}
	
	public void setChartId(int chartId) {
		this.chartId = chartId;
	}
	
	public Integer getId() {
		return id;
	}
	
	/**
	 * The primary key for this ChartType. If this is null, the database will generate the integer
	 * primary key because we marked the ChartType.id column to auto_increment. <br/>
	 * <br/>
	 * 
	 * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
	 */
	public void setId(Integer id) {
		this.id = id;
	}
}
