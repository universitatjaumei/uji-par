package es.uji.apps.par.comparator;

import es.uji.apps.par.model.Butaca;

import java.util.Comparator;

public class ButacaComparator implements Comparator<Butaca> {
    @Override
    public int compare(Butaca butaca1, Butaca butaca2) {
        if (butaca1.getFila() != null && butaca1.getNumero() != null && butaca2.getFila() != null && butaca2.getNumero() != null)
        {
            Integer fila1 = Integer.parseInt(butaca1.getFila());
            Integer fila2 = Integer.parseInt(butaca2.getFila());
            int compareFila = fila1.compareTo(fila2);
            if (compareFila == 0) {
                Integer numero1 = Integer.parseInt(butaca1.getNumero());
                Integer numero2 = Integer.parseInt(butaca2.getNumero());
                return numero1.compareTo(numero2);
            }
            else {
                return compareFila;
            }
        }

        return 0;
    }
}
