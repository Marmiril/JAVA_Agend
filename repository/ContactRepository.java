/*
 * ============================================================
 * Project:   Agenda de Contactos (Console CSV)
 * File:      ContactRepository.java
 * Package:   agend.repository
 * Author:    Ángel Plata Benítez
 * Created:   2025-10-27
 * Version:   1.0
 * 
 * Description:
 *     Repositorio de acceso a datos para la Agenda.
 *     Se encarga de cargar y guardar contactos en un archivo CSV.
 *
 *     Formato de cada fila CSV:
 *         id;name;phone;email;createdAt
 *
 *     Notas:
 *     - createdAt se guarda ya formateado como String.
 *     - Si falta createdAt al leer, se genera la fecha actual.
 *
 * License:
 *     This code is released under the MIT License.
 * ============================================================
 */
package agend.repository;

import agend.Config;
import agend.classes.Contact;
import static agend.helpers.ConHelp.printf;
import agend.helpers.FileHelp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
/**
 * Repositorio de contactos.
 *
 * <p>Responsabilidades principales:
 * <ul>
 *     <li>Leer todos los contactos desde el CSV</li>
 *     <li>Escribir todos los contactos en el CSV</li>
 *     <li>Normalizar y sanear datos para evitar corrupción en el archivo</li>
 *     <li>Ofrecer utilidades de comprobación (duplicados, etc.)</li>
 * </ul>
 *
 * <p>El archivo CSV se guarda en la ruta indicada por {@link Config#DATA_FILE},
 * con el separador definido en {@link Config#SEP}.</p>
 */
public class ContactRepository {
    /** Ruta al archivo CSV físico. */
    private Path csvFile;
    
    /** Separador de columnas usado en el CSV. */
    private final String SEP = Config.SEP;
    
    /** Formato de fecha/hora usado para createdAt. */
    private final DateTimeFormatter FMT = DateTimeFormatter.ofPattern(Config.DATE_PATTERN);
    
     /**
     * Crea un repositorio usando la ruta por defecto.
     * Ruta actual: data/agend.csv
     */
    public ContactRepository() {
        this.csvFile = Path.of("data", "agend.csv");
    }
    
    
     /**
     * Crea un repositorio con una ruta CSV personalizada.
     *
     * @param csvPath ruta absoluta o relativa al archivo CSV
     */
    public ContactRepository(String csvPath) {
        this.csvFile = Path.of(csvPath);
    }
    
    
    /* ====================== API PÚBLICA ====================== */
     /**
     * Carga todos los contactos desde el archivo CSV.
     *
     * <p>Si el archivo no existe, devuelve una lista vacía sin error.</p>
     *
     * @return lista de contactos leídos (posiblemente vacía)
     */
    public List<Contact>loadAll() {
        List<Contact> result = new ArrayList<>();
        if(!Files.exists(csvFile)) {
            return result;
        }
        
        try (BufferedReader br = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
            String line;
            int nLine = 0;
            
            while((line = br.readLine()) != null) {
                nLine++;
                if(line.isBlank()) continue;
                
                Optional<Contact> maybe = parseLine(line);
                if (maybe.isPresent()) {            
                    result.add(maybe.get());
                } else {
                    printf("Línea no válida: %d%n", nLine);
                }
            }
        } catch (IOException ex) {
            printf("Error leyendo el archivo CSV: %s%n", ex.getMessage());
        }
        return result;
    }
    
     /**
     * Intenta convertir una línea CSV en un objeto {@link Contact}.
     *
     * @param line línea completa del CSV
     * @return Optional con el contacto parseado o vacío si hay error
     */
    private Optional<Contact> parseLine(String line) {
        String[] parts = line.split(Pattern.quote(SEP), -1);
        if(parts.length != 5) return Optional.empty();
        
        try{
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String phone = parts [2].trim();
            String email = parts[3].trim();
            String createdAt = parts[4].isBlank()
                    ? LocalDateTime.now().format(FMT)
                    : parts[4].trim();

            return Optional.of(new Contact(id, name, phone, email, createdAt));
        } catch (NumberFormatException e) {
            printf("Error: ID no válido en línea %s%n", line);
        } catch (Exception e) {
            printf("Error al parsear línea CSV -> %s%n", line);
        }
        return Optional.empty();
    }
    
     /**
     * Guarda en disco la lista completa de contactos,
     * sobrescribiendo el archivo CSV.
     *
     * <p>Si la carpeta o el archivo no existen, se crean.</p>
     *
     * @param contacts lista completa de contactos a guardar
     */
    public void saveAll(List<Contact>contacts) {
        try {
            FileHelp.enFilExs(csvFile);
            try (BufferedWriter bw = Files.newBufferedWriter(csvFile, StandardCharsets.UTF_8)) {
                for (Contact c : contacts) {
                    bw.write(buildCsvLine(c));
                    bw.newLine();
                }
            }
        } catch (IOException ex) {
        printf("Error escribiendo CSV: %s%n", ex.getMessage());
        }
    }
    
     /**
     * Construye la línea CSV correspondiente a un contacto.
     *
     * @param c contacto origen
     * @return línea formateada lista para escritura en archivo
     */
    private String buildCsvLine (Contact c) {
        return String.join(SEP,
            String.valueOf(c.getId()),
            sanitize(c.getName()),
            sanitize(c.getPhone()),
            sanitize(c.getEmail()),
            sanitize(c.getCreatedAt())
        );        
    }    
    
     /**
     * Limpia una cadena de texto para evitar que contenga el separador CSV.
     *
     * @param s cadena original (puede ser null)
     * @return cadena saneada (sin separador), o "" si era null
     */
    private String sanitize(String s) {
        if (s == null) return "";
        return s.replace(SEP, " ");
    }        
    
     /**
     * Comprueba si un valor ya existe en la lista de contactos,
     * ignorando mayúsculas/minúsculas y espacios.
     *
     * <p>Sirve para validar duplicados (por ejemplo, email repetido o
     * teléfono repetido). Permite excluir un índice concreto,
     * típico caso: estás modificando un contacto y no quieres
     * que se compare consigo mismo.</p>
     *
     * @param contacts   lista de contactos actual
     * @param value      valor a comprobar (por ejemplo, el email nuevo)
     * @param keyExtractor función que extrae el campo relevante de cada contacto
     *                     (por ejemplo, {@code Contact::getEmail})
     * @param skipIndex  índice que se debe ignorar en la comprobación,
     *                   normalmente el contacto que estamos editando
     * @return {@code true} si ya existe otro contacto con ese mismo valor
     */
    
    public static boolean checkEx02(
            List<Contact> contacts,
            String value,
            Function<Contact, String> keyExtractor,
            int skipIndex) {
        String needle = normalize(value);
        
        for (int i = 0; i < contacts.size(); i++) {
            
            // Saltar el contacto que se está editando.
            if(i == skipIndex) {
                continue;
            }
                Contact c = contacts.get(i);
                String current = normalize(keyExtractor.apply(c));
                
                if(current.equals(needle)) {
                    return true;
                }
          }
        return false;
}
    
     /**
     * Normaliza una cadena para comparación:
     * trim + toLowerCase(Locale.ROOT).
     *
     * @param s cadena original (puede ser null)
     * @return cadena normalizada lista para comparar
     */
    public static String normalize(String s) {
        return(s == null) ? "" : s.trim().toLowerCase(Locale.ROOT);
    }
}
