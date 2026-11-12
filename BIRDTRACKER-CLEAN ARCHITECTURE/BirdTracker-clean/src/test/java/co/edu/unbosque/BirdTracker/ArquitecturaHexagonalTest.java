package co.edu.unbosque.BirdTracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Revisa los imports del código fuente para que la dirección de las dependencias no se rompa:
 * el hexágono (paquete aplicacion) no puede depender de los adaptadores ni de ningún framework.
 */
class ArquitecturaHexagonalTest {
    private static final Path RAIZ = Path.of("src/main/java/co/edu/unbosque/BirdTracker");
    private static final String BASE = "co.edu.unbosque.BirdTracker.";

    private static List<String> importsDe(String subcarpeta) throws IOException {
        Path carpeta = RAIZ.resolve(subcarpeta);
        try (Stream<Path> archivos = Files.walk(carpeta)) {
            List<Path> fuentes = archivos.filter(p -> p.toString().endsWith(".java")).toList();
            assertThat(fuentes).as("archivos .java en " + carpeta).isNotEmpty();
            return fuentes.stream().flatMap(ArquitecturaHexagonalTest::lineas)
                    .filter(linea -> linea.startsWith("import "))
                    .toList();
        }
    }

    private static Stream<String> lineas(Path archivo) {
        try {
            return Files.readAllLines(archivo).stream().map(String::strip);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    @DisplayName("La aplicación (el hexágono) no importa adaptadores ni la configuración")
    void aplicacion_noDependeDeLosAdaptadores() throws IOException {
        assertThat(importsDe("aplicacion"))
                .noneMatch(i -> i.contains(BASE + "adaptador"))
                .noneMatch(i -> i.contains(BASE + "configuracion"));
    }

    @Test
    @DisplayName("La aplicación no importa Spring, JPA, Jakarta ni Swagger")
    void aplicacion_noDependeDeFrameworks() throws IOException {
        assertThat(importsDe("aplicacion"))
                .noneMatch(i -> i.contains("org.springframework"))
                .noneMatch(i -> i.contains("jakarta."))
                .noneMatch(i -> i.contains("io.swagger"))
                .noneMatch(i -> i.contains("org.hibernate"));
    }

    @Test
    @DisplayName("Los adaptadores de entrada y de salida no se conocen entre sí")
    void adaptadores_noSeConocenEntreSi() throws IOException {
        assertThat(importsDe("adaptador/entrada")).noneMatch(i -> i.contains(BASE + "adaptador.salida"));
        assertThat(importsDe("adaptador/salida")).noneMatch(i -> i.contains(BASE + "adaptador.entrada"));
    }
}
