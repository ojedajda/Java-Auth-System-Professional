<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Listar Usuarios - Coordinador</title>
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
                        <h2 class="mb-0 text-primary">
                            <i class="fas fa-list me-2"></i>Lista de Usuarios
                        </h2>
                        <span class="badge bg-primary">Coordinador</span>
                    </div>

                    <div class="alert alert-warning">
                        <i class="fas fa-eye me-2"></i>
                        <strong>Permisos de Coordinador:</strong> Puede consultar y listar usuarios, pero no modificarlos o eliminarlos.
                    </div>

                    <!-- Tabla de usuarios (solo lectura) -->
                    <div class="card card-dashboard">
                        <div class="card-header bg-primary text-white">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-users me-2"></i>Usuarios del Sistema
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-striped table-hover">
                                    <thead class="table-primary">
                                        <tr>
                                            <th>ID</th>
                                            <th>Nombre</th>
                                            <th>Correo Electrónico</th>
                                            <th>Fecha Registro</th>
                                            <th>Roles</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="usuario" items="${usuarios}">
                                            <tr>
                                                <td>${usuario.idUsuario}</td>
                                                <td>${usuario.nombre}</td>
                                                <td>${usuario.correo}</td>
                                                <td>${usuario.fechaRegistro}</td>
                                                <td>
                                                    <c:forEach var="rol" items="${usuario.roles}">
                                                        <span class="badge bg-success me-1">${rol.rol}</span>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            
                            <c:if test="${empty usuarios}">
                                <div class="text-center py-4">
                                    <i class="fas fa-users fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">No hay usuarios registrados</h5>
                                </div>
                            </c:if>
                        </div>
                    </div>

                    <!-- Información de solo lectura -->
                    <div class="alert alert-info mt-4">
                        <h6><i class="fas fa-info-circle me-2"></i>Modo Solo Lectura</h6>
                        <p class="mb-0">
                            Como coordinador, tienes acceso de solo lectura a la información de usuarios. 
                            No puedes modificar, eliminar o gestionar roles de los usuarios.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>