package com.nexsoft.showcaseviewlib;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.os.Build;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nexsoft.showcaseviewlib.config.DismissType;
import com.nexsoft.showcaseviewlib.config.Gravity;
import com.nexsoft.showcaseviewlib.config.PointerType;
import com.nexsoft.showcaseviewlib.listener.GuideListener;
import com.nexsoft.showcaseviewlib.storage.SessionGuide;

@SuppressLint("ViewConstructor")
public class GuideView extends RelativeLayout {

    private static final int INDICATOR_HEIGHT = 40;
    private static final int MESSAGE_VIEW_PADDING = 5;
    private static final int SIZE_ANIMATION_DURATION = 700;
    private static final int APPEARING_ANIMATION_DURATION = 400;
    private static final int CIRCLE_INDICATOR_SIZE = 6;
    private static final int LINE_INDICATOR_WIDTH_SIZE = 3;
    private static final int STROKE_CIRCLE_INDICATOR_SIZE = 3;
    private static final int RADIUS_SIZE_TARGET_RECT = 15;
    private static final int MARGIN_INDICATOR = 15;

    private static final int BACKGROUND_COLOR = 0x99000000;
    private static final int CIRCLE_INNER_INDICATOR_COLOR = 0xffcccccc;
    private static final int CIRCLE_INDICATOR_COLOR = Color.WHITE;
    private static final int LINE_INDICATOR_COLOR = Color.WHITE;

    private final Paint selfPaint = new Paint();
    private final Paint paintLine = new Paint();
    private final Paint paintCircle = new Paint();
    private final Paint paintCircleInner = new Paint();
    private final Paint targetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Xfermode X_FER_MODE_CLEAR = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    private int index;
    private View target;
    private int lastIndex;
    private RectF targetRect;
    private final Rect selfRect = new Rect();

    private float density;
    private float stopY;
    private boolean isTop;
    private boolean mIsShowing;
    private int yMessageView = 0;

    private float startYLineAndCircle;
    private float circleIndicatorSize = 0;
    private float circleIndicatorSizeFinal;
    private float circleInnerIndicatorSize = 0;
    private float lineIndicatorWidthSize;
    private int messageViewPadding;
    private float marginGuide;
    private float strokeCircleWidth;
    private float indicatorHeight;

    private boolean isPerformedAnimationSize = false;

    private GuideListener mGuideListener;
    private Gravity mGravity;
    private DismissType dismissType;
    private PointerType pointerType;
    private final GuideMessageView mMessageView;
    BackGroundWhite backGroundWhite;
    RelativeLayout relativeLayout;

    private GuideView(Context context, View view, int lastIndex, String sessionKey, int index, Boolean isAllowToShowCheckBox, Boolean isOnlyFirstTime, boolean gotoNewClassWithButton, Class<?> linkClass) {
        super(context);
        SessionGuide.beginInitialization(context);

        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        this.target = view;
        this.lastIndex = lastIndex;
        this.index = index;

        density = context.getResources().getDisplayMetrics().density;
        init();


        if (view instanceof Targetable) {
            targetRect = ((Targetable) view).boundingRect();
        } else {
            int[] locationTarget = new int[2];
            target.getLocationOnScreen(locationTarget);
            targetRect = new RectF(
                    locationTarget[0],
                    locationTarget[1],
                    locationTarget[0] + target.getWidth(),
                    locationTarget[1] + target.getHeight()
            );
        }

        mMessageView = new GuideMessageView(getContext());
        mMessageView.setPadding(
                messageViewPadding,
                messageViewPadding,
                messageViewPadding,
                messageViewPadding
        );

        backGroundWhite = new BackGroundWhite(getContext());
        backGroundWhite.setPadding(
                messageViewPadding,
                messageViewPadding,
                messageViewPadding,
                messageViewPadding
        );

        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;

        LayoutParams lpsBackgroundMargin = new LayoutParams((widthPixels * 8 / 10), ViewGroup.LayoutParams.WRAP_CONTENT);
        lpsBackgroundMargin.setMargins(0, 25, 0, 0);

        LayoutParams lpsBackgroundMargin2 = new LayoutParams((widthPixels * 8 / 10), ViewGroup.LayoutParams.WRAP_CONTENT);
        lpsBackgroundMargin2.setMarginStart(25);

        backGroundWhite.setColor(Color.WHITE);

        // check box, button
        TextView btnSkip = new TextView(context);
        btnSkip.setTextColor(Color.WHITE);
        btnSkip.setTextSize(12);

        CheckBox checkBox = new CheckBox(context);

        TextView btnSkip2 = new TextView(context);
        CheckBox checkBox1 = new CheckBox(context);


        checkBox.setText(context.getResources().getString(R.string.dont_show_again));
        checkBox.setTextSize(12);
        checkBox.setTextColor(Color.WHITE);

        checkBox1.setText(context.getResources().getString(R.string.dont_show_again));
        checkBox1.setTextSize(12);
        checkBox1.setTextColor(Color.TRANSPARENT);

        if (SessionGuide.getSessionGlobalBoolean(sessionKey)) {
            checkBox.setChecked(SessionGuide.getSessionGlobalBoolean(sessionKey));
        }

        checkBox.setPadding(5, 5, 5, 5);
        checkBox1.setPadding(5, 5, 5, 5);

        checkBox.setOnClickListener(view1 -> {
                    if (checkBox.isChecked()) {
                        btnSkip.setText(R.string.skip);

                    } else {
                        if (index > 0) {
                            btnSkip.setText(R.string.lanjut);
                        } else {
                            btnSkip.setText(R.string.mulai_tur);
                        }
                    }
                }
        );

        if (index > 0 && index < lastIndex - 1) {
            btnSkip.setText(R.string.lanjut);
        } else if (index == lastIndex - 1) {
            btnSkip.setText(R.string.selesai);

        } else {
            btnSkip.setText(R.string.mulai_tur);
        }

        btnSkip2.setTextSize(12);
        btnSkip2.setTextColor(Color.TRANSPARENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkBox.setButtonTintList(context.getResources().getColorStateList(R.color.white));
            checkBox1.setButtonTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            btnSkip.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_baseline_arrow_forward_ios_24), null);
            btnSkip2.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_baseline_arrow_forward_ios_24), null);
        }

        btnSkip2.setPadding(5, 5, 5, 5);
        btnSkip.setPadding(5, 10, 5, 5);
        btnSkip.setGravity(View.TEXT_ALIGNMENT_CENTER);
        btnSkip.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btnSkip.setOnClickListener(v -> {
            if (checkBox.isChecked()) {

                SessionGuide.setSessionGlobalBoolean(sessionKey, true);
                dismiss(lastIndex);

            } else if (index == lastIndex - 1) {
                SessionGuide.setSessionGlobalBoolean(sessionKey, Boolean.TRUE.equals(isOnlyFirstTime));

                dismiss(index);
            } else {
                if (gotoNewClassWithButton && linkClass != null) {
                    ActivityHelper.showActivity((Activity) context, linkClass, false, sessionKey, sessionKey);
                }
                dismiss(index);

            }

        });


        // set relative button
        RelativeLayout relativeButton = new RelativeLayout(context);
        relativeButton.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        LayoutParams lps = (LayoutParams) generateDefaultLayoutParams();
        lps.setMargins(10, 0, 10, 0);

        relativeButton.setLayoutParams(lps);
        relativeButton.setPaddingRelative(0, 10, 0, 10);

        RelativeLayout relativeButtonTwo = new RelativeLayout(context);
        relativeButtonTwo.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        LayoutParams lpsBackground = (LayoutParams) generateDefaultLayoutParams();
        lpsBackground.setMargins(10, 0, 10, 0);
        relativeButtonTwo.setLayoutParams(lpsBackground);
        relativeButtonTwo.setPaddingRelative(0, 10, 0, 10);

        // set param
        LayoutParams paramsCheckbox = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsCheckbox.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsCheckbox.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        paramsCheckbox.addRule(RelativeLayout.LEFT_OF, btnSkip.getId());
        checkBox.setVisibility(GONE);
        checkBox.setLayoutParams(paramsCheckbox);

        LayoutParams paramsBtnSkip = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsBtnSkip.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsBtnSkip.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        btnSkip.setLayoutParams(paramsBtnSkip);

        LayoutParams paramsCheckbox1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsCheckbox1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsCheckbox1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        paramsCheckbox1.addRule(RelativeLayout.LEFT_OF, btnSkip2.getId());
        checkBox1.setVisibility(GONE);
        checkBox1.setLayoutParams(paramsCheckbox);

        LayoutParams paramsBtnSkip1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsBtnSkip1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsBtnSkip1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        btnSkip2.setLayoutParams(paramsBtnSkip1);


        if (index > 0) {
            checkBox.setVisibility(VISIBLE);
            checkBox1.setVisibility(VISIBLE);
        }

        // add view
        if (Boolean.TRUE.equals(isAllowToShowCheckBox)) {
            relativeButton.addView(checkBox);
            relativeButtonTwo.addView(checkBox1);
        }

        relativeButton.addView(btnSkip);
        relativeButtonTwo.addView(btnSkip2);

        relativeLayout = new RelativeLayout(context);

        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        mMessageView.setLayoutParams(lpsBackgroundMargin);
        backGroundWhite.setLayoutParams(lpsBackgroundMargin2);

        mMessageView.addView(relativeButton);
        backGroundWhite.addView(relativeButtonTwo);


        relativeLayout.addView(backGroundWhite);
        relativeLayout.addView(mMessageView);

        addView(
                relativeLayout
        );

        setMessageLocation(resolveMessageViewLocation());

        ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                setMessageLocation(resolveMessageViewLocation());

                if (target instanceof Targetable) {
                    targetRect = ((Targetable) target).boundingRect();
                } else {
                    int[] locationTarget = new int[2];
                    target.getLocationOnScreen(locationTarget);
                    targetRect = new RectF(
                            locationTarget[0],
                            locationTarget[1],
                            locationTarget[0] + target.getWidth(),
                            locationTarget[1] + target.getHeight()
                    );
                }

                selfRect.set(
                        getPaddingLeft(),
                        getPaddingTop(),
                        getWidth() - getPaddingRight(),
                        getHeight() - getPaddingBottom()
                );

                marginGuide = (int) (isTop ? marginGuide : -marginGuide);
                startYLineAndCircle = (isTop ? targetRect.bottom : targetRect.top) + marginGuide;
                stopY = yMessageView + indicatorHeight;
                startAnimationSize();
                getViewTreeObserver().addOnGlobalLayoutListener(this);
            }
        };
        getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
    }

    private void startAnimationSize() {
        if (!isPerformedAnimationSize) {
            final ValueAnimator circleSizeAnimator = ValueAnimator.ofFloat(
                    0f,
                    circleIndicatorSizeFinal
            );
            circleSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    circleIndicatorSize = (float) circleSizeAnimator.getAnimatedValue();
                    circleInnerIndicatorSize = (float) circleSizeAnimator.getAnimatedValue() - density;
                    postInvalidate();
                }
            });

            final ValueAnimator linePositionAnimator = ValueAnimator.ofFloat(
                    stopY,
                    startYLineAndCircle
            );
            linePositionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    startYLineAndCircle = (float) linePositionAnimator.getAnimatedValue();
                    postInvalidate();
                }
            });

            linePositionAnimator.setDuration(SIZE_ANIMATION_DURATION);
            linePositionAnimator.start();
            linePositionAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    circleSizeAnimator.setDuration(SIZE_ANIMATION_DURATION);
                    circleSizeAnimator.start();
                    isPerformedAnimationSize = true;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    //
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                    //
                }
            });
        }
    }

    private void init() {
        lineIndicatorWidthSize = LINE_INDICATOR_WIDTH_SIZE * density;
        marginGuide = MARGIN_INDICATOR * density;
        indicatorHeight = INDICATOR_HEIGHT * density;
        messageViewPadding = (int) (MESSAGE_VIEW_PADDING * density);
        strokeCircleWidth = STROKE_CIRCLE_INDICATOR_SIZE * density;
        circleIndicatorSizeFinal = CIRCLE_INDICATOR_SIZE * density;
    }

    private int getNavigationBarSize() {
        Resources resources = getContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private boolean isLandscape() {
        int display_mode = getResources().getConfiguration().orientation;
        return display_mode != Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (target != null) {

            selfPaint.setColor(BACKGROUND_COLOR);
            selfPaint.setStyle(Paint.Style.FILL);
            selfPaint.setAntiAlias(true);
            canvas.drawRect(selfRect, selfPaint);

            paintLine.setStyle(Paint.Style.FILL);
            paintLine.setColor(LINE_INDICATOR_COLOR);
            paintLine.setStrokeWidth(lineIndicatorWidthSize);
            paintLine.setAntiAlias(true);

            paintCircle.setStyle(Paint.Style.STROKE);
            paintCircle.setColor(CIRCLE_INDICATOR_COLOR);
            paintCircle.setStrokeCap(Paint.Cap.ROUND);
            paintCircle.setStrokeWidth(strokeCircleWidth);
            paintCircle.setAntiAlias(true);

            paintCircleInner.setStyle(Paint.Style.FILL);
            paintCircleInner.setColor(CIRCLE_INNER_INDICATOR_COLOR);
            paintCircleInner.setAntiAlias(true);

            final float x = (targetRect.left / 2 + targetRect.right / 2);

            switch (pointerType) {
                case circle:
                    canvas.drawLine(x, startYLineAndCircle, x, stopY, paintLine);
                    canvas.drawCircle(x, startYLineAndCircle, circleIndicatorSize, paintCircle);
                    canvas.drawCircle(x, startYLineAndCircle, circleInnerIndicatorSize, paintCircleInner);
                    break;
                case arrow:
                    canvas.drawLine(x, startYLineAndCircle, x, stopY, paintLine);
                    Path path = new Path();
                    if (isTop) {
                        path.moveTo(x, startYLineAndCircle - (circleIndicatorSize * 2));
                        path.lineTo(x + circleIndicatorSize, startYLineAndCircle);
                        path.lineTo(x - circleIndicatorSize, startYLineAndCircle);
                        path.close();
                    } else {
                        path.moveTo(x, startYLineAndCircle + (circleIndicatorSize * 2));
                        path.lineTo(x + circleIndicatorSize, startYLineAndCircle);
                        path.lineTo(x - circleIndicatorSize, startYLineAndCircle);
                        path.close();
                    }
                    canvas.drawPath(path, paintCircle);
                    break;
                case none:
                    //draw no line and no pointer
                    break;
            }
            targetPaint.setXfermode(X_FER_MODE_CLEAR);
            targetPaint.setAntiAlias(true);

            if (target instanceof Targetable) {
                canvas.drawPath(((Targetable) target).guidePath(), targetPaint);
            } else {
                canvas.drawRoundRect(
                        targetRect,
                        RADIUS_SIZE_TARGET_RECT,
                        RADIUS_SIZE_TARGET_RECT,
                        targetPaint
                );
            }
        }
    }

    public boolean isShowing() {
        return mIsShowing;
    }

    public void dismiss(int index) {
        ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).removeView(this);
        mIsShowing = false;
        if (mGuideListener != null) {
            mGuideListener.onDismiss(target, index);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (dismissType) {

                case outside:
                    if (!isViewContains(relativeLayout, x, y)) {
                        dismiss(index);
                    }
                    break;

                case anywhere:
                    dismiss(index);
                    break;

                case targetView:
                    if (targetRect.contains(x, y)) {
                        dismiss(index);
                    }
                    break;

                case selfView:
                    if (isViewContains(relativeLayout, x, y)) {
                        dismiss(index);
                    }
                    break;

                case outsideTargetAndMessage:
                    if (!(targetRect.contains(x, y) || isViewContains(relativeLayout, x, y))) {
                        dismiss(index);
                    }
            }
            return true;
        }
        return false;
    }

    private boolean isViewContains(View view, float rx, float ry) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        int w = view.getWidth();
        int h = view.getHeight();

        return !(rx < x || rx > x + w || ry < y || ry > y + h);
    }

    private void setMessageLocation(Point p) {
        relativeLayout.setX(p.x);
        relativeLayout.setY(p.y);
        postInvalidate();
    }

    public void updateGuideViewLocation() {
        requestLayout();
    }

    private Point resolveMessageViewLocation() {

        int xMessageView;
        if (mGravity == Gravity.center) {
            xMessageView = (int) (targetRect.left - relativeLayout.getWidth() / 2 + target.getWidth() / 2);
        } else {
            xMessageView = (int) (targetRect.right) - relativeLayout.getWidth();
        }

        if (isLandscape()) {
            xMessageView -= getNavigationBarSize();
        }

        if (xMessageView + relativeLayout.getWidth() > getWidth()) {
            xMessageView = getWidth() - relativeLayout.getWidth();
        }
        if (xMessageView < 0) {
            xMessageView = 0;
        }

        //set message view bottom
        if ((targetRect.top + (indicatorHeight)) > getHeight() / 2f) {
            isTop = false;
            yMessageView = (int) (targetRect.top - relativeLayout.getHeight() - indicatorHeight);
        }
        //set message view top
        else {
            isTop = true;
            yMessageView = (int) (targetRect.top + target.getHeight() + indicatorHeight);
        }

        if (yMessageView < 0) {
            yMessageView = 0;
        }

        return new Point(xMessageView, yMessageView);
    }

    public void show() {
        this.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        this.setClickable(false);

        ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).addView(this);
        AlphaAnimation startAnimation = new AlphaAnimation(0.0f, 1.0f);
        startAnimation.setDuration(APPEARING_ANIMATION_DURATION);
        startAnimation.setFillAfter(true);
        this.startAnimation(startAnimation);
        mIsShowing = true;
    }

    public void setTitle(String str) {
        mMessageView.setTitle(str);
        backGroundWhite.setTitle(str);
    }

    public void setContentText(String str) {
        mMessageView.setContentText(str);
        backGroundWhite.setContentText(str);
    }

    public void setLinkToActivity(String link) {
        mMessageView.setLinkText(link);
        backGroundWhite.setLinkText(link);
    }

    public void setLinkToActivityIntent(Class<?> linkClass, boolean gotoNewClassWithButton) {
        mMessageView.setLinkToActivity(linkClass, gotoNewClassWithButton);
        backGroundWhite.setLinkToActivity(linkClass, gotoNewClassWithButton);
    }

    public void setContentSpan(Spannable span) {
        mMessageView.setContentSpan(span);
        backGroundWhite.setContentSpan(span);
    }

    public void setTitleTypeFace(Typeface typeFace) {
        mMessageView.setTitleTypeFace(typeFace);
        backGroundWhite.setTitleTypeFace(typeFace);
    }

    public void setContentTypeFace(Typeface typeFace) {
        mMessageView.setContentTypeFace(typeFace);
        backGroundWhite.setContentTypeFace(typeFace);
    }

    public void setTitleTextSize(int size) {
        mMessageView.setTitleTextSize(size);
        backGroundWhite.setTitleTextSize(size);
    }

    public void setContentTextSize(int size) {
        mMessageView.setContentTextSize(size);
        backGroundWhite.setContentTextSize(size);
    }

    public void setBackGroundColor(int backGroundColor) {
        mMessageView.setColor(backGroundColor);
    }

    public static class Builder {

        private View targetView;
        private int lastIndex;
        private String sessionKey;
        private int backGroundColor;
        private String title, contentText;
        private Gravity gravity;
        private DismissType dismissType;
        private PointerType pointerType;
        private final Context context;
        private Spannable contentSpan;
        private Typeface titleTypeFace, contentTypeFace;
        private GuideListener guideListener;
        private int titleTextSize;
        private int contentTextSize;
        private float lineIndicatorHeight;
        private float lineIndicatorWidthSize;
        private float circleIndicatorSize;
        private float circleInnerIndicatorSize;
        private float strokeCircleWidth;
        private String linkText;
        private Class<?> linkClass;
        private boolean isOnlyFirstTime;
        private boolean gotoNewClassWithButton;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTargetView(View view) {
            this.targetView = view;
            return this;
        }

        public Builder setLastIndex(int lastIndex) {
            this.lastIndex = lastIndex;
            return this;
        }

        public Builder setSessionKey(String sessionKey) {
            this.sessionKey = sessionKey;
            return this;
        }

        public Builder setOnlyFirstTime(boolean onlyFirstTime) {
            this.isOnlyFirstTime = onlyFirstTime;
            return this;
        }

        public Builder setBackGroundColor(int color) {
            this.backGroundColor = color;
            return this;
        }

        /**
         * gravity GuideView
         *
         * @param gravity it should be one type of Gravity enum.
         **/
        public Builder setGravity(Gravity gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * defining a title
         *
         * @param title a title. for example: submit button.
         **/
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * defining a description for the target view
         *
         * @param contentText a description. for example: this button can for submit your information..
         **/
        public Builder setContentText(String contentText) {
            this.contentText = contentText;
            return this;
        }

        public Builder setLinkText(String linkText) {
            this.linkText = linkText;
            return this;
        }

        public Builder setLinkClass(Class<?> linkClass) {
            this.linkClass = linkClass;
            return this;
        }

        public Builder setGoToNewClassWithButton(boolean gotoNewClassWithButton) {
            this.gotoNewClassWithButton = gotoNewClassWithButton;
            return this;
        }

        /**
         * setting spannable type
         *
         * @param span a instance of spannable
         **/
        public Builder setContentSpan(Spannable span) {
            this.contentSpan = span;
            return this;
        }

        /**
         * setting font type face
         *
         * @param typeFace a instance of type face (font family)
         **/
        public Builder setContentTypeFace(Typeface typeFace) {
            this.contentTypeFace = typeFace;
            return this;
        }

        /**
         * adding a listener on show case view
         *
         * @param guideListener a listener for events
         **/
        public Builder setGuideListener(GuideListener guideListener) {
            this.guideListener = guideListener;
            return this;
        }

        /**
         * setting font type face
         *
         * @param typeFace a instance of type face (font family)
         **/
        public Builder setTitleTypeFace(Typeface typeFace) {
            this.titleTypeFace = typeFace;
            return this;
        }

        /**
         * the defined text size overrides any defined size in the default or provided style
         *
         * @param size title text by sp unit
         * @return builder
         */
        public Builder setContentTextSize(int size) {
            this.contentTextSize = size;
            return this;
        }

        /**
         * the defined text size overrides any defined size in the default or provided style
         *
         * @param size title text by sp unit
         * @return builder
         */
        public Builder setTitleTextSize(int size) {
            this.titleTextSize = size;
            return this;
        }

        /**
         * this method defining the type of dismissing function
         *
         * @param dismissType should be one type of DismissType enum. for example: outside -> Dismissing with click on outside of MessageView
         */
        public Builder setDismissType(DismissType dismissType) {
            this.dismissType = dismissType;
            return this;
        }

        /**
         * changing line height indicator
         *
         * @param height you can change height indicator (Converting to Dp)
         */
        public Builder setIndicatorHeight(float height) {
            this.lineIndicatorHeight = height;
            return this;
        }

        /**
         * changing line width indicator
         *
         * @param width you can change width indicator
         */
        public Builder setIndicatorWidthSize(float width) {
            this.lineIndicatorWidthSize = width;
            return this;
        }

        /**
         * changing circle size indicator
         *
         * @param size you can change circle size indicator
         */
        public Builder setCircleIndicatorSize(float size) {
            this.circleIndicatorSize = size;
            return this;
        }

        /**
         * changing inner circle size indicator
         *
         * @param size you can change inner circle indicator size
         */
        public Builder setCircleInnerIndicatorSize(float size) {
            this.circleInnerIndicatorSize = size;
            return this;
        }

        /**
         * changing stroke circle size indicator
         *
         * @param size you can change stroke circle indicator size
         */
        public Builder setCircleStrokeIndicatorSize(float size) {
            this.strokeCircleWidth = size;
            return this;
        }

        /**
         * this method defining the type of pointer
         *
         * @param pointerType should be one type of PointerType enum. for example: arrow -> To show arrow pointing to target view
         */
        public Builder setPointerType(PointerType pointerType) {
            this.pointerType = pointerType;
            return this;
        }

        public GuideView build(int index, boolean isAllowToShowCheckBox) {
            GuideView guideView = new GuideView(context, targetView, lastIndex, sessionKey
                    , index, isAllowToShowCheckBox
                    , isOnlyFirstTime, gotoNewClassWithButton, linkClass);
            guideView.mGravity = gravity != null ? gravity : Gravity.auto;
            guideView.dismissType = dismissType != null ? dismissType : DismissType.targetView;
            guideView.pointerType = pointerType != null ? pointerType : PointerType.circle;
            float density = context.getResources().getDisplayMetrics().density;

            guideView.setTitle(title);
            if (contentText != null) {
                guideView.setContentText(contentText);
            }
            if (titleTextSize != 0) {
                guideView.setTitleTextSize(titleTextSize);
            }
            if (contentTextSize != 0) {
                guideView.setContentTextSize(contentTextSize);
            }
            if (contentSpan != null) {
                guideView.setContentSpan(contentSpan);
            }
            if (titleTypeFace != null) {
                guideView.setTitleTypeFace(titleTypeFace);
            }
            if (contentTypeFace != null) {
                guideView.setContentTypeFace(contentTypeFace);
            }
            if (guideListener != null) {
                guideView.mGuideListener = guideListener;
            }
            if (lineIndicatorHeight != 0) {
                guideView.indicatorHeight = lineIndicatorHeight * density;
            }
            if (lineIndicatorWidthSize != 0) {
                guideView.lineIndicatorWidthSize = lineIndicatorWidthSize * density;
            }
            if (circleIndicatorSize != 0) {
                guideView.circleIndicatorSize = circleIndicatorSize * density;
            }
            if (circleInnerIndicatorSize != 0) {
                guideView.circleInnerIndicatorSize = circleInnerIndicatorSize * density;
            }
            if (strokeCircleWidth != 0) {
                guideView.strokeCircleWidth = strokeCircleWidth * density;
            }

            if (linkText != null) {
                guideView.setLinkToActivity(linkText);
            }

            if (linkClass != null) {
                guideView.setLinkToActivityIntent(linkClass, gotoNewClassWithButton);
            }

            if (backGroundColor != 0) {
                guideView.setBackGroundColor(backGroundColor);
            }

            return guideView;
        }
    }
}

