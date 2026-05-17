package calendario.api.presentacion.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import calendario.api.core.servicios.ICalendarioServicio;
import calendario.api.dominio.entidades.Calendario;

@RestController
@RequestMapping("/api/calendario")
public class CalendarioControlador {

    @Autowired
    private ICalendarioServicio servicio;

    @GetMapping("/generar/{anio}")
    public ResponseEntity<?> generar(@PathVariable int anio) {
        try {
            return ResponseEntity.ok(servicio.generarCalendario(anio));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error generando el calendario: " + ex.getMessage());
        }
    }

    @GetMapping("/listar/{anio}")
    public ResponseEntity<?> listar(@PathVariable int anio) {
        try {
            List<Calendario> datos = servicio.listar(anio);
            return ResponseEntity.ok(datos);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error listando el calendario: " + ex.getMessage());
        }
    }
}
