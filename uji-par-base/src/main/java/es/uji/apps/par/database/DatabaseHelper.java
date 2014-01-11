package es.uji.apps.par.database;

import java.math.BigDecimal;

public interface DatabaseHelper
{
    public String paginate(int start, int limit, String sql);

    public Integer castId(Object id);

    public Boolean castBoolean(Object value);

    public BigDecimal castBigDecimal(Object value);

    public String caseString(String condicion, String[] condicionesValores);

    public String trueString();

    public String falseString();
    
    public String trunc(String campo, String formato);
}