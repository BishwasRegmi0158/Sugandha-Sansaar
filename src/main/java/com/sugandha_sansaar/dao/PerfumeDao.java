package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Perfume;
import java.sql.SQLException;
import java.util.List;


public interface PerfumeDao {

    boolean addPerfume(Perfume perfume) throws SQLException;

    List<Perfume> getAllPerfumes() throws SQLException;

    Perfume getPerfumeById(int id) throws SQLException;

    boolean updatePerfume(Perfume perfume) throws SQLException;

    boolean updateStock(int perfumeId, int newStock) throws SQLException;

    boolean deletePerfume(int id) throws SQLException;

    int getTotalPerfumes() throws SQLException;

    int getOutOfStockCount() throws SQLException;

    int getLowStockCount() throws SQLException;

    int getTotalBrands() throws SQLException;

    List<String> getAllCategories() throws SQLException;

    List<String> getAllBrands() throws SQLException;
}