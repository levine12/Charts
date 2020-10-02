package org.openmrs.module.graphing;

import java.io.Serializable;
import java.util.Date;
import org.openmrs.BaseOpenmrsData;

public class ChartType extends BaseOpenmrsData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String name;
	
	private Date dateCreated;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
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
