package calendario.api.dominio.dtos;

public class FestivoDto {
    private String festivo;
    private String fecha;

    public FestivoDto() {
    }

    public FestivoDto(String festivo, String fecha) {
        this.festivo = festivo;
        this.fecha = fecha;
    }

    public String getFestivo() {
        return festivo;
    }

    public void setFestivo(String festivo) {
        this.festivo = festivo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
