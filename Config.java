/*
 * ============================================================
 * Project:   Agenda de Contactos (Console CSV)
 * File:      Config.java
 * Package:   agend
 * Author:    Ángel Plata Benítez
 * Created:   2025-10-27
 * Version:   1.0
 * 
 * Description:
 *     Clase de configuración global del proyecto.
 *     Define las rutas, separadores y formato de fecha
 *     utilizados por toda la aplicación.
 *
 * License:
 *     This code is released under the MIT License.
 * ============================================================
 */
package agend;

/**
 * Clase de configuración de la Agenda.
 * 
 * <p>Contiene las constantes utilizadas de forma global
 * por la aplicación, como la ruta del archivo CSV,
 * el separador de campos y el formato de fecha.</p>
 */
public class Config {
     /** Ruta donde se almacenarán los datos de la agenda. */
    public static final String DATA_FILE = "data/agend.csv";
      /** Separador utilizado en el archivo CSV. */
    public static final String SEP =";";
     /** Patrón de formato de fecha estándar (ISO simple). */
    public static final String DATE_PATTERN =  "yyyy-MM-dd HH:mm";
}
