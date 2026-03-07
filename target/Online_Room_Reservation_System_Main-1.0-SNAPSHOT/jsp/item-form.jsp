<%--
  Created by IntelliJ IDEA.
  User: pasiy
  Date: 7/19/2025
  Time: 8:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty item.id ? 'Edit' : 'Add'} Item - Pahana Edu</title>
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
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/item">Items</a></li>
            <li class="breadcrumb-item active">${not empty item.id ? 'Edit' : 'Add'} Item</li>
        </ol>
    </nav>

    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">
                        <i class="bi bi-${not empty item.id ? 'pencil-square' : 'plus-circle'} me-2"></i>
                        ${not empty item.id ? 'Edit' : 'Add New'} Item
                    </h5>
                </div>
                <div class="card-body">
                    <!-- Error Alert -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <!-- Item Form -->
                    <form action="${pageContext.request.contextPath}/item" method="post">
                        <input type="hidden" name="action" value="${not empty item.id ? 'update' : 'save'}">
                        <c:if test="${not empty item.id}">
                            <input type="hidden" name="id" value="${item.id}">
                        </c:if>

                        <div class="mb-3">
                            <label for="name" class="form-label">
                                <i class="bi bi-box me-1"></i>Item Name <span class="text-danger">*</span>
                            </label>
                            <input type="text"
                                   class="form-control"
                                   id="name"
                                   name="name"
                                   value="${item.name}"
                                   placeholder="Enter item name"
                                   required>
                        </div>

                        <div class="mb-3">
                            <label for="description" class="form-label">
                                <i class="bi bi-card-text me-1"></i>Description
                            </label>
                            <textarea class="form-control"
                                      id="description"
                                      name="description"
                                      rows="3"
                                      placeholder="Enter item description (optional)">${item.description}</textarea>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="unitPrice" class="form-label">
                                    <span class="me-1">₨</span>Unit Price (LKR) <span class="text-danger">*</span>

                                </label>
                                <input type="number"
                                       class="form-control"
                                       id="unitPrice"
                                       name="unitPrice"
                                       value="${item.unitPrice}"
                                       placeholder="0.00"
                                       step="0.01"
                                       min="0"
                                       required>
                                <div class="form-text">Enter price in Rupees (e.g., 150.00)</div>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label for="stockQuantity" class="form-label">
                                    <i class="bi bi-boxes me-1"></i>Stock Quantity <span class="text-danger">*</span>
                                </label>
                                <input type="number"
                                       class="form-control"
                                       id="stockQuantity"
                                       name="stockQuantity"
                                       value="${item.stockQuantity}"
                                       placeholder="0"
                                       min="0"
                                       required>
                                <div class="form-text">Enter available quantity</div>
                            </div>
                        </div>

                        <hr class="my-4">

                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <a href="${pageContext.request.contextPath}/item"
                               class="btn btn-secondary">
                                <i class="bi bi-x-circle me-1"></i>Cancel
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-check-circle me-1"></i>
                                ${not empty item.id ? 'Update' : 'Save'} Item
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Stock Info Card (for edit mode) -->
            <c:if test="${not empty item.id}">
                <div class="card mt-3">
                    <div class="card-body">
                        <h6 class="card-title">Stock Information</h6>
                        <p class="mb-1">
                            <strong>Current Stock:</strong>
                            <c:choose>
                                <c:when test="${item.stockQuantity == 0}">
                                    <span class="text-danger">Out of Stock</span>
                                </c:when>
                                <c:when test="${item.stockQuantity < 10}">
                                    <span class="text-warning">${item.stockQuantity} units (Low Stock)</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-success">${item.stockQuantity} units</span>
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <small class="text-muted">
                            <i class="bi bi-info-circle me-1"></i>
                            Items with less than 10 units are marked as low stock
                        </small>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Ensure price has two decimal places
    document.getElementById('unitPrice').addEventListener('blur', function() {
        if (this.value) {
            this.value = parseFloat(this.value).toFixed(2);
        }
    });
</script>
</body>
</html>