package es.uji.apps.par.database;

import java.math.BigDecimal;
import java.math.BigInteger;

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
        if (value == null)
            return null;
        else if (value instanceof Float)
            return new BigDecimal((Float)value);
        else if (value instanceof BigInteger)
            return new BigDecimal((BigInteger)value);
        else 
            throw new RuntimeException("Unknown cast type for: " + value);
    }

    @Override
    public String caseString(String expresion, String[] condicionesValores)
    {
        StringBuffer buff = new StringBuffer();
        
        buff.append(" CASE ");
        
        for (int i=0; i<condicionesValores.length; i+=2)
        {
            buff.append(" WHEN ");
            buff.append(expresion);
            buff.append("=");
            buff.append(condicionesValores[i]);
            buff.append(" THEN ");
            buff.append(condicionesValores[i+1]);
        }
        
        buff.append(" END ");
        
        return buff.toString();
    }
    
    public String trueString()
    {
        return "true";
    }
    
    public String falseString()
    {
        return "false";
    }
}
