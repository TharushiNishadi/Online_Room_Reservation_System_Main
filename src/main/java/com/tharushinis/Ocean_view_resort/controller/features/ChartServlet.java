package com.tharushinis.Ocean_view_resort.controller.features;

import com.tharushinis.Ocean_view_resort.service.ChartDataService;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ChartServlet", urlPatterns = {"/api/chart"})
public class ChartServlet extends HttpServlet {
    private ChartDataService chartDataService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        chartDataService = new ChartDataService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String chartType = request.getParameter("type");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {
            Object data = null;

            switch (chartType) {
                case "monthly":
                    data = chartDataService.getMonthlySalesData();
                    break;

                case "weekly":
                    data = chartDataService.getWeeklySalesData();
                    break;

                case "topItems":
                    data = chartDataService.getTopSellingItems(5);
                    break;

                case "customerDistribution":
                    data = chartDataService.getCustomerDistribution();
                    break;

                case "dashboard":
                    // Return all chart data for dashboard
                    Map<String, Object> dashboardData = new HashMap<>();
                    dashboardData.put("monthly", chartDataService.getMonthlySalesData());
                    dashboardData.put("weekly", chartDataService.getWeeklySalesData());
                    dashboardData.put("topItems", chartDataService.getTopSellingItems(5));
                    dashboardData.put("customerTypes", chartDataService.getCustomerDistribution());
                    data = dashboardData;
                    break;

                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Invalid chart type");
                    data = error;
            }

            out.print(gson.toJson(data));

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            out.print(gson.toJson(error));
        }

        out.flush();
    }
}
