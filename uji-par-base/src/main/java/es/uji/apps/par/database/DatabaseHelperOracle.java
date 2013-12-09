package es.uji.apps.par.database;

import java.math.BigDecimal;

public class DatabaseHelperOracle implements DatabaseHelper
{
    public String paginate(int start, int limit, String sql)
    {
        return " select * from " +
        "    (select a.*, rownum as rn from (" + sql + ") a " +
        "    where rownum <= " + (start+limit) + ") " +
        " where rn>=" + (start+1);   
    }
    
    public Integer castId(Object id)
    {
        return ((BigDecimal)id).intValue();
    }
    
    public Boolean castBoolean(Object value)
    {
        return ((BigDecimal)value).intValue() == 1;
    }
    
    public BigDecimal castBigDecimal(Object value)
    {
        return (BigDecimal)value;
    }
}
