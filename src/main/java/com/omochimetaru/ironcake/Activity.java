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
public class Activity extends android.app.Activity implements TextureView.SurfaceTextureListener {
    private long application;

    private TextureView textureView;

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

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
