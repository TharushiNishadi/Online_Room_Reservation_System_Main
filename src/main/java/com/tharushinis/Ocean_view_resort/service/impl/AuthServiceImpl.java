package com.tharushinis.Ocean_view_resort.service.impl;

import com.tharushinis.Ocean_view_resort.dao.UserDAO;
import com.tharushinis.Ocean_view_resort.dao.impl.UserDAOImpl;
import com.tharushinis.Ocean_view_resort.model.User;
import com.tharushinis.Ocean_view_resort.service.AuthService;

public class AuthServiceImpl implements AuthService {

    private UserDAO userDAO;

    public AuthServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public User authenticate(String username, String password) throws Exception {
        // Validate input authenticate
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter your username.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter your password.");
        }
        //find user validate details
        User user = userDAO.findByUsernameAndPassword(username.trim(), password);
        {
            if (user == null) {
                throw new IllegalArgumentException("Login failed. Please check your username and password.");
            }
        }
        return user;

    }

    @Override
    public boolean isUsernameExists(String username) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        User user = userDAO.findByUsername(username.trim());
        return user != null;
    }

    @Override
    public User register(User user) throws Exception {
        // Validate user
        validateUser(user);

        // Check if username already exists
        if (isUsernameExists(user.getUsername())) {
            throw new Exception("Username already exists");
        }
        //Save user

        return userDAO.save(user);

    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            // Basic email validation
            if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }

    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws Exception {
        // Validate input
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Current password cannot be empty");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        // Find user
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new Exception("User not found");
        }

        // Verify old password
        if (!user.getPassword().equals(oldPassword)) {
            throw new Exception("Current password is incorrect");
        }

        // Update password
        user.setPassword(newPassword);
        return userDAO.update(user);
    }
}
