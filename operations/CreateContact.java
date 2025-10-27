/*
 * ============================================================
 * Project:   Agenda de Contactos (Console CSV)
 * File:      CreateContact.java
 * Package:   agend.operations
 * Author:    Ángel Plata Benítez
 * Created:   2025-10-27
 * Version:   1.0
 * 
 * Description:
 *     Lógica de creación de un nuevo contacto.
 *     - Solicita al usuario nombre, teléfono y email.
 *     - Valida formato (teléfono numérico, email con "@").
 *     - Comprueba duplicados frente a la lista existente.
 *     - Calcula el siguiente ID disponible.
 *     - Inserta el contacto en memoria y lo guarda en CSV.
 *
 * License:
 *     This code is released under the MIT License.
 * ============================================================
 */

package agend.operations;

import agend.Config;
import agend.classes.Contact;
import static agend.helpers.ConHelp.printf;
import static agend.helpers.ConHelp.readLine;
import agend.repository.ContactRepository;
import java.util.List;
import java.util.function.Function;

/**
 * Operación de alta de contacto.
 *
 * <p>Esta clase implementa el flujo completo para dar de alta
 * un nuevo contacto en la agenda: entrada por consola,
 * validación, comprobación de duplicados y persistencia.</p>
 */
public class CreateContact {
    
    /** Repositorio de persistencia en CSV. */
    private final ContactRepository repo;
    
        /**
     * Constructor por defecto.
     * Usa la ruta CSV configurada en {@link Config#DATA_FILE}.
     */
    public CreateContact() {
        this.repo = new ContactRepository(Config.DATA_FILE);        
    }
    
        /**
     * Ejecuta el proceso interactivo de creación de un contacto.
     *
     * <p>Flujo:
     * <ol>
     *   <li>Carga todos los contactos existentes.</li>
     *   <li>Pide nombre, teléfono y email al usuario.</li>
     *   <li>Valida formatos y evita duplicados.</li>
     *   <li>Calcula el siguiente ID libre.</li>
     *   <li>Guarda el nuevo contacto en disco.</li>
     * </ol>
     * </p>
     */
    public void run() {
        printf("============ NUEVO CONTACTO ============%n");
        
        // Cargar lista actual.
        List<Contact> contacts = repo.loadAll();
        
        // Recoger datos básicos.
        // Nombre (obligatorio y no duplicado).
        String name;
        while (true) {
               name = readLine("Nombre: ");
               if (!checkEx(contacts, name, Contact::getName)) break;
               printf("Ya existe un contacto con ese nombre. Indique otro: %n");
        }
        // Teléfono (sólo dígitos, rango de longitud, no duplicado).
        String phone;
        while (true){
            phone = readLine("Teléfono: ");
            if (!isValidPhone(phone)) {
                printf("Formato de teléfono no válido. Debe tener sólo dígitos (7 -15).%n");
                continue;
            }            
            if (!checkEx(contacts, phone, Contact::getPhone)) break;
            printf("Ya existe un contacto con ese teléfono. Indique otro: %n");
        }
        
        String email;
        while (true) {
            email = readLine("Email: ");
            if (!isValidEmail(email)) {
                printf("Email no válido. Ejemplo: usuario@dominio.com.%n"); 
                continue;
            }            
            if (!checkEx(contacts, email, Contact::getEmail)) break;
            printf("Ya existe un contacto con ese email. Indique otro.%n");
        }
        
        // Calcular ID más alto.
        int nextId = contacts.isEmpty()
                ? 1
                : contacts.stream().mapToInt(Contact::getId).max().getAsInt() + 1;
        
        // Crear objeto y añadir a la lista.
        Contact newContact = new Contact(nextId, name, phone, email);
        contacts.add(newContact);
        
        // Guardado en CSV.
        repo.saveAll(contacts);
      
        printf("Contacto guardado correctamente.%n");
    }
    
    /**
     * Comprueba si un valor ya existe en la lista de contactos.
     *
     * <p>Se usa para evitar duplicados de nombre, teléfono o email.</p>
     *
     * @param contacts lista de contactos actual
     * @param value valor a comprobar
     * @param keyExtractor función que extrae el campo a comparar
     *                     (por ejemplo, {@code Contact::getEmail})
     * @return {@code true} si ya existe un contacto con ese mismo valor
     */
    private boolean checkEx(List<Contact> contacts, String value, Function<Contact, String>keyExtractor){
        var needle = ContactRepository.normalize(value);
        for (Contact c : contacts) {
            if (ContactRepository.normalize(keyExtractor.apply(c)).equals(needle)) return true;
        }
        return false;
    }

    
    /**
     * Valida un teléfono.
     * Debe ser numérico y tener entre 7 y 15 dígitos.
     *
     * @param phone teléfono introducido por el usuario
     * @return {@code true} si cumple el patrón
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^\\d{7,15}$");
    }
    
    /**
     * Valida un email.
     * Reglas mínimas: algo@algo.algo, sin espacios.
     *
     * @param email email introducido por el usuario
     * @return {@code true} si parece un correo válido
     */
   public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }
    
    
}
