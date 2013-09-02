package es.uji.apps.par.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.StringPath;

import es.uji.apps.par.model.OrdreGrid;
import es.uji.apps.par.utils.Utils;

public class BaseDAO {
	@PersistenceContext
    protected EntityManager entityManager;
	protected static final String ASC = "ASC";
	protected static final String DESC = "DESC";

    public EntityManager getEntityManager() {
    	return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
    	this.entityManager = entityManager;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected OrderSpecifier<String> getSort(EntityPath entity, String sortParameter, String optionalColumn, String direction) {
    	if (hasSort(sortParameter)) {
    		OrdreGrid sort = Utils.getSortFromParameter(sortParameter);
			ComparablePath comparablePath = new ComparablePath<Comparable>(Comparable.class, entity, sort.getPropietat());
			return new OrderSpecifier(sort.getOrdre().equals("ASC")?com.mysema.query.types.Order.ASC:com.mysema.query.types.Order.DESC, comparablePath);
		} else {
			optionalColumn = (optionalColumn.equals(""))?"id":optionalColumn;
			StringPath strPath = new StringPath(entity, optionalColumn);
			return (direction.equals("")||direction.equals(BaseDAO.ASC))?strPath.asc():strPath.desc();
		}
    }

	protected boolean hasSort(String sortParameter) {
		OrdreGrid sort = Utils.getSortFromParameter(sortParameter);
		return sort != null && sort.getOrdre() != null && sort.getPropietat() != null &&
			!sort.getOrdre().equals("") && !sort.getPropietat().equals("");
	}
    
    @SuppressWarnings("rawtypes")
	protected OrderSpecifier<String> getSort(EntityPath entity, String sortParameter) {
    	return getSort(entity, sortParameter, "", BaseDAO.ASC);
    }
    

}
