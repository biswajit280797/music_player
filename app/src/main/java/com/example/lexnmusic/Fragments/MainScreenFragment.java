package com.example.lexnmusic.Fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.lexnmusic.Adapters.MainScreenAdapter;
import com.example.lexnmusic.CustomClass.Songs;
import com.example.lexnmusic.R;
import java.util.ArrayList;
import java.util.Collections;

public class MainScreenFragment extends Fragment {
    ArrayList<Songs> songsList;
    RelativeLayout nowPlayingBottomBar;
    RelativeLayout visibleLayout;
    ImageButton playPauseButton;
    TextView nowPlaying;
    TextView songTitle;
    ImageView nowPlayingBarEqualizer;
    RelativeLayout noSongs;
    RecyclerView recyclerView;
    Activity myActivity;
    MainScreenAdapter mainScreenAdapter;
    public static MediaPlayer mainScreenMediaPlayer;
    public static int trackPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
        nowPlayingBottomBar = view.findViewById(R.id.nowPlayingBottomBar);
        playPauseButton = view.findViewById(R.id.playPauseButton);
        nowPlaying = view.findViewById(R.id.nowPlaying);
        songTitle = view.findViewById(R.id.songTitle);
        nowPlayingBarEqualizer = view.findViewById(R.id.nowPlayingBarEqualizer);
        noSongs = view.findViewById(R.id.noSongs);
        recyclerView = view.findViewById(R.id.allSongs);
        visibleLayout = view.findViewById(R.id.visibleLayout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        songsList = getSongsFromPhone();
        SharedPreferences getShared = myActivity.getSharedPreferences("action_sort", Context.MODE_PRIVATE);
        Boolean byName = getShared.getBoolean("Sort By Name", false);
        Boolean byDateAdded = getShared.getBoolean("Sort By DateAdded", false);

        if (songsList == null) {
            noSongs.setVisibility(View.VISIBLE);
            visibleLayout.setVisibility(View.INVISIBLE);
        } else {
            mainScreenAdapter = new MainScreenAdapter(songsList, myActivity);
            recyclerView.setLayoutManager(new LinearLayoutManager(myActivity));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mainScreenAdapter.notifyDataSetChanged();
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(mainScreenAdapter);
        }

        if (songsList != null) {
            if (byName == true) {
                Collections.sort(songsList, Songs.nameComparator);
                mainScreenAdapter.notifyDataSetChanged();
            } else if (byDateAdded == true) {
                Collections.sort(songsList, Songs.dateComparator);
                mainScreenAdapter.notifyDataSetChanged();
            }
        }
        bottomBar();
    }

    @Override
    public void onCreateOptionsMenu( Menu menu,MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_screen_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sortByName) {
            SharedPreferences shrd = myActivity.getSharedPreferences("action_sort", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shrd.edit();
            editor.putBoolean("Sort By Name", true);
            editor.putBoolean("Sort By DateAdded", false);
            editor.apply();
            if (songsList != null) Collections.sort(songsList, Songs.nameComparator);
            mainScreenAdapter.notifyDataSetChanged();
        } else if (item.getItemId() == R.id.sortByRecentlyAdded) {
            SharedPreferences shrd = myActivity.getSharedPreferences("action_sort", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shrd.edit();
            editor.putBoolean("Sort By Name", false);
            editor.putBoolean("Sort By DateAdded", true);
            editor.apply();
            if (songsList != null) Collections.sort(songsList, Songs.dateComparator);
            mainScreenAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
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
        //if (SongPlayingFragment.mediaPlayer != null && SongPlayingFragment.mediaPlayer.isPlaying())
        //SongPlayingFragment.mediaPlayer.start();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public ArrayList<Songs> getSongsFromPhone() { //Function to fetch songs from phonr

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
        }
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
                nowPlayingBottomBar.setVisibility(View.VISIBLE);
                playPauseButton.setBackgroundResource(R.drawable.pause_icon);
                SongPlayingFragment.mediaPlayer.start();
            } else {
                nowPlayingBottomBar.setVisibility(View.VISIBLE);
                playPauseButton.setBackgroundResource(R.drawable.play_icon);
                SongPlayingFragment.mediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBottomBarClick() {

        nowPlayingBottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SongPlayingFragment.songHelper._songId==null) return;
                mainScreenMediaPlayer = SongPlayingFragment.mediaPlayer;
                SongPlayingFragment songPlayingFragment = new SongPlayingFragment();
                Bundle extras = new Bundle();
                extras.putString("Song Title", SongPlayingFragment.songHelper._songTitle);//Song Title
                extras.putString("Song Artist", SongPlayingFragment.songHelper._songArtist);//Song Artist
                extras.putString("Song Path", SongPlayingFragment.songHelper._songData);//Song Path
                extras.putLong("Song Id", SongPlayingFragment.songHelper._songId);//Song Id
                extras.putLong("Song Position", SongPlayingFragment.songHelper._currentPosition);//Song Position
                extras.putParcelableArrayList("Parcel Songs", SongPlayingFragment.songsArrayList); //Sending the songs details using ArraryList
                extras.putString("MainScreenBottomBar", "success");
                if (mainScreenMediaPlayer != null && mainScreenMediaPlayer.isPlaying())
                    extras.putBoolean("MainScreenPlayerState", true);
                else extras.putBoolean("MainScreenPlayerState", false);
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

                if (SongPlayingFragment.mediaPlayer != null) {
                    if (SongPlayingFragment.mediaPlayer.isPlaying()) {
                        SongPlayingFragment.mediaPlayer.pause();
                        trackPosition = SongPlayingFragment.mediaPlayer.getCurrentPosition();
                        playPauseButton.setBackgroundResource(R.drawable.play_icon);
                    } else {
                        trackPosition = SongPlayingFragment.mediaPlayer.getCurrentPosition();
                        SongPlayingFragment.mediaPlayer.seekTo(trackPosition);
                        SongPlayingFragment.mediaPlayer.start();
                        playPauseButton.setBackgroundResource(R.drawable.pause_icon);
                    }
                }
            }
        });
    }
}
