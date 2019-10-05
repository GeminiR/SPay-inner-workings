/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.String
 */
package com.samsung.android.spayfw.d.a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.samsung.android.spayfw.d.a.a;

public class b
extends a {
    private static b BB = null;
    private static Context mContext;
    private SQLiteDatabase Bq;

    private b(Context context, String string, int n2) {
        super(context, string, null, n2);
        Log.i((String)"SeDbOpenHelper", (String)"super: ");
    }

    public static b c(Context context, String string, int n2) {
        Class<b> class_ = b.class;
        synchronized (b.class) {
            Log.i((String)"SeDbOpenHelper", (String)"getInstance");
            if (BB == null) {
                mContext = context.getApplicationContext();
                BB = new b(mContext, string, n2);
            }
            b b2 = BB;
            // ** MonitorExit[var6_3] (shouldn't be in output)
            return b2;
        }
    }

    @Override
    public SQLiteDatabase getWritableDatabase(byte[] arrby) {
        b b2 = this;
        synchronized (b2) {
            if (this.Bq == null) {
                this.Bq = super.getWritableDatabase(arrby);
            }
            SQLiteDatabase sQLiteDatabase = this.Bq;
            return sQLiteDatabase;
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        if (!sQLiteDatabase.isReadOnly()) {
            // empty if block
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        Log.i((String)"SeDbOpenHelper", (String)"Database is created");
    }

    @Override
    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
        Log.i((String)"SeDbOpenHelper", (String)("upgrade database from version " + n2 + " to version " + n3));
        if (n2 == n3) {
            Log.i((String)"SeDbOpenHelper", (String)"oldVersion == newVersion, no need to upgrade!");
            return;
        } else {
            if (n2 > n3) {
                Log.e((String)"SeDbOpenHelper", (String)"database cannot be downgraded, please update to the latest version");
                this.onDowngrade(sQLiteDatabase, n2, n3);
                return;
            }
            if (n2 == 0 || n2 == 1) {
                ++n2;
            }
            if (n2 != n3) return;
            {
                Log.i((String)"SeDbOpenHelper", (String)"database is upgraded");
                return;
            }
        }
    }
}
