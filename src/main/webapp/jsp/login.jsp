<%--
  Created by IntelliJ IDEA.
  User: pasiy
  Date: 7/19/2025
  Time: 5:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Ocean View Resort Online Room Reservation System</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .login-container {
            max-width: 400px;
            width: 100%;
        }

        .login-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
        }

        .login-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px 15px 0 0;
            padding: 30px;
            text-align: center;
        }

        .login-body {
            padding: 40px 30px;
        }

        .btn-login {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            padding: 12px;
            font-weight: 600;
            transition: transform 0.2s;
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }
    </style>
</head>
<body>
<div class="login-container">
    <div class="card login-card">
        <div class="login-header">
            <h3 class="mb-0">
                <i class="bi bi-shop me-2"></i>Ocean View Resort
            </h3>
            <p class="mb-0 mt-2">Online Room Reservation System</p>
        </div>
        <div class="login-body">
            <h5 class="text-center mb-4">Welcome Back!</h5>

            <!-- Error Alert -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert" >
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Login Form -->
            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="mb-3">
                    <label for="username" class="form-label">
                        <i class="bi bi-person-fill me-1"></i>Username
                    </label>
                    <input type="text"
                           class="form-control form-control-lg"
                           id="username"
                           name="username"
                           value="${username}"
                           placeholder="Enter your username"
                           required
                           autofocus>
                </div>

                <div class="mb-4">
                    <label for="password" class="form-label">
                        <i class="bi bi-lock-fill me-1"></i>Password
                    </label>
                    <input type="password"
                           class="form-control form-control-lg"
                           id="password"
                           name="password"
                           placeholder="Enter your password"
                           required>
                </div>

                <div class="d-grid">
                    <button type="submit" class="btn btn-primary btn-lg btn-login">
                        <i class="bi bi-box-arrow-in-right me-2"></i>Login
                    </button>
                </div>
            </form>

            <hr class="my-4">

            <div class="text-center text-muted">
                <small>
                    <i class="bi bi-info-circle me-1"></i>
                    Forgot password ? Contact your IT Admin
                </small>
            </div>
        </div>
    </div>

    <div class="text-center mt-3 text-muted">
        <small>&copy; 2026 Ocean View Resort - ICBT CIS6003 Project</small>
    </div>
</div>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>