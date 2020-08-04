package com.enricot44.publast.service.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.enricot44.publast.service.model.Cocktail;

import java.util.List;

public class CocktailRepository {
    private CocktailDao cocktailDao;
    private LiveData<List<Cocktail>> allCocktails;

    public CocktailRepository(Application application) {
        CocktailDatabase db = CocktailDatabase.getDatabase(application);

        cocktailDao = db.cocktailDao();
        allCocktails = cocktailDao.getAllCocktails();
    }

    public LiveData<List<Cocktail>> getAllCocktails() {
        return allCocktails;
    }

    public void insert(Cocktail cocktail) {
        new SaveCocktailTask(cocktailDao).execute(cocktail);
    }

    public void delete(Cocktail cocktail) {
        cocktailDao.deleteCocktail(cocktail);
    }

    public void deleteAllCocktails() {
        cocktailDao.deleteCocktails();
    }

    public Cocktail getCocktail(Cocktail cocktail) {
        return cocktailDao.getCocktail(cocktail.getName());
    }
}
