package com.enricot44.publast.view.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.enricot44.publast.service.model.Cocktail;
import com.enricot44.publast.service.repository.CocktailRepository;

import java.util.List;

public class CocktailViewModel extends AndroidViewModel {
    private CocktailRepository mRepository;

    private LiveData<List<Cocktail>> mCocktails;

    public CocktailViewModel(Application application) {
        super(application);
        mRepository = new CocktailRepository(application);
        mCocktails = mRepository.getAllCocktails();
    }

    public LiveData<List<Cocktail>> getAllCocktails() {
        return mCocktails;
    }

    public void insert(Cocktail cocktail) {
        mRepository.insert(cocktail);
    }

    public boolean cocktailExists(Cocktail cocktail) {
        Cocktail selectedCocktail = mRepository.getCocktail(cocktail);

        if (selectedCocktail != null) {
            return true;
        } else {
            return false;
        }
    }

    public void delete(Cocktail cocktail) {
        mRepository.delete(cocktail);
    }

    public void deleteAllCocktails() {
        mRepository.deleteAllCocktails();
    }
}
