package com.wzy.wzytv.controls;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

/**
 * Created by zy on 2016/10/8.
 */

public class VolumeController {
    private static final String TAG = VolumeController.class.getSimpleName();

    /**
     * 音量减
     *
     * @param act
     * @param screenHeight
     * @param deltaY
     */
    public static void volumeDown(Activity act, int screenHeight, float deltaY) {
        AudioManager audioManager = (AudioManager) act.getSystemService(Context.AUDIO_SERVICE);
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float down = deltaY / screenHeight * max;
        float volumn = Math.max(current + down, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) volumn, AudioManager.FLAG_SHOW_UI);
    }

    /**
     * 音量加
     *
     * @param act
     * @param screenHeight
     * @param deltaY
     */
    public static void volumeUp(Activity act, int screenHeight, float deltaY) {
        AudioManager audioManager = (AudioManager) act.getSystemService(Context.AUDIO_SERVICE);
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float up = deltaY / screenHeight * max;
        if (up < 1) {
            up = up + 1;
        }
        float volumn = Math.min(current + up, max);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) volumn, AudioManager.FLAG_SHOW_UI);

    }
}
