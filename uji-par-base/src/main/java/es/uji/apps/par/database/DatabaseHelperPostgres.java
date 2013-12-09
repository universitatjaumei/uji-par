package es.uji.apps.par.database;

import java.math.BigDecimal;

public class DatabaseHelperPostgres implements DatabaseHelper
{
    public String paginate(int start, int limit, String sql)
    {
        return " select * from (" + sql + ") as s " +
               " offset " + start + " limit " + limit;
    }
    
    public Integer castId(Object id)
    {
        return (Integer)id;
    }
    
    public Boolean castBoolean(Object value)
    {
        return (Boolean)value;
    }
    
    public BigDecimal castBigDecimal(Object value)
    {
        return new BigDecimal((Float)value);
    }
}
