package calendario.api.aplicacion.servicios;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import calendario.api.core.servicios.ICalendarioServicio;
import calendario.api.dominio.dtos.FestivoDto;
import calendario.api.dominio.entidades.Calendario;
import calendario.api.dominio.entidades.Tipo;
import calendario.api.infraestructura.integracion.FestivoCliente;
import calendario.api.infraestructura.repositorios.ICalendarioRepositorio;
import calendario.api.infraestructura.repositorios.ITipoRepositorio;

@Service
public class CalendarioServicio implements ICalendarioServicio {

    private static final String TIPO_LABORAL = "Día laboral";
    private static final String TIPO_FIN_DE_SEMANA = "Fin de Semana";
    private static final String TIPO_FESTIVO = "Día festivo";
    private static final Locale LOCALE_ES = new Locale("es", "CO");

    @Autowired
    private ICalendarioRepositorio calendarioRepositorio;

    @Autowired
    private ITipoRepositorio tipoRepositorio;

    @Autowired
    private FestivoCliente festivoCliente;

    @Override
    @Transactional
    public boolean generarCalendario(int anio) {
        validarAnio(anio);

        LocalDate inicio = LocalDate.of(anio, 1, 1);
        LocalDate fin = LocalDate.of(anio, 12, 31);

        List<FestivoDto> festivos = festivoCliente.obtenerFestivos(anio);
        Set<LocalDate> fechasFestivas = convertirAFechas(festivos);
        Map<String, Tipo> tipos = asegurarTiposBase();

        calendarioRepositorio.deleteByFechaBetween(inicio, fin);

        List<Calendario> registros = new ArrayList<>();
        LocalDate fecha = inicio;

        while (!fecha.isAfter(fin)) {
            Tipo tipo = resolverTipo(fecha, fechasFestivas, tipos);
            String descripcion = obtenerNombreDia(fecha.getDayOfWeek());

            registros.add(new Calendario(null, fecha, tipo, descripcion));
            fecha = fecha.plusDays(1);
        }

        calendarioRepositorio.saveAll(registros);
        return true;
    }

    @Override
    public List<Calendario> listar(int anio) {
        validarAnio(anio);
        LocalDate inicio = LocalDate.of(anio, 1, 1);
        LocalDate fin = LocalDate.of(anio, 12, 31);
        return calendarioRepositorio.findByFechaBetweenOrderByFecha(inicio, fin);
    }

    private void validarAnio(int anio) {
        if (anio < 1) {
            throw new IllegalArgumentException("El año debe ser mayor que cero");
        }
    }

    private Set<LocalDate> convertirAFechas(List<FestivoDto> festivos) {
        Set<LocalDate> fechas = new HashSet<>();
        for (FestivoDto festivo : festivos) {
            fechas.add(LocalDate.parse(festivo.getFecha()));
        }
        return fechas;
    }

    private Map<String, Tipo> asegurarTiposBase() {
        Map<String, Tipo> tipos = new HashMap<>();
        tipos.put(TIPO_LABORAL, obtenerOCrearTipo(TIPO_LABORAL));
        tipos.put(TIPO_FIN_DE_SEMANA, obtenerOCrearTipo(TIPO_FIN_DE_SEMANA));
        tipos.put(TIPO_FESTIVO, obtenerOCrearTipo(TIPO_FESTIVO));
        return tipos;
    }

    private Tipo obtenerOCrearTipo(String nombreTipo) {
        return tipoRepositorio.findByTipo(nombreTipo)
            .orElseGet(() -> tipoRepositorio.save(new Tipo(null, nombreTipo)));
    }

    private Tipo resolverTipo(LocalDate fecha, Set<LocalDate> fechasFestivas, Map<String, Tipo> tipos) {
        if (fechasFestivas.contains(fecha)) {
            return tipos.get(TIPO_FESTIVO);
        }

        DayOfWeek dayOfWeek = fecha.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return tipos.get(TIPO_FIN_DE_SEMANA);
        }

        return tipos.get(TIPO_LABORAL);
    }

    private String obtenerNombreDia(DayOfWeek dayOfWeek) {
        String nombre = dayOfWeek.getDisplayName(TextStyle.FULL, LOCALE_ES);
        return nombre.substring(0, 1).toUpperCase(LOCALE_ES) + nombre.substring(1);
    }
}
