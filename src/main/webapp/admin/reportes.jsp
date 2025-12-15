<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reportes - Administrador</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
        .main-content {
            background-color: #f8f9fa;
        }
        .card-dashboard {
            border: none;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        .stat-card {
            border: none;
            border-radius: 10px;
            color: white;
            padding: 20px;
            margin-bottom: 20px;
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
                        <h2 class="mb-0 text-info">
                            <i class="fas fa-chart-bar me-2"></i>Reportes del Sistema
                        </h2>
                        <span class="badge bg-info">Administrador</span>
                    </div>

                    <!-- Estadísticas principales -->
                    <div class="row mb-4">
                        <div class="col-md-3">
                            <div class="stat-card bg-primary">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h3>${estadisticas.totalUsuarios}</h3>
                                        <p class="mb-0">Total Usuarios</p>
                                    </div>
                                    <i class="fas fa-users fa-2x opacity-75"></i>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stat-card bg-success">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h3>${estadisticas.usuariosActivos}</h3>
                                        <p class="mb-0">Usuarios Activos</p>
                                    </div>
                                    <i class="fas fa-user-check fa-2x opacity-75"></i>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stat-card bg-warning">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h3>
                                            <c:set var="totalRoles" value="0" />
                                            <c:forEach var="rol" items="${estadisticas.usuariosPorRol}">
                                                <c:set var="totalRoles" value="${totalRoles + rol.cantidad}" />
                                            </c:forEach>
                                            ${totalRoles}
                                        </h3>
                                        <p class="mb-0">Total Asignaciones</p>
                                    </div>
                                    <i class="fas fa-user-tag fa-2x opacity-75"></i>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stat-card bg-info">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h3>${estadisticas.usuariosPorRol.size()}</h3>
                                        <p class="mb-0">Roles Configurados</p>
                                    </div>
                                    <i class="fas fa-tags fa-2x opacity-75"></i>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Gráficos -->
                    <div class="row">
                        <!-- Gráfico de usuarios por rol -->
                        <div class="col-md-6 mb-4">
                            <div class="card card-dashboard">
                                <div class="card-header bg-primary text-white">
                                    <h5 class="card-title mb-0">
                                        <i class="fas fa-chart-pie me-2"></i>Usuarios por Rol
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <canvas id="chartRoles" width="400" height="300"></canvas>
                                </div>
                            </div>
                        </div>

                        <!-- Gráfico de accesos por día -->
                        <div class="col-md-6 mb-4">
                            <div class="card card-dashboard">
                                <div class="card-header bg-success text-white">
                                    <h5 class="card-title mb-0">
                                        <i class="fas fa-chart-line me-2"></i>Accesos por Día (Últimos 7 días)
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <canvas id="chartAccesos" width="400" height="300"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Tabla de estadísticas detalladas -->
                    <div class="card card-dashboard">
                        <div class="card-header bg-info text-white">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-table me-2"></i>Estadísticas Detalladas
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>Rol</th>
                                            <th>Usuarios Asignados</th>
                                            <th>Porcentaje</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="rol" items="${estadisticas.usuariosPorRol}">
                                            <tr>
                                                <td>
                                                    <span class="badge bg-primary">${rol.rol}</span>
                                                </td>
                                                <td>${rol.cantidad}</td>
                                                <td>
                                                    <fmt:formatNumber value="${(rol.cantidad / estadisticas.totalUsuarios) * 100}" 
                                                                      maxFractionDigits="1" />%
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Gráfico de usuarios por rol
        const ctxRoles = document.getElementById('chartRoles').getContext('2d');
        const chartRoles = new Chart(ctxRoles, {
            type: 'pie',
            data: {
                labels: [
                    <c:forEach var="rol" items="${estadisticas.usuariosPorRol}" varStatus="status">
                        '${rol.rol}'<c:if test="${!status.last}">, </c:if>
                    </c:forEach>
                ],
                datasets: [{
                    data: [
                        <c:forEach var="rol" items="${estadisticas.usuariosPorRol}" varStatus="status">
                            ${rol.cantidad}<c:if test="${!status.last}">, </c:if>
                        </c:forEach>
                    ],
                    backgroundColor: [
                        '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40'
                    ]
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });

        // Gráfico de accesos por día
        const ctxAccesos = document.getElementById('chartAccesos').getContext('2d');
        const chartAccesos = new Chart(ctxAccesos, {
            type: 'line',
            data: {
                labels: [
                    <c:forEach var="acceso" items="${estadisticas.accesosPorDia}" varStatus="status">
                        '${acceso.fecha}'<c:if test="${!status.last}">, </c:if>
                    </c:forEach>
                ],
                datasets: [{
                    label: 'Accesos',
                    data: [
                        <c:forEach var="acceso" items="${estadisticas.accesosPorDia}" varStatus="status">
                            ${acceso.accesos}<c:if test="${!status.last}">, </c:if>
                        </c:forEach>
                    ],
                    borderColor: '#28a745',
                    backgroundColor: 'rgba(40, 167, 69, 0.1)',
                    tension: 0.4,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>