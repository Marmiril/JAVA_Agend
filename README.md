Agenda de Contactos (Console CSV)
🇪🇸 Descripción

Aplicación de consola escrita en Java para la gestión básica de contactos.
Permite crear, listar, modificar, eliminar y buscar contactos guardados en un archivo CSV local.

El diseño se basa en una arquitectura modular con paquetes separados para clases, operaciones, ayudantes (helpers) y repositorio de persistencia.

Funcionalidades principales

* Crear contacto
Solicita nombre, teléfono y correo electrónico.
Valida formato, evita duplicados y asigna automáticamente el siguiente ID disponible.

* Listar contactos
Muestra todos los registros guardados en formato tabular.

* Modificar contacto
Permite editar nombre, teléfono o email.
Comprueba que el nuevo valor no duplique otro contacto existente.

* Eliminar contacto
Solicita confirmación antes de borrar el contacto seleccionado por ID.

* Buscar contacto
Admite búsqueda por ID, nombre, teléfono o email (búsqueda parcial o exacta según el campo).

Validaciones y control de datos:
- Teléfono: numérico, entre 7 y 15 dígitos.
- Email: debe contener un @ y un dominio válido.
- Duplicados: controlados por nombre, teléfono y correo.
- CSV: se crea automáticamente si no existe (data/agend.csv).

Estructura del proyecto
agend/
 ├── classes/
 │   └── Contact.java
 │
 ├── helpers/
 │   ├── ConHelp.java
 │   ├── FileHelp.java
 │   ├── TableHelp.java
 │
 ├── operations/
 │   ├── CreateContact.java
 │   ├── ListContacts.java
 │   ├── ModifyContact.java
 │   ├── DeleteContact.java
 │   └── SearchContact.java
 │
 ├── repository/
 │   └── ContactRepository.java
 │
 ├── Config.java
 └── Main.java

Ejecución:
Compilar y ejecutar desde consola o NetBeans:

javac -d bin $(find src -name "*.java")
java -cp bin agend.Main


El programa muestra un menú interactivo en consola:

================== AGENDA ==================
1 - Crear contacto.
2 - Listar contactos.
3 - Modificar contacto.
4 - Eliminar contacto.
5 - Buscar contacto.
0 - SALIR...

Requisitos:
- Java 17 o superior (probado en JDK 23)
- Permisos de escritura en el directorio data/

Licencia:
Este proyecto se distribuye bajo la licencia MIT.
Puedes usar, modificar y redistribuir el código libremente citando la autoría original.

Autor
Ángel Plata Benítez
2025 — Proyecto educativo de aprendizaje en Java.

🇬🇧 English Version
Description

Console-based Java application for basic contact management.
It allows creating, listing, modifying, deleting, and searching contacts stored in a local CSV file.

The system follows a modular architecture with separated packages for entities, operations, helpers, and repository management.

Main Features

* Create Contact
Prompts for name, phone, and email.
Validates format, prevents duplicates, and assigns an automatic incremental ID.

* List Contacts
Displays all records in a formatted table.

* Modify Contact
Lets the user edit name, phone, or email.
Validates new values and prevents duplicates.

* Delete Contact
Confirms deletion before removing a contact by ID.

* Search Contact
Supports search by ID, name, phone, or email (case-insensitive for text fields).

Data Validation:
- Phone: numeric, 7–15 digits.
- Email: must contain @ and a valid domain.
- Duplicate control: enforced by name, phone, and email.
- CSV file: automatically created at data/agend.csv if missing.

Project Structure:
- Same structure as shown in the Spanish section above.

How to Run:
- Compile and execute from console or NetBeans:
javac -d bin $(find src -name "*.java")
java -cp bin agend.Main

Requirements:
- Java 17 or higher (tested on JDK 23)
- Write permission for the data/ directory

License:
This project is released under the MIT License.
You may freely use, modify, and distribute it with attribution.

Author:
Ángel Plata Benítez
2025 — Educational Java project for learning purposes.
