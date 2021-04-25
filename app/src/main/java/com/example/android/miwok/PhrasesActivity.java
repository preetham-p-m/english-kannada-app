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

public class PhrasesActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_phrase);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        final ArrayList<Word> phrases = new ArrayList<>();
        phrases.add(new Word("Where are you going?", "Nivu ellige hoguttiddira?", R.raw.phrases_where_are_you_going));
        phrases.add(new Word("What is your name?", "Ninna hesarenu?", R.raw.phrases_what_is_your_name));
        phrases.add(new Word("My name is ...", "Nanna hesaru ...", R.raw.phrases_my_name_is));
        phrases.add(new Word("How are you feeling?", "Nivu hege bhavisuttiddiri?", R.raw.phrases_how_are_you_feeling));
        phrases.add(new Word("I'm feeling good", "Nana channagidini", R.raw.phrases_i_am_feeling_good));
        phrases.add(new Word("Are you comming?", "Nivu baruvira? or Ninu baruveya?", R.raw.phrases_are_you_comming));
        phrases.add(new Word("Yes, I'm comming.", "Haudu, nanu baruttiddene.", R.raw.phrases_yes_i_am_comming));
        phrases.add(new Word("Let's go", "Hogoṇa", R.raw.phrases_lets_go));
        phrases.add(new Word("Come here", "Illi ba", R.raw.phrases_come_here));


        WordAdapter item = new WordAdapter(this, phrases, R.color.category_phrases);
        ListView list = (ListView) findViewById(R.id.list_phrases);
        assert list != null;
        list.setAdapter(item);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word phrase = phrases.get(position);
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

                sound = MediaPlayer.create(PhrasesActivity.this, phrase.getAudioSound());
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
        }
    }
}