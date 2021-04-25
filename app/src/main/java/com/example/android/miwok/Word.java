package com.example.android.miwok;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.media.Image;

public class Word {

    private String mDefaultTranslation;
    private String mKannadaTranslation;
    private int mImageView = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;
    private int mAudioSound;

    public Word(String defaultTranslation, String kannadaTranslation,int audioSound) {
        mDefaultTranslation = defaultTranslation;
        mKannadaTranslation = kannadaTranslation;
        mAudioSound = audioSound;
    }

    public Word(String defaultTranslation, String kannadaTranslation,int audioSound, int image) {
        mDefaultTranslation = defaultTranslation;
        mKannadaTranslation = kannadaTranslation;
        mImageView = image;
        mAudioSound = audioSound;
    }


    //To get default language Translation
    public String getDefaltTranslation() {
        return mDefaultTranslation;
    }

    //To get kannada language Translation
    public String getKannadaTranslation() {
        return mKannadaTranslation;
    }

    public int getImageResourceId() {
        return mImageView;
    }

    public boolean hasImage() {
        return mImageView != NO_IMAGE_PROVIDED;
    }

    public int getAudioSound(){
        return mAudioSound;
    }
}
