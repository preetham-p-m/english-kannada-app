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

public class FamilyActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_family);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> family = new ArrayList<>();
        family.add(new Word("father", "Tande", R.raw.family_father, R.drawable.family_father));
        family.add(new Word("mother", "Tayi", R.raw.family_mother, R.drawable.family_mother));
        family.add(new Word("son", "Maga", R.raw.family_son, R.drawable.family_son));
        family.add(new Word("daughter", "Magalu", R.raw.family_daughter, R.drawable.family_daughter));
        family.add(new Word("older brother", "Anna", R.raw.family_elder_brother, R.drawable.family_older_brother));
        family.add(new Word("older sister", "Akka", R.raw.family_elder_sister, R.drawable.family_older_sister));
        family.add(new Word("younger brother", "Tamma", R.raw.family_younger_brother, R.drawable.family_younger_brother));
        family.add(new Word("younger sister", "Tangi", R.raw.family_younger_sister, R.drawable.family_younger_sister));
        family.add(new Word("grandfather", "Ajja", R.raw.family_grandfather, R.drawable.family_grandfather));
        family.add(new Word("grandmother", "Ajji", R.raw.family_grandmother, R.drawable.family_grandmother));


        WordAdapter item = new WordAdapter(this, family, R.color.category_family);
        ListView list = (ListView) findViewById(R.id.list_family);
        assert list != null;
        list.setAdapter(item);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word fam = family.get(position);
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

                    sound = MediaPlayer.create(FamilyActivity.this, fam.getAudioSound());
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