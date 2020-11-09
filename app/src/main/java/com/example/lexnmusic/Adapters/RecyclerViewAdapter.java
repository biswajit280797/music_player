package com.example.lexnmusic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lexnmusic.R;
import com.example.lexnmusic.Fragments.AboutUsFragment;
import com.example.lexnmusic.Fragments.FavoritesFragment;
import com.example.lexnmusic.Fragments.MainScreenFragment;
import com.example.lexnmusic.Fragments.SettingsFragment;
import com.example.lexnmusic.MainActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private int icon[];
    private String text[];
    private Context context;
    private DrawerLayout drawerLayout;

    public RecyclerViewAdapter(int[] icon, String[] text, Context context, DrawerLayout drawerLayout) {
        this.icon = icon;
        this.text = text;
        this.context = context;
        this.drawerLayout = drawerLayout;
    }

    @NonNull
    @Override

    //Creates the viewholer & puts views in it
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_custom_navigationdrawer, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {

        int icons = icon[position];
        String texts = text[position];
        holder.imageView.setImageResource(icons);
        holder.textView.setText(texts);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    MainScreenFragment mainScreenFragment = new MainScreenFragment();
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.detailsFragment, mainScreenFragment, "MainScreenFragment")
                            .commit();
                } else if (position == 1) {
                    FavoritesFragment favoritesFragment = new FavoritesFragment();
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.detailsFragment, favoritesFragment, "FavoritesFragment")
                            .addToBackStack("SongPlayingFragment")
                            .commit();
                } else if (position == 2) {
                    SettingsFragment settingsFragment = new SettingsFragment();
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.detailsFragment, settingsFragment, "SettingsFragment")
                            .addToBackStack("SongPlayingFragment")
                            .commit();
                } else if (position == 3) {
                    AboutUsFragment aboutUsFragment = new AboutUsFragment();
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.detailsFragment, aboutUsFragment, "FavoritesFragment")
                            .addToBackStack("SongPlayingFragment")
                            .commit();
                }
                drawerLayout.closeDrawers();
            }
        });
    }

    @Override
    public int getItemCount() {
        return icon.length;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public RecyclerViewHolder(@NonNull final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.navigationDrawerIcon);
            textView = itemView.findViewById(R.id.navigationDrawerText);
        }
    }
}