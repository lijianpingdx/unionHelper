package com.dax.union.helper;

public class JniPcap {
    static {
        System.loadLibrary("test");
    }
    public native static String getStrFromJNI();

    public native static byte[] getNextPackage();

    public native static String startCaptor();

    public native static void stoptCaptor();
}
