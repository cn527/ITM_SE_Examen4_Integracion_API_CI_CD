package calendario.api.core.servicios;

import java.util.List;

import calendario.api.dominio.entidades.Calendario;

public interface ICalendarioServicio {
    boolean generarCalendario(int anio);
    List<Calendario> listar(int anio);
}
