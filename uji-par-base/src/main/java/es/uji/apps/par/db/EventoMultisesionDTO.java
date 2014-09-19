package es.uji.apps.par.db;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="PAR_EVENTOS_MULTISESION", uniqueConstraints={@UniqueConstraint(columnNames={"ID", "EVENTO_ID"})})
public class EventoMultisesionDTO {
    @Id
    @SequenceGenerator(name="PAR_EVENTOS_MULTISESION_ID_GENERATOR", sequenceName="HIBERNATE_SEQUENCE")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PAR_EVENTOS_MULTISESION_ID_GENERATOR")
    private long id;

    @ManyToOne
    @JoinColumn(name="EVENTO_ID")
    private EventoDTO parEvento;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EventoDTO getParEvento() {
        return parEvento;
    }

    public void setParEvento(EventoDTO parEvento) {
        this.parEvento = parEvento;
    }
}
