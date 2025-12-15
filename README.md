🛡️ Sistema de Autenticación y Control de Acceso (RBAC)
Este proyecto es una solución robusta de gestión de identidades desarrollada en Java Web, implementando un modelo de Control de Acceso Basado en Roles (RBAC) y siguiendo el patrón de diseño MVC.




🚀 Requerimientos Funcionales

Registro de Usuarios: Permite el registro de nuevos usuarios con validación de correo único.




Seguridad de Datos: Las contraseñas se almacenan encriptadas en la base de datos utilizando algoritmos SHA-256 o MD5.



Autenticación Segura: Sistema de inicio de sesión con verificación contra base de datos y manejo de HttpSession.



Control de Roles: Implementación de roles (Administrador, Coordinador, Docente) que determinan el acceso a secciones específicas del sistema.




Protección de Rutas: Uso de Filtros (Java Filter) para impedir el acceso no autorizado a páginas protegidas.


Gestión de Sesiones: Funcionalidad de cierre de sesión con invalidación de sesión y redirección segura.



Auditoría: Registro de accesos incluyendo fecha, hora y dirección IP del usuario.

🛠️ Stack Tecnológico

Lenguaje: Java Web (Servlets y JSP).




Gestor de Proyectos: Apache Maven.




Servidor de Aplicaciones: Apache Tomcat 9.0.



Base de Datos: MySQL con modelo relacional muchos a muchos.



Interfaz de Usuario: Diseño responsivo con Bootstrap 5.

📊 Arquitectura de Base de Datos
El sistema utiliza tres tablas principales (usuarios, roles y usuariosRoles) para permitir una relación flexible de muchos a muchos.

📁 Estructura del Código

dao/: Implementación del patrón Data Access Object para consultas SQL seguras.


filtros/: Capa de seguridad para el control de acceso y protección de vistas.


servlets/: Controladores encargados de la lógica de navegación y autenticación.



modelos/: Entidades que representan los datos del sistema bajo buenas prácticas de POO.
