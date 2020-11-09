package com.example.lexnmusic;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lexnmusic.Adapters.RecyclerViewAdapter;
import com.example.lexnmusic.Fragments.MainScreenFragment;
import com.example.lexnmusic.Fragments.SongPlayingFragment;

public class MainActivity extends AppCompatActivity {

     RecyclerView recyclerView;
     RecyclerViewAdapter recyclerViewAdapter;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //navigation drawer texts
        int navDrawerIcon[] = {R.drawable.navigation_allsongs,
                R.drawable.navigation_favorites,
                R.drawable.navigation_settings,
                R.drawable.navigation_aboutus};

        String navDrawerText[] = {"All Songs", "Favorites", "Settings", "About Us"};
        drawer = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        recyclerView=findViewById(R.id.navigationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapter = new RecyclerViewAdapter(navDrawerIcon, navDrawerText, MainActivity.this, drawer);
        recyclerViewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerViewAdapter);

        MainScreenFragment mainScreenFragment = new MainScreenFragment();
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detailsFragment, mainScreenFragment, "MainScreenFragment")
                .commit();
    }
}





