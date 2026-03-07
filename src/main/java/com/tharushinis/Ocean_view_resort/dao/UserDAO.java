package com.tharushinis.Ocean_view_resort.dao;


import com.tharushinis.Ocean_view_resort.model.User;


import java.sql.SQLException;


public interface UserDAO extends GenericDAO<User, Integer> {


    User findByUsernameAndPassword(String username, String password) throws SQLException;


    User findByUsername(String username) throws SQLException;
}