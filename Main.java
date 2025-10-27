/*
 * ============================================================
 * Project:   Agenda de Contactos (Console CSV)
 * File:      Main.java
 * Package:   agend
 * Author:    Ángel Plata Benítez
 * Created:   2025-10-27
 * Version:   1.0
 * 
 * Description:
 *     Punto de entrada principal de la aplicación Agenda.
 *     Muestra un menú en consola y delega las operaciones
 *     en las clases correspondientes (Create, List, Modify,
 *     Delete, Search).
 *
 * License:
 *     This code is released under the MIT License.
 * ============================================================
 */

package agend;

import static agend.helpers.ConHelp.printf;
import static agend.helpers.ConHelp.pause;
import static agend.helpers.ConHelp.readLine;
import agend.operations.CreateContact;
import agend.operations.DeleteContact;
import agend.operations.ListContacts;
import agend.operations.ModifyContact;
import agend.operations.SearchContact;

/**
 * Clase principal de la aplicación "Agenda de Contactos".
 * 
 * <p>Muestra el menú principal en consola y ejecuta las
 * operaciones elegidas por el usuario.</p>
 */
public class Main {

    public static void main(String[] args) {

            /**
     * Método principal. Controla el flujo general del programa
     * mostrando el menú principal y llamando a las clases de
     * operaciones correspondientes.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
        
        while (true) {            
                    printf("================== AGENDA ==================%n");
            printf("1 - Crear contacto.%n");
            printf("2 - Listar contactos.%n");
            printf("3 - Modificar contacto.%n");
            printf("4 - Eliminar contacto.%n");
            printf("5 - Buscar contacto.%n");
            printf("0 - SALIR...%n%n");            
        
        
        String option = readLine("Seleccione una opción...");
   
        switch (option) {
                case "1" -> { new CreateContact().run(); pause(); } 
                case "2" -> { new ListContacts().run(); pause(); }
                case "3" -> { new ModifyContact().run(); pause(); }                               
                case "4" -> { new DeleteContact().run(); pause(); }
                case "5" -> { new SearchContact().run(); pause(); }                   
                case "0" -> { printf("Saliendo de la aplicación...%n"); return; }
                default -> {printf("Opción incorrecta."); continue; }               
            }        
        }
    }
}
