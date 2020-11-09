package com.example.lexnmusic.Adapters;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lexnmusic.R;
import com.example.lexnmusic.CustomClass.Songs;
import com.example.lexnmusic.Fragments.SongPlayingFragment;
import com.example.lexnmusic.R;

import java.util.ArrayList;

public class MainScreenAdapter extends RecyclerView.Adapter<MainScreenAdapter.MainScreenViewHolder> {

    ArrayList<Songs> songsArrayList;
    Context context;

    public MainScreenAdapter(ArrayList<Songs> songsArrayList, Context context) {
        this.songsArrayList = songsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MainScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_songs_adapter, parent, false);
        return new MainScreenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainScreenViewHolder holder, final int position) {

        final Songs songDetails = songsArrayList.get(position);
        holder.songTitle.setText(songDetails.getSongTitle());
        holder.songArtist.setText(songDetails.getSongArtist());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int p= holder.getAdapterPosition();
                Bundle extras = new Bundle();
                SongPlayingFragment songPlayingFragment = new SongPlayingFragment();
                //extras.putInt("Current Position",p); //Sending the current adapter position clicked
                extras.putString("Song Title",songDetails.getSongTitle());//Song Title
                extras.putString("Song Artist",songDetails.getSongArtist());//Song Artist
                extras.putString("Song Path",songDetails.getSongData());//Song Path
                extras.putLong("Song Id",songDetails.getSongId());//Song Id
                extras.putLong("Song Position",holder.getAdapterPosition());//Song Position
                extras.putParcelableArrayList("Parcel Songs", songsArrayList); //Sending the songs details using ArraryList
                if (SongPlayingFragment.mediaPlayer!=null && SongPlayingFragment.mediaPlayer.isPlaying()) extras.putBoolean("PlayerMode", true);
                else extras.putBoolean("PlayerMode", false);
                songPlayingFragment.setArguments(extras); //binding the arguements to the bundle object

                ((FragmentActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detailsFragment, songPlayingFragment, "Song Playing Fragment")
                        .addToBackStack("SongPlayingFragment")
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {

        if (songsArrayList == null) {
            return 0;
        } else return (songsArrayList.size());
    }

    public class MainScreenViewHolder extends RecyclerView.ViewHolder {

        TextView songTitle;
        TextView songArtist;
        RelativeLayout songAdapter;

        public MainScreenViewHolder(@NonNull View itemView) {
            super(itemView);

            songTitle = itemView.findViewById(R.id.titleTrack);
            songArtist = itemView.findViewById(R.id.titleArtist);
            songAdapter = itemView.findViewById(R.id.customRowLayout);
        }
    }
}
