package com.example.lexnmusic.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lexnmusic.R;


import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {
    Switch shakeSwitch;
    TextView textView;
    View lineView;
    Activity myActivity;
    String shakeFeature = "ShakeFeature";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        shakeSwitch = view.findViewById(R.id.shakeSwitch);
        textView = view.findViewById(R.id.textView);
        //lineView = view.findViewById(R.id.lineView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences getShared = myActivity.getSharedPreferences(shakeFeature, MODE_PRIVATE);
        Boolean shakeMode = getShared.getBoolean("feature", false);
        if (shakeMode==true)
            shakeSwitch.setChecked(true);
        else shakeSwitch.setChecked(false);

            shakeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()) {
                        SharedPreferences setShared = myActivity.getSharedPreferences(shakeFeature, MODE_PRIVATE);
                        SharedPreferences.Editor editor = setShared.edit();
                        editor.putBoolean("feature", true);
                        editor.apply();
                        Toast.makeText(myActivity, "Shake to change song activated", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences setShared = myActivity.getSharedPreferences(shakeFeature, MODE_PRIVATE);
                        SharedPreferences.Editor editor = setShared.edit();
                        editor.putBoolean("feature", false);
                        editor.apply();
                        Toast.makeText(myActivity, "Shake to change song deactivated", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
