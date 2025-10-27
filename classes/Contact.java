/*
 * ============================================================
 * Project:   Agenda de Contactos (Console CSV)
 * File:      Contact.java
 * Package:   agend.classes
 * Author:    Ángel Plata Benítez
 * Created:   2025-10-27
 * Version:   1.0
 * 
 * Description:
 *     Clase que representa un contacto dentro de la agenda.
 *     Contiene los campos básicos de identificación y
 *     métodos para mostrar y exportar su información.
 *
 * License:
 *     This code is released under the MIT License.
 * ============================================================
 */
package agend.classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static agend.helpers.ConHelp.printf;
import agend.Config;


/**
 * Clase que define la estructura y comportamiento de un contacto.
 * 
 * <p>Incluye identificador, nombre, teléfono, correo electrónico y
 * fecha de creación. Ofrece métodos para mostrar la información
 * en consola y exportarla al formato CSV utilizado por el sistema.</p>
 */
public class Contact {
    
    // Campos / Atributos.
    /** Identificador único del contacto. */
    private int id;
    /** Nombre completo del contacto. */
    private String name;
    /** Número de teléfono asociado. */
    private String phone;
    /** Dirección de correo electrónico. */
    private String email;
    /** Fecha y hora de creación del contacto. */
    private String createdAt;
    
     /**
     * Constructor principal.
     *
     * @param id identificador único
     * @param name nombre del contacto
     * @param phone teléfono del contacto
     * @param email correo electrónico
     * @param createdAt fecha de creación (en formato definido por {@link Config#DATE_PATTERN})
     */
    public Contact(int id, String name, String phone, String email, String createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
    }
    
    /**
     * Constructor auxiliar para nuevas inserciones.
     * <p>Asigna automáticamente la fecha de creación actual.</p>
     *
     * @param id identificador único
     * @param name nombre del contacto
     * @param phone teléfono del contacto
     * @param email correo electrónico
     */
    public Contact(int id, String name, String phone, String email) {
        this(id, name, phone, email,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Config.DATE_PATTERN)));
    }
    
    /**
     * Muestra la información del contacto en consola.
     */
    public void showInfo() {
        printf("[%d] - %s%n", id, name);
        printf("Teléfono: %s%n", phone);
        printf("Email: %s%n", email);
        printf("Fecha: %s%n", createdAt);        
    }
    
    // Getters de acceso a campos privados.
    
     /** @return identificador del contacto */
    public int getId() { return id; }
    /** @return nombre del contacto */
    public String getName() { return name; }
    /** @return teléfono del contacto */
    public String getPhone() { return phone; }
    /** @return email del contacto */
    public String getEmail() { return email; }
    /** @return fecha de creación del contacto */
    public String getCreatedAt() { return createdAt; }
    
    // Setters para la modificación de campos.
    
    /** @param name nuevo nombre del contacto */
    public void setName(String name) {this.name = name;}
    /** @param phone nuevo teléfono del contacto */
    public void setPhone(String phone) {this.phone = phone;}
    /** @param email nuevo email del contacto */
    public void setEmail(String email) {this.email = email;}
    
     /**
     * Devuelve una representación del contacto en formato CSV.
     *
     * @return línea CSV con los campos del contacto
     */
    public String toCsv() {
        return String.join(Config.SEP, 
                String.valueOf (id), name, phone, email, createdAt);
    }
}
