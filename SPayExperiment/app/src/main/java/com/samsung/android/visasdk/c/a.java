/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 */
package com.samsung.android.visasdk.c;

import com.samsung.android.spayfw.b.Log;
import java.util.ArrayList;

public final class a {
    private static ArrayList<Object> oJ = new ArrayList();

    public static void d(String string, String string2) {
        Log.d("PAYWAVE_" + string, string2);
    }

    public static void e(String string, String string2) {
        Log.e("PAYWAVE_" + string, string2);
    }

    public static void i(String string, String string2) {
        Log.i("PAYWAVE_" + string, string2);
    }

    public static void w(String string, String string2) {
        Log.w("PAYWAVE_" + string, string2);
    }
}

