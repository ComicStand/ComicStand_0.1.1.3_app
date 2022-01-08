package com.s.video.musicas.scooby.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.s.video.musicas.scooby.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YouTUbeFragmentFragment extends Fragment  {


    YouTubePlayerView youtubeView;
    String video;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_you_t_ube_fragment, container, false);
        youtubeView = view.findViewById(R.id.youtubeView);




        video=getArguments().getString("video");
        play();
        return view;
    }



    private void play(){
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    try {
                        youTubePlayer.loadVideo(video);
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                            //   youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);

                    } catch (Exception e) {
                        e.getMessage();

                    }
                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        //Comment out this because of Google playstore restriction on API that occur while upload on play store.
        //message on upload - Leaked GCP API Keys
        //Your app contains exposed Google Cloud Platform (GCP) API keys. Please see this Google Help Center article for details.
        //
        //com.s.video.musicas.scooby.MpaRouteActivity->getDirectionsUrl
        //com.s.video.musicas.scooby.fragment.YouTUbeFragmentFragment->play


       // String id = "AIzaSyDLdwzK5Een4YDjCou08NyhsizOkViSQZo";
      //  youtubeView.initialize(id, onInitializedListener);
    }

}