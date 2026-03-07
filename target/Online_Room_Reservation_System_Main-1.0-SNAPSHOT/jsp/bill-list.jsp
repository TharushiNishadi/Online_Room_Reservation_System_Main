<%--
  Created by IntelliJ IDEA.
  User: pasiy
  Date: 7/19/2025
  Time: 9:05 PM
  To change this template use File | Settings | File Templates.
--%>
<<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bills - Pahana Edu Billing System</title>
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
            <h2><i class="bi bi-receipt me-2"></i>All Bills</h2>
        </div>
        <div class="col text-end">
            <a href="${pageContext.request.contextPath}/export?type=sales&format=excel"
               class="btn btn-success me-2">
                <i class="bi bi-file-earmark-excel me-2"></i>Export Sales Report
            </a>
            <a href="${pageContext.request.contextPath}/bill"
               class="btn btn-primary">
                <i class="bi bi-plus-circle me-2"></i>Create New Bill
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

    <!-- Bills Table -->
    <div class="card">
        <div class="card-header">
            <h5 class="mb-0">
                <i class="bi bi-list-ul me-2"></i>Bill History
                <span class="badge bg-secondary">${totalBills} bills</span>
            </h5>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${empty bills}">
                    <div class="alert alert-info">
                        <i class="bi bi-info-circle me-2"></i>
                        No bills found. Click "Create New Bill" to generate your first bill.
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th>Bill #</th>
                                <th>Date</th>
                                <th>Customer</th>
                                <th>Amount</th>
                                <th>Created By</th>
                                <th width="150">Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${bills}" var="bill">
                                <tr>
                                    <td>
                                        <span class="badge bg-primary">${bill.billNumber}</span>
                                    </td>
                                    <td>
                                        <i class="bi bi-calendar3 me-1"></i>
                                        <fmt:formatDate value="${bill.billDate}" pattern="dd-MM-yyyy"/>
                                    </td>
                                    <td>
                                        <strong>${bill.customer.name}</strong>
                                        <c:if test="${not empty bill.customer.phone}">
                                            <br><small class="text-muted">
                                            <i class="bi bi-telephone"></i> ${bill.customer.phone}
                                        </small>
                                        </c:if>
                                    </td>
                                    <td>
                                        <strong>Rs. <fmt:formatNumber value="${bill.totalAmount}" pattern="#,##0.00"/></strong>
                                    </td>
                                    <td>
                                        <i class="bi bi-person me-1"></i>${bill.createdBy.fullName}
                                    </td>
                                    <td>
                                        <div class="btn-group" role="group">
                                            <a href="${pageContext.request.contextPath}/bill?action=view&id=${bill.id}"
                                               class="btn btn-sm btn-info" title="View">
                                                <i class="bi bi-eye"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/export?type=bill&format=pdf&id=${bill.id}"
                                               class="btn btn-sm btn-danger" title="Download PDF">
                                                <i class="bi bi-file-earmark-pdf"></i>
                                            </a>
                                            <c:if test="${not empty bill.customer.email}">
                                                <a href="${pageContext.request.contextPath}/export?type=bill&format=pdf&id=${bill.id}&email=true"
                                                   class="btn btn-sm btn-success" title="Email Bill"
                                                   onclick="return confirm('Send bill to ${bill.customer.email}?');">
                                                    <i class="bi bi-envelope"></i>
                                                </a>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                            <tfoot>
                            <tr class="table-info">
                                <th colspan="3" class="text-end">Total Revenue:</th>
                                <th colspan="3">
                                    <c:set var="totalRevenue" value="0"/>
                                    <c:forEach items="${bills}" var="bill">
                                        <c:set var="totalRevenue" value="${totalRevenue + bill.totalAmount}"/>
                                    </c:forEach>
                                    Rs. <fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00"/>
                                </th>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>