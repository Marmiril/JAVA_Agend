
/*
 * ============================================================
 * Project:   Agenda de Contactos (Console CSV)
 * File:      FileHelp.java
 * Package:   agend.helpers
 * Author:    Ángel Plata Benítez
 * Created:   2025-10-27
 * Version:   1.0
 * 
 * Description:
 *     Clase auxiliar para gestión de archivos y directorios.
 *     Verifica la existencia de carpetas o archivos y los crea
 *     automáticamente si no existen.
 *
 * License:
 *     This code is released under the MIT License.
 * ============================================================
 */

package agend.helpers;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
/**
 * Clase de utilidades para operaciones de archivos.
 * 
 * <p>Permite comprobar la existencia de un directorio o archivo
 * y crearlo en caso de que no exista. Utiliza {@link java.nio.file.Files}
 * para operaciones seguras de E/S.</p>
 */
public class FileHelp {
    
    /**
     * Crea una carpeta si no existe.
     *
     * @param dir ruta del directorio a comprobar o crear
     * @throws UncheckedIOException si ocurre un error de E/S
     */
    public static void enDirExs(Path dir) {
        try {
            if (dir != null && !Files.exists(dir)) {
                Files.createDirectory(dir);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException("Error al crear la carpeta: " + dir, ex);
        }
    }
    
    /**
     * Crea un archivo si no existe.
     *
     * <p>También crea el directorio contenedor en caso de ser necesario.</p>
     *
     * @param file ruta del archivo a comprobar o crear
     * @throws UncheckedIOException si ocurre un error de E/S
     */
    public static void enFilExs(Path file) {
        try {
            enDirExs(file.getParent());
            if(!Files.exists(file)) {
                Files.createFile(file);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException("Error creando el archivo: " + file, ex);
        }
    }
    
}
