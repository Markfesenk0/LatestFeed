package com.example.latestfeed;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.latestfeed.Adapters.PackageDetailsAdapter;
import com.example.latestfeed.Entities.Song;
import com.example.latestfeed.IsraelPost.Entities.Parcel;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.logging.LogRecord;

public class PreviewMediaPlayer extends AppCompatDialogFragment {
    private ImageView playPuaseImage, songImage;
    private TextView currentTime, totalTime, songTitle, songArtist;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private String url;
    private Song song;
    private Handler handler = new Handler();
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.music_player_dialog, null);

        builder.setView(view);

        playPuaseImage = view.findViewById(R.id.play_pause_button);
        currentTime = view.findViewById(R.id.current_time_text);
        totalTime = view.findViewById(R.id.total_time_text);
        seekBar = view.findViewById(R.id.seek_bar);
        songTitle = view.findViewById(R.id.song_title);
        songArtist = view.findViewById(R.id.song_artist);
        songImage = view.findViewById(R.id.song_image);
        mediaPlayer = new MediaPlayer();
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    System.out.println("progress: " + progress);
                    mediaPlayer.seekTo((progress * mediaPlayer.getDuration()) / 100);
                    currentTime.setText(toTimer(mediaPlayer.getCurrentPosition()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        playPuaseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    playPuaseImage.setImageResource(R.drawable.ic_play);
                } else {
                    mediaPlayer.start();
                    playPuaseImage.setImageResource(R.drawable.ic_pause);
                    updateSeekBar();
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playPuaseImage.setImageResource(R.drawable.ic_play);
            }
        });
        if (getArguments() != null) {
//            url = getArguments().getString("url");
            song = (Song) getArguments().getSerializable("song");
            url = song.getPreviewUrl();
            songTitle.setText(song.getTitle());
            songArtist.setText(song.getArtist());
            try {
                Picasso.get().load(song.getImgUrl()).into(songImage);
            } catch (IllegalArgumentException e) {
                Picasso.get().load(R.drawable.placeholder).into(songImage);
                System.out.println(e.getMessage());
            }
            songImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLink(song.getSongUrl());
                }
            });
        }
        prepareMediaPlayer();
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.pause();
    }

    public void openLink(String url) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }catch (ActivityNotFoundException ex){
            Toast.makeText(getContext(), "Can't open URL.\nTry again later", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareMediaPlayer() {
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
//            totalTime.setText(toTimer(mediaPlayer.getDuration()));
            totalTime.setText(toTimer(30000));
        } catch (Exception e) {
            Toast.makeText(getContext(), "OOPSIE: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mediaPlayer.getCurrentPosition();
            currentTime.setText(toTimer(currentDuration));
        }
    };

    private void updateSeekBar() {
        if(mediaPlayer.isPlaying()) {
            seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(updater, 1000);
        }
    }

    private String toTimer(long milliseconds) {
        String timerString = "";
        String secondsString = "";

        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (minutes> 0) {
            timerString = minutes + ":";
        } else {
            timerString = "0:";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = secondsString + seconds;
        }

        return timerString + secondsString;
    }
}
