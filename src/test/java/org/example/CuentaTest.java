package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class CuentaTest {
    public static Cuenta cuenta;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
    }

    @Nested
    class TestingCuentas {


        @Test
        @Timeout(value = 10, unit = TimeUnit.MILLISECONDS)
        @DisplayName("Pruebas de creación de Cuenta")
        void testCuenta() {
            String esperado = "Andres";
            String real = cuenta.getPersona();
            Assertions.assertEquals(esperado, real);
        }

        @Test
        @Tag("saldo")
        @DisplayName("Pruebas de Saldo en cuenta")
        void saldoCuenta() {
            assertEquals(cuenta.getSaldo().doubleValue(), 1000.12345);
        }

        @Test
        @Disabled
        @DisplayName("Pruebas de Saldo en cuenta deshabilitado")
        void saldoCuenta2() {
            Cuenta cuenta = new Cuenta("Malcom", new BigDecimal("12345.12345"));

            assertEquals(cuenta.getSaldo().doubleValue(), 12345.12345);
        }
    }

    @Nested
    class TestingBancos {
        @Test
        @Tag("saldo")
        @DisplayName("Pruebas de Transferencia")
        void testTransferirDineroCuentas() {

            Cuenta origen2 = new Cuenta("Pablo", new BigDecimal("1500"));

            Banco banco = new Banco();
            banco.setNombre("Galicia");
            banco.transferir(cuenta, origen2, new BigDecimal("500.12345"));

            assertEquals(cuenta.getSaldo().toPlainString(), "500.00000", "Comprobación de Cuenta de Origen");
            assertEquals(origen2.getSaldo().toPlainString(), "2000.12345", "Comprobación de Cuenta de Destino");
        }

        @Test
        @DisplayName("Pruebas de Banco")
        void testRelacionBancoCuenta() {
            Cuenta origen1 = new Cuenta("Pedro", new BigDecimal("1500.12345"));
            Cuenta origen2 = new Cuenta("Pablo", new BigDecimal("1000"));

            Banco banco = new Banco();
            banco.setNombre("Galicia");

            banco.addCuenta(origen1);
            banco.addCuenta(origen2);
            banco.transferir(origen1, origen2, new BigDecimal("1000.12345"));

            assertEquals(origen1.getSaldo().toPlainString(), "500.00000");
            assertEquals(origen2.getSaldo().toPlainString(), "2000.12345");
            assertEquals(2, banco.getCuentas().size());
            assertEquals("Galicia", origen1.getBanco().getNombre());
            assertEquals("Pedro", banco.getCuentas().stream().filter(c -> c.getPersona().equals("Pedro")).findFirst().get().getPersona());

            assertAll(() -> {
                assertEquals(origen1.getSaldo().toPlainString(), "500.00000", () -> "Tu saldo es insuficiente 1");
            }, () -> {
                assertEquals(origen2.getSaldo().toPlainString(), "2000.12345", () -> "Tu saldo es insuficiente 2");
            }, () -> {
                assertEquals(2, banco.getCuentas().size(), () -> "Tu saldo es insuficiente 3");
            });
        }


    }

    @Nested
    class TestingCondicional {
        @Test
        @DisplayName("Mostrar variables de entorno")
        void imprimirVariablesDeEntorno() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v) -> System.out.println(k + " " + v));
        }

        @Test
        @DisplayName("Prueba Condicional según cantidad de núcleos en procesador")
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "18")
        void testsIfProcessors() {
            System.out.println("Tienes 8 procesadores en tu PC");
        }

        @Test
        @DisplayName("Pruebas de Transferencia Assumption")
        void testTransferirDineroCuentasAssumption() {

            Cuenta origen2 = new Cuenta("Pablo", new BigDecimal("1500"));

            Banco banco = new Banco();
            banco.setNombre("Galicia");
            banco.transferir(cuenta, origen2, new BigDecimal("500.12345"));

            Boolean check = "dev".equals(System.getProperty("ENV"));
            System.out.println(check);
            assumeTrue(check);

            assertEquals(cuenta.getSaldo().toPlainString(), "500.00000", "Comprobación de Cuenta de Origen");
            assertEquals(origen2.getSaldo().toPlainString(), "2000.12345", "Comprobación de Cuenta de Destino");
        }

    }

    @Nested
    class TestingConRepeticiones {
        @RepeatedTest(10)
        @DisplayName("Mostrar variables de entorno")
        void imprimirVariablesDeEntorno() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v) -> System.out.println(k + " " + v));
        }

        @Nested
        @Tag ("saldo")
        class TestingParametrizado {
            @ParameterizedTest
            @ValueSource(strings = {"100", "200", "500", "1500"})
            void pruebasParametrizadas(String montoto) {
                Cuenta origen2 = new Cuenta("Pablo", new BigDecimal(montoto));

                Banco banco = new Banco();
                banco.setNombre("Galicia");
                banco.transferir(cuenta, origen2, new BigDecimal("500.12345"));

                assertEquals(cuenta.getSaldo().toPlainString(), "500.00000", "Comprobación de Cuenta de Origen");
                assertEquals(origen2.getSaldo().toPlainString(), "2000.12345", "Comprobación de Cuenta de Destino");
            }

            @ParameterizedTest
            @CsvFileSource(resources = "/data.csv")
            void pruebasParametrizadasCsvFile(String montoto) {
                Cuenta origen2 = new Cuenta("Pablo", new BigDecimal(montoto));

                Banco banco = new Banco();
                banco.setNombre("Galicia");
                banco.transferir(cuenta, origen2, new BigDecimal(montoto));

                assert cuenta.getSaldo().doubleValue() > 0;

            }

            @ParameterizedTest
            @MethodSource("metodato")
            void pruebasParametrizadasMethod(String montoto) {
                Cuenta origen2 = new Cuenta("Pablo", new BigDecimal(montoto));

                Banco banco = new Banco();
                banco.setNombre("Galicia");
                banco.transferir(cuenta, origen2, new BigDecimal(montoto));
                assert cuenta.getSaldo().doubleValue() > 0;

            }

            static List<String> metodato() {
                return Arrays.asList("100", "200", "500", "1500");
            }

            @ParameterizedTest
            @CsvSource({"500,500.12345", "1000,0.12345", "800,200.12345", "300.1234,700.00005"})
            void pruebaDébitoConVariosMontos(String debito, String saldo) {
                cuenta.debito(new BigDecimal(debito));
                assertEquals(new BigDecimal(saldo).doubleValue(), cuenta.getSaldo().doubleValue());
            }

        }
    }

}