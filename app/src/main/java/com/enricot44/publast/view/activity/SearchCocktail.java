package com.enricot44.publast.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.transition.Fade;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.enricot44.publast.R;
import com.enricot44.publast.databinding.ActivitySearchPubBinding;
import com.enricot44.publast.view.adapter.CocktailAdapter;
import com.enricot44.publast.service.model.Cocktail;
import com.enricot44.publast.utilities.IOUtils;
import com.enricot44.publast.service.repository.SearchCocktailTask;

import java.util.ArrayList;


public class SearchCocktail extends AppCompatActivity {

    ActivitySearchPubBinding binding;

    private ArrayList<Cocktail> cocktailList = new ArrayList<>();
    private CocktailAdapter cocktailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_pub);
        setSupportActionBar(binding.searchPubToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.contentSearchCocktailLayout.searchCocktailName.requestFocus();

        binding.contentSearchCocktailLayout.searchCocktailName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            searchCocktail(binding.contentSearchCocktailLayout.searchCocktailName.getText().toString());
                            break;
                    }
                }

                return false;
            }
        });

        loadDataToRV(cocktailList);
    }

    private void loadDataToRV(final ArrayList<Cocktail> cocktailList) {
        cocktailAdapter = new CocktailAdapter(this, cocktailList);
        binding.contentSearchCocktailLayout.searchCocktailRv.setAdapter(cocktailAdapter);
        binding.contentSearchCocktailLayout.searchCocktailRv.setLayoutManager(new LinearLayoutManager(this));
        cocktailAdapter.addClickItemImplementation(new CocktailAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View clickedView) {
                Cocktail cocktail = cocktailAdapter.getItems().get(position);
                goToCocktailDetail(cocktail, clickedView);
            }
        });
        cocktailAdapter.notifyDataSetChanged();
    }

    private void goToCocktailDetail(Cocktail cocktail, View clickedView) {
        getWindow().setExitTransition(new Fade());

        Intent cocktailDetailIntent = new Intent(this, CocktailDetail.class);
        cocktailDetailIntent.putExtra("Cocktail", (Parcelable) cocktail);
        cocktailDetailIntent.putExtra("ImageURL", IOUtils.saveImageAsFile(this, cocktail.getThumbnail()));

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(clickedView.findViewById(R.id.rv_item_saved_cocktail_image), "cocktailImage"));

        startActivity(cocktailDetailIntent, options.toBundle());
    }

    private void searchCocktail(String cocktailName) {
        new SearchCocktailTask(cocktailAdapter).execute(cocktailName);

        binding.contentSearchCocktailLayout.searchCocktailRv.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.contentSearchCocktailLayout.searchCocktailTil.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 0);
        binding.contentSearchCocktailLayout.searchCocktailTil.setLayoutParams(layoutParams);
    }

    public void clearText(View view) {
        binding.contentSearchCocktailLayout.searchCocktailName.setText("");
        binding.contentSearchCocktailLayout.searchCocktailName.requestFocus();
    }
}