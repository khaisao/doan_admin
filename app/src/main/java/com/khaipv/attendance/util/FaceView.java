package com.khaipv.attendance.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Size;
import android.view.View;

import androidx.annotation.Nullable;

import com.kbyai.facesdk.FaceBox;
import com.khaipv.attendance.ui.teacher.faceReco.FaceRecoKbiFragment;

import java.util.List;

public class FaceView extends View {

    private Context context;
    private Paint realPaint;
    private Paint spoofPaint;

    private Size frameSize;

    private List<FaceBox> faceBoxes;
    private List<FaceRecoKbiFragment.FaceDataWithName> faceDataWithName;
    private String name;

    public FaceView(Context context) {
        this(context, null);

        this.context = context;
        init();
    }

    public FaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }

    public void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        realPaint = new Paint();
        realPaint.setStyle(Paint.Style.STROKE);
        realPaint.setStrokeWidth(3);
        realPaint.setColor(Color.GREEN);
        realPaint.setAntiAlias(true);
        realPaint.setTextSize(50);

        spoofPaint = new Paint();
        spoofPaint.setStyle(Paint.Style.STROKE);
        spoofPaint.setStrokeWidth(3);
        spoofPaint.setColor(Color.RED);
        spoofPaint.setAntiAlias(true);
        spoofPaint.setTextSize(50);
    }

    public void setFrameSize(Size frameSize)
    {
        this.frameSize = frameSize;
    }

    public void setFaceBoxes(List<FaceBox> faceBoxes, String name)
    {
        this.faceBoxes = faceBoxes;
        this.name = name;
        invalidate();
    }

    public void setFaceBoxes(List<FaceRecoKbiFragment.FaceDataWithName> faceDataWithName)
    {
        this.faceDataWithName = faceDataWithName;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        if (frameSize != null &&  faceBoxes != null) {
//            float x_scale = this.frameSize.getWidth() / (float)canvas.getWidth();
//            float y_scale = this.frameSize.getHeight() / (float)canvas.getHeight();
//
//            for (int i = 0; i < faceBoxes.size(); i++) {
//                FaceBox faceBox = faceBoxes.get(i);
//                    realPaint.setStrokeWidth(3);
//                    realPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//                    if(faceBox == null){
//
//                    }
//                    canvas.drawText(name, (faceBox.x1 / x_scale) + 10, (faceBox.y1 / y_scale) - 30, realPaint);
//
//                    realPaint.setStyle(Paint.Style.STROKE);
//                    realPaint.setStrokeWidth(5);
//                    canvas.drawRect(new Rect((int)(faceBox.x1 / x_scale), (int)(faceBox.y1 / y_scale),
//                            (int)(faceBox.x2 / x_scale), (int)(faceBox.y2 / y_scale)), realPaint);
//            }
//        }

        if (frameSize != null &&  faceDataWithName != null) {
            float x_scale = this.frameSize.getWidth() / (float)canvas.getWidth();
            float y_scale = this.frameSize.getHeight() / (float)canvas.getHeight();

            for (int i = 0; i < faceDataWithName.size(); i++) {
                FaceBox faceBox = faceDataWithName.get(i).getFace();
                String name = faceDataWithName.get(i).getName();
                realPaint.setStrokeWidth(3);
                realPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawText(name, (faceBox.x1 / x_scale) + 10, (faceBox.y1 / y_scale) - 30, realPaint);

                realPaint.setStyle(Paint.Style.STROKE);
                realPaint.setStrokeWidth(5);
                canvas.drawRect(new Rect((int)(faceBox.x1 / x_scale), (int)(faceBox.y1 / y_scale),
                        (int)(faceBox.x2 / x_scale), (int)(faceBox.y2 / y_scale)), realPaint);
            }
        }
    }
}
