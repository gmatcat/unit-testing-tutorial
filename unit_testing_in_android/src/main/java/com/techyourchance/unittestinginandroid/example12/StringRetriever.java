package com.techyourchance.unittestinginandroid.example12;

import android.content.Context;

public class StringRetriever {
    private final Context mContext;

    public StringRetriever(Context context) {
        mContext = context;
    }

    public String getString(int id) {
        int x = 2;
        return mContext.getString(id);
    }
}
