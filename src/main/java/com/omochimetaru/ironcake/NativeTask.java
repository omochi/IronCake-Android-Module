package com.omochimetaru.ironcake;

import android.util.Log;

/**
 * Created by omochi on 2014/01/29.
 */

//  ネイティブのick::function<JNIEnv *, jobject> *をラップするクラス
//  ネイティブ側で明示的なreleaseの呼び出しが必要
//  Run前のreleaseが可能

public class NativeTask implements Runnable {
    static {
        System.loadLibrary("IronCake");
        nativeStaticInit();
    }
    private static native void nativeStaticInit();

    //native ick::function<void (*)()> *
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
