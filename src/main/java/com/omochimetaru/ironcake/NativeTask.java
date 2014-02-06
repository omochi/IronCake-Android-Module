package com.omochimetaru.ironcake;

import android.util.Log;

/**
 * Created by omochi on 2014/01/29.
 */

//  ネイティブ側から生成する、Java側から実行できるTaskクラス。
//  Java側ではRunnableになっている。
//  ネイティブ側の型はick::Function<void (*)(JNIEnv *,jobject)>。
//  ぶら下げているそれをreleaseでJavaかネイティブどちらかから解放する必要がある。

public class NativeTask implements Runnable {
    static {
        System.loadLibrary("IronCake");
        nativeStaticInit();
    }
    private static native void nativeStaticInit();

    private long function;

    public NativeTask(){
    }

    @Override
    protected void finalize() throws Throwable {
        if(function!=0){
            Log.w(IronCake.TAG, "NativeTask function may leaks");
            release();
        }
        super.finalize();
    }

    @Override
    public native void run();

    public native void release();
}
