<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Perfil - Sistema de Autenticación</title>
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
            transition: transform 0.3s;
        }
        .card-dashboard:hover {
            transform: translateY(-5px);
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
                                <a class="nav-link active" href="perfil.jsp">
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
                        <h2 class="mb-0 text-success">
                            <i class="fas fa-user me-2"></i>Mi Perfil
                        </h2>
                    </div>

                    <div class="alert alert-success">
                        <i class="fas fa-info-circle me-2"></i>
                        <strong>Funciones de Docente:</strong> Solo puede ver y modificar su información personal.
                    </div>

                    <!-- MENSAJES DE ÉXITO/ERROR AGREGADOS -->
                    <c:if test="${not empty exito}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <i class="fas fa-check-circle me-2"></i>${exito}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fas fa-exclamation-triangle me-2"></i>${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <div class="row">
                        <div class="col-md-8">
                            <div class="card card-dashboard">
                                <div class="card-header">
                                    <h5 class="card-title mb-0">
                                        <i class="fas fa-user-edit me-2"></i>Información Personal
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <form action="actualizarPerfil" method="POST">
                                        <div class="row mb-3">
                                            <div class="col-md-6">
                                                <label class="form-label">Nombre Completo</label>
                                                <input type="text" class="form-control" name="nombre" 
                                                       value="${usuario.nombre}" required>
                                            </div>
                                            <div class="col-md-6">
                                                <label class="form-label">Correo Electrónico</label>
                                                <input type="email" class="form-control" value="${usuario.correo}" 
                                                       readonly style="background-color: #f8f9fa;">
                                                <small class="text-muted">El correo no se puede modificar</small>
                                            </div>
                                        </div>
                                        
                                        <div class="row mb-3">
                                            <div class="col-md-6">
                                                <label class="form-label">Fecha de Registro</label>
                                                <input type="text" class="form-control" value="${usuario.fechaRegistro}" 
                                                       readonly style="background-color: #f8f9fa;">
                                            </div>
                                            <div class="col-md-6">
                                                <label class="form-label">Roles Asignados</label>
                                                <div>
                                                    <c:forEach var="rol" items="${usuario.roles}">
                                                        <span class="badge bg-success me-1">${rol.rol}</span>
                                                    </c:forEach>
                                                </div>
                                            </div>
                                        </div>

                                        <hr>
                                        <h6 class="mb-3">
                                            <i class="fas fa-key me-2"></i>Cambiar Contraseña
                                        </h6>
                                        <div class="mb-3">
                                            <label class="form-label">Nueva Contraseña</label>
                                            <input type="password" class="form-control" name="nuevaPassword" 
                                                   placeholder="Dejar en blanco para no cambiar">
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label">Confirmar Nueva Contraseña</label>
                                            <input type="password" class="form-control" name="confirmPassword" 
                                                   placeholder="Repetir nueva contraseña">
                                        </div>

                                        <button type="submit" class="btn btn-success">
                                            <i class="fas fa-save me-2"></i>Actualizar Información
                                        </button>
                                        <a href="perfil.jsp" class="btn btn-secondary">
                                            <i class="fas fa-times me-2"></i>Cancelar
                                        </a>
                                    </form>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-md-4">
                            <div class="card card-dashboard">
                                <div class="card-header">
                                    <h5 class="card-title mb-0">
                                        <i class="fas fa-info-circle me-2"></i>Mi Cuenta
                                    </h5>
                                </div>
                                <div class="card-body text-center">
                                    <i class="fas fa-user-circle fa-5x text-success mb-3"></i>
                                    <h5>${usuario.nombre}</h5>
                                    <p class="text-muted">${usuario.correo}</p>
                                    <div class="mt-3">
                                        <c:forEach var="rol" items="${usuario.roles}">
                                            <span class="badge bg-success">${rol.rol}</span>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>