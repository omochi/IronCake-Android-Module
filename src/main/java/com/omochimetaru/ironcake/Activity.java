package com.omochimetaru.ironcake;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

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
public abstract class Activity extends android.app.Activity implements TextureView.SurfaceTextureListener {
    static {
        System.loadLibrary("IronCake");
    }

    private long application;

    private TextureView textureView;

    //ネイティブ化してオーバライドするように
    protected abstract long controllerConstruct();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        textureView = new TextureView(this);
        textureView.setSurfaceTextureListener(this);
        setContentView(textureView);

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
    }

    private native void nativeOnResume();

    @Override
    protected void onPause(){
        nativeOnPause();

        super.onPause();
    }

    private native void nativeOnPause();


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        nativeOnSurfaceTextureAvailable(surfaceTexture, width, height);
    }

    private native void nativeOnSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height);

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
        nativeOnSurfaceTextureSizeChanged(surfaceTexture, width, height);
    }

    private native void nativeOnSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height);

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        nativeOnSurfaceTextureDestroyed(surfaceTexture);
        return false;
    }

    private native void nativeOnSurfaceTextureDestroyed(SurfaceTexture surfaceTexture);

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
