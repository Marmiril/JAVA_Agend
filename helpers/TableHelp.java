/*
 * ============================================================
 * Project:   Agenda de Contactos (Console CSV)
 * File:      TableHelp.java
 * Package:   agend.helpers
 * Author:    Ángel Plata Benítez
 * Created:   2025-10-27
 * Version:   1.0
 * 
 * Description:
 *     Utilidades para mostrar y seleccionar contactos en consola.
 *     Incluye:
 *       - Impresión tabular de contactos
 *       - Selección interactiva por ID
 *       - Búsqueda filtrada mediante funciones lambda
 *
 * License:
 *     This code is released under the MIT License.
 * ============================================================
 */
package agend.helpers;
import agend.classes.Contact;
import static agend.helpers.ConHelp.printf;
import static agend.helpers.ConHelp.readLine;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Métodos auxiliares relacionados con la visualización y
 * selección de contactos.
 * 
 * <p>Proporciona utilidades para:
 * <ul>
 *   <li>Imprimir una tabla con los contactos</li>
 *   <li>Solicitar al usuario un ID válido</li>
 *   <li>Filtrar listas de contactos según un criterio textual</li>
 * </ul>
 * </p>
 */
public class TableHelp {

    /**
     * Imprime una tabla con encabezados y filas de contactos.
     *
     * <p>Si la lista es nula o está vacía, se indica al usuario
     * que no hay resultados y no se intenta imprimir la tabla.</p>
     *
     * @param contacts lista de contactos a mostrar
     */
    public static void printContactsTable(List<Contact> contacts) {
        if (contacts == null) {
            printf("%nNo hay contactos guardados para mostrar.%n");
            return;
        }
        if(contacts.isEmpty()) {
            printf("No se han encontrado contacto con ese criterio%n");
            return;
        }
         printf("%n%-4s %-20s %-25s %-30s %-16s%n",
                "ID", "Nombre", "Teléfono", "Email", "Creado");
        printf("---------------------------------------------------------------------------------------------------------%n");

        for (Contact c : contacts) {
            printf("%-4d %-20s %-25s %-30s %-16s%n",
                    c.getId(),
                    safe(c.getName()),
                    safe(c.getPhone()),
                    safe(c.getEmail()),
                    safe(c.getCreatedAt()));
            }
      }
    
    /**
     * Busca en la lista el índice (posición) de un contacto por su ID.
     *
     * @param contacts lista completa de contactos
     * @param id identificador buscado
     * @return índice dentro de la lista, o -1 si no se encuentra
     */
    public static int indexOfId(List<Contact>contacts, int id) {
        if(contacts == null) return -1;
        for(int i = 0; i < contacts.size(); i++) {
            if(contacts.get(i).getId() == id) return i;
        }
        return -1;
    }

    /**
     * Pide al usuario que seleccione un ID existente.
     *
     * <p>Opcionalmente muestra la tabla antes de pedir el ID.
     * Permite cancelar introduciendo 0.</p>
     *
     * @param contacts lista de contactos disponible
     * @param showTable si es {@code true}, imprime la tabla antes de preguntar
     * @return el ID válido elegido por el usuario, -1 si se cancela, o {@code null} si la lista está vacía
     */
    public static Integer promptExistingId(List<Contact> contacts, boolean showTable) {
        if (contacts == null || contacts.isEmpty()) {
            return null;
        }        
        if (showTable) {
            printContactsTable(contacts);
        }        
        while (true) {
            String idStr = readLine("%nIndique el Id (0 para cancelar): ");
            if(idStr.equals("0")) {
                printf("%nOperación cancelada.%n");
                return -1;
            }
            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException ex) {
                printf("ID no válido. Sea un número entero: %n");
                continue;
            }
            
            int index = indexOfId(contacts, id);
            if (index == -1) {
                printf("No existe contacto con ese ID: %d%n", id);
                continue;
            }
            return id; 
        }        
    }
    
    /**
     * Muestra un submenú para elegir un criterio de búsqueda.
     *
     * <p>Devuelve un número entre 1 y 4 según el campo elegido,
     * o 0 si el usuario cancela.</p>
     *
     * @return opción seleccionada por el usuario (0-4)
     */

    public static int promptCrite(){
        printf("1 - ID%n");
        printf("2 - Nombre%n");
        printf("3 - Teléfono%n");
        printf("4 - Email%n");
        printf("0 - Cancelar%n");
        
        while(true) {
                    String input = readLine("Seleccione una opción (0-4)%n").trim();
                    
                    if (input.equals("0")) { printf("Operación cancelada.%n"); return 0; }                                      
                   
                    try { int option = Integer.parseInt(input);
                         if (option >=1 && option <=4)
                            { return option; }
                         else { printf("Opción incorrecta: 0 - 4%n"); }
                    } catch (NumberFormatException ex) {
                        printf("Ha de indicar un número válido.%n");
                       }                  
        }
    }
    
    /**
     * Devuelve una nueva lista con los contactos cuyo campo coincide
     * (parcialmente, sin distinción de mayúsculas/minúsculas)
     * con el valor indicado.
     *
     * <p>El campo a evaluar se obtiene pasando una función
     * (por ejemplo, {@code Contact::getName}).</p>
     *
     * @param contacts lista de contactos a filtrar
     * @param value texto a buscar (subcadena)
     * @param getter función que extrae el campo de cada contacto
     * @return lista de contactos que hacen "match"
     */
    public static List<Contact> lambda01(List<Contact> contacts, String value, Function<Contact, String>getter) {
        List<Contact> result = new ArrayList<>();
        if(contacts == null || contacts.isEmpty()) return result;
        
        String needle = (value == null ? "" : value).toLowerCase();
        
        for (Contact c : contacts) {
            String field = getter.apply(c);
            String hay = (field == null ? "" : field).toLowerCase();
            if(hay.contains(needle)) {
                result.add(c);
            }
        }
        return result;
    }
    
    /**
     * Evita imprimir null en las columnas de la tabla.
     *
     * @param s cadena potencialmente nula
     * @return cadena segura (vacía si era null)
     */        
    private static String safe (String s) {
        return s == null ? "" : s.trim();
    }
    
}
