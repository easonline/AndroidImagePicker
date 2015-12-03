/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.pizidea.imagepicker.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.pizidea.imagepicker.Util;
import com.pizidea.imagepicker.ui.AvatarCropFragment;

/**
 * <b>Desc your Class</b><br/>
 * Created by Eason.Lai on 2015/11/1 10:42 <br/>
 * contact：easonline7@gmail.com <br/>
 */
public class SuperImageView extends ImageView {

    static  float MAX_SCALE = 4.0f;
    float imageW;
    float imageH;
    float rotatedImageW;
    float rotatedImageH;
    float viewW;
    float viewH;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    static final int NONE = 0;// init state
    static final int DRAG = 1;// drag
    static final int ZOOM = 2;// zoom
    static final int ROTATE = 3;// rotate
    static final int ZOOM_OR_ROTATE = 4; // zoom or rotate
    int mode = NONE;

    PointF pA = new PointF();
    PointF pB = new PointF();
    PointF mid = new PointF();
    PointF lastClickPos = new PointF();
    long lastClickTime = 0;
    double rotation = 0.0;
    float dist = 1f;

    private int topBottomMargins ;
    private int leftMargins = 30;//there are margins at left and right side of the ImageView

    public SuperImageView(Context context) {
        super(context);
        init();
    }

    public SuperImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SuperImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setScaleType(ImageView.ScaleType.MATRIX);
    }

    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setImageWidthHeight();
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setImageWidthHeight();
    }

    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setImageWidthHeight();
    }

    private void setImageWidthHeight() {
        Drawable d = getDrawable();
        if (d == null) {
            return;
        }
        imageW = rotatedImageW = d.getIntrinsicWidth();
        imageH = rotatedImageH = d.getIntrinsicHeight();
        initImage();
    }

    public double getImageRotation() {
        return rotation;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewW = w;
        viewH = h;
        //topBottomMargins = h - Util.dp2px(getContext(),330);
        topBottomMargins = h - w + leftMargins * 2;
        if (oldw == 0) {
            initImage();
        } else {
            fixScale();
            fixTranslation();
            setImageMatrix(matrix);
        }
    }

    private void initImage() {
        if (viewW <= 0 || viewH <= 0 || imageW <= 0 || imageH <= 0) {
            return;
        }
        mode = NONE;
        matrix.setScale(0, 0);
        fixScale();
        fixTranslation();
        centerBitmap();

        MAX_SCALE *= 2.25;//TODO calculate the max scale
        setImageMatrix(matrix);
    }

    private void fixScale() {
        float p[] = new float[9];
        matrix.getValues(p);
        float curScale = Math.abs(p[0]) + Math.abs(p[1]);

        float minScale = Math.max((float) (viewW - leftMargins*2) / (float) rotatedImageW,
                (float) (viewH - topBottomMargins) / (float) rotatedImageH);
        if (curScale < minScale) {
            if (curScale > 0) {
                double scale = minScale / curScale;
                p[0] = (float) (p[0] * scale);
                p[1] = (float) (p[1] * scale);
                p[3] = (float) (p[3] * scale);
                p[4] = (float) (p[4] * scale);
                matrix.setValues(p);
            } else {
                matrix.setScale(minScale, minScale);
            }
        }
    }

    private float maxPostScale() {
        float p[] = new float[9];
        matrix.getValues(p);
        float curScale = Math.abs(p[0]) + Math.abs(p[1]);
        /*if(curScale >=4){
            return .0f;
        }*/

        float minScale = Math.min((float) viewW / (float) rotatedImageW,
                (float) viewH / (float) rotatedImageH);
        float maxScale = Math.max(minScale, MAX_SCALE);
        return maxScale / curScale;
    }

    private void fixTranslation() {
        RectF rect = new RectF(0, 0, imageW, imageH);
        matrix.mapRect(rect);

        float height = rect.height();
        float width = rect.width();

        float deltaX = 0, deltaY = 0;

        if (width < viewW-leftMargins*2) {
            deltaX = (viewW - width) / 2 - rect.left+leftMargins;
        } else if (rect.left > leftMargins) {
            deltaX = -rect.left+leftMargins;
        } else if (rect.right < viewW-leftMargins) {
            deltaX = viewW - rect.right-leftMargins;
        }

        if (height < viewH-topBottomMargins) {
            deltaY = (viewH - height) / 2 - rect.top;
        } else if (rect.top > topBottomMargins/2) {
            deltaY = -rect.top+topBottomMargins/2;
        } else if (rect.bottom < viewH-topBottomMargins/2) {
            deltaY = viewH - rect.bottom-topBottomMargins/2;
        }
        matrix.postTranslate(deltaX, deltaY);
    }

    /**
     * make the image bitmap center
     */
    private void centerBitmap() {
        RectF rect = new RectF(0, 0, imageW, imageH);
        matrix.mapRect(rect);

        float height = rect.height();
        float width = rect.width();

        float deltaX = 0, deltaY = 0;

        deltaX = (viewW - width - leftMargins*2)/2;
        deltaY = -(viewH - height - topBottomMargins)/2;

        matrix.postTranslate(deltaX, deltaY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // first point down
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                pA.set(event.getX(), event.getY());
                pB.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            // second point down
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getActionIndex() > 1)
                    break;
                dist = spacing(event.getX(0), event.getY(0), event.getX(1),
                        event.getY(1));
                //if >10 continuous,make it multi mode
                if (dist > 10f) {
                    savedMatrix.set(matrix);
                    pA.set(event.getX(0), event.getY(0));
                    pB.set(event.getX(1), event.getY(1));
                    mid.set((event.getX(0) + event.getX(1)) / 2,
                            (event.getY(0) + event.getY(1)) / 2);
                    mode = ZOOM_OR_ROTATE;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (mode == DRAG) {
                    if (spacing(pA.x, pA.y, pB.x, pB.y) < 50) {
                        long now = System.currentTimeMillis();
                        if (now - lastClickTime < 500
                                && spacing(pA.x, pA.y, lastClickPos.x,
                                lastClickPos.y) < 50) {
                            doubleClick(pA.x, pA.y);
                            now = 0;
                        }
                        lastClickPos.set(pA);
                        lastClickTime = now;
                    }
                } else if (mode == ROTATE) {
                    int level = (int) Math.floor((rotation + Math.PI / 4)
                            / (Math.PI / 2));
                    if (level == 4)
                        level = 0;
                    matrix.set(savedMatrix);
                    matrix.postRotate(90 * level, mid.x, mid.y);
                    if (level == 1 || level == 3) {
                        float tmp = rotatedImageW;
                        rotatedImageW = rotatedImageH;
                        rotatedImageH = tmp;
                        fixScale();
                    }
                    fixTranslation();
                    setImageMatrix(matrix);
                }
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:

                if (mode == ZOOM_OR_ROTATE) {
                    PointF pC = new PointF(event.getX(1) - event.getX(0) + pA.x,
                            event.getY(1) - event.getY(0) + pA.y);
                    double a = spacing(pB.x, pB.y, pC.x, pC.y);
                    double b = spacing(pA.x, pA.y, pC.x, pC.y);
                    double c = spacing(pA.x, pA.y, pB.x, pB.y);
                    if (a >= 10) {
                        double cosB = (a * a + c * c - b * b) / (2 * a * c);
                        double angleB = Math.acos(cosB);
                        double PID4 = Math.PI / 4;
                        if (angleB > PID4 && angleB < 3 * PID4) {
                            mode = ROTATE;
                            rotation = 0;
                        } else {
                            mode = ZOOM;
                        }
                    }
                }

                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    pB.set(event.getX(), event.getY());
                    matrix.postTranslate(event.getX() - pA.x, event.getY() - pA.y);
                    fixTranslation();
                    setImageMatrix(matrix);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event.getX(0), event.getY(0),
                            event.getX(1), event.getY(1));
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float tScale = Math.min(newDist / dist, maxPostScale());
                        if(tScale==0){
                            break;
                        }else{
                            matrix.postScale(tScale, tScale, mid.x, mid.y);
                            fixScale();
                            fixTranslation();
                            setImageMatrix(matrix);
                        }
                    }
                } else if (mode == ROTATE) {
                    PointF pC = new PointF(event.getX(1) - event.getX(0) + pA.x,
                            event.getY(1) - event.getY(0) + pA.y);
                    double a = spacing(pB.x, pB.y, pC.x, pC.y);
                    double b = spacing(pA.x, pA.y, pC.x, pC.y);
                    double c = spacing(pA.x, pA.y, pB.x, pB.y);
                    if (b > 10) {
                        double cosA = (b * b + c * c - a * a) / (2 * b * c);
                        double angleA = Math.acos(cosA);
                        double ta = pB.y - pA.y;
                        double tb = pA.x - pB.x;
                        double tc = pB.x * pA.y - pA.x * pB.y;
                        double td = ta * pC.x + tb * pC.y + tc;
                        if (td > 0) {
                            angleA = 2 * Math.PI - angleA;
                        }
                        rotation = angleA;
                        matrix.set(savedMatrix);
                        matrix.postRotate((float) (rotation * 180 / Math.PI),
                                mid.x, mid.y);
                        setImageMatrix(matrix);
                    }

                }
                break;
        }
        return true;
    }

    /**
     * distance of two points
     */
    private float spacing(float x1, float y1, float x2, float y2) {
        float x = x1 - x2;
        float y = y1 - y2;
        return (float) Math.sqrt(x * x + y * y);
    }

    private void doubleClick(float x, float y) {
        float p[] = new float[9];
        matrix.getValues(p);
        float curScale = Math.abs(p[0]) + Math.abs(p[1]);

        float minScale = Math.max((float) (viewW - leftMargins*2) / (float) rotatedImageW,
                (float) (viewH - topBottomMargins) / (float) rotatedImageH);
        if (curScale <= minScale + 0.01) { // zoom
            float toScale = Math.max(minScale, MAX_SCALE) / curScale;
            matrix.postScale(toScale, toScale, x, y);
        } else { // scale
            float toScale = minScale / curScale;
            matrix.postScale(toScale, toScale, x, y);
            fixTranslation();
        }
        setImageMatrix(matrix);
    }


    /**
     * <b>get the scaled display image Matrix Rect to calculate the zoom ratio<b/><br/>
     * @return
     */
    public RectF getMatrixRect(){
        Matrix matrix = this.matrix;
        RectF rectF = new RectF(0.0F, 0.0F, 0.0F, 0.0F);

        rectF.set(0.0F, 0.0F, getDrawable().getIntrinsicWidth(),getDrawable().getIntrinsicHeight());

        matrix.mapRect(rectF);

        return rectF;

    }


}
