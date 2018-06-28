package com.github.dual_navdrawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static List<String> rightLabel = new ArrayList<>();

    public static List<String> rightIcon = new ArrayList<>();

    public static List<Integer> rightId = new ArrayList<>();

    private DrawerLayout drawer;

    private ActionBarDrawerToggle toggle_left;

    private NavigationView navView_right;

    public static void leftDrawerAction(Context cont, int id) {
        if (id == 0)
            doSomething(cont, "Left 0");
        else if (id == 1)
            doSomething(cont, "Left 1");
        else if (id == 2)
            doSomething(cont, "Left 2");
        else if (id == 3)
            doSomething(cont, "Left 3");
        else if (id == 4)
            doSomething(cont, "Left 4");
        else if (id == 5)
            doSomething(cont, "Left 5");
        else if (id == 6)
            doSomething(cont, "Left 6");
    }

    private static void doSomething(Context cont, String str) {
        Toast.makeText(cont, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar to show
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show no title, might return null. inspect when force close
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e) {
            Log.i("Main Activity", "Title bar failed to show");
            e.printStackTrace();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Show left navigation bar when toggle icon is clicked
        toggle_left = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                // Make the hamburger icon to not changed to arrow icon after the drawer opened
                super.onDrawerSlide(drawerView, 0);

                // When opened, bring options to front so they can be selected
                drawerView.bringToFront();
                drawerView.requestLayout();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Disable the animation of hamburger icon changes to arrow icon
                super.onDrawerSlide(drawerView, 0);
            }
        };
        drawer.addDrawerListener(toggle_left);

        NavigationView navView_left = (NavigationView) findViewById(R.id.nav_view_left);
        navView_left.setNavigationItemSelectedListener(this);

        navView_right = (NavigationView) findViewById(R.id.nav_view_right);
        setRightNavigation();

        View actbar_layout = findViewById(R.id.act_bar_layout);
        View content_layout = actbar_layout.findViewById(R.id.content_layout);
        RelativeLayout bot_RelLayout = (RelativeLayout) content_layout.findViewById(R.id.bot_rel_layout);

        // Do configuration
        Configurator config = new Configurator(this);
        config.setConfig("app.xml");
        config.setConfigHandler(new ConfHandler(this, navView_left, navView_right, bot_RelLayout));
        config.startParse();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle_left.syncState();
    }

    @Override
    public void onBackPressed() {
        // Set action when Back key pressed
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if (drawer.isDrawerOpen(GravityCompat.END))
            drawer.closeDrawer(GravityCompat.END);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        final Menu rightMenu = navView_right.getMenu();
        final SearchView searchView = (SearchView) rightMenu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Close keyboard
                searchView.clearFocus();

                // Don't call the searchable activity
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                rightMenu.removeGroup(R.id.group_right);

                if (!query.equalsIgnoreCase(""))
                    // Loop all function
                    for (int id = 0; id < rightLabel.size(); id++) {
                        String function = rightLabel.get(id);
                        // Check whether the function label matched with query
                        if (function.toLowerCase().matches(query.toLowerCase() + "(.*)"))
                            try {
                                InputStream stream = getApplicationContext().getAssets().open(rightIcon.get(id));
                                rightMenu.add(R.id.group_right, id, Menu.NONE, function).setIcon(Drawable.createFromStream(stream, null)).setCheckable(true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                else // Query.equalsIgnoreCase("")
                    // Loop for default function's menu item
                    for (int id = 0; id < rightId.size(); id++)
                        try {
                            InputStream stream = getApplicationContext().getAssets().open(rightIcon.get(id));
                            rightMenu.add(R.id.group_right, id, Menu.NONE, rightLabel.get(id)).setIcon(Drawable.createFromStream(stream, null)).setCheckable(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                // No need to show the default suggestion
                return true;
            }
        });

        // Catch event on [x] button inside search view
        int closeButtonId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);

        // Get the search close button image view
        ImageView closeButton = (ImageView) searchView.findViewById(closeButtonId);

        // Set close button on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear search bar & close keyboard
                searchView.setQuery("", false);
                searchView.clearFocus();
            }
        });

        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.clearFocus();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        if (item.getItemId() == R.id.action_settings)
            drawer.openDrawer(GravityCompat.END);

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        leftDrawerAction(getApplicationContext(), item.getItemId());

        // Line below is used if the left drawer is expected to close immediately after a menu item is clicked
        /* drawer.closeDrawer(GravityCompat.START); */

        return true;
    }

    // Used for setting the right navigation drawer
    private void setRightNavigation() {
        // Set right navigation to listen on selected option
        navView_right.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == 0)
                    doSomething(getApplicationContext(), "Right 0");
                else if (id == 1)
                    doSomething(getApplicationContext(), "Right 1");
                else if (id == 2)
                    doSomething(getApplicationContext(), "Right 2");
                else if (id == 3)
                    doSomething(getApplicationContext(), "Right 3");
                else if (id == 4)
                    doSomething(getApplicationContext(), "Right 4");
                else if (id == 5)
                    doSomething(getApplicationContext(), "Right 5");
                else if (id == 6)
                    doSomething(getApplicationContext(), "Right 6");

                // Line below is used if the right drawer is expected to close immediately after a menu item is clicked
                /* drawer.closeDrawer(GravityCompat.END); */

                return true;
            }
        });
    }
}
