package com.enricot44.publast.service.repository;

import android.os.AsyncTask;

import com.enricot44.publast.service.model.Cocktail;

class SaveCocktailTask extends AsyncTask<Cocktail, Void, Void> {

    private CocktailDao cocktailDao;

    public SaveCocktailTask(CocktailDao cocktailDao) {
        this.cocktailDao = cocktailDao;
    }

    @Override
    protected Void doInBackground(Cocktail... cocktails) {
        cocktailDao.insert(cocktails[0]);
        return null;
    }
}
