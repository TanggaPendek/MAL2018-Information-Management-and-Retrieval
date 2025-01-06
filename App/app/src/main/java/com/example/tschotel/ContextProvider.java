package com.example.tschotel;

import android.content.Context;

public class ContextProvider {
    private static Context context;

    public static void init(Context appContext) {
        context = appContext.getApplicationContext();
    }

    public static Context getContext() {
        if (context == null) {
            throw new IllegalStateException("ContextProvider is not initialized. Call init() first.");
        }
        return context;
    }
}
