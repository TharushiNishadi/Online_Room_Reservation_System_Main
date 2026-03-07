<%--
  Created by IntelliJ IDEA.
  User: pasiy
  Date: 7/19/2025
  Time: 8:01 PM
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
    <title>Dashboard - Pahana Edu Billing System</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background-color: #0f172a;
            color: #e2e8f0;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        }

        /* Sidebar */
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

        /* Main Content */
        .main-content {
            margin-left: 250px;
            padding: 30px;
        }

        /* Header */
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .header h1 {
            font-size: 2rem;
            font-weight: 700;
            margin: 0;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .user-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background-color: #3b82f6;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
        }

        /* Stats Cards */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-card {
            background-color: #1e293b;
            border-radius: 12px;
            padding: 24px;
            border: 1px solid #334155;
            transition: all 0.3s ease;
        }

        .stat-card:hover {
            transform: translateY(-4px);
            border-color: #3b82f6;
        }

        .stat-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 12px;
        }

        .stat-title {
            color: #94a3b8;
            font-size: 0.875rem;
            font-weight: 500;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }

        .stat-icon {
            width: 40px;
            height: 40px;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.25rem;
        }

        .stat-value {
            font-size: 2rem;
            font-weight: 700;
            margin: 8px 0;
        }

        .stat-change {
            font-size: 0.875rem;
            display: flex;
            align-items: center;
            gap: 4px;
        }

        .stat-change.positive {
            color: #10b981;
        }

        .stat-change.negative {
            color: #ef4444;
        }

        /* Charts */
        .chart-grid {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 20px;
            margin-bottom: 30px;
        }

        .chart-card {
            background-color: #1e293b;
            border-radius: 12px;
            padding: 24px;
            border: 1px solid #334155;
        }

        .chart-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .chart-title {
            font-size: 1.125rem;
            font-weight: 600;
        }

        .chart-container {
            position: relative;
            height: 300px;
        }

        /* Quick Actions */
        .quick-actions {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
        }

        .action-card {
            background-color: #1e293b;
            border: 1px solid #334155;
            border-radius: 8px;
            padding: 20px;
            text-align: center;
            text-decoration: none;
            color: #e2e8f0;
            transition: all 0.3s ease;
        }

        .action-card:hover {
            background-color: #334155;
            color: #fff;
            transform: translateY(-2px);
        }

        .action-icon {
            font-size: 2rem;
            margin-bottom: 10px;
            color: #3b82f6;
        }

        /* Color Variants */
        .bg-blue { background-color: #3b82f6; }
        .bg-green { background-color: #10b981; }
        .bg-yellow { background-color: #f59e0b; }
        .bg-purple { background-color: #8b5cf6; }
        .bg-red { background-color: #ef4444; }
        .bg-cyan { background-color: #06b6d4; }

        /* Responsive */
        @media (max-width: 768px) {
            .sidebar {
                transform: translateX(-100%);
            }
            .main-content {
                margin-left: 0;
            }
            .chart-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<!-- Sidebar -->
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
<div class="main-content">
    <!-- Header -->
    <div class="header">
        <h1>Dashboard</h1>
        <div class="user-info">
            <span>Welcome, ${sessionScope.fullName}</span>
            <div class="user-avatar">
                ${sessionScope.fullName.charAt(0)}
            </div>
        </div>
    </div>

    <!-- Stats Grid -->
    <div class="stats-grid">
        <!-- Total Sales -->
        <div class="stat-card">
            <div class="stat-header">
                <span class="stat-title">Total Sales</span>
                <div class="stat-icon bg-blue">
                    <i class="bi bi-currency-dollar text-white"></i>
                </div>
            </div>
            <div class="stat-value">Rs <fmt:formatNumber value="${totalRevenue}" pattern="#,##0"/></div>
            <div class="stat-change positive">
                <i class="bi bi-arrow-up"></i>
                <span>+8%</span>
            </div>
        </div>

        <!-- Total Bills -->
        <div class="stat-card">
            <div class="stat-header">
                <span class="stat-title">Total Bills</span>
                <div class="stat-icon bg-green">
                    <i class="bi bi-receipt text-white"></i>
                </div>
            </div>
            <div class="stat-value">${totalBills}</div>
            <div class="stat-change positive">
                <i class="bi bi-arrow-up"></i>
                <span>+12%</span>
            </div>
        </div>

        <!-- Customers -->
        <div class="stat-card">
            <div class="stat-header">
                <span class="stat-title">Customers</span>
                <div class="stat-icon bg-purple">
                    <i class="bi bi-people text-white"></i>
                </div>
            </div>
            <div class="stat-value">${totalCustomers}</div>
            <div class="stat-change positive">
                <i class="bi bi-arrow-up"></i>
                <span>+5%</span>
            </div>
        </div>

        <!-- Items -->
        <div class="stat-card">
            <div class="stat-header">
                <span class="stat-title">Total Items</span>
                <div class="stat-icon bg-yellow">
                    <i class="bi bi-box text-white"></i>
                </div>
            </div>
            <div class="stat-value">${totalItems}</div>
            <c:if test="${lowStockCount > 0}">
                <div class="stat-change negative">
                    <i class="bi bi-exclamation-triangle"></i>
                    <span>${lowStockCount} low stock</span>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Charts -->
    <div class="chart-grid">
        <!-- Revenue Chart -->
        <div class="chart-card">
            <div class="chart-header">
                <h3 class="chart-title">Revenue Overview</h3>
                <select class="form-select form-select-sm" style="width: auto; background-color: #334155; color: #e2e8f0; border-color: #475569;">
                    <option>This Month</option>
                    <option>Last Month</option>
                    <option>This Year</option>
                </select>
            </div>
            <div class="chart-container">
                <canvas id="revenueChart"></canvas>
            </div>
        </div>

        <!-- Top Products -->
        <div class="chart-card">
            <div class="chart-header">
                <h3 class="chart-title">Top Selling Items</h3>
            </div>
            <div class="chart-container">
                <canvas id="topItemsChart"></canvas>
            </div>
        </div>
    </div>

    <!-- Quick Actions -->
    <h3 class="mb-3">Quick Actions</h3>
    <div class="quick-actions">
        <a href="${pageContext.request.contextPath}/customer?action=add" class="action-card">
            <div class="action-icon"><i class="bi bi-person-plus"></i></div>
            <div>Add Customer</div>
        </a>
        <a href="${pageContext.request.contextPath}/item?action=add" class="action-card">
            <div class="action-icon"><i class="bi bi-plus-circle"></i></div>
            <div>Add Item</div>
        </a>
        <a href="${pageContext.request.contextPath}/bill" class="action-card">
            <div class="action-icon"><i class="bi bi-receipt-cutoff"></i></div>
            <div>New Bill</div>
        </a>
        <a href="${pageContext.request.contextPath}/export?type=sales&format=excel" class="action-card">
            <div class="action-icon"><i class="bi bi-download"></i></div>
            <div>Export Report</div>
        </a>
    </div>
</div>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>

<script>
    // Chart configurations with dark theme
    Chart.defaults.color = '#94a3b8';
    Chart.defaults.borderColor = '#334155';

    // Revenue Chart
    const revenueCtx = document.getElementById('revenueChart').getContext('2d');
    new Chart(revenueCtx, {
        type: 'line',
        data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            datasets: [{
                label: 'Revenue',
                data: [30000, 35000, 32000, 40000, 38000, 45000, 50000, 48000, 52000, 55000, 58000, 60000],
                borderColor: '#3b82f6',
                backgroundColor: 'rgba(59, 130, 246, 0.1)',
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                x: {
                    grid: {
                        color: '#334155'
                    }
                },
                y: {
                    grid: {
                        color: '#334155'
                    },
                    ticks: {
                        callback: function(value) {
                            return 'Rs ' + value.toLocaleString();
                        }
                    }
                }
            }
        }
    });

    // Top Items Chart
    const topItemsCtx = document.getElementById('topItemsChart').getContext('2d');
    new Chart(topItemsCtx, {
        type: 'doughnut',
        data: {
            labels: ['Notebooks', 'Pens', 'Calculators', 'Others'],
            datasets: [{
                data: [35, 25, 20, 20],
                backgroundColor: ['#3b82f6', '#10b981', '#f59e0b', '#8b5cf6']
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        padding: 15
                    }
                }
            }
        }
    });
</script>
</body>
</html>