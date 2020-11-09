package com.example.lexnmusic.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.example.lexnmusic.R;
import com.example.lexnmusic.R;

public class AboutUsFragment extends Fragment {

    GLAudioVisualizationView glAudioVisualizationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_about_us, container, false);
        glAudioVisualizationView=view.findViewById(R.id.aboutUsVisualizer);
        return view;
    }

}
