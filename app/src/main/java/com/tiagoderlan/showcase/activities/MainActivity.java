package com.tiagoderlan.showcase.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.tiagoderlan.showcase.R;
import com.tiagoderlan.showcase.database.DatabaseHelper;
import com.tiagoderlan.showcase.fragments.MenuFragment;
import com.tiagoderlan.showcase.fragments.ProductsFragment;
import com.tiagoderlan.showcase.interfaces.OnFilteredCallback;
import com.tiagoderlan.showcase.interfaces.OnUpdateCallback;
import com.tiagoderlan.showcase.models.Category;
import com.tiagoderlan.showcase.tasks.enums.GetDataTaskResult;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnUpdateCallback, OnFilteredCallback
{
    private Stack<Fragment> popStack = new Stack<Fragment>();

    private int popStackIndex = 0;

    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle toggle;

    private Fragment currentFragment;

    private MenuFragment menuFragment;

    private Snackbar snack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        setUpLanding();
        setUpMenu();

        setUpDrawerToggle();
    }

    private void setUpMenu()
    {
        menuFragment = new MenuFragment();
        menuFragment.setOnFilteredCallback(this);
        switchMenu(menuFragment);
    }

    private void setUpLanding()
    {
        currentFragment = new ProductsFragment();
        ((ProductsFragment)currentFragment).setCallback(this);
        stackFragment(currentFragment);

    }

    private void setUpDrawerToggle()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toggle = new ActionBarDrawerToggle (
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
            {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            };

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);

                //((DrawableFragment)currentFragment).updateMenu(slideOffset);

            }
        };

        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
        drawerLayout.setScrimColor(Color.TRANSPARENT);

        toggle.syncState();
    }

    public void switchFragment(Fragment fragment)
    {
        currentFragment = fragment;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack("current");
        transaction.replace(R.id.content_frame, currentFragment, "current").commit();
    }

    public void switchMenu(MenuFragment fragment)
    {
        menuFragment = fragment;

        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, menuFragment, "menu").commit();
    }

    public void stackFragment(Fragment fragment)
    {
        popStack.add(fragment);

        switchFragment(fragment);

        popStackIndex++;
    }


    @Override
    public void onBackPressed()
    {
        popStack();
    }

    public void popStack()
    {
        FragmentManager fm = getSupportFragmentManager();

        popStackIndex--;

        if(popStackIndex == 0)
        {
            popStackIndex = 1;

            endOfStackDialog();
        }
        else
        {
            popStack.pop();
            currentFragment = popStack.get(popStackIndex - 1);
            fm.popBackStackImmediate();
        }

    }

    private void endOfStackDialog()
    {
        if(snack == null || !snack.isShown())
        {
            snack = Snackbar.make(findViewById(R.id.content_frame), "Aperte novamente para sair", Snackbar.LENGTH_LONG);

            snack.show();;
        }
        else if (snack.isShown())
        {
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.action_wipe)
        {
            DatabaseHelper.getInstance(this).clearTables();

            ((ProductsFragment)currentFragment).loadList();

            menuFragment.update();
        }

        return toggle != null && toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updated(GetDataTaskResult result)
    {

        switch (result)
        {
            case Connection:
                Snackbar.make(findViewById(R.id.content_frame), "Problemas com a conexão :(", Snackbar.LENGTH_LONG).show();
                break;
            case Failed:
                Snackbar.make(findViewById(R.id.content_frame), "Aconteceu algum problema :(", Snackbar.LENGTH_LONG).show();
                break;
        }




        menuFragment.update();
    }

    @Override
    public void onFilterSelect(Category category)
    {
        ((ProductsFragment)currentFragment).setCategory(category);

        drawerLayout.closeDrawers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ((ProductsFragment)currentFragment).loadList();


        ((ProductsFragment)currentFragment).setCategory(menuFragment.getCategory());
    }
}
