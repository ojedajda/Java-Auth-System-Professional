# Java-Auth-System-Professional

# 🛡️ Sistema de Autenticación y Control de Acceso (RBAC)

Sistema robusto de gestión de usuarios y seguridad desarrollado en **Java Web**, siguiendo el patrón de diseño **MVC** y gestionado con **Maven**.

## 🚀 Características Técnicas
* [cite_start]**Seguridad:** Cifrado de contraseñas con algoritmos **SHA-256/MD5** para proteger los datos sensibles[cite: 4, 13, 48].
* [cite_start]**Control de Acceso por Roles (RBAC):** Implementación de roles jerárquicos (**Administrador, Coordinador, Docente**) que determinan los permisos y vistas dentro del sistema[cite: 5, 25, 29, 37].
* [cite_start]**Gestión de Sesiones:** Control seguro mediante `HttpSession` y validación de cierre de sesión[cite: 20, 33, 34].
* [cite_start]**Protección de Rutas:** Uso de **Filtros (Java Filters)** para impedir el acceso no autorizado a páginas protegidas[cite: 35, 36].
* [cite_start]**Auditoría:** Registro detallado de accesos incluyendo fecha, hora y dirección IP[cite: 38, 39].

## 🛠️ Stack Tecnológico
* [cite_start]**Backend:** Java EE (Servlets y JSP)[cite: 4, 41].
* [cite_start]**Servidor:** Apache Tomcat 9.0[cite: 43].
* [cite_start]**Base de Datos:** MySQL con diseño relacional muchos a muchos[cite: 6, 44].
* [cite_start]**Frontend:** Interfaces responsivas con **Bootstrap 5**[cite: 47].

## 📊 Arquitectura de Base de Datos
El sistema utiliza una estructura de tablas relacionales para gestionar usuarios, roles y auditorías de forma eficiente.

![Diagrama de la base de datos](Diagrama%20de%20la%20base%20de%20datos.png)

## 📁 Estructura del Proyecto
El proyecto está organizado siguiendo estándares profesionales:
* `dao/`: Lógica de acceso a datos (consultas SQL seguras).
* `servlets/`: Controladores de la aplicación.
* `filtros/`: Capa de seguridad y protección de rutas.
* `modelos/`: Representación de las entidades del sistema.
