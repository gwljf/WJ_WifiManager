package com.wj.parameters;

/**
 * Created by wlf on 10/26/14.
 */
public class Parameter {

    private static int signalGood = -60;
    private static int signalFair = -80;

    public static void setSignalGood(int _SignalGood){
        signalGood = _SignalGood;
    }

    public static void setSignalFair(int _SignalFair){
        signalFair = _SignalFair;
    }

    public static int getSignalGood(){
        return signalGood;
    }

    public static int getSignalFair(){
        return signalFair;
    }

}
