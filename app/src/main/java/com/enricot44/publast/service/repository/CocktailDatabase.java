package com.enricot44.publast.service.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.enricot44.publast.service.model.Cocktail;
import com.enricot44.publast.utilities.Converter;

@Database(entities = {Cocktail.class}, version = 2, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class CocktailDatabase extends RoomDatabase {

    private static CocktailDatabase cocktailDatabase;

    public abstract CocktailDao cocktailDao();

    public static CocktailDatabase getDatabase(final Context context) {
        if (cocktailDatabase == null) {
            synchronized (CocktailDatabase.class) {
                if (cocktailDatabase == null) {
                    cocktailDatabase = Room.databaseBuilder(context.getApplicationContext(), CocktailDatabase.class, "cocktail_database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return cocktailDatabase;
    }
}
