package com.pizidea.imagepicker.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.accessibility.AccessibilityEvent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

public class SuperCheckBox extends CompoundButton {

    private boolean canChecked = true;

    public SuperCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SuperCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuperCheckBox(Context context) {
        super(context);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return CheckBox.class.getName();
    }

    public boolean isCanChecked() {
        return canChecked;
    }

    public void setCanChecked(boolean canChecked) {
        this.canChecked = canChecked;
    }


    @Override
    public void toggle() {
        if(canChecked){
            super.toggle();
        }
    }

    @Override
    public boolean performClick() {

        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }

        return handled;
    }


}
