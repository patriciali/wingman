package com.patriciasays.wingman.activity;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;

public abstract class MicrophoneListenerActivity extends Activity {

    public static final int SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = 8*1024;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord mAudioRecord;
    private RecordRunnable mRecordRunnable;

    protected abstract void handleSamples(double[] normalizedSamples);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
                CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
        mRecordRunnable = new RecordRunnable(mAudioRecord, BUFFER_SIZE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecordRunnable.mRunning = true;
        new Thread(mRecordRunnable).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecordRunnable.mRunning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized(mAudioRecord) {
            mAudioRecord.release();
        }
    }

    private class RecordRunnable implements Runnable {

        private final AudioRecord mAudioRecord;
        private short[] mBuffer;
        private double[] mNormalizedSamples;
        private volatile boolean mRunning;

        public RecordRunnable(AudioRecord record, int bufSize) {
            mAudioRecord = record;
            mBuffer = new short[bufSize];
            mNormalizedSamples = new double[bufSize];
        }

        @Override
        public void run() {
            synchronized(mAudioRecord) {
                mAudioRecord.startRecording();

                OUTER:while(mRunning) {
                    int bytesRead = 0;
                    while(bytesRead < mBuffer.length) {
                        if (!mRunning) {
                            break OUTER;
                        }
                        bytesRead += mAudioRecord.read(mBuffer, bytesRead, mBuffer.length - bytesRead);
                    }

                    for(int i = 0; i < mBuffer.length; i++) {
                        double shortSample = mBuffer[i];
                        double sample = shortSample >= 0 ? (shortSample / Short.MAX_VALUE) : -(shortSample / Short.MIN_VALUE);
                        mNormalizedSamples[i] = sample;
                    }
                    handleSamples(mNormalizedSamples);
                }

                mAudioRecord.stop();
            }
        }
    }

}
