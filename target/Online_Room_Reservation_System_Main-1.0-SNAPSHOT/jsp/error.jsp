<%--
  Created by IntelliJ IDEA.
  User: pasiy
  Date: 7/19/2025
  Time: 9:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Pahana Edu</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            margin: 0;
        }
        .error-container {
            text-align: center;
            padding: 40px;
        }
        .error-icon {
            font-size: 80px;
            color: #dc3545;
            margin-bottom: 20px;
        }
        .error-code {
            font-size: 120px;
            font-weight: bold;
            color: #6c757d;
            margin: 0;
            line-height: 1;
        }
    </style>
</head>
<body>
<div class="error-container">
    <div class="error-icon">
        <i class="bi bi-exclamation-triangle-fill"></i>
    </div>

    <c:choose>
        <c:when test="${pageContext.errorData.statusCode == 404}">
            <h1 class="error-code">404</h1>
            <h2 class="mt-3">Page Not Found</h2>
            <p class="text-muted">
                Sorry, the page you are looking for doesn't exist.<br>
                You may have mistyped the address or the page may have moved.
            </p>
        </c:when>
        <c:when test="${pageContext.errorData.statusCode == 500}">
            <h1 class="error-code">500</h1>
            <h2 class="mt-3">Internal Server Error</h2>
            <p class="text-muted">
                Something went wrong on our server.<br>
                Please try again later or contact support if the problem persists.
            </p>
        </c:when>
        <c:when test="${pageContext.errorData.statusCode == 403}">
            <h1 class="error-code">403</h1>
            <h2 class="mt-3">Access Forbidden</h2>
            <p class="text-muted">
                You don't have permission to access this resource.<br>
                Please login with appropriate credentials.
            </p>
        </c:when>
        <c:otherwise>
            <h1 class="error-code">Error</h1>
            <h2 class="mt-3">Something Went Wrong</h2>
            <p class="text-muted">
                An unexpected error has occurred.<br>
                Please try again or contact support.
            </p>
        </c:otherwise>
    </c:choose>

    <!-- Error Details (only show in development) -->
    <c:if test="${pageContext.exception != null}">
        <div class="mt-4">
            <details>
                <summary class="text-muted small">Technical Details</summary>
                <div class="mt-2 text-start">
                    <small class="text-muted">
                        <strong>Exception:</strong> ${pageContext.exception.class.name}<br>
                        <strong>Message:</strong> ${pageContext.exception.message}
                    </small>
                </div>
            </details>
        </div>
    </c:if>

    <div class="mt-4">
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">
                    <i class="bi bi-house-door me-2"></i>Go to Dashboard
                </a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                    <i class="bi bi-box-arrow-in-right me-2"></i>Go to Login
                </a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>