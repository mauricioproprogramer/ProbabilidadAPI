import java.util.Locale;
import java.util.Scanner;

/**
 * MÓDULO «PROBABILIDAD API»
 * Cálculos básicos de probabilidad mediante funciones:
 *   a) Probabilidad clásica (Laplace)
 *   b) Frecuencia relativa (enfoque frecuentista)
 *   c) Independencia de eventos
 */
public class ProbabilidadAPI {

    /** Tolerancia para comparar números decimales (evita errores de redondeo). */
    private static final double EPSILON = 1e-9;

    // =====================================================================
    //  a) PROBABILIDAD CLÁSICA (regla de Laplace)
    //     P(A) = casos favorables / casos posibles
    //     Supone que todos los resultados son equiprobables.
    // =====================================================================
    public static double probabilidadClasica(int casosFavorables, int casosPosibles) {
        if (casosPosibles <= 0) {
            throw new IllegalArgumentException("Los casos posibles deben ser mayores que 0.");
        }
        if (casosFavorables < 0) {
            throw new IllegalArgumentException("Los casos favorables no pueden ser negativos.");
        }
        if (casosFavorables > casosPosibles) {
            throw new IllegalArgumentException("Los casos favorables no pueden superar a los posibles.");
        }
        return (double) casosFavorables / casosPosibles;
    }

    // =====================================================================
    //  b) FRECUENCIA RELATIVA
    //     fr = veces que ocurrió el evento / experimentos realizados
    //     Se estima a partir de la experimentación; tiende a la probabilidad
    //     teórica cuando el número de experimentos crece (ley de los grandes números).
    // =====================================================================
    public static double frecuenciaRelativa(int cantidadEventos, int cantidadExperimentos) {
        if (cantidadExperimentos <= 0) {
            throw new IllegalArgumentException("Los experimentos realizados deben ser mayores que 0.");
        }
        if (cantidadEventos < 0) {
            throw new IllegalArgumentException("La cantidad de eventos no puede ser negativa.");
        }
        if (cantidadEventos > cantidadExperimentos) {
            throw new IllegalArgumentException("Los eventos no pueden superar a los experimentos.");
        }
        return (double) cantidadEventos / cantidadExperimentos;
    }

    // =====================================================================
    //  c) INDEPENDENCIA DE EVENTOS
    //     A y B son independientes si P(A ∩ B) = P(A) · P(B)
    // =====================================================================
    public static boolean sonIndependientes(double pA, double pB, double pAyB) {
        validarProbabilidad(pA, "P(A)");
        validarProbabilidad(pB, "P(B)");
        validarProbabilidad(pAyB, "P(A ∩ B)");
        if (pAyB > Math.min(pA, pB) + EPSILON) {
            throw new IllegalArgumentException("P(A ∩ B) no puede ser mayor que P(A) ni que P(B).");
        }
        return Math.abs(pAyB - pA * pB) < EPSILON;
    }

    private static void validarProbabilidad(double p, String nombre) {
        if (Double.isNaN(p) || p < 0 || p > 1) {
            throw new IllegalArgumentException(nombre + " debe estar entre 0 y 1.");
        }
    }

    // =====================================================================
    //  PRESENTACIÓN DE RESULTADOS
    // =====================================================================
    private static String formato(double p) {
        return String.format(Locale.US, "%.4f (%.2f%%)", p, p * 100);
    }

    private static void mostrarClasica(int fav, int pos) {
        double p = probabilidadClasica(fav, pos);
        System.out.println("  P(A) = " + fav + " / " + pos + " = " + formato(p));
        System.out.println("  Interpretación: de cada 100 repeticiones ideales, se esperan "
                + String.format(Locale.US, "%.1f", p * 100) + " resultados favorables.");
    }

    private static void mostrarFrecuencia(int eventos, int experimentos) {
        double fr = frecuenciaRelativa(eventos, experimentos);
        System.out.println("  fr = " + eventos + " / " + experimentos + " = " + formato(fr));
        System.out.println("  Interpretación: en este experimento el evento ocurrió en el "
                + String.format(Locale.US, "%.2f%%", fr * 100) + " de las veces.");
    }

    private static void mostrarIndependencia(double pA, double pB, double pAyB) {
        boolean indep = sonIndependientes(pA, pB, pAyB);
        double producto = pA * pB;
        System.out.println("  P(A) = " + pA + "   P(B) = " + pB + "   P(A ∩ B) = " + pAyB);
        System.out.println("  P(A)·P(B) = " + String.format(Locale.US, "%.4f", producto));
        if (indep) {
            System.out.println("  Resultado: P(A ∩ B) = P(A)·P(B)  ->  los eventos SON independientes.");
            System.out.println("  Interpretación: que ocurra A no modifica la probabilidad de B.");
        } else {
            System.out.println("  Resultado: P(A ∩ B) ≠ P(A)·P(B)  ->  los eventos NO son independientes.");
            System.out.println("  Interpretación: A y B están relacionados; la ocurrencia de uno afecta al otro.");
        }
    }

    // =====================================================================
    //  EJEMPLOS DE USO (uno por función, con resultado esperado verificable)
    // =====================================================================
    private static void ejemplos() {
        System.out.println("\n===== EJEMPLOS DE USO =====");

        System.out.println("\nEjemplo 1 - Probabilidad clásica:");
        System.out.println("  Sacar un número par al lanzar un dado (favorables = 3, posibles = 6).");
        mostrarClasica(3, 6); // 0.5

        System.out.println("\nEjemplo 2 - Frecuencia relativa:");
        System.out.println("  Al lanzar una moneda 200 veces salió cara 108 veces.");
        mostrarFrecuencia(108, 200); // 0.54

        System.out.println("\nEjemplo 3 - Eventos independientes:");
        System.out.println("  A = cara en moneda 1, B = cara en moneda 2.");
        mostrarIndependencia(0.5, 0.5, 0.25); // independientes

        System.out.println("\nEjemplo 4 - Eventos NO independientes:");
        System.out.println("  Extraer 2 ases de un mazo sin reposición: A = 1.er as, B = 2.º as.");
        // P(A)=4/52, P(B)=4/52 (por simetría), P(A∩B)=(4/52)(3/51)
        mostrarIndependencia(4.0 / 52, 4.0 / 52, (4.0 / 52) * (3.0 / 51));
    }

    // =====================================================================
    //  PRUEBAS AUTOMÁTICAS (verifican que los cálculos sean correctos)
    // =====================================================================
    private static void pruebas() {
        System.out.println("\n===== PRUEBAS AUTOMÁTICAS =====");
        verificar("Clásica 3/6 = 0.5", Math.abs(probabilidadClasica(3, 6) - 0.5) < EPSILON);
        verificar("Clásica 0/10 = 0", probabilidadClasica(0, 10) == 0.0);
        verificar("Clásica 10/10 = 1", probabilidadClasica(10, 10) == 1.0);
        verificar("Frecuencia 108/200 = 0.54", Math.abs(frecuenciaRelativa(108, 200) - 0.54) < EPSILON);
        verificar("Independientes 0.5, 0.5, 0.25", sonIndependientes(0.5, 0.5, 0.25));
        verificar("No independientes 0.5, 0.4, 0.3", !sonIndependientes(0.5, 0.4, 0.3));
        verificar("Error si posibles = 0", lanzaError(() -> probabilidadClasica(1, 0)));
        verificar("Error si favorables > posibles", lanzaError(() -> probabilidadClasica(7, 6)));
        verificar("Error si eventos > experimentos", lanzaError(() -> frecuenciaRelativa(11, 10)));
        verificar("Error si P(A) = 1.5", lanzaError(() -> sonIndependientes(1.5, 0.5, 0.2)));
    }

    private static void verificar(String descripcion, boolean ok) {
        System.out.println("  [" + (ok ? "OK" : "FALLÓ") + "] " + descripcion);
    }

    private static boolean lanzaError(Runnable r) {
        try {
            r.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    // =====================================================================
    //  ENTRADA DE DATOS Y MENÚ
    // =====================================================================
    private static int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Valor inválido. Ingrese un número entero.");
            }
        }
    }

    private static double leerDecimal(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("  Valor inválido. Ingrese un número (ej: 0.5).");
            }
        }
    }

    /** Pregunta si se quiere volver al menú o cerrar el programa. */
    private static boolean preguntarSiContinuar(Scanner sc) {
        while (true) {
            System.out.println("\n¿Qué desea hacer ahora?");
            System.out.println(" 1. Volver al menú y elegir otra opción");
            System.out.println(" 2. Cerrar el programa");
            System.out.print("Elija 1 o 2: ");
            String r = sc.nextLine().trim();
            if (r.equals("1")) return true;
            if (r.equals("2")) return false;
            System.out.println("  Opción no válida. Escriba 1 o 2.");
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n=====================================");
        System.out.println("        PROBABILIDAD API");
        System.out.println("=====================================");
        System.out.println(" 1. Probabilidad clásica");
        System.out.println(" 2. Frecuencia relativa");
        System.out.println(" 3. Independencia de eventos");
        System.out.println(" 4. Ver ejemplos de uso");
        System.out.println(" 5. Ejecutar pruebas automáticas");
        System.out.println(" 0. Salir");
        System.out.print("Elija una opción: ");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            String opcion = sc.nextLine().trim();
            boolean opcionValida = true;

            try {
                switch (opcion) {
                    case "1":
                        System.out.println("\n--- Probabilidad clásica ---");
                        int fav = leerEntero(sc, "Casos favorables: ");
                        int pos = leerEntero(sc, "Casos posibles: ");
                        mostrarClasica(fav, pos);
                        break;
                    case "2":
                        System.out.println("\n--- Frecuencia relativa ---");
                        int ev = leerEntero(sc, "Cantidad de veces que ocurrió el evento: ");
                        int exp = leerEntero(sc, "Cantidad de experimentos realizados: ");
                        mostrarFrecuencia(ev, exp);
                        break;
                    case "3":
                        System.out.println("\n--- Independencia de eventos ---");
                        double pA = leerDecimal(sc, "P(A): ");
                        double pB = leerDecimal(sc, "P(B): ");
                        double pAyB = leerDecimal(sc, "P(A ∩ B): ");
                        mostrarIndependencia(pA, pB, pAyB);
                        break;
                    case "4":
                        ejemplos();
                        break;
                    case "5":
                        pruebas();
                        break;
                    case "0":
                        salir = true;
                        opcionValida = false; // no preguntar, ya eligió salir
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        opcionValida = false; // vuelve al menú directo
                }
            } catch (IllegalArgumentException e) {
                System.out.println("  Error: " + e.getMessage());
            }

            // Después de mostrar resultados (o un error de datos), preguntar si sigue
            if (opcionValida && !salir) {
                if (!preguntarSiContinuar(sc)) {
                    salir = true;
                }
            }
        }

        System.out.println("\nHasta luego.");
        sc.close();
    }
}