package com.nexsoft.showcaseview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class LinearHint extends LinearLayout {

    public LinearHint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

            setOrientation(VERTICAL);

            Button button = new Button(context);
            button.setText("Cek cek cek");

            addView(button, new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

    }
}
