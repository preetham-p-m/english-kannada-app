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

public class NumbersActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_numbers);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Created a Word ,which takes to input and gives output
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("One", "Ondu", R.raw.number_one, R.drawable.number_one));
        words.add(new Word("Two", "Eradu", R.raw.number_two, R.drawable.number_two));
        words.add(new Word("Three", "Muru", R.raw.number_three, R.drawable.number_three));
        words.add(new Word("Four", "Nalku", R.raw.number_four, R.drawable.number_four));
        words.add(new Word("Five", "Aidu", R.raw.number_five, R.drawable.number_five));
        words.add(new Word("Six", "Aru", R.raw.number_six, R.drawable.number_six));
        words.add(new Word("Seven", "Elu", R.raw.number_seven, R.drawable.number_seven));
        words.add(new Word("Eight", "Entu", R.raw.number_eight, R.drawable.number_eight));
        words.add(new Word("Nine", "Ombattu", R.raw.number_nine, R.drawable.number_nine));
        words.add(new Word("Ten", "Hattu", R.raw.number_ten, R.drawable.number_ten));

        // Implimentation of word
        WordAdapter itemsAdapter = new WordAdapter(this, words, R.color.category_numbers);

        ListView listView = (ListView) findViewById(R.id.list_numbers);
        assert listView != null;
        listView.setAdapter(itemsAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);
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


                    sound = MediaPlayer.create(NumbersActivity.this, word.getAudioSound());
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