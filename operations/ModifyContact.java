/*
 * ============================================================
 * Project:   Agenda de Contactos (Console CSV)
 * File:      ModifyContact.java
 * Package:   agend.operations
 * Author:    Ángel Plata Benítez
 * Created:   2025-10-27
 * Version:   1.0
 * 
 * Description:
 *     Permite modificar un contacto existente.
 *     Flujo:
 *       1. Carga todos los contactos y los muestra en tabla.
 *       2. Solicita al usuario un ID válido.
 *       3. Muestra el contacto seleccionado.
 *       4. Pide qué campo quiere modificar (nombre/teléfono/email).
 *       5. Valida el nuevo valor (formato y duplicados).
 *       6. Solicita confirmación final.
 *       7. Sobrescribe el CSV con los cambios.
 *
 * License:
 *     This code is released under the MIT License.
 * ============================================================
 */
package agend.operations;

import agend.Config;
import agend.classes.Contact;
import static agend.helpers.ConHelp.confirm;
import static agend.helpers.ConHelp.printf;
import static agend.helpers.ConHelp.readLine;
import static agend.helpers.TableHelp.printContactsTable;
import static agend.helpers.TableHelp.promptExistingId;

import static agend.operations.CreateContact.*;
import agend.repository.ContactRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase responsable de modificar un contacto existente.
 *
 * <p>Se apoya en:
 * <ul>
 *   <li>{@link ContactRepository} para leer/guardar los datos.</li>
 *   <li>{@link agend.helpers.TableHelp} para mostrar la tabla y pedir IDs válidos.</li>
 *   <li>Los validadores de {@link CreateContact} para email/teléfono.</li>
 * </ul>
 *
 * <p>Las modificaciones se validan para evitar duplicados (por ejemplo,
 * dos contactos con el mismo email) y para forzar formatos correctos.</p>
 */
public class ModifyContact {
    
    /** Repositorio de persistencia en CSV. */
    private final ContactRepository repo;
    
    
     /**
     * Constructor por defecto.
     * Usa el archivo CSV configurado en {@link Config#DATA_FILE}.
     */
    public ModifyContact() {
        this.repo = new ContactRepository(Config.DATA_FILE);
    }
    
     /**
     * Índice en la lista {@code contacts} del contacto actualmente seleccionado
     * para edición. Se establece tras elegir el ID.
     */
    public int targetIndex;
    
    
     /**
     * Campos que se pueden modificar en un contacto.
     * Cada enum tiene una etiqueta legible para mostrarla al usuario.
     */
    private enum Field {
    NAME("nombre"),
    PHONE("teléfono"),
    EMAIL("email");
    
    private final String label;
    Field(String label) { this.label = label; }
    public String label() { return label; }
    }
    
     /**
     * Lista de contactos cargada de disco al iniciar la operación.
     * Se mantiene en memoria durante toda la edición.
     */
    List<Contact>contacts;
    
      /**
     * Ejecuta el flujo interactivo de modificación de contacto.
     *
     * <p>Pasos:
     * <ol>
     *   <li>Cargar todos los contactos actuales.</li>
     *   <li>Mostrar la tabla completa.</li>
     *   <li>Pedir al usuario un ID existente (o cancelar).</li>
     *   <li>Mostrar el contacto elegido.</li>
     *   <li>Pedir qué campo se quiere cambiar.</li>
     *   <li>Pedir el nuevo valor, validarlo y aplicarlo.</li>
     *   <li>Confirmar y escribir en CSV.</li>
     * </ol>
     * </p>
     */
    public void run() {
        printf("=========== MODIFICAR CONTACTO ===========%n");
        
        // 1 - Carga de todos los contactos.
        contacts = repo.loadAll();
        List<Contact>result = new ArrayList<>(); 
        
         // 2 - Selección del contacto por ID (se muestra tabla antes de pedir ID).
        // promptExistingId() devuelve:
        //   -1 si el usuario cancela.
        //    id válido en caso contrario.
        int index =promptExistingId(contacts, true);
        if (index == -1) {return;}
        
        // 3 - Localizar el contacto con ese ID.
        Contact choos = null;        
        for(Contact c : contacts) { if (c.getId() == index) { choos = c; } }
        targetIndex = contacts.indexOf(choos);
        
        // Se guarda el índice real dentro de la lista (lo usaremos al validar duplicados).
        result.add(choos);      
        printContactsTable(result);
        
        // 4 - Preguntar qué campo quiere modificar.
        while (true) {
            printf("Indique qué desea modificar: %n");
            printf("1 - Nombre%n");
            printf("2 - Teléfono%n");
            printf("3 - Email%n");
            printf("0 - Cancelar%n"); 
            
            String option =readLine(">");
            
            if(option.equals("0")) {return;}
                      
            Field choosField = null;
            switch (option) {
             case "1": choosField = Field.NAME; break;
             case "2": choosField = Field.PHONE; break;
             case "3": choosField = Field.EMAIL; break;
             default:
                 printf ("Opción no válida.%n");
                 continue;                     
          }        
            
         // 5 - Pedir el nuevo valor para el campo elegido (con validación).
         String newVal = modifyData(choosField);         
       
         // 6 - Aplicar el cambio al objeto en memoria.
         switch (choosField) { 
             case NAME -> { if (newVal !=  null) {choos.setName(newVal); } else { return; }}
             case PHONE -> { if (newVal != null) { choos.setPhone(newVal); } else { return; }}
             case EMAIL -> { if ( newVal != null) { choos.setEmail(newVal); } else { return; }}            
         }
         
         // 7 - Mostrar el contacto modificado para confirmación visual.
         printContactsTable(result);
         
         // 8 - Confirmación final antes de persistir.
         boolean conf = confirm( "¿Confirma los cambios hechos?");
         if (!conf) { printf("No se han guardado los cambios realizados."); return; }
         
         // 9 - Guardar toda la lista (sobrescribe CSV).
         repo.saveAll(contacts);
         printf("Contacto actualizado.%n");
         
         break;
        }
    }
    
     /**
     * Solicita al usuario un nuevo valor para el campo indicado,
     * valida ese valor y comprueba duplicados si procede.
     *
     * <p>Reglas:
     * <ul>
     *   <li>El usuario puede cancelar introduciendo "0".</li>
     *   <li>NAME: no puede existir otro contacto con el mismo nombre.</li>
     *   <li>PHONE: debe ser numérico 7-15 dígitos y no duplicado.</li>
     *   <li>EMAIL: debe tener forma básica usuario@dominio.tld y no duplicado.</li>
     * </ul>
     * </p>
     *
     * @param f campo a modificar (NAME, PHONE o EMAIL)
     * @return el nuevo valor validado, o {@code null} si el usuario cancela
     */
    public String modifyData(Field f) {
        String nuevo;
        
        while (true) {
            printf("Indique el nuevo %s: ", f.label());
            nuevo = readLine("");
          
            // Validaciones dependiendo del campo.
            switch(f) {
                case NAME -> { if(nuevo.equals("0"))  { printf("Operación cancelada%n"); return null;}
                if(repo.checkEx02(contacts, nuevo, c -> c.getName(), targetIndex)) { printf("Ya existe un contacto con ese nombre.%n"); continue; } }
                case PHONE ->
                {
                    if(nuevo.equals("0")) { printf("Operación cancelada.%n"); return null; }     
                      
                    if (!isValidPhone(nuevo)) { printf("El teléfono debe contener solo dígitos (7 a 15). Inténtelo de nuevo.%n");
                        continue;
                    }                  
                    if(repo.checkEx02(contacts, nuevo, c ->c.getPhone(), targetIndex)) { printf("Ya existe un contacto con ese teléfono....%n"); continue; }
                }                
                case EMAIL -> {
                    if(nuevo.equals("0")) { printf("Operación cancelada.%n"); return null; }                   
                    
                    if(!isValidEmail(nuevo)) { printf("Formato incorrecto. Ejemplo: usuario@dominio.com.%n");
                       continue;
                    }                    
                    if(repo.checkEx02(contacts, nuevo, c->c.getEmail(), targetIndex)) { printf("Ya existe un contacto con ese email.%n"); continue; }
                }                
            }            
            break;
        }                     
        return nuevo;
    }
    
}
    
