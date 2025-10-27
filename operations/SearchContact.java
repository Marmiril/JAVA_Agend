/*
 * ============================================================
 * Project:   Agenda de Contactos (Console CSV)
 * File:      SearchContact.java
 * Package:   agend.operations
 * Author:    Ángel Plata Benítez
 * Created:   2025-10-27
 * Version:   1.0
 * 
 * Description:
 *     Permite buscar contactos por distintos criterios:
 *       - ID (coincidencia exacta)
 *       - Nombre (contiene, sin distinción mayúsc/minúsc)
 *       - Teléfono (contiene / coincide)
 *       - Email (contiene, sin distinción mayúsc/minúsc)
 *
 *     Muestra los resultados en formato tabla.
 *     No modifica datos.
 *
 * License:
 *     This code is released under the MIT License.
 * ============================================================
 */

package agend.operations;

import agend.Config;
import agend.classes.Contact;
import static agend.helpers.ConHelp.pause;
import static agend.helpers.ConHelp.printf;
import static agend.helpers.ConHelp.readLine;
import static agend.helpers.TableHelp.lambda01;
import static agend.helpers.TableHelp.printContactsTable;
import static agend.helpers.TableHelp.promptCrite;
import agend.repository.ContactRepository;
import java.util.List;

/**
 * Operación de búsqueda de contactos.
 *
 * <p>El usuario elige el criterio de búsqueda (ID, nombre, teléfono o email),
 * introduce un valor, y se muestran los resultados coincidentes.
 * No altera el archivo CSV ni modifica contactos existentes.</p>
 */
public class SearchContact {
    
     /**
     * Ejecuta el flujo interactivo de búsqueda.
     *
     * <p>Comportamiento:
     * <ol>
     *   <li>Lista todos los contactos actuales.</li>
     *   <li>Pide al usuario un criterio de búsqueda.</li>
     *   <li>Solicita el valor a buscar según el criterio elegido.</li>
     *   <li>Muestra las coincidencias en una tabla o avisa si no hay resultados.</li>
     * </ol>
     * </p>
     */
    public void run() {
        
        printf("============ BUSCAR CONTACTO ============%n");
        
        ContactRepository repo = new ContactRepository(Config.DATA_FILE);
        List<Contact> contacts = repo.loadAll();
        
        // Mostrar estado actual de la agenda al usuario.
        printContactsTable(contacts);
        
        printf("Indique por medio de qué dato desea buscar el contacto:%n");
        int option = promptCrite(); if( option == 0) return;
        
        // Resultados potenciales de la búsqueda (para criterios 2-4).
        List<Contact> results = List.of();
        
        switch (option) {
            case 1 ->{
                // Búsqueda por ID exacto
                String input = readLine("Indique el ID para la búsqueda (0 para cancelar):%n").trim();
                if (input.equals("0")) {
                    printf("Operación cancelada.%n");
                    return;
                }
                
                try {
                    int id = Integer.parseInt(input);
                    
                    // Buscar coincidencia exacta de ID.
                    Contact found = null;
                    for (Contact c : contacts) {
                        if(c.getId() == id) {
                            found = c;
                            break;
                        }
                    }
                    
                    if (found != null) {
                        printf("========= RESULTADOS =========%n");
                        printContactsTable(List.of(found));
                        return;
                    } else {
                        printf("No existe contacto con ese ID: %d%n", id);
                        return;
                    }                    
                } catch (NumberFormatException ex) {
                    printf("Ha de indicar un número válido:%n");
                }          
            }           
           
            case 2 ->{ String value = readLine("Indique el nombre para la búsqueda:%n"); 
            results = lambda01(contacts, value, Contact::getName); }
            case 3 ->{ String value = readLine("Indique el teléfono para la búsqueda:%n"); 
            results = lambda01(contacts, value, Contact::getPhone); }
            case 4 ->{ String value = readLine("Indique el email para la búsqueda:%n");
            results = lambda01(contacts, value, Contact::getEmail); }           
            default -> {
                printf("Opción no válida.%n");
                pause();
                return;
    }
        }
          // Mostrar resultados de las búsquedas por nombre/teléfono/email.
          printContactsTable(results);      
          return;        
    }
}
