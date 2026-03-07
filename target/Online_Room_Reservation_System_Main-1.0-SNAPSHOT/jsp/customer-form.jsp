<%--
  Created by IntelliJ IDEA.
  User: pasiy
  Date: 7/19/2025
  Time: 8:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty customer.id ? 'Edit' : 'Add'} Customer - Pahana Edu</title>
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
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/customer">Customers</a></li>
            <li class="breadcrumb-item active">${not empty customer.id ? 'Edit' : 'Add'} Customer</li>
        </ol>
    </nav>

    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">
                        <i class="bi bi-person-${not empty customer.id ? 'gear' : 'plus'} me-2"></i>
                        ${not empty customer.id ? 'Edit' : 'Add New'} Customer
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

                    <!-- Customer Form -->
                    <form action="${pageContext.request.contextPath}/customer" method="post">
                        <input type="hidden" name="action" value="${not empty customer.id ? 'update' : 'save'}">
                        <c:if test="${not empty customer.id}">
                            <input type="hidden" name="id" value="${customer.id}">
                        </c:if>

                        <div class="mb-3">
                            <label for="name" class="form-label">
                                <i class="bi bi-person me-1"></i>Customer Name <span class="text-danger">*</span>
                            </label>
                            <input type="text"
                                   class="form-control"
                                   id="name"
                                   name="name"
                                   value="${customer.name}"
                                   placeholder="Enter customer name"
                                   required>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label">
                                <i class="bi bi-envelope me-1"></i>Email
                            </label>
                            <input type="email"
                                   class="form-control"
                                   id="email"
                                   name="email"
                                   value="${customer.email}"
                                   placeholder="customer@example.com">
                            <div class="form-text">Optional: Enter a valid email address</div>
                        </div>

                        <div class="mb-3">
                            <label for="phone" class="form-label">
                                <i class="bi bi-telephone me-1"></i>Phone Number
                            </label>
                            <input type="tel"
                                   class="form-control"
                                   id="phone"
                                   name="phone"
                                   value="${customer.phone}"
                                   placeholder="0771234567"
                                   pattern="[0-9]{10}"
                                   maxlength="10">
                            <div class="form-text">Optional: Enter 10-digit phone number (e.g., 0771234567)</div>
                        </div>

                        <div class="mb-4">
                            <label for="address" class="form-label">
                                <i class="bi bi-geo-alt me-1"></i>Address
                            </label>
                            <textarea class="form-control"
                                      id="address"
                                      name="address"
                                      rows="3"
                                      placeholder="Enter customer address">${customer.address}</textarea>
                            <div class="form-text">Optional: Enter customer's address</div>
                        </div>

                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <a href="${pageContext.request.contextPath}/customer"
                               class="btn btn-secondary">
                                <i class="bi bi-x-circle me-1"></i>Cancel
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-check-circle me-1"></i>
                                ${not empty customer.id ? 'Update' : 'Save'} Customer
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Client-side validation for phone number
    document.getElementById('phone').addEventListener('input', function(e) {
        this.value = this.value.replace(/[^0-9]/g, '');
    });
</script>
</body>
</html>