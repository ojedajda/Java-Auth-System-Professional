<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Asignar Roles - Administrador</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        :root {
            --primary-color: #667eea;
            --secondary-color: #764ba2;
        }
        .sidebar {
            background: linear-gradient(135deg, var(--primary-color) 0%, var(--secondary-color) 100%);
            color: white;
            min-height: 100vh;
        }
        .sidebar .nav-link {
            color: white;
            padding: 12px 20px;
            margin: 5px 0;
            border-radius: 8px;
        }
        .sidebar .nav-link:hover {
            background: rgba(255,255,255,0.1);
        }
        .sidebar .nav-link.active {
            background: rgba(255,255,255,0.2);
        }
        .main-content {
            background-color: #f8f9fa;
        }
        .card-dashboard {
            border: none;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar Fijo -->
            <div class="col-md-3 col-lg-2 sidebar p-0">
                <div class="p-4">
                    <h4 class="text-center mb-4">
                        <i class="fas fa-shield-alt me-2"></i>Sistema Auth
                    </h4>
                    
                    <!-- Información del usuario -->
                    <div class="text-center mb-4 p-3 bg-white bg-opacity-10 rounded">
                        <i class="fas fa-user-circle fa-2x mb-2"></i>
                        <h6 class="mb-1">${usuario.nombre}</h6>
                        <small>${usuario.correo}</small>
                        <div class="mt-2">
                            <c:forEach var="rol" items="${usuario.roles}">
                                <span class="badge bg-light text-dark">${rol.rol}</span>
                            </c:forEach>
                        </div>
                    </div>

                    <!-- Menú de navegación -->
                    <ul class="nav flex-column">
                        <c:set var="tieneAdmin" value="false" />
                        <c:set var="tieneCoordinador" value="false" />
                        <c:set var="tieneDocente" value="false" />
                        
                        <c:forEach var="rol" items="${usuario.roles}">
                            <c:if test="${rol.rol == 'Administrador'}">
                                <c:set var="tieneAdmin" value="true" />
                            </c:if>
                            <c:if test="${rol.rol == 'Coordinador'}">
                                <c:set var="tieneCoordinador" value="true" />
                            </c:if>
                            <c:if test="${rol.rol == 'Docente'}">
                                <c:set var="tieneDocente" value="true" />
                            </c:if>
                        </c:forEach>
                        
                        <c:if test="${tieneAdmin}">
                            <li class="nav-item">
                                <a class="nav-link" href="../admin/dashboard.jsp">
                                    <i class="fas fa-crown me-2"></i>Panel Admin
                                </a>
                            </li>
                        </c:if>
                        <c:if test="${tieneCoordinador}">
                            <li class="nav-item">
                                <a class="nav-link" href="../coordinador/dashboard.jsp">
                                    <i class="fas fa-chart-line me-2"></i>Panel Coordinador
                                </a>
                            </li>
                        </c:if>
                        <c:if test="${tieneDocente}">
                            <li class="nav-item">
                                <a class="nav-link" href="../docente/perfil.jsp">
                                    <i class="fas fa-user me-2"></i>Mi Perfil
                                </a>
                            </li>
                        </c:if>
                        <li class="nav-item">
                            <a class="nav-link" href="../logout">
                                <i class="fas fa-sign-out-alt me-2"></i>Cerrar Sesión
                            </a>
                        </li>
                    </ul>
                </div>
            </div>

            <!-- Main Content -->
            <div class="col-md-9 col-lg-10 main-content">
                <div class="p-4">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h2 class="mb-0 text-warning">
                            <i class="fas fa-user-tag me-2"></i>Asignar Roles a Usuario
                        </h2>
                        <a href="gestionRoles" class="btn btn-secondary">
                            <i class="fas fa-arrow-left me-2"></i>Volver
                        </a>
                    </div>

                    <c:if test="${not empty usuarioEditar}">
                        <!-- Información del usuario -->
                        <div class="card card-dashboard mb-4">
                            <div class="card-header bg-info text-white">
                                <h5 class="card-title mb-0">
                                    <i class="fas fa-user me-2"></i>Información del Usuario
                                </h5>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <p><strong>ID:</strong> ${usuarioEditar.idUsuario}</p>
                                        <p><strong>Nombre:</strong> ${usuarioEditar.nombre}</p>
                                    </div>
                                    <div class="col-md-6">
                                        <p><strong>Correo:</strong> ${usuarioEditar.correo}</p>
                                        <p><strong>Fecha Registro:</strong> ${usuarioEditar.fechaRegistro}</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Formulario de roles -->
                        <div class="card card-dashboard">
                            <div class="card-header bg-warning text-dark">
                                <h5 class="card-title mb-0">
                                    <i class="fas fa-user-tag me-2"></i>Seleccionar Roles
                                </h5>
                            </div>
                            <div class="card-body">
                                <form action="gestionRoles" method="POST">
                                    <input type="hidden" name="idUsuario" value="${usuarioEditar.idUsuario}">
                                    
                                    <div class="mb-3">
                                        <label class="form-label fw-bold">Roles Disponibles:</label>
                                        <div class="row">
                                            <c:forEach var="rol" items="${todosRoles}">
                                                <div class="col-md-4 mb-2">
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="checkbox" 
                                                               name="roles" value="${rol.idRol}" 
                                                               id="rol${rol.idRol}"
                                                               <c:if test="${usuarioEditar.roles.stream().anyMatch(r -> r.idRol == rol.idRol).orElse(false)}">checked</c:if>>
                                                        <label class="form-check-label" for="rol${rol.idRol}">
                                                            <span class="badge bg-primary">${rol.rol}</span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>

                                    <div class="alert alert-info">
                                        <h6><i class="fas fa-info-circle me-2"></i>Descripción de Roles:</h6>
                                        <ul class="mb-0">
                                            <li><strong>Administrador:</strong> Acceso total al sistema, puede gestionar usuarios y roles.</li>
                                            <li><strong>Coordinador:</strong> Puede consultar reportes y listar usuarios (solo lectura).</li>
                                            <li><strong>Docente:</strong> Solo puede ver y modificar su información personal.</li>
                                        </ul>
                                    </div>

                                    <button type="submit" class="btn btn-success">
                                        <i class="fas fa-save me-2"></i>Guardar Roles
                                    </button>
                                    <a href="gestionRoles" class="btn btn-secondary">
                                        <i class="fas fa-times me-2"></i>Cancelar
                                    </a>
                                </form>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${empty usuarioEditar}">
                        <div class="alert alert-danger">
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            No se encontró el usuario especificado.
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>