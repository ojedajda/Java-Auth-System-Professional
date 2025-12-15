<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error 403 - Acceso Denegado</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-light">
    <div class="container vh-100 d-flex align-items-center justify-content-center">
        <div class="text-center">
            <i class="fas fa-ban fa-5x text-danger mb-4"></i>
            <h1 class="display-4 fw-bold text-danger">Error 403</h1>
            <h3 class="mb-4">Acceso Denegado</h3>
            <p class="lead mb-4">No tienes permisos para acceder a esta página.</p>
            <a href="dashboard.jsp" class="btn btn-primary btn-lg">
                <i class="fas fa-home me-2"></i>Volver al Dashboard
            </a>
        </div>
    </div>
</body>
</html>