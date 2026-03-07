package com.tharushinis.Ocean_view_resort.dao;


import com.tharushinis.Ocean_view_resort.model.Item;

import java.sql.SQLException;
import java.util.List;



public interface ItemDAO extends GenericDAO<Item, Integer> {


    List<Item> findByNamePattern(String namePattern) throws SQLException;


    List<Item> findAvailableItems() throws SQLException;


    boolean updateStock(int itemId, int quantity) throws SQLException;
}