/*
 * ============================================================
 * Project:   Agenda de Contactos (Console CSV)
 * File:      ConHelp.java
 * Package:   agend.helpers
 * Author:    Ángel Plata Benítez
 * Created:   2025-10-27
 * Version:   1.0
 * 
 * Description:
 *     Clase auxiliar para la interacción por consola.
 *     Incluye métodos estáticos para impresión formateada,
 *     lectura validada de texto y confirmaciones simples.
 *
 * License:
 *     This code is released under the MIT License.
 * ============================================================
 */
package agend.helpers;

import java.util.Scanner;

/**
 * Clase de utilidades para la interacción con la consola.
 * 
 * <p>Proporciona métodos estáticos para imprimir texto,
 * leer cadenas no vacías, pausar la ejecución y confirmar acciones.</p>
 */
public class ConHelp {
    
    /** Escáner único utilizado para todas las lecturas desde consola. */
    public static final Scanner SC = new Scanner(System.in, "UTF-8");
        
     /**
     * Imprime texto formateado en consola.
     *
     * @param format cadena de formato (igual que {@code printf} estándar)
     * @param args argumentos opcionales a sustituir en el formato
     */
    public static void printf(String format, Object... args){
        System.out.printf(format, args);
    }
    
     /**
     * Solicita al usuario una línea de texto no vacía.
     *
     * <p>El método repite el prompt hasta que el usuario
     * introduce una cadena válida.</p>
     *
     * @param prompt mensaje que se mostrará al usuario
     * @return cadena introducida (sin espacios iniciales ni finales)
     */
    public static String readLine (String prompt) {
        String input;
        while (true) {
            System.out.printf(prompt + " ");
            input = SC.nextLine().trim();
            
            if (!input.isEmpty()) {
                return input;
            }
            
            System.out.printf("El campo no puede estar vacío. Inténtelo de nuevo.%n");
        }
    }
    
     /**
     * Pausa la ejecución hasta que el usuario presiona ENTER.
     */
    public static void pause() {
        System.out.printf("Pulse ENTER para continuar.%n");
        SC.nextLine();
    }
    
     /**
     * Solicita una confirmación al usuario (S/N).
     *
     * @param prompt mensaje a mostrar antes de solicitar confirmación
     * @return {@code true} si el usuario responde 'S' o 's'
     */
    public static boolean confirm(String prompt) {            
            String response = readLine(prompt + " S para confirmar.");
            return response != null && response.trim().equalsIgnoreCase("S");           
        
    }
}
