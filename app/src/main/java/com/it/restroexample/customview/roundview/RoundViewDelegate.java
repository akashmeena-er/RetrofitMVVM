package com.it.restroexample.customview.roundview;

import com.it.restroexample.R;

public class RoundViewDelegate {
    private android.view.View view;
    private android.content.Context context;
    private android.graphics.drawable.GradientDrawable gd_background = new android.graphics.drawable.GradientDrawable();
    private android.graphics.drawable.GradientDrawable gd_background_press = new android.graphics.drawable.GradientDrawable();
    private int backgroundColor;
    private int backgroundPressColor;
    private int cornerRadius;
    private int cornerRadius_TL;
    private int cornerRadius_TR;
    private int cornerRadius_BL;
    private int cornerRadius_BR;
    private int strokeWidth;
    private int strokeColor;
    private int strokePressColor;
    private int textPressColor;
    private boolean isRadiusHalfHeight;
    private boolean isRadiusHalfHeightLeft;
    private boolean isRadiusHalfHeightRight;
    private boolean isWidthHeightEqual;
    private boolean isRippleEnable;
    private float[] radiusArr = new float[8];

    public RoundViewDelegate(android.view.View view, android.content.Context context, android.util.AttributeSet attrs) {
        this.view = view;
        this.context = context;
        obtainAttributes(context, attrs);
    }

    private void obtainAttributes(android.content.Context context, android.util.AttributeSet attrs) {
        android.content.res.TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundTextView);
        backgroundColor = ta.getColor(R.styleable.RoundTextView_rv_backgroundColor, android.graphics.Color.TRANSPARENT);
        backgroundPressColor = ta.getColor(R.styleable.RoundTextView_rv_backgroundPressColor, Integer.MAX_VALUE);
        cornerRadius = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius, 0);
        strokeWidth = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_strokeWidth, 0);
        strokeColor = ta.getColor(R.styleable.RoundTextView_rv_strokeColor, android.graphics.Color.TRANSPARENT);
        strokePressColor = ta.getColor(R.styleable.RoundTextView_rv_strokePressColor, Integer.MAX_VALUE);
        textPressColor = ta.getColor(R.styleable.RoundTextView_rv_textPressColor, Integer.MAX_VALUE);
        isRadiusHalfHeight = ta.getBoolean(R.styleable.RoundTextView_rv_isRadiusHalfHeight, false);
        isWidthHeightEqual = ta.getBoolean(R.styleable.RoundTextView_rv_isWidthHeightEqual, false);
        cornerRadius_TL = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_TL, 0);
        cornerRadius_TR = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_TR, 0);
        cornerRadius_BL = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_BL, 0);
        cornerRadius_BR = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_BR, 0);
        isRippleEnable = ta.getBoolean(R.styleable.RoundTextView_rv_isRippleEnable, true);
        isRadiusHalfHeightLeft = ta.getBoolean(R.styleable.RoundTextView_rv_isRadiusHalfHeightLeft, false);
        isRadiusHalfHeightRight = ta.getBoolean(R.styleable.RoundTextView_rv_isRadiusHalfHeightRight, false);
        ta.recycle();
    }


    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = dp2px(cornerRadius);
        setBgSelector();
    }

    public void setCornerRadiusLeft(int cornerRadius) {
        this.cornerRadius_BL = dp2px(cornerRadius);
        this.cornerRadius_TL = dp2px(cornerRadius);
        setBgSelector();
    }

    public void setCornerRadiusRight(int cornerRadius) {
        this.cornerRadius_BR = dp2px(cornerRadius);
        this.cornerRadius_TR = dp2px(cornerRadius);
        setBgSelector();
    }








    public boolean isRadiusHalfHeight() {
        return isRadiusHalfHeight;
    }

    public boolean isRadiusHalfHeightLeft() {
        return isRadiusHalfHeightLeft;
    }

    public boolean isRadiusHalfHeightRight() {
        return isRadiusHalfHeightRight;
    }

    public boolean isWidthHeightEqual() {
        return isWidthHeightEqual;
    }


    protected int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = this.context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    private void setDrawable(android.graphics.drawable.GradientDrawable gd, int color, int strokeColor) {
        gd.setColor(color);

        //The corners are ordered top-left, top-right, bottom-right, bottom-left
        if (cornerRadius_TL > 0 || cornerRadius_TR > 0 || cornerRadius_BR > 0 || cornerRadius_BL > 0) {
            radiusArr[0] = cornerRadius_TL;
            radiusArr[1] = cornerRadius_TL;
            radiusArr[2] = cornerRadius_TR;
            radiusArr[3] = cornerRadius_TR;
            radiusArr[4] = cornerRadius_BR;
            radiusArr[5] = cornerRadius_BR;
            radiusArr[6] = cornerRadius_BL;
            radiusArr[7] = cornerRadius_BL;
            gd.setCornerRadii(radiusArr);
        } else {
            gd.setCornerRadius(cornerRadius);
        }

        gd.setStroke(strokeWidth, strokeColor);
    }

    public void setBgSelector() {
        android.graphics.drawable.StateListDrawable bg = new android.graphics.drawable.StateListDrawable();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP && isRippleEnable) {
            setDrawable(gd_background, backgroundColor, strokeColor);
            android.graphics.drawable.RippleDrawable rippleDrawable = new android.graphics.drawable.RippleDrawable(
                    getPressedColorSelector(backgroundColor, backgroundPressColor), gd_background, null);
            view.setBackground(rippleDrawable);
        } else {
            setDrawable(gd_background, backgroundColor, strokeColor);
            bg.addState(new int[]{-android.R.attr.state_pressed}, gd_background);
            if (backgroundPressColor != Integer.MAX_VALUE || strokePressColor != Integer.MAX_VALUE) {
                setDrawable(gd_background_press, backgroundPressColor == Integer.MAX_VALUE ? backgroundColor : backgroundPressColor,
                        strokePressColor == Integer.MAX_VALUE ? strokeColor : strokePressColor);
                bg.addState(new int[]{android.R.attr.state_pressed}, gd_background_press);
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {//16
                view.setBackground(bg);
            } else {
                //noinspection deprecation
                view.setBackgroundDrawable(bg);
            }
        }

        if (view instanceof android.widget.TextView) {
            if (textPressColor != Integer.MAX_VALUE) {
                android.content.res.ColorStateList textColors = ((android.widget.TextView) view).getTextColors();
                android.content.res.ColorStateList colorStateList = new android.content.res.ColorStateList(
                        new int[][]{new int[]{-android.R.attr.state_pressed}, new int[]{android.R.attr.state_pressed}},
                        new int[]{textColors.getDefaultColor(), textPressColor});
                ((android.widget.TextView) view).setTextColor(colorStateList);
            }
        }
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    private android.content.res.ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {
        return new android.content.res.ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_activated},
                        new int[]{}
                },
                new int[]{
                        pressedColor,
                        pressedColor,
                        pressedColor,
                        normalColor
                }
        );
    }
}
