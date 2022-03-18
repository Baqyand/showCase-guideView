package com.nexsoft.showcaseviewlib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BackGroundWhite extends LinearLayout {


    private static final int RADIUS_SIZE = 5;
    private static final int PADDING_SIZE = 10;
    private static final int BOTTOM_PADDING_SIZE = 5;
    private static final int DEFAULT_TITLE_TEXT_SIZE = 18;
    private static final int DEFAULT_CONTENT_TEXT_SIZE = 14;

    private final Paint mPaint;
    private final RectF mRect;

    private final TextView mTitleTextView;
    private final TextView mContentTextView;
    private final TextView mLinkToActivity;
    private Class<?> linkToActivity;
    private Context context;
    int[] location = new int[2];

    BackGroundWhite(Context context) {
        super(context);

        this.context = context;

        float density = context.getResources().getDisplayMetrics().density;
        setWillNotDraw(false);
        setOrientation(VERTICAL);

        mRect = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        final int padding = (int) (PADDING_SIZE * density);
        final int paddingBottom = (int) (BOTTOM_PADDING_SIZE * density);

        mTitleTextView = new TextView(context);
        mTitleTextView.setPadding(padding, padding, padding, paddingBottom);
        mTitleTextView.setGravity(Gravity.LEFT);
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TITLE_TEXT_SIZE);
        mTitleTextView.setTextColor(Color.TRANSPARENT);
        addView(
                mTitleTextView,
                new LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        mContentTextView = new TextView(context);
        mContentTextView.setTextColor(Color.TRANSPARENT);
        mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CONTENT_TEXT_SIZE);
        mContentTextView.setPadding(padding, paddingBottom, padding, padding);
        mContentTextView.setGravity(Gravity.LEFT);
        addView(
                mContentTextView,
                new LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        mLinkToActivity = new TextView(context);
        mLinkToActivity.setTextColor(Color.TRANSPARENT);
        mLinkToActivity.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CONTENT_TEXT_SIZE);
        mLinkToActivity.setPadding(padding, paddingBottom, padding, padding);
        mLinkToActivity.setGravity(Gravity.LEFT);
        mLinkToActivity.setClickable(true);
        mLinkToActivity.setFocusable(true);
        mLinkToActivity.setLinksClickable(true);
        mLinkToActivity.setLinkTextColor(Color.BLACK);
        addView(
                mLinkToActivity,
                new LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

    }

    public void setTitle(String title) {
        if (title == null) {
            removeView(mTitleTextView);
            return;
        }
        mTitleTextView.setText(title);
    }

    public void setContentText(String content) {
        mContentTextView.setText(content);
    }

    public void setLinkText(String linkText) {

        if (linkText == null) {
            removeView(mLinkToActivity);
            return;
        }
        Spanned spanned = Html.fromHtml(linkText);

        mLinkToActivity.setText(spanned);

    }

    public void setLinkToActivity(Class<?> linkToActivity) {
        if (linkToActivity == null) {
            removeView(mLinkToActivity);
            return;
        }

        this.linkToActivity = linkToActivity;

        mLinkToActivity.setText(getContext().getString(R.string.go_to) + linkToActivity.getName());
        mLinkToActivity.setOnClickListener(view -> ActivityHelper.showActivity((Activity) context, linkToActivity, false));

    }


    public void setContentSpan(Spannable content) {
        mContentTextView.setText(content);
    }

    public void setContentTypeFace(Typeface typeFace) {
        mContentTextView.setTypeface(typeFace);
    }

    public void setTitleTypeFace(Typeface typeFace) {
        mTitleTextView.setTypeface(typeFace);
    }

    public void setLinkTypeFace(Typeface typeFace) {
        mTitleTextView.setTypeface(typeFace);
    }

    public void setTitleTextSize(int size) {
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setContentTextSize(int size) {
        mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setLinkTextSize(int size) {
        mLinkToActivity.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }


    public void setColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.getLocationOnScreen(location);

        mRect.set(
                getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom()
        );

        final int density = (int) getResources().getDisplayMetrics().density;
        final int radiusSize = RADIUS_SIZE * density;
        canvas.drawRoundRect(mRect, radiusSize, radiusSize, mPaint);
    }
}

