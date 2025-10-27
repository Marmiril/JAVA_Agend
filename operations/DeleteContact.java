/*
 * ============================================================
 * Project:   Agenda de Contactos (Console CSV)
 * File:      DeleteContact.java
 * Package:   agend.operations
 * Author:    Ángel Plata Benítez
 * Created:   2025-10-27
 * Version:   1.0
 * 
 * Description:
 *     Operación de borrado de un contacto.
 *     - Muestra los contactos existentes.
 *     - Solicita un ID válido.
 *     - Pide confirmación al usuario.
 *     - Elimina el contacto del listado y guarda el CSV.
 *
 * License:
 *     This code is released under the MIT License.
 * ============================================================
 */
package agend.operations;

import agend.classes.Contact;
import static agend.helpers.ConHelp.confirm;
import static agend.helpers.ConHelp.printf;
import agend.helpers.TableHelp;
import static agend.helpers.TableHelp.indexOfId;
import static agend.helpers.TableHelp.promptExistingId;
import agend.repository.ContactRepository;
import java.util.List;
/**
 * Clase responsable de eliminar un contacto por su ID.
 *
 * <p>Flujo general:
 * <ol>
 *   <li>Imprime los contactos en formato tabla.</li>
 *   <li>Pide al usuario el ID de un contacto existente.</li>
 *   <li>Muestra los datos del contacto objetivo.</li>
 *   <li>Solicita confirmación explícita.</li>
 *   <li>Elimina y persiste los cambios en el CSV.</li>
 * </ol>
 * </p>
 */

public class DeleteContact {
    
    /** Repositorio para cargar y guardar contactos en CSV. */
    private final ContactRepository repo;
    
    /**
     * Constructor por defecto.
     * Usa un repositorio con la configuración estándar.
     */
    public DeleteContact() {
        this(new ContactRepository());
    }
    
    
    /**
     * Constructor con inyección de repositorio (útil para test).
     *
     * @param repo instancia de {@link ContactRepository} a utilizar.
     */
    public DeleteContact(ContactRepository repo) {
        this.repo = repo;
    }
    
     /**
     * Ejecuta el proceso interactivo de eliminación.
     *
     * <p>Si no hay contactos, o el usuario cancela,
     * la operación finaliza sin cambios.</p>
     */    
    public void run() {
        
        printf("=========== ELIMINAR CONTACTO ===========%n");
        
         // 1. Cargar datos actuales.
        List<Contact> contacts = repo.loadAll();
        
        // 2. Mostrar tabla al usuario.
        TableHelp.printContactsTable(contacts);
        
        // 3. Solicitar ID existente.
        //    promptExistingId(false) no imprime de nuevo la tabla.
        Integer id = promptExistingId(contacts, false);
        if(id == null) return; 
        
        // 4. Buscar índice del contacto por ID.
        int index = indexOfId(contacts, id);          
        if (index == -1) { printf("Error al buscar el contacto.%n"); return; }
        
        Contact target = contacts.get(index);
        
        // 5. Mostrar preview del contacto que se va a borrar.
        printf("%nVa a eliminar el contacto: %n");
        printf(" ID:%d%n", target.getId());
        printf(" Nombre: %s%n", target.getName());
        printf(" Teléfono: %s%n", target.getPhone());
        printf(" Email: %s%n", target.getEmail());
        
        
        // 6. Confirmar acción.
        if(!confirm("\n¿Seguro que desea eliminar este contacto?")) {
            printf("Operación cancelada...%n");
            return;           
        }
        
        // 7. Eliminar de la lista en memoria.
        contacts.remove(index);
        
        // 8. Guardar en CSV.
        repo.saveAll(contacts);
        
        printf("%nContacto eliminado correctamente.%n");
    } 
    

}
