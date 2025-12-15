# 🛡️ Sistema de Autenticación y Control de Acceso (RBAC)

Este proyecto es una **solución robusta de gestión de identidades** desarrollada en **Java Web**, que implementa un **Modelo de Control de Acceso Basado en Roles (RBAC)** y sigue el **patrón de diseño MVC (Modelo–Vista–Controlador)**.
Su objetivo es garantizar la **seguridad, autenticación y autorización** de los usuarios dentro del sistema.


## 🚀 Requerimientos Funcionales

* **Registro de Usuarios**
  Permite el registro de nuevos usuarios con validación de correo electrónico único.

* **Seguridad de Datos**
  Las contraseñas se almacenan de forma encriptada en la base de datos utilizando algoritmos de hash como **SHA-256** o **MD5**.

* **Autenticación Segura**
  Sistema de inicio de sesión con validación contra la base de datos y manejo de sesiones mediante `HttpSession`.

* **Control de Roles (RBAC)**
  Implementación de roles:

  * Administrador
  * Coordinador
  * Docente
    Cada rol define el acceso a secciones específicas del sistema.

* **Protección de Rutas**
  Uso de **Filtros (Java Filter)** para impedir el acceso no autorizado a páginas protegidas.

* **Gestión de Sesiones**
  Funcionalidad de cierre de sesión con invalidación de sesión y redirección segura.

* **Auditoría de Accesos**
  Registro de accesos de los usuarios, incluyendo:

  * Fecha
  * Hora
  * Dirección IP

---

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java Web (Servlets y JSP)
* **Gestor de Dependencias:** Apache Maven
* **Servidor de Aplicaciones:** Apache Tomcat 9.0
* **Base de Datos:** MySQL (modelo relacional muchos a muchos)
* **Interfaz de Usuario:** Bootstrap 5 (diseño responsivo)

---

## 📊 Arquitectura de Base de Datos

El sistema utiliza un **modelo relacional flexible** basado en tres tablas principales:

* `usuarios`
* `roles`
* `usuarios_roles`

Este diseño permite una **relación muchos a muchos** entre usuarios y roles, facilitando la escalabilidad y administración de permisos.

---

## 📁 Estructura del Código

```plaintext
dao/        → Implementación del patrón DAO para consultas SQL seguras  
filtros/    → Capa de seguridad para el control de acceso y protección de vistas  
servlets/   → Controladores que manejan la lógica de autenticación y navegación  
modelos/    → Entidades del sistema aplicando buenas prácticas de POO  
