package org.openmrs.module.graphing;

import java.io.Serializable;
import org.openmrs.BaseOpenmrsData;

public class Chart extends BaseOpenmrsData implements Serializable {
	
	private int id;
	
	private int chartTypeId;
	
	private String xaxisLabel, yaxisLabel, title;
	
	public int getchartTypeId() {
		return chartTypeId;
	}
	
	public void setchartTypeId(int chartTypeId) {
		this.chartTypeId = chartTypeId;
	}
	
	// private boolean voided;
	public String getXaxisLabel() {
		return xaxisLabel;
	}
	
	public void setXaxisLabel(String xaxisLabel) {
		this.xaxisLabel = xaxisLabel;
	}
	
	public String getYaxisLabel() {
		return yaxisLabel;
	}
	
	public void setYaxisLabel(String yaxisLabel) {
		this.yaxisLabel = yaxisLabel;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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
