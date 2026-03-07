<%-- Dashboard.jsp (Fixed) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Ocean View Resort Room Reservation System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { background-color: #0f172a; color: #e2e8f0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; }
        .sidebar { background-color: #1e293b; min-height: 100vh; width: 250px; position: fixed; left: 0; top: 0; padding: 20px 0; }
        .sidebar-brand { padding: 0 20px 30px; font-size: 1.5rem; font-weight: 700; color: #fff; }
        .sidebar-menu { list-style: none; padding: 0; margin: 0; }
        .sidebar-menu li { margin-bottom: 5px; }
        .sidebar-menu a { display: flex; align-items: center; padding: 12px 20px; color: #94a3b8; text-decoration: none; transition: all 0.3s ease; }
        .sidebar-menu a:hover { background-color: #334155; color: #fff; }
        .sidebar-menu a.active { background-color: #3b82f6; color: #fff; }
        .sidebar-menu i { margin-right: 12px; font-size: 1.1rem; }

        .main-content { margin-left: 250px; padding: 30px; }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
        .header h1 { font-size: 2rem; font-weight: 700; margin: 0; }
        .user-info { display: flex; align-items: center; gap: 15px; }
        .user-avatar { width: 40px; height: 40px; border-radius: 50%; background-color: #3b82f6; display: flex; align-items: center; justify-content: center; font-weight: 600; }

        .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-bottom: 30px; }
        .stat-card { background-color: #1e293b; border-radius: 12px; padding: 24px; border: 1px solid #334155; transition: all 0.3s ease; }
        .stat-card:hover { transform: translateY(-4px); border-color: #3b82f6; }
        .stat-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 12px; }
        .stat-title { color: #94a3b8; font-size: 0.875rem; font-weight: 500; text-transform: uppercase; letter-spacing: 0.05em; }
        .stat-icon { width: 40px; height: 40px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 1.25rem; }
        .stat-value { font-size: 2rem; font-weight: 700; margin: 8px 0; }
        .stat-change { font-size: 0.875rem; display: flex; align-items: center; gap: 4px; }
        .stat-change.positive { color: #10b981; }
        .stat-change.negative { color: #ef4444; }

        .chart-grid { display: grid; grid-template-columns: 2fr 1fr; gap: 20px; margin-bottom: 30px; }
        .chart-card { background-color: #1e293b; border-radius: 12px; padding: 24px; border: 1px solid #334155; }
        .chart-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .chart-title { font-size: 1.125rem; font-weight: 600; }
        .chart-container { position: relative; height: 300px; }

        .quick-actions { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; }
        .action-card { background-color: #1e293b; border: 1px solid #334155; border-radius: 8px; padding: 20px; text-align: center; text-decoration: none; color: #e2e8f0; transition: all 0.3s ease; }
        .action-card:hover { background-color: #334155; color: #fff; transform: translateY(-2px); }
        .action-icon { font-size: 2rem; margin-bottom: 10px; color: #3b82f6; }

        .bg-blue { background-color: #3b82f6; }
        .bg-green { background-color: #10b981; }
        .bg-yellow { background-color: #f59e0b; }
        .bg-purple { background-color: #8b5cf6; }

        @media (max-width: 768px) {
            .sidebar { transform: translateX(-100%); }
            .main-content { margin-left: 0; }
            .chart-grid { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>

<c:set var="fullName" value="${sessionScope.fullName}" />
<c:if test="${empty fullName}">
    <c:set var="fullName" value="User" />
</c:if>

<c:set var="avatarLetter" value="U" />
<c:if test="${not empty fullName}">
    <c:set var="avatarLetter" value="${fn:substring(fullName,0,1)}" />
</c:if>

<!-- Sidebar -->
<div class="sidebar">
    <div class="sidebar-brand">
        <i class="bi bi-shop me-2"></i>Ocean View Resort
    </div>
    <ul class="sidebar-menu">
        <li><a href="${pageContext.request.contextPath}/dashboard" class="active"><i class="bi bi-speedometer2"></i>Dashboard</a></li>
        <li><a href="${pageContext.request.contextPath}/customer"><i class="bi bi-people"></i>Customers</a></li>
        <li><a href="${pageContext.request.contextPath}/item"><i class="bi bi-box"></i>Rooms</a></li>
        <li><a href="${pageContext.request.contextPath}/bill"><i class="bi bi-receipt"></i>Billing</a></li>
        <li><a href="${pageContext.request.contextPath}/bill?action=list"><i class="bi bi-list-ul"></i>All Bills</a></li>
        <li><a href="${pageContext.request.contextPath}/help"><i class="bi bi-question-circle"></i>Help</a></li>
        <li><a href="${pageContext.request.contextPath}/logout"><i class="bi bi-box-arrow-right"></i>Logout</a></li>
    </ul>
</div>

<div class="main-content">
    <div class="header">
        <h1>Dashboard</h1>
        <div class="user-info">
            <span>Welcome, ${fullName}</span>
            <div class="user-avatar">${avatarLetter}</div>
        </div>
    </div>

    <c:if test="${empty totalRevenue}"><c:set var="totalRevenue" value="0" /></c:if>
    <c:if test="${empty totalBills}"><c:set var="totalBills" value="0" /></c:if>
    <c:if test="${empty totalCustomers}"><c:set var="totalCustomers" value="0" /></c:if>
    <c:if test="${empty totalItems}"><c:set var="totalItems" value="0" /></c:if>
    <c:if test="${empty lowStockCount}"><c:set var="lowStockCount" value="0" /></c:if>

    <!-- Stats Grid -->
    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-header">
                <span class="stat-title">Total Sales</span>
                <div class="stat-icon bg-blue"><i class="bi bi-currency-dollar text-white"></i></div>
            </div>
            <div class="stat-value">Rs <fmt:formatNumber value="${totalRevenue}" pattern="#,##0"/></div>
            <div class="stat-change positive"><i class="bi bi-arrow-up"></i><span>+8%</span></div>
        </div>

        <div class="stat-card">
            <div class="stat-header">
                <span class="stat-title">Total Bills</span>
                <div class="stat-icon bg-green"><i class="bi bi-receipt text-white"></i></div>
            </div>
            <div class="stat-value">${totalBills}</div>
            <div class="stat-change positive"><i class="bi bi-arrow-up"></i><span>+12%</span></div>
        </div>

        <div class="stat-card">
            <div class="stat-header">
                <span class="stat-title">Customers</span>
                <div class="stat-icon bg-purple"><i class="bi bi-people text-white"></i></div>
            </div>
            <div class="stat-value">${totalCustomers}</div>
            <div class="stat-change positive"><i class="bi bi-arrow-up"></i><span>+5%</span></div>
        </div>

        <div class="stat-card">
            <div class="stat-header">
                <span class="stat-title">Total Rooms</span>
                <div class="stat-icon bg-yellow"><i class="bi bi-box text-white"></i></div>
            </div>
            <div class="stat-value">${totalItems}</div>
            <c:if test="${lowStockCount > 0}">
                <div class="stat-change negative"><i class="bi bi-exclamation-triangle"></i><span>${lowStockCount} low stock</span></div>
            </c:if>
        </div>
    </div>


    <!-- ... (remaining part unchanged) ... -->
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>
</body>
</html>