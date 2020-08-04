package com.enricot44.publast.view.activity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.enricot44.publast.R;
import com.enricot44.publast.databinding.ActivityMainBinding;
import com.enricot44.publast.service.RememberToDrink;
import com.enricot44.publast.service.model.Cocktail;
import com.enricot44.publast.utilities.IOUtils;
import com.enricot44.publast.utilities.ViewUtils;
import com.enricot44.publast.view.adapter.CocktailAdapter;
import com.enricot44.publast.view.viewmodel.CocktailViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.transition.Fade;
import android.util.Pair;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private CocktailViewModel cocktailViewModel;

    private CocktailAdapter cocktailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPub();
            }
        });
        setUpActionBar();
        initializeRV();

        cocktailViewModel = ViewModelProviders.of(this).get(CocktailViewModel.class);
        cocktailViewModel.getAllCocktails().observe(this, new Observer<List<Cocktail>>() {
            @Override
            public void onChanged(final List<Cocktail> cocktails) {
                cocktailAdapter.setCocktails(cocktails);
            }
        });
    }

    private void initializeRV() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Cocktail cocktailToDelete = cocktailAdapter.getItems().get(position);
                cocktailViewModel.delete(cocktailToDelete);
                ViewUtils.showSnackbar(getBaseContext(), binding.mainCoordinatorLayout, R.string.cocktail_removed_from_favourites, new ViewUtils.OnUndo() {
                    @Override
                    public void undo() {
                        cocktailViewModel.insert(cocktailToDelete);
                    }
                });
            }
        });

        cocktailAdapter = new CocktailAdapter(this, new ArrayList<Cocktail>());
        cocktailAdapter.addClickItemImplementation(new CocktailAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View clickedView) {
                goToCocktailDetail(cocktailAdapter.getItems().get(position), clickedView);
            }
        });

        binding.contentMainId.rvSavedPubs.setAdapter(cocktailAdapter);
        binding.contentMainId.rvSavedPubs.setLayoutManager(new LinearLayoutManager(this));
        itemTouchHelper.attachToRecyclerView(binding.contentMainId.rvSavedPubs);
    }

    private void setUpActionBar() {
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.mainDrawerLayout, binding.toolbar, R.string.drawer_open, R.string.drawer_closed);
        binding.mainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_clear_all_cocktails:
                        showWarningBeforeDeletingAllCocktails();
                        return true;
                }
                return false;
            }
        });
    }

    private void showWarningBeforeDeletingAllCocktails() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.warning)
                .setMessage(R.string.delete_all_cocktails)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cocktailViewModel.deleteAllCocktails();
                        binding.mainDrawerLayout.close();
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void goToCocktailDetail(Cocktail cocktail, View clickedView) {
        getWindow().setExitTransition(new Fade());

        Intent cocktailDetailIntent = new Intent(this, CocktailDetail.class);
        cocktailDetailIntent.putExtra("Cocktail", (Parcelable) cocktail);
        cocktailDetailIntent.putExtra("ImageURL", IOUtils.saveImageAsFile(this, cocktail.getThumbnail()));

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(clickedView.findViewById(R.id.rv_item_saved_cocktail_image), "cocktailImage"));

        startActivity(cocktailDetailIntent, options.toBundle());
    }

    private void searchPub() {
        Intent searchPubIntent = new Intent(this, SearchCocktail.class);
        startActivity(searchPubIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.remember_to_drink) {
            Intent myService = new Intent(this, RememberToDrink.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(myService);
            } else {
                startService(myService);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}