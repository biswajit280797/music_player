package com.example.lexnmusic.Fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.lexnmusic.Adapters.FavoritesAdapter;
import com.example.lexnmusic.CustomClass.Songs;
import com.example.lexnmusic.Database.FavoriteDatabase;
import com.example.lexnmusic.MainActivity;
import com.example.lexnmusic.R;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    public static RelativeLayout noFavorites;
    public RelativeLayout favoritesHiddenBar;
    public RelativeLayout visibleLayout;
    TextView songTitle;
    ImageView playPauseButton;
    public static  RecyclerView favoritesRecyclerView;
    Activity myActivity;
    ArrayList<Songs> refreshList = new ArrayList<Songs>();
    ArrayList<Songs> fetchListFromDevice;
    ArrayList<Songs> getListFromDatabase;
    public static MediaPlayer mEdiaPlayer;
    public static int trackPosition;
    FavoritesAdapter favoritesAdapter;
    FavoriteDatabase favoriteDatabase;


    public MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        noFavorites = view.findViewById(R.id.noFavorites);
        favoritesHiddenBar = view.findViewById(R.id.favoritesHiddenBar);
        visibleLayout = view.findViewById(R.id.visibleLayout);
        songTitle = view.findViewById(R.id.songTitle);
        playPauseButton = view.findViewById(R.id.playPauseButton);
        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Songs> favoriteSongs=FavoriteDatabase.retriveDataFromFirebase();
        //displayFavoritesBySearch();
        if (favoriteSongs == null) {
            favoritesRecyclerView.setVisibility(View.INVISIBLE);
            noFavorites.setVisibility(View.VISIBLE);
        } else {
            favoritesAdapter = new FavoritesAdapter(favoriteSongs, myActivity);

            favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(myActivity));
            favoritesRecyclerView.setItemAnimator(new DefaultItemAnimator());
            favoritesAdapter.notifyDataSetChanged();
            favoritesRecyclerView.setHasFixedSize(true);
            favoritesRecyclerView.setAdapter(favoritesAdapter);
        }
        bottomBar();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        myActivity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //Get Songs From Phone
    public ArrayList<Songs> getSongsFromPhone() {

        ArrayList<Songs> arrayList = new ArrayList<Songs>();
        ContentResolver contentResolver = myActivity.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int dateAdded = songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);

            while (songCursor.moveToNext()) {
                Long currentId = songCursor.getLong(songId);
                Long currentDateAdded = songCursor.getLong(dateAdded);
                String currentSongTitle = songCursor.getString(songTitle);
                String currentSongArtist = songCursor.getString(songArtist);
                String currentSongData = songCursor.getString(songData);

                arrayList.add(new Songs(currentId, currentSongTitle, currentSongArtist, currentSongData, currentDateAdded));
            }
        } else return null;

        return arrayList;
    }

    public void bottomBar() { //Bottom bar code
        try {
            onBottomBarClick();
            songTitle.setText(SongPlayingFragment.songHelper._songTitle);
            SongPlayingFragment.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    songTitle.setText(SongPlayingFragment.songHelper._songTitle);
                    SongPlayingFragment.onSongComplete();
                }
            });

            if (SongPlayingFragment.mediaPlayer.isPlaying()) {
                playPauseButton.setBackgroundResource(R.drawable.pause_icon);
                favoritesHiddenBar.setVisibility(View.VISIBLE);
            } else {
                favoritesHiddenBar.setVisibility(View.VISIBLE);
                playPauseButton.setBackgroundResource(R.drawable.play_icon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBottomBarClick() {
        favoritesHiddenBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SongPlayingFragment.songHelper._songId == null) return;
                mEdiaPlayer = SongPlayingFragment.mediaPlayer;
                Bundle extras = new Bundle();
                SongPlayingFragment songPlayingFragment = new SongPlayingFragment();
                extras.putString("Song Title", SongPlayingFragment.songHelper._songTitle);//Song Title
                extras.putString("Song Artist", SongPlayingFragment.songHelper._songArtist);//Song Artist
                extras.putString("Song Path", SongPlayingFragment.songHelper._songData);//Song Path
                extras.putLong("Song Id", SongPlayingFragment.songHelper._songId);//Song Id
                extras.putLong("Song Position", SongPlayingFragment.songHelper._currentPosition);//Song Position
                extras.putParcelableArrayList("Parcel Songs", SongPlayingFragment.songsArrayList); //Sending the songs details using ArraryList
                extras.putString("FavBottomBar", "success");
                songPlayingFragment.setArguments(extras); //binding the arguements to the bundle object

                getFragmentManager().beginTransaction()
                        .replace(R.id.detailsFragment, songPlayingFragment, "SongPlayingFragment")
                        .addToBackStack("SongPlayingFragment")
                        .commit();
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SongPlayingFragment.mediaPlayer.isPlaying()) {
                    SongPlayingFragment.mediaPlayer.pause();
                    trackPosition = SongPlayingFragment.mediaPlayer.getCurrentPosition();
                    playPauseButton.setBackgroundResource(R.drawable.play_icon);
                } else {
                    SongPlayingFragment.mediaPlayer.seekTo(trackPosition);
                    SongPlayingFragment.mediaPlayer.start();
                    playPauseButton.setBackgroundResource(R.drawable.pause_icon);
                }
            }
        });
    }

    //Display favorites by searching
    /**public void displayFavoritesBySearch() {
        if (favoriteDatabase.checkSize() > 0) {
            getListFromDatabase = favoriteDatabase.queryDBList();
            fetchListFromDevice = getSongsFromPhone();
            if (fetchListFromDevice != null && getListFromDatabase != null) {
                for (int i = 0; i < getListFromDatabase.size(); i++) {
                    for (int j = 0; j < fetchListFromDevice.size(); j++) {
                        if (((getListFromDatabase.get(i)).getSongId()) == (fetchListFromDevice.get(j).getSongId()))
                            refreshList.add(getListFromDatabase.get(i));
                    }
                }
            } else ;

            if (refreshList == null) {
                //favoritesRecyclerView.setVisibility(View.INVISIBLE);
                noFavorites.setVisibility(View.VISIBLE);
            } else {
                favoritesAdapter = new FavoritesAdapter(refreshList, myActivity);
                favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(myActivity));
                favoritesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                favoritesAdapter.notifyDataSetChanged();
                favoritesRecyclerView.setHasFixedSize(true);
                favoritesRecyclerView.setAdapter(favoritesAdapter);
            }

        } else {
            //visibleLayout.setVisibility(View.INVISIBLE);
            favoritesRecyclerView.setVisibility(View.INVISIBLE);
            noFavorites.setVisibility(View.VISIBLE);
        }
    }*/
}
