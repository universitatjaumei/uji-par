package es.uji.apps.par.ficheros.utils;

import es.uji.apps.par.RegistroSerializaException;

public class FicherosUtils
{
    public static void compruebaCodigoSala(String codigoSala) throws RegistroSerializaException
    {
        if (codigoSala.length() > 6)
            throw new RegistroSerializaException(
                    "El codigoSala es un string de tamaño mayor de 6 carácteres: codigoSala=" + codigoSala);
    }
}
