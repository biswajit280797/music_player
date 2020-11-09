package com.example.lexnmusic.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.cleveroad.audiovisualization.VisualizerDbmHandler;
import com.example.lexnmusic.CustomClass.SongHelper;
import com.example.lexnmusic.CustomClass.Songs;
import com.example.lexnmusic.Database.FavoriteDatabase;
import com.example.lexnmusic.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SongPlayingFragment extends Fragment {

    public static AudioVisualization audioVisualization;
    public static View view;
    public static Boolean shuffleModeValue;
    public static String shuffleMode = "ShuffleMode";
    public static String loopMode = "LoopMode";
    public static SharedPreferences getShuffleMode;
    public static VisualizerDbmHandler vizualizerHandler;
    public static SharedPreferences getLoopMode;
    public Boolean loopModeValue;
    public static GLAudioVisualizationView glView;
    public static MediaPlayer mediaPlayer;
    public static int currentPosition;
    public static SongHelper songHelper = new SongHelper();
    public static TextView startTimeText;
    public static TextView endTimeText;
    public static ImageButton playPauseButton;
    public static ImageButton previousButton;
    public static ImageButton nextButton;
    public static ImageButton shuffleButton;
    public static ImageButton loopButton;
    public static SeekBar seekBar;
    public static TextView titleArtist;
    public static TextView titleSong;
    public static ArrayList<Songs> songsArrayList;
    public static Songs songDetails;
    public static Activity myActivity;
    public static int randomPosition;
    public static Long startTime;
    public static ImageView fab;
    public static SharedPreferences loopEditor;
    public static SharedPreferences shuffleEditor;
    //OnActivityCreated Song variables
    public static Long songId;
    public static String songTitle;
    public static String songArtist;
    public static String songData;
    public static boolean mode;
    String favBottomArgs;
    String mainScreenBottomArgs;
    FavoritesFragment favoritesFragment;
    Float acceleration = 0f;
    Float accelerationCurrent = 0f;
    Float accelerationLast = 0f;
    public FavoriteDatabase favoriteDatabase;
    public static SensorManager sensorManager;
    public static SensorEventListener sensorEventListener;
    public static Runnable r = new Runnable() {
        @Override
        public void run() {
            Long getCurrentTime = Long.valueOf(mediaPlayer.getCurrentPosition());
            startTimeText.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(getCurrentTime),
                    (TimeUnit.MILLISECONDS.toSeconds(getCurrentTime) - TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getCurrentTime))) % 60));
            seekBar.setProgress(getCurrentTime.intValue());//converting long to integer

            new Handler().postDelayed(this, 1000);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) myActivity.getSystemService(Context.SENSOR_SERVICE);
        acceleration = 0.0f;
        accelerationCurrent = SensorManager.GRAVITY_EARTH;
        accelerationLast = SensorManager.GRAVITY_EARTH;
        bindShakeListener();
    }

    @Override //Setting up the views takes place in this fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_song_playing1, container, false);
        startTimeText = view.findViewById(R.id.startTime);
        endTimeText = view.findViewById(R.id.endTime);
        playPauseButton = view.findViewById(R.id.playPauseButton);
        previousButton = view.findViewById(R.id.previousButton);
        nextButton = view.findViewById(R.id.nextIcon);
        shuffleButton = view.findViewById(R.id.shuffleButton);
        loopButton = view.findViewById(R.id.loopButton);
        seekBar = view.findViewById(R.id.seekBar);
        titleSong = view.findViewById(R.id.titleSong);
        titleArtist = view.findViewById(R.id.trackArtist);
        glView = view.findViewById(R.id.visualizer_view);
        fab = view.findViewById(R.id.favoriteIcon);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        favoriteDatabase = new FavoriteDatabase();
        Bundle extras = getArguments();
        songHelper.isPlaying = true;
        songHelper.isLoop = false;
        songHelper.isShuffle = false;
        mode = extras.getBoolean("PlayerMode");
        if (mode == true) {
            try {
                currentPosition = extras.getInt("Song Position");
                songsArrayList = extras.getParcelableArrayList("Parcel Songs");
                songDetails = songsArrayList.get(currentPosition);//fetching the song details of the current position
                songTitle = extras.getString("Song Title");
                songArtist = extras.getString("Song Artist");
                songId = extras.getLong("Song Id");
                songData = extras.getString("Song Path");

                songHelper._songId = songId;
                songHelper._songTitle = songTitle;
                songHelper._songArtist = songArtist;
                songHelper._songData = songData;
                songHelper._currentPosition = currentPosition;

                updateTextViews(songHelper._songTitle, songHelper._songArtist); //Updates text views
                mediaPlayer.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                currentPosition = extras.getInt("Song Position");
                songsArrayList = extras.getParcelableArrayList("Parcel Songs");
                songDetails = songsArrayList.get(currentPosition);//fetching the song details of the current position
                songTitle = extras.getString("Song Title");
                songArtist = extras.getString("Song Artist");
                songId = extras.getLong("Song Id");
                songData = extras.getString("Song Path");

                songHelper._songId = songId;
                songHelper._songTitle = songTitle;
                songHelper._songArtist = songArtist;
                songHelper._songData = songData;
                songHelper._currentPosition = currentPosition;

                updateTextViews(songHelper._songTitle, songHelper._songArtist); //Updates text views

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //getting the favorite and mainscreen bottom bar arguements
        favBottomArgs = extras.getString("FavBottomBar");
        mainScreenBottomArgs = extras.getString("MainScreenBottomBar");
        if (favBottomArgs != null) {
            mediaPlayer = FavoritesFragment.mEdiaPlayer;
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                mediaPlayer.pause();
                songHelper.isPlaying = false;
            } else {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                mediaPlayer.start();
                songHelper.isPlaying = true;
            }
        } else if (mainScreenBottomArgs != null) {
            mediaPlayer = MainScreenFragment.mainScreenMediaPlayer;
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                mediaPlayer.pause();
                songHelper.isPlaying = false;
            } else {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                mediaPlayer.start();
                songHelper.isPlaying = true;
            }
        } else {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(myActivity, Uri.parse(songHelper._songData));
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }//getting the main screen bottom bar and fav arguemnets

        // setting audio visualization handler. This will REPLACE previously set speech recognizer handler
        vizualizerHandler = DbmHandler.Factory.newVisualizerHandler(getContext(), mediaPlayer);
        audioVisualization.linkTo(vizualizerHandler);
        processInformation(mediaPlayer);// setting audio visualization handler. This will REPLACE previously set speech recognizer handler


        //Setting up the playPause button icon
        if (songHelper.isPlaying)
            playPauseButton.setBackgroundResource(R.drawable.pause_icon);
        else playPauseButton.setBackgroundResource(R.drawable.play_icon);
        //setting the playPause button icon

        //setting the on completelistener listener function
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onSongComplete();
            }
        });
        clickHandler();//setting the on completelistener listener function

        //Setting up the shuffle mode using shared preferences
        getShuffleMode = myActivity.getSharedPreferences(shuffleMode, Context.MODE_PRIVATE);
        shuffleModeValue = getShuffleMode.getBoolean("Mode", false);
        if (shuffleModeValue == true) {
            songHelper.isShuffle = true;
            songHelper.isLoop = false;
            shuffleButton.setBackgroundResource(R.drawable.shuffle_icon);
            loopButton.setBackgroundResource(R.drawable.loop_white_icon);
        } else {
            songHelper.isShuffle = false;
            shuffleButton.setBackgroundResource(R.drawable.shuffle_white_icon);
        }

        //Setting up the loop mode using shared preferences
        getLoopMode = myActivity.getSharedPreferences(loopMode, Context.MODE_PRIVATE);
        loopModeValue = getLoopMode.getBoolean("Mode", false);
        if (loopModeValue) {
            songHelper.isShuffle = false;
            songHelper.isLoop = true;
            shuffleButton.setBackgroundResource(R.drawable.shuffle_white_icon);
            loopButton.setBackgroundResource(R.drawable.loop_icon);
        } else {
            songHelper.isLoop = false;
            loopButton.setBackgroundResource(R.drawable.loop_white_icon);
        }

        //Setting up the favorite button
        //if (favoriteDatabase.checkIfIdExists(songHelper._songId.intValue()))
        //fab.setImageDrawable(ContextCompat.getDrawable(myActivity, R.drawable.favorite_on));
        // else fab.setImageDrawable(ContextCompat.getDrawable(myActivity, R.drawable.favorite_off));
        if (favoriteDatabase.checkIfSongExists(songHelper._songId.intValue()) == true)
            fab.setImageDrawable(ContextCompat.getDrawable(myActivity, R.drawable.favorite_on));
        else fab.setImageDrawable(ContextCompat.getDrawable(myActivity, R.drawable.favorite_off));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        audioVisualization = glView;
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
        audioVisualization.onResume();
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onPause() {
        super.onPause();
        audioVisualization.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        audioVisualization.release();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.song_playing_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_redirect);
        menuItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_redirect) {
            myActivity.onBackPressed();
            return false;
        }
        return false;
    }

    public static void clickHandler() {
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //playPauseButton function
                if ((Boolean) mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    songHelper.isPlaying = false;
                    playPauseButton.setBackgroundResource(R.drawable.play_icon);
                } else {
                    mediaPlayer.start();
                    songHelper.isPlaying = true;
                    playPauseButton.setBackgroundResource(R.drawable.pause_icon);
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //previous button function
                songHelper.isPlaying = true;
                if (songHelper.isLoop)
                    loopButton.setBackgroundResource(R.drawable.loop_white_icon);
                playPrevious();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //next button function
                songHelper.isPlaying = true;
                if (songHelper.isShuffle) {
                    playNext("PlayNextNormalShuffle");
                } else {
                    playNext("PlayNextNormal");
                }
            }

        });

        loopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //loop button function
                if (songHelper.isLoop) {
                    songHelper.isLoop = false;
                    loopButton.setBackgroundResource(R.drawable.loop_white_icon);
                } else {
                    songHelper.isLoop = true;
                    songHelper.isShuffle = false;
                    loopButton.setBackgroundResource(R.drawable.loop_icon);
                    shuffleButton.setBackgroundResource(R.drawable.shuffle_white_icon);
                }
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {//shuffle button function
            @Override
            public void onClick(View v) {
                loopEditor = myActivity.getSharedPreferences(loopMode, Context.MODE_PRIVATE);
                SharedPreferences.Editor editLoop = loopEditor.edit();
                shuffleEditor = myActivity.getSharedPreferences(shuffleMode, Context.MODE_PRIVATE);
                SharedPreferences.Editor editShuffle = shuffleEditor.edit();
                if (songHelper.isShuffle == true) { //when the shuffle button is not on
                    shuffleButton.setBackgroundResource(R.drawable.shuffle_white_icon);
                    songHelper.isShuffle = false;
                    editShuffle.putBoolean("Mode", false);
                    editShuffle.apply();
                } else {
                    songHelper.isShuffle = true;
                    songHelper.isLoop = false;
                    shuffleButton.setBackgroundResource(R.drawable.shuffle_icon);
                    loopButton.setBackgroundResource(R.drawable.loop_white_icon);
                    editShuffle.putBoolean("Mode", true);
                    editShuffle.apply();

                    editLoop.putBoolean("Mode", false);
                    editLoop.apply();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() { //fav button function
            @Override
            public void onClick(View v) {//Setting up the favorite button
                if (FavoriteDatabase.checkIfSongExists(songHelper._songId.intValue()) == true) {
                    fab.setImageDrawable(ContextCompat.getDrawable(myActivity, R.drawable.favorite_off));
                    //FavoriteDatabase.deleteFavoritesFromFirebase(songHelper._songId.intValue());
                    //Toast.makeText(myActivity, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                } else {
                    fab.setImageDrawable(ContextCompat.getDrawable(myActivity, R.drawable.favorite_on));
                    //favoriteDatabase.storeAsFavorite(songHelper._songId, songHelper._songTitle, songHelper._songArtist, songHelper._songData);
                    FavoriteDatabase.uploadFavoritesToFirebase(songHelper._songId.intValue(), songHelper._songTitle, songHelper._songArtist, songHelper._songData, null, myActivity);
                    //Toast.makeText(myActivity, "Added to Favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //playNext func is executed when next button is tapped
    public static void playNext(String check) {

        if (check.equalsIgnoreCase("PlayNextNormal")) {
            songHelper.isShuffle = false;
            currentPosition = currentPosition + 1;
            songHelper._currentPosition = currentPosition;
        } else if (check.equalsIgnoreCase("PlayNextNormalShuffle")) {
            songHelper.isShuffle = true;
            randomPosition = (new Random()).nextInt(songsArrayList.size());
            currentPosition = randomPosition;
            songHelper._currentPosition = currentPosition;
        }

        if (currentPosition == songsArrayList.size()-1)
            currentPosition = 0; //checks if the song is the last in the list then it automatically moves to the first one

        Songs nextSong = songsArrayList.get(currentPosition);
        songHelper._songId = nextSong.getSongId();
        songHelper._songTitle = nextSong.getSongTitle();
        songHelper._songArtist = nextSong.getSongArtist();
        songHelper._songData = nextSong.getSongData();

        updateTextViews(songHelper._songTitle, songHelper._songArtist); //Updating the song title & song artist

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(myActivity, Uri.parse(songHelper._songData));
            mediaPlayer.prepare();
            mediaPlayer.start();
            processInformation(mediaPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (FavoriteDatabase.checkIfSongExists(songHelper._songId.intValue()) == true)
            fab.setImageDrawable(ContextCompat.getDrawable(myActivity, R.drawable.favorite_on));
        else fab.setImageDrawable(ContextCompat.getDrawable(myActivity, R.drawable.favorite_off));
    }

    //playPrevious func is used when previous button is tapped
    public static void playPrevious() {
        currentPosition = currentPosition - 1;
        if (currentPosition == -1) currentPosition = 0;
            songHelper._currentPosition=currentPosition;

        if (songHelper.isPlaying)
            playPauseButton.setBackgroundResource(R.drawable.pause_icon);
        else playPauseButton.setBackgroundResource((R.drawable.play_icon));
        songHelper.isLoop = false;
        Songs previousSong = songsArrayList.get(currentPosition);
        songHelper._songId = previousSong.getSongId();
        songHelper._songTitle = previousSong.getSongTitle();
        songHelper._songArtist = previousSong.getSongArtist();
        songHelper._songData = previousSong.getSongData();
        updateTextViews(songHelper._songTitle, songHelper._songArtist);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(myActivity, Uri.parse(songHelper._songData));
            mediaPlayer.prepare();
            mediaPlayer.start();
            processInformation(mediaPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (FavoriteDatabase.checkIfSongExists(songHelper._songId.intValue()) == true)
            fab.setImageDrawable(ContextCompat.getDrawable(myActivity, R.drawable.favorite_on));
        else fab.setImageDrawable(ContextCompat.getDrawable(myActivity, R.drawable.favorite_off));
    }

    public static void onSongComplete() { //executes the fun when the song is complete
        if (songHelper.isShuffle) {
            playNext("PlayNextNormalShuffle");
            songHelper.isPlaying = true;
        } else if (songHelper.isLoop) {
            songHelper.isPlaying = true;

            Songs onSongComplete = songsArrayList.get(songHelper._currentPosition);
            songHelper._songId = onSongComplete.getSongId();
            songHelper._songTitle = onSongComplete.getSongTitle();
            songHelper._songArtist = onSongComplete.getSongArtist();
            songHelper._songData = onSongComplete.getSongData();
            songHelper._currentPosition = currentPosition;
            updateTextViews(songHelper._songTitle, songHelper._songArtist);

            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(myActivity, Uri.parse(songHelper._songData));
                mediaPlayer.prepare();
                mediaPlayer.start();
                processInformation(mediaPlayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {playNext("PlayNextNormal");
        songHelper.isPlaying = true;}

        if (FavoriteDatabase.checkIfSongExists(songHelper._songId.intValue()) == true)
            fab.setImageDrawable(ContextCompat.getDrawable(myActivity, R.drawable.favorite_on));
        else fab.setImageDrawable(ContextCompat.getDrawable(myActivity, R.drawable.favorite_off));
    }

    public static void updateTextViews(String songtitle, String songartist) {
        titleSong.setText(songtitle);
        titleArtist.setText(songartist);
    }

    public static void processInformation(MediaPlayer mediaPlayer) {
        startTime = Long.valueOf(mediaPlayer.getCurrentPosition());//Gets the current song position
        Long finalTime = Long.valueOf(mediaPlayer.getDuration());//Gets the final song position
        seekBar.setMax(finalTime.intValue());
        startTimeText.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(startTime),
                TimeUnit.MILLISECONDS.toSeconds(startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime))));

        endTimeText.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(finalTime),
                TimeUnit.MILLISECONDS.toSeconds(finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime))));

        new Handler().postDelayed(r, 1000);
    }

    public void bindShakeListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Float x = event.values[0];
                Float y = event.values[1];
                Float z = event.values[2];
                Double aDouble = Math.sqrt(x * x + y * y + z * z);
                accelerationLast = accelerationCurrent;
                accelerationCurrent = aDouble.floatValue();
                Float delta = accelerationCurrent - accelerationLast;
                acceleration = acceleration * 0.9f + delta;
                if (acceleration > 12) {
                    SharedPreferences getShared = myActivity.getSharedPreferences("ShakeFeature", Context.MODE_PRIVATE);
                    Boolean shakeMode = getShared.getBoolean("feature", false);
                    if (shakeMode == true)
                        playNext("PlayNextNormal");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }


}
