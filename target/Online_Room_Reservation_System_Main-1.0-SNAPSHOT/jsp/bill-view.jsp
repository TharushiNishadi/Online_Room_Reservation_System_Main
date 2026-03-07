<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bill Details - Pahana Edu</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .invoice-header {
            background-color: #f8f9fa;
            padding: 30px;
            border-radius: 10px;
            margin-bottom: 30px;
        }
        .invoice-table th {
            background-color: #e9ecef;
        }
        @media print {
            .no-print {
                display: none;
            }
            .invoice-container {
                margin: 0;
                padding: 0;
            }
        }
    </style>
</head>
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
<div class="container my-4 invoice-container">
    <!-- Success Alert -->
    <c:if test="${not empty param.success}">
        <div class="alert alert-success alert-dismissible fade show no-print" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i>${param.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Action Buttons -->
    <div class="row mb-3 no-print">
        <div class="col">
            <a href="${pageContext.request.contextPath}/bill?action=list" class="btn btn-secondary">
                <i class="bi bi-arrow-left me-2"></i>Back to Bills
            </a>
        </div>
        <div class="col text-end">
            <div class="btn-group">
                <button onclick="window.print()" class="btn btn-primary">
                    <i class="bi bi-printer me-2"></i>Print
                </button>
                <a href="${pageContext.request.contextPath}/export?type=bill&format=pdf&id=${bill.id}"
                   class="btn btn-danger">
                    <i class="bi bi-file-earmark-pdf me-2"></i>Download PDF
                </a>
                <c:if test="${not empty bill.customer.email}">
                    <button onclick="emailBill()" class="btn btn-success">
                        <i class="bi bi-envelope me-2"></i>Email Bill
                    </button>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Invoice -->
    <div class="card">
        <div class="card-body p-5">
            <!-- Company Header -->
            <div class="invoice-header text-center">
                <h1 class="display-6 fw-bold">PAHANA EDU</h1>
                <p class="mb-0">Online Billing System</p>
                <p class="text-muted">123 Main Street, Colombo | Tel: 011-1234567</p>
            </div>

            <!-- Bill Info -->
            <div class="row mb-4">
                <div class="col-md-6">
                    <h5 class="text-primary">Bill To:</h5>
                    <p class="mb-1"><strong>${bill.customer.name}</strong></p>
                    <c:if test="${not empty bill.customer.email}">
                        <p class="mb-1"><i class="bi bi-envelope me-1"></i>${bill.customer.email}</p>
                    </c:if>
                    <c:if test="${not empty bill.customer.phone}">
                        <p class="mb-1"><i class="bi bi-telephone me-1"></i>${bill.customer.phone}</p>
                    </c:if>
                    <c:if test="${not empty bill.customer.address}">
                        <p class="mb-0"><i class="bi bi-geo-alt me-1"></i>${bill.customer.address}</p>
                    </c:if>
                </div>
                <div class="col-md-6 text-md-end">
                    <h5 class="text-primary">Bill Details:</h5>
                    <p class="mb-1"><strong>Bill Number:</strong> ${bill.billNumber}</p>
                    <p class="mb-1"><strong>Date:</strong>
                        <fmt:formatDate value="${bill.billDate}" pattern="dd MMMM yyyy"/>
                    </p>
                    <p class="mb-0"><strong>Prepared By:</strong> ${bill.createdBy.fullName}</p>
                </div>
            </div>

            <!-- Items Table -->
            <div class="table-responsive">
                <table class="table table-bordered invoice-table">
                    <thead>
                    <tr>
                        <th width="50">#</th>
                        <th>Item Description</th>
                        <th width="150" class="text-end">Unit Price</th>
                        <th width="100" class="text-center">Quantity</th>
                        <th width="150" class="text-end">Total</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${bill.billItems}" var="billItem" varStatus="status">
                        <tr>
                            <td>${status.count}</td>
                            <td>
                                <strong>${billItem.item.name}</strong>
                                <c:if test="${not empty billItem.item.description}">
                                    <br><small class="text-muted">${billItem.item.description}</small>
                                </c:if>
                            </td>
                            <td class="text-end">
                                Rs. <fmt:formatNumber value="${billItem.unitPrice}" pattern="#,##0.00"/>
                            </td>
                            <td class="text-center">${billItem.quantity}</td>
                            <td class="text-end">
                                Rs. <fmt:formatNumber value="${billItem.totalPrice}" pattern="#,##0.00"/>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr>
                        <th colspan="4" class="text-end">Subtotal:</th>
                        <th class="text-end">
                            Rs. <fmt:formatNumber value="${bill.totalAmount}" pattern="#,##0.00"/>
                        </th>
                    </tr>
                    <tr>
                        <th colspan="4" class="text-end">Tax (0%):</th>
                        <th class="text-end">Rs. 0.00</th>
                    </tr>
                    <tr class="table-primary">
                        <th colspan="4" class="text-end fs-5">Grand Total:</th>
                        <th class="text-end fs-5">
                            Rs. <fmt:formatNumber value="${bill.totalAmount}" pattern="#,##0.00"/>
                        </th>
                    </tr>
                    </tfoot>
                </table>
            </div>

            <!-- Footer -->
            <div class="mt-5 pt-3 border-top">
                <div class="row">
                    <div class="col-md-6">
                        <p class="text-muted mb-0">
                            <small>Generated on:
                                <fmt:formatDate value="${bill.createdAt}" pattern="dd-MM-yyyy HH:mm:ss"/>
                            </small>
                        </p>
                    </div>
                    <div class="col-md-6 text-md-end">
                        <p class="text-muted mb-0">
                            <small>Thank you for your business!</small>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    function emailBill() {
        if (confirm('Send bill to ${bill.customer.email}?')) {
            // Show loading message
            const btn = event.target;
            const originalText = btn.innerHTML;
            btn.disabled = true;
            btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Sending...';

            // Redirect to export servlet with email parameter
            window.location.href = '${pageContext.request.contextPath}/export?type=bill&format=pdf&id=${bill.id}&email=true';

            // Reset button after delay
            setTimeout(() => {
                btn.disabled = false;
                btn.innerHTML = originalText;
            }, 3000);
        }
    }
</script>
</body>
</html>