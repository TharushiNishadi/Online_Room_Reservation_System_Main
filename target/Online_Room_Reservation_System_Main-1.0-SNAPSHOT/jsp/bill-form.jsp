<%--
  Created by IntelliJ IDEA.
  User: pasiy
  Date: 7/19/2025
  Time: 9:05 PM
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
    <title>Create New Bill - Pahana Edu</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .item-row {
            background-color: #f8f9fa;
            padding: 15px;
            margin-bottom: 10px;
            border-radius: 5px;
        }
        .total-section {
            background-color: #e9ecef;
            padding: 20px;
            border-radius: 10px;
            margin-top: 20px;
        }
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
            <h2><i class="bi bi-receipt-cutoff me-2"></i>Create New Bill</h2>
        </div>
        <div class="col text-end">
            <a href="${pageContext.request.contextPath}/bill?action=list"
               class="btn btn-secondary">
                <i class="bi bi-list-ul me-2"></i>View All Bills
            </a>
        </div>
    </div>

    <!-- Error Alert -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Bill Form -->
    <form action="${pageContext.request.contextPath}/bill" method="post" id="billForm">
        <input type="hidden" name="action" value="create">

        <div class="card mb-4">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0"><i class="bi bi-person-check me-2"></i>Customer Information</h5>
            </div>
            <div class="card-body">
                <div class="mb-3">
                    <label for="customerId" class="form-label">
                        Select Customer <span class="text-danger">*</span>
                    </label>
                    <select class="form-select form-select-lg" id="customerId" name="customerId" required>
                        <option value="">-- Select Customer --</option>
                        <c:forEach items="${customers}" var="customer">
                            <option value="${customer.id}">
                                    ${customer.name}
                                <c:if test="${not empty customer.phone}"> - ${customer.phone}</c:if>
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>

        <div class="card mb-4">
            <div class="card-header bg-success text-white">
                <h5 class="mb-0"><i class="bi bi-cart-check me-2"></i>Add Items</h5>
            </div>
            <div class="card-body">
                <div id="itemsContainer">
                    <!-- First Item Row -->
                    <div class="item-row" data-row="0">
                        <div class="row">
                            <div class="col-md-3 mb-2">
                                <label>Select Item</label>
                                <select class="form-select item-select" name="itemId" onchange="updatePrice(0)">
                                    <option value="">-- Select Item --</option>
                                    <c:forEach items="${items}" var="item">
                                        <option value="${item.id}"
                                                data-price="${item.unitPrice}"
                                                data-stock="${item.stockQuantity}">
                                                ${item.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-md-2 mb-2">
                                <label>Unit Price</label>
                                <input type="text" class="form-control unit-price" readonly value="Rs. 0.00">
                            </div>

                            <div class="col-md-2 mb-2">
                                <label>Available Stock</label>
                                <input type="text" class="form-control stock-value" readonly value="0">
                            </div>

                            <div class="col-md-1 mb-2">
                                <label>Qty</label>
                                <input type="number" class="form-control quantity-input" name="quantity" min="0" value="0"
                                       onchange="calculateRowTotal(0)">
                            </div>

                            <div class="col-md-2 mb-2">
                                <label>Total</label>
                                <input type="text" class="form-control row-total" readonly value="Rs. 0.00">
                            </div>

                            <div class="col-md-2 mb-2 d-flex align-items-end">
                                <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(0)" style="display: none;">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </div>
                        </div>
                    </div>

                </div>

                <button type="button" class="btn btn-success mt-3" onclick="addItemRow()">
                    <i class="bi bi-plus-circle me-2"></i>Add Another Item
                </button>
            </div>
        </div>

        <!-- Total Section -->
        <div class="total-section">
            <div class="row">
                <div class="col-md-8 text-end">
                    <h4>Grand Total:</h4>
                </div>
                <div class="col-md-4">
                    <h4 id="grandTotal" class="text-primary">Rs. 0.00</h4>
                </div>
            </div>
        </div>

        <!-- Action Buttons -->
        <div class="mt-4 text-center">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary btn-lg">
                <i class="bi bi-x-circle me-2"></i>Cancel
            </a>
            <button type="submit" class="btn btn-primary btn-lg">
                <i class="bi bi-check-circle me-2"></i>Create Bill
            </button>
        </div>
    </form>
</div>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Custom JavaScript for Bill Calculations -->
<script>
    let rowCount = 0;
    let itemPrices = {};

    // Store item prices for quick access
    <c:forEach items="${items}" var="item">
    itemPrices[${item.id}] = ${item.unitPrice};
    </c:forEach>

    function updatePrice(rowIndex) {
        const row = document.querySelector('.item-row[data-row="' + rowIndex + '"]');
        const select = row.querySelector('.item-select');
        const selectedOption = select.options[select.selectedIndex];

        const unitPriceInput = row.querySelector('.unit-price');
        const quantityInput = row.querySelector('.quantity-input');
        const stockInput = row.querySelector('.stock-value');

        if (selectedOption.value) {
            const price = parseFloat(selectedOption.getAttribute('data-price'));
            const stock = parseInt(selectedOption.getAttribute('data-stock'));


            unitPriceInput.value = 'Rs. ' + price.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');

            stockInput.value = stock;

            // Set quantity limit and adjust if needed
            quantityInput.max = stock;
            if (parseInt(quantityInput.value) > stock) {
                quantityInput.value = stock;
            }

        } else {
            unitPriceInput.value = 'Rs. 0.00';
        }

        calculateRowTotal(rowIndex);
    }


    function calculateRowTotal(rowIndex) {
        const row = document.querySelector('.item-row[data-row="' + rowIndex + '"]');
        const select = row.querySelector('.item-select');
        const quantity = parseInt(row.querySelector('.quantity-input').value) || 0;
        const totalField = row.querySelector('.row-total');

        if (select.value && quantity > 0) {
            const price = itemPrices[select.value];
            const total = price * quantity;
            totalField.value = 'Rs. ' + total.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        } else {
            totalField.value = 'Rs. 0.00';
        }

        calculateGrandTotal();
    }

    function calculateGrandTotal() {
        let grandTotal = 0;
        const rows = document.querySelectorAll('.item-row');

        rows.forEach(row => {
            const select = row.querySelector('.item-select');
            const quantity = parseInt(row.querySelector('.quantity-input').value) || 0;

            if (select.value && quantity > 0) {
                const price = itemPrices[select.value];
                grandTotal += price * quantity;
            }
        });

        document.getElementById('grandTotal').textContent =
            'Rs. ' + grandTotal.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }

    function addItemRow() {
        rowCount++;
        const container = document.getElementById('itemsContainer');
        const newRow = document.createElement('div');
        newRow.className = 'item-row';
        newRow.setAttribute('data-row', rowCount);

        newRow.innerHTML = `
                <div class="row">
                    <div class="col-md-6 mb-2">
                        <label>Select Item</label>
                        <select class="form-select item-select" name="itemId" onchange="updatePrice(${rowCount})">
                            <option value="">-- Select Item --</option>
                            <c:forEach items="${items}" var="item">
                                <option value="${item.id}"
                                        data-price="${item.unitPrice}"
                                        data-stock="${item.stockQuantity}">
                                    ${item.name} - Rs. <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/>
                                    (Stock: ${item.stockQuantity})
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2 mb-2">
                        <label>Quantity</label>
                        <input type="number" class="form-control quantity-input" name="quantity"
                               min="0" value="0" onchange="calculateRowTotal(${rowCount})">
                    </div>
                    <div class="col-md-3 mb-2">
                        <label>Total</label>
                        <input type="text" class="form-control row-total" readonly
                               value="Rs. 0.00">
                    </div>
                    <div class="col-md-1 mb-2 d-flex align-items-end">
                        <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(${rowCount})">
                            <i class="bi bi-trash"></i>
                        </button>
                    </div>
                </div>
            `;

        container.appendChild(newRow);

        // Show delete button on first row if more than one row exists
        if (container.children.length > 1) {
            container.querySelector('.item-row[data-row="0"] .btn-danger').style.display = 'block';
        }
    }

    function removeRow(rowIndex) {
        const row = document.querySelector('.item-row[data-row="' + rowIndex + '"]');
        row.remove();
        calculateGrandTotal();

        // Hide delete button on first row if only one row remains
        const container = document.getElementById('itemsContainer');
        if (container.children.length === 1) {
            container.querySelector('.btn-danger').style.display = 'none';
        }
    }

    // Form validation
    document.getElementById('billForm').addEventListener('submit', function(e) {
        const customerId = document.getElementById('customerId').value;
        if (!customerId) {
            e.preventDefault();
            alert('Please select a customer');
            return;
        }

        let hasItems = false;
        const rows = document.querySelectorAll('.item-row');

        rows.forEach(row => {
            const select = row.querySelector('.item-select');
            const quantity = parseInt(row.querySelector('.quantity-input').value) || 0;

            if (select.value && quantity > 0) {
                hasItems = true;
            }
        });

        if (!hasItems) {
            e.preventDefault();
            alert('Please add at least one item with quantity');
            return;
        }
    });
</script>
</body>
</html>