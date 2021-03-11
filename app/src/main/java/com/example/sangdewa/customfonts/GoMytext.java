/*
 * Copyright (c) Bakulapp.com 2020
 * Dev : Moh. Lukman Sholeh
 */

package com.example.sangdewa.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class GoMytext extends TextView {

    public GoMytext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GoMytext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GoMytext(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/NeoSansStdBlackTR.otf");
            setTypeface(tf);
        }
    }

}