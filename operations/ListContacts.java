/*
 * ============================================================
 * Project:   Agenda de Contactos (Console CSV)
 * File:      ListContacts.java
 * Package:   agend.operations
 * Author:    Ángel Plata Benítez
 * Created:   2025-10-27
 * Version:   1.0
 * 
 * Description:
 *     Muestra en consola todos los contactos almacenados
 *     en el archivo CSV. Utiliza TableHelp para el formato tabular.
 *
 * License:
 *     This code is released under the MIT License.
 * ============================================================
 */
package agend.operations;

import static agend.helpers.ConHelp.*;

import agend.classes.Contact;
import agend.Config;
import agend.helpers.TableHelp;
import agend.repository.ContactRepository;

import java.util.List;

/**
 * Clase responsable de listar los contactos almacenados.
 * 
 * <p>Lee el archivo CSV mediante {@link ContactRepository}
 * y muestra los registros en formato tabular a través de
 * {@link TableHelp#printContactsTable(List)}.</p>
 */

public class ListContacts {
    
    /** Repositorio encargado de la lectura de contactos. */
    private final ContactRepository repo;
    
     /**
     * Constructor por defecto.
     * Usa la ruta CSV definida en {@link Config#DATA_FILE}.
     */
    public ListContacts() {
        this.repo = new ContactRepository(Config.DATA_FILE);
    } 
    
    
    /**
     * Ejecuta la operación de listado de contactos.
     *
     * <p>Si no hay contactos, el método auxiliar mostrará
     * un mensaje indicando que no existen registros.</p>
     */
    public void run() {
        
        printf("%n=========== LISTA DE CONTACTOS ===========%n");

        List<Contact> contacts = repo.loadAll();
        TableHelp.printContactsTable(contacts);        
    }
    
}
