<%--
  Created by IntelliJ IDEA.
  User: pasiy
  Date: 7/19/2025
  Time: 8:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Items - Pahana Edu Billing System</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .sidebar {
            background-color: #1e293b;
            min-height: 100vh;
            width: 250px;
            position: fixed;
            left: 0;
            top: 0;
            padding: 20px 0;
        }

        .sidebar-brand {
            padding: 0 20px 30px;
            font-size: 1.5rem;
            font-weight: 700;
            color: #fff;
        }

        .sidebar-menu {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .sidebar-menu li {
            margin-bottom: 5px;
        }

        .sidebar-menu a {
            display: flex;
            align-items: center;
            padding: 12px 20px;
            color: #94a3b8;
            text-decoration: none;
            transition: all 0.3s ease;
        }

        .sidebar-menu a:hover {
            background-color: #334155;
            color: #fff;
        }

        .sidebar-menu a.active {
            background-color: #3b82f6;
            color: #fff;
        }

        .sidebar-menu i {
            margin-right: 12px;
            font-size: 1.1rem;
        }
    </style>
</head>
<body class="bg-light">
<!-- Navigation Bar -->
<div class="sidebar">
    <div class="sidebar-brand">
        <i class="bi bi-shop me-2"></i>Pahana Edu
    </div>
    <ul class="sidebar-menu">
        <li>
            <a href="${pageContext.request.contextPath}/dashboard" class="active">
                <i class="bi bi-speedometer2"></i>Dashboard
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/customer">
                <i class="bi bi-people"></i>Customers
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/item">
                <i class="bi bi-box"></i>Items
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/bill">
                <i class="bi bi-receipt"></i>Billing
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/bill?action=list">
                <i class="bi bi-list-ul"></i>All Bills
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/help">
                <i class="bi bi-question-circle"></i>Help
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/logout">
                <i class="bi bi-box-arrow-right"></i>Logout
            </a>
        </li>
    </ul>
</div>

<!-- Main Content -->
<div class="container my-4">
    <div class="row mb-4">
        <div class="col">
            <h2><i class="bi bi-box me-2"></i>Item Management</h2>
        </div>
        <div class="col text-end">
            <a href="${pageContext.request.contextPath}/export?type=items&format=excel"
               class="btn btn-success me-2">
                <i class="bi bi-file-earmark-excel me-2"></i>Export Inventory
            </a>
            <a href="${pageContext.request.contextPath}/item?action=add"
               class="btn btn-primary">
                <i class="bi bi-plus-circle me-2"></i>Add New Item
            </a>
        </div>
    </div>

    <!-- Success Alert -->
    <c:if test="${not empty param.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i>${param.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Error Alert -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>${param.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Low Stock Alert -->
    <c:if test="${lowStockCount > 0}">
        <div class="alert alert-warning">
            <i class="bi bi-exclamation-triangle me-2"></i>
            <strong>Low Stock Alert:</strong> ${lowStockCount} item(s) have stock below 10 units.
            <button class="btn btn-warning btn-sm float-end" onclick="sendLowStockAlert()">
                <i class="bi bi-envelope me-1"></i>Send Alert Email
            </button>
        </div>
    </c:if>

    <!-- Item Table -->
    <div class="card">
        <div class="card-header">
            <h5 class="mb-0">
                <i class="bi bi-list-ul me-2"></i>Item List
                <span class="badge bg-secondary">${totalItems} items</span>
            </h5>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${empty items}">
                    <div class="alert alert-info">
                        <i class="bi bi-info-circle me-2"></i>
                        No items found. Click "Add New Item" to get started.
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Unit Price</th>
                                <th>Stock</th>
                                <th>Status</th>
                                <th width="200">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${items}" var="item">
                                <tr>
                                    <td>${item.id}</td>
                                    <td><strong>${item.name}</strong></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty item.description}">
                                                ${item.description}
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">No description</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>Rs. <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.stockQuantity < 10}">
                                                        <span class="text-warning fw-bold">
                                                            <i class="bi bi-exclamation-triangle"></i> ${item.stockQuantity}
                                                        </span>
                                            </c:when>
                                            <c:otherwise>
                                                ${item.stockQuantity}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.stockQuantity == 0}">
                                                <span class="badge bg-danger">Out of Stock</span>
                                            </c:when>
                                            <c:when test="${item.stockQuantity < 10}">
                                                <span class="badge bg-warning text-dark">Low Stock</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-success">In Stock</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/item?action=edit&id=${item.id}"
                                           class="btn btn-sm btn-warning">
                                            <i class="bi bi-pencil"></i> Edit
                                        </a>
                                        <a href="${pageContext.request.contextPath}/item?action=delete&id=${item.id}"
                                           class="btn btn-sm btn-danger"
                                           onclick="return confirm('Are you sure you want to delete this item?');">
                                            <i class="bi bi-trash"></i> Delete
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    function sendLowStockAlert() {
        if (confirm('Send low stock alert email to admin?')) {
            fetch('${pageContext.request.contextPath}/export', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'action=sendLowStockAlert'
            })
                .then(response => response.text())
                .then(data => {
                    alert(data);
                })
                .catch(error => {
                    alert('Error sending alert: ' + error);
                });
        }
    }
</script>
</body>
</html>