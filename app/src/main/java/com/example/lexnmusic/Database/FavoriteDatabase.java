package com.example.lexnmusic.Database;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.lexnmusic.Adapters.FavoritesAdapter;
import com.example.lexnmusic.CustomClass.Songs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.lexnmusic.Fragments.FavoritesFragment.favoritesRecyclerView;
import static com.example.lexnmusic.Fragments.FavoritesFragment.noFavorites;
import static com.example.lexnmusic.Fragments.SongPlayingFragment.myActivity;

public class FavoriteDatabase {

    public static int storeId = 0;
    public static Songs songObj;
    public static ArrayList<Songs> favoriteSongs;

    public static void uploadFavoritesToFirebase(int id, String title, String artist, String path, Long date, final Context context) {
        songObj = new Songs(Long.valueOf(id), title, artist, path, date);
        com.google.firebase.database.FirebaseDatabase.getInstance().getReference("SongDetails").push().setValue(songObj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                Log.e("Error",e.getMessage());
            }
        });
    }

    public static ArrayList<Songs> retriveDataFromFirebase() {
        DatabaseReference databaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("SongDetails");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    songObj = ds.getValue(Songs.class);
                    favoriteSongs.add(new Songs(songObj.getSongId(), songObj.getSongTitle(), songObj.getSongArtist(), songObj.getSongData(), null));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return favoriteSongs;
    }

    public static void deleteFavoritesFromFirebase(int id) {
        DatabaseReference databaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("SongDetails").child(Integer.toString(id));
        databaseReference.removeValue();
        Toast.makeText(myActivity, "Deleted from Favorites", Toast.LENGTH_SHORT).show();
    }

    public static boolean checkIfSongExists(final int id) {
        DatabaseReference databaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("SongDetails");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Songs songObj = ds.getValue(Songs.class);
                    if (songObj.getSongId() == id)
                        storeId = id;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (storeId == id) return true;
        else return false;
    }
}
