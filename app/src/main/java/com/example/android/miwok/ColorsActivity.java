package com.example.android.miwok;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

public class ColorsActivity extends AppCompatActivity {
    private MediaPlayer sound;
    private AudioManager mAudioManager;
    MediaPlayer.OnCompletionListener mCompletionListner = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                //Pause MediaPlayer
                sound.pause();
                sound.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                //Resume MediaPlayer
                sound.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                //Stop MediaPlayer
                releaseMediaPlayer();
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        final ArrayList<Word> colors = new ArrayList<>();
        colors.add(new Word("Red", "Kempu", R.raw.color_red, R.drawable.color_red));
        colors.add(new Word("mustard yellow", "Halade", R.raw.color_yellow, R.drawable.color_mustard_yellow));
        colors.add(new Word("dusty yellow", "Godi", R.raw.color_dusty_yellow, R.drawable.color_dusty_yellow));
        colors.add(new Word("green", "Hasiru", R.raw.color_green, R.drawable.color_green));
        colors.add(new Word("brown", "Kandu", R.raw.color_brown, R.drawable.color_brown));
        colors.add(new Word("gray", "Budu", R.raw.color_grey, R.drawable.color_gray));
        colors.add(new Word("black", "Kappu", R.raw.color_black, R.drawable.color_black));
        colors.add(new Word("white", "Bili", R.raw.color_white, R.drawable.color_white));


        WordAdapter item = new WordAdapter(this, colors, R.color.category_colors);
        ListView list = (ListView) findViewById(R.id.list_colors);
        assert list != null;
        list.setAdapter(item);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word color = colors.get(position);
                releaseMediaPlayer();


                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    // Gained access for audio

                    sound = MediaPlayer.create(ColorsActivity.this, color.getAudioSound());
                    sound.start();
                    sound.setOnCompletionListener(mCompletionListner);
                }

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (sound != null) {
            sound.release();
            sound = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}