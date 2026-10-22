package co.edu.unbosque.BirdTracker.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

import co.edu.unbosque.BirdTracker.dobles.RelojFijo;
import co.edu.unbosque.BirdTracker.model.Avistamiento;
import co.edu.unbosque.BirdTracker.repository.AvistamientoRepository;
import co.edu.unbosque.BirdTracker.validacion.Reloj;

@SpringBootTest
@AutoConfigureMockMvc
class AvistamientoApiIntegrationTest {
    @TestConfiguration
    static class RelojDePrueba {
        @Bean
        @Primary
        Reloj relojFijo() {
            return new RelojFijo();
        }
    }

    private static final String VALIDO = """
            {"especie":"Colibrí","lugar":"Humedal La Conejera","fecha":"2026-10-21","observador":"Julián"}
            """;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AvistamientoRepository repositorio;

    @BeforeEach
    void limpiarBaseDeDatos() {
        repositorio.deleteAll();
    }

    private Long existente(String especie) {
        return repositorio.save(new Avistamiento(especie, "Parque Simón Bolívar", LocalDate.of(2026, 9, 29), "Ana"))
                .getId();
    }

    @Test
    void post_conDatosValidos_responde201ConLocationYLuegoGetLoDevuelve() throws Exception {
        MvcResult resultado = mvc.perform(post("/avistamientos").contentType(MediaType.APPLICATION_JSON).content(VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.especie").value("Colibrí"))
                .andExpect(jsonPath("$.fecha").value("2026-10-21"))
                .andExpect(header().string("Location", containsString("/avistamientos/")))
                .andReturn();

        Integer id = JsonPath.read(resultado.getResponse().getContentAsString(), "$.id");
        mvc.perform(get("/avistamientos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lugar").value("Humedal La Conejera"))
                .andExpect(jsonPath("$.observador").value("Julián"));
    }

    @Test
    void post_conEspecieDe2Caracteres_responde400IndicandoElCampoYNoGuarda() throws Exception {
        String cuerpo = """
                {"especie":"ab","lugar":"Humedal","fecha":"2026-10-01","observador":"Julián"}
                """;

        mvc.perform(post("/avistamientos").contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(containsString("especie")))
                .andExpect(jsonPath("$.detalles.especie").exists());
        mvc.perform(get("/avistamientos")).andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void post_conFechaDeManana_responde400IndicandoLaFecha() throws Exception {
        String cuerpo = """
                {"especie":"Colibrí","lugar":"Humedal","fecha":"2026-10-22","observador":"Julián"}
                """;

        mvc.perform(post("/avistamientos").contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.fecha").value(containsString("posterior a hoy")));
    }

    @Test
    void post_conFechaMalFormada_responde400IndicandoLaFecha() throws Exception {
        String cuerpo = """
                {"especie":"Colibrí","lugar":"Humedal","fecha":"21/10/2026","observador":"Julián"}
                """;

        mvc.perform(post("/avistamientos").contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.fecha").exists());
    }

    @Test
    void post_conJsonMalFormado_responde400() throws Exception {
        mvc.perform(post("/avistamientos").contentType(MediaType.APPLICATION_JSON).content("{\"especie\":"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("JSON inválido o mal formado"));
    }

    @Test
    void getTodos_conDosRegistros_responde200ConLaLista() throws Exception {
        existente("Colibrí");
        existente("Tingua");

        mvc.perform(get("/avistamientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].especie").value(org.hamcrest.Matchers.containsInAnyOrder("Colibrí", "Tingua")));
    }

    @Test
    void getPorId_existente_responde200ConSusDatos() throws Exception {
        Long id = existente("Copetón");

        mvc.perform(get("/avistamientos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.especie").value("Copetón"))
                .andExpect(jsonPath("$.fecha").value("2026-09-29"));
    }

    @Test
    void getPorId_inexistente_responde404() throws Exception {
        mvc.perform(get("/avistamientos/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Avistamiento no encontrado"));
    }

    @Test
    void put_conDatosValidos_responde200YElCambioPersiste() throws Exception {
        Long id = existente("Copetón");
        String cuerpo = """
                {"especie":"Mirla patiamarilla","lugar":"Cerros orientales","fecha":"2026-10-20","observador":"Julián"}
                """;

        mvc.perform(put("/avistamientos/" + id).contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.especie").value("Mirla patiamarilla"));

        mvc.perform(get("/avistamientos/" + id))
                .andExpect(jsonPath("$.lugar").value("Cerros orientales"))
                .andExpect(jsonPath("$.fecha").value("2026-10-20"));
    }

    @Test
    void put_conLugarVacio_responde400YNoModifica() throws Exception {
        Long id = existente("Copetón");
        String cuerpo = """
                {"especie":"Copetón","lugar":"  ","fecha":"2026-10-20","observador":"Julián"}
                """;

        mvc.perform(put("/avistamientos/" + id).contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.lugar").exists());

        mvc.perform(get("/avistamientos/" + id)).andExpect(jsonPath("$.lugar").value("Parque Simón Bolívar"));
    }

    @Test
    void put_aIdInexistente_responde404() throws Exception {
        mvc.perform(put("/avistamientos/999999").contentType(MediaType.APPLICATION_JSON).content(VALIDO))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_existente_responde204YLuegoGetResponde404() throws Exception {
        Long id = existente("Copetón");

        mvc.perform(delete("/avistamientos/" + id)).andExpect(status().isNoContent());

        mvc.perform(get("/avistamientos/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void delete_inexistente_responde404() throws Exception {
        mvc.perform(delete("/avistamientos/999999")).andExpect(status().isNotFound());
    }

    @Test
    void resumen_agrupaYCuentaPorEspecie() throws Exception {
        existente("Colibrí");
        existente("Colibrí");
        existente("Tingua");

        mvc.perform(get("/avistamientos/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].especie").value("Colibrí"))
                .andExpect(jsonPath("$[0].cantidad").value(2))
                .andExpect(jsonPath("$[1].especie").value("Tingua"))
                .andExpect(jsonPath("$[1].cantidad").value(1));
    }
}
