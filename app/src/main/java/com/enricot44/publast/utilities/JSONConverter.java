package com.enricot44.publast.utilities;

import com.enricot44.publast.service.model.Cocktail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONConverter {
    public static ArrayList<Cocktail> retrieveCocktailsByJSON(String input) {
        ArrayList<Cocktail> cocktails = new ArrayList<>();

        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(input);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("drinks");

            // Initialize iterator and results fields.
            int i = 0;

            // Look for results in the items array, exiting
            // when both the title and author
            // are found or when all items have been checked.
            while (i < itemsArray.length()) {
                // Get the current item information.
                JSONObject drink = itemsArray.getJSONObject(i);

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {

                    String ingredients = "";

                    String name = null;
                    String category = null;
                    String thumbnailUrl = null;
                    String instruction = null;

                    Cocktail cocktail = new Cocktail();

                    name = drink.getString("strDrink");
                    category = drink.getString("strCategory");
                    thumbnailUrl = drink.getString("strDrinkThumb");
                    instruction = drink.getString("strInstructions");

                    for (int j = 1; j <= 15; j++) {
                        if (drink.getString("strMeasure" + j) != "null") {
                            ingredients = ingredients.concat(", " + drink.getString("strMeasure" + j));
                        }

                        if (drink.getString("strIngredient" + j) != "null") {
                            ingredients = ingredients.concat(drink.getString("strIngredient" + j));
                        }
                    }
                    // Remove first comma and space
                    ingredients = ingredients.substring(2);

                    cocktail.setName(name);
                    cocktail.setCategory(category);
                    cocktail.setThumbnailUrl(thumbnailUrl);
                    cocktail.setInstruction(instruction);
                    cocktail.setIngredients(ingredients);

                    cocktails.add(cocktail);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }


        } catch (Exception e) {
        }

        return cocktails;
    }
}
