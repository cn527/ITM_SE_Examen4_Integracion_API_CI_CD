package calendario.api.infraestructura.integracion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import calendario.api.dominio.dtos.FestivoDto;

@Service
public class FestivoCliente {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${integracion.festivos.base-url:http://localhost:8080/api/festivos}")
    private String baseUrl;

    public List<FestivoDto> obtenerFestivos(int anio) {
        String url = baseUrl + "/obtener/" + anio;

        try {
            ResponseEntity<FestivoDto[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<FestivoDto[]>() {}
            );

            FestivoDto[] body = response.getBody();
            if (body == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(body);
        } catch (RestClientException ex) {
            throw new RuntimeException("No fue posible consumir la API de Festivos en: " + url, ex);
        }
    }
}
