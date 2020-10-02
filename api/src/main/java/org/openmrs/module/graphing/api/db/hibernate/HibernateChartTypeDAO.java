package org.openmrs.module.graphing.api.db.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.openmrs.api.APIException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.graphing.ChartType;
import org.openmrs.module.graphing.api.db.ChartTypeDao;

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

public class HibernateChartTypeDAO implements ChartTypeDao {
	
	private DbSessionFactory sessionFactory;
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		
	}
	
	@Override
	public List<ChartType> getAllChartTypes() {
		// FOLLOWING IS AUTO GENERATED SO CHECK CORRECTNESS
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ChartType.class);
		return crit.list();
	}
	
	@Override
	public ChartType saveChartType(ChartType item) throws APIException {
		sessionFactory.getCurrentSession().saveOrUpdate(item);
		return item;
	}
	
}
