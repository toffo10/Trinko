package com.enricot44.publast.service.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.enricot44.publast.service.model.Cocktail;

import java.util.List;

@Dao
public interface CocktailDao {

    @Insert
    void insert(Cocktail cocktail);

    @Query("DELETE FROM cocktail")
    void deleteAll();

    @Query("SELECT * from cocktail ORDER BY name ASC")
    LiveData<List<Cocktail>> getAllCocktails();

    @Query("SELECT * from cocktail WHERE name=:name")
    Cocktail getCocktail(String name);

    @Delete
    void deleteCocktail(Cocktail cocktail);

    @Query("DELETE FROM cocktail")
    void deleteCocktails();
}
