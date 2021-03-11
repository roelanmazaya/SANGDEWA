/*
 * Copyright (c) Bakulapp.com 2020
 * Dev : Moh. Lukman Sholeh
 */

package com.example.sangdewa.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by one on 3/12/15.
 */
public class MyEditText extends EditText {

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/NeoSansStd MediumtR.otf");
            setTypeface(tf);
        }
    }

}