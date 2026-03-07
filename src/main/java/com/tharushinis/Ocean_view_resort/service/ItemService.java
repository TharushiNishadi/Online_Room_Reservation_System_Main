package com.tharushinis.Ocean_view_resort.service;


import com.tharushinis.Ocean_view_resort.model.Item;

import java.util.List;


public interface ItemService extends BaseService<Item> {


    List<Item> searchByName(String namePattern) throws Exception;


    List<Item> getAvailableItems() throws Exception;


    boolean updateStock(int itemId, int quantity) throws Exception;


    boolean hasSufficientStock(int itemId, int requiredQuantity) throws Exception;

    List<Item> getLowStockItems() throws Exception;
}