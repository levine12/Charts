package org.openmrs.module.graphing.api.db.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.openmrs.api.APIException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.graphing.Chart;
import org.openmrs.module.graphing.ChartConcept;
import org.openmrs.module.graphing.api.db.ChartConceptDao;

/**
 * This is a Hibernate object. It gives us metadata about the currently connected database, the
 * current session, the current db user, etc. To save and get objects, calls should go through
 * sessionFactory.getCurrentSession() <br/>
 * <br/>
 * This is called by Spring. See the /metadata/moduleApplicationContext.xml for the "sessionFactory"
 * setting. See the applicationContext-service.xml file in CORE openmrs for where the actual
 * "sessionFactory" object is first defined.
 * 
 * @param sessionFactory
 */

public class HibernateChartConceptDAO implements ChartConceptDao {
	
	private DbSessionFactory sessionFactory;
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		
	}
	
	@Override
	public List<ChartConcept> getAllChartConcepts() {
		// FOLLOWING IS AUTO GENERATED SO CHECK CORRECTNESS
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ChartConcept.class);
		return crit.list();
	}
	
	public List<ChartConcept> getAllChartConceptsForChart(int chartId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ChartConcept.class);
		crit.add(Restrictions.eq("chartId", chartId));
		return crit.list();
	}
	
	@Override
	public ChartConcept saveChartConcept(ChartConcept item) throws APIException {
		sessionFactory.getCurrentSession().saveOrUpdate(item);
		return item;
	}
	
}
