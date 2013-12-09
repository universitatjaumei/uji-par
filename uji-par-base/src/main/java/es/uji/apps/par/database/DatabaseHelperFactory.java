package es.uji.apps.par.database;

import es.uji.apps.par.config.Configuration;

public class DatabaseHelperFactory
{
    public static DatabaseHelper newInstance()
    {
        if (Configuration.getJdbUrl().toUpperCase().contains("ORACLE"))
        {
            return new DatabaseHelperOracle();
        }
        else if (Configuration.getJdbUrl().toUpperCase().contains("POSTGRES"))
        {
            return new DatabaseHelperPostgres();
        }
        else
        {
            throw new RuntimeException("No se ha encontrado un DatabaseHelper según la url de conexión JDBC: " + Configuration.getJdbUrl());
        }
    }
}
