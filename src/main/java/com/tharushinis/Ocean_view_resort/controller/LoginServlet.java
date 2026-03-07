package com.tharushinis.Ocean_view_resort.controller;

import com.tharushinis.Ocean_view_resort.model.User;
import com.tharushinis.Ocean_view_resort.service.AuthService;
import com.tharushinis.Ocean_view_resort.service.impl.AuthServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthServiceImpl();
    }

    // When user opens /login
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // If already logged in, go to dashboard
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/jsp/dashboard.jsp");
            return;
        }

        // Otherwise show login page
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }

    // When user submits login form
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {

            // Authenticate user
            User user = authService.authenticate(username, password);

            // Create session
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("fullName", user.getFullName());

            // Session timeout (30 minutes)
            session.setMaxInactiveInterval(30 * 60);

            // Redirect to dashboard
            response.sendRedirect(request.getContextPath() + "/jsp/dashboard.jsp");

        } catch (Exception e) {

            // Send error message back to login page
            request.setAttribute("error", e.getMessage());
            request.setAttribute("username", username);

            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}