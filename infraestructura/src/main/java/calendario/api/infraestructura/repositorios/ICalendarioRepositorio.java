package calendario.api.infraestructura.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import calendario.api.dominio.entidades.Calendario;

@Repository
public interface ICalendarioRepositorio extends JpaRepository<Calendario, Long> {
    List<Calendario> findByFechaBetweenOrderByFecha(LocalDate desde, LocalDate hasta);
    void deleteByFechaBetween(LocalDate desde, LocalDate hasta);
}
