package com.enricot44.publast.view.activity;

import android.os.Bundle;

import com.enricot44.publast.R;
import com.enricot44.publast.databinding.ActivityCocktailDetailBinding;
import com.enricot44.publast.service.model.Cocktail;
import com.enricot44.publast.utilities.IOUtils;
import com.enricot44.publast.utilities.ViewUtils;
import com.enricot44.publast.view.viewmodel.CocktailViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.transition.Fade;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class CocktailDetail extends AppCompatActivity {

    private Cocktail cocktail;
    private ActivityCocktailDetailBinding binding;

    private CocktailViewModel cocktailViewModel = new CocktailViewModel(getApplication());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cocktail_detail);
        setSupportActionBar(binding.toolbar);

        cocktail = getIntent().getParcelableExtra("Cocktail");

        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());

        setAlphaAnimation(0, 1, binding.fab);

        populateHeader(cocktail);
        populateBody(cocktail);
        checkIfIsStarred();
    }


    private void checkIfIsStarred() {
        if (cocktailViewModel.cocktailExists(cocktail)) {
            binding.fab.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on));
        } else {
            binding.fab.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off));
        }
    }

    private void populateHeader(Cocktail cocktail) {
        cocktail.setThumbnail(IOUtils.retrieveImageFromFile(this, getIntent().getStringExtra("ImageURL")));
        binding.imageToolbar.setImageBitmap(cocktail.getThumbnail());
        binding.toolbarLayout.setTitle(cocktail.getName());
    }


    private void populateBody(Cocktail cocktail) {
        binding.idContentCocktailDetail.cocktailDetailHeaderIngredients.setText(R.string.Ingredients);
        binding.idContentCocktailDetail.cocktailDetailBodyIngredients.setText(cocktail.getIngredients());
        binding.idContentCocktailDetail.cocktailDetailHeaderInstruction.setText(R.string.Instruction);
        binding.idContentCocktailDetail.cocktailDetailBodyInstruction.setText(cocktail.getInstruction());
    }

    private void setAlphaAnimation(int firstAlpha, int lastAlpha, View view) {
        AlphaAnimation animation1 = new AlphaAnimation(firstAlpha, lastAlpha);
        animation1.setDuration(500);
        animation1.setFillAfter(true);
        view.startAnimation(animation1);
    }

    public void saveCocktail(View view) {
        if (cocktailViewModel.cocktailExists(cocktail)) {
            binding.fab.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off));
            cocktailViewModel.delete(cocktail);
            ViewUtils.showSnackbar(this, binding.cocktailDetailCoordinatorLayout, R.string.cocktail_removed_from_favourites, null);
        } else {
            binding.fab.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on));
            cocktailViewModel.insert(cocktail);
            ViewUtils.showSnackbar(this, binding.cocktailDetailCoordinatorLayout, R.string.cocktail_added_to_favourites, null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setAlphaAnimation(1, 0, binding.fab);
    }
}