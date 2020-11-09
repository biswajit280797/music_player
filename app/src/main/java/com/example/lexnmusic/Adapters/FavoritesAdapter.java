package com.example.lexnmusic.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lexnmusic.CustomClass.Songs;
import com.example.lexnmusic.Fragments.SongPlayingFragment;
import com.example.lexnmusic.R;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavScreenViewHolder> {
    ArrayList<Songs> songsArrayList;
    Context context;

    public FavoritesAdapter(ArrayList<Songs> songsArrayList, Context context) {
        this.songsArrayList = songsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_songs_adapter, parent, false);
        return new FavScreenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavScreenViewHolder holder, final int position) {
        // final Songs songDetails = songsArrayList.get(position);
        //holder.songTitle.setText(songDetails.getSongTitle());
        //holder.songArtist.setText(songDetails.getSongArtist());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int p= holder.getAdapterPosition();
                Bundle extras = new Bundle();
                SongPlayingFragment songPlayingFragment = new SongPlayingFragment();
                //extras.putInt("Current Position",p); //Sending the current adapter position clicked
                // extras.putString("Song Title", songDetails.getSongTitle());//Song Title
                // extras.putString("Song Artist", songDetails.getSongArtist());//Song Artist
                //extras.putString("Song Path", songDetails.getSongData());//Song Path
                // extras.putLong("Song Id", songDetails.getSongId());//Song Id
                //extras.putLong("Song Position", position);//Song Position

                extras.putParcelableArrayList("Parcel Songs", songsArrayList); //Sending the songs details using ArraryList
                songPlayingFragment.setArguments(extras); //binding the arguements to the bundle object

                ((FragmentActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detailsFragment, songPlayingFragment, "SongPlayingFragment")
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (songsArrayList.size());
    }

    public class FavScreenViewHolder extends RecyclerView.ViewHolder {

        TextView songTitle;
        TextView songArtist;
        RelativeLayout songAdapter;

        public FavScreenViewHolder(@NonNull View itemView) {
            super(itemView);

            songTitle = itemView.findViewById(R.id.favtitleTrack);
            songArtist = itemView.findViewById(R.id.favtitleArtist);
            songAdapter = itemView.findViewById(R.id.favRowLayout);
        }
    }
}

