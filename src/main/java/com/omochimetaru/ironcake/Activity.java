package com.omochimetaru.ironcake;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by omochi on 2014/01/24.
 */

/*
    onCreateでapplicationのLaunch,
    eglDisplayの初期化
    onCreateのユーザサイドで画面configを設定しておく
    onSurfaceTextureAvailableで、
    設定されたconfigでeglContext, eglSurfaceを生成、
    ユーザサイドにGLの初期化を通知
    onSurfaceTextureChangedで、
    eglSurfaceを再生成
    ユーザサイドに画面サイズの変更を通知
    onSurfaceTextureDestroyedで、
    eglContext, eglSurfaceを解放
    ユーザサイドにGLの終了を通知

    onDestroyでeglDisplayの解放

 */
public abstract class Activity extends android.app.Activity implements SurfaceHolder.Callback {
    static {
        System.loadLibrary("IronCake");
        nativeStaticInit();
    }
    private static native void nativeStaticInit();

    private long application;

    private Handler mainThreadHandler;
    private SurfaceView surfaceView;
    private Timer updateTimer;

    //ネイティブ化してオーバライドするように
    protected abstract long controllerConstruct();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mainThreadHandler = new Handler(Looper.getMainLooper());

        surfaceView = new SurfaceView(this);
        surfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        surfaceView.getHolder().addCallback(this);

        setContentView(surfaceView);

        updateTimer = new Timer();

        nativeOnCreate();
    }

    private native void nativeOnCreate();

    @Override
    protected void onDestroy(){
        nativeOnDestroy();

        super.onDestroy();
    }

    private native void nativeOnDestroy();

    @Override
    protected void onResume(){
        super.onResume();

        nativeOnResume();

        onUpdateTimer();
    }

    private native void nativeOnResume();

    @Override
    protected void onPause(){
        updateTimer.cancel();

        nativeOnPause();

        super.onPause();
    }

    private native void nativeOnPause();

    @Override
    public native void surfaceCreated(SurfaceHolder surfaceHolder);

    @Override
    public native void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height);

    @Override
    public native void surfaceDestroyed(SurfaceHolder surfaceHolder);

    private native void update();

    private void scheduleUpdateTimer(float delay){
        updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                onUpdateTimer();
            }
        } , (int)(delay * 1000));
    }
    private void onUpdateTimer(){
        //メインスレで同期更新する
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                Activity.this.update();
            }
        });
    }
}
