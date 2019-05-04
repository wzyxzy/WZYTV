package com.wzy.sandezi.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.wzy.sandezi.algorithm.VideoCatch;
import com.wzy.sandezi.beans.Point;

import java.util.List;

public class MyLineActivity extends AppCompatActivity {
    private List<Point> points;
    private List<Point> pointsN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CustomView1(this));
        points = getIntent().getParcelableArrayListExtra("pointList");
        VideoCatch videoCatch = new VideoCatch(points);
        pointsN = videoCatch.getPointSort();
        Log.d("pointsN",pointsN.toString());
    }

    /**
     * 使用内部类 自定义一个简单的View
     *
     * @author Administrator
     */
    class CustomView1 extends View {

        Paint paint;
        Paint paintText;

        public CustomView1(Context context) {
            super(context);
            paint = new Paint();
            paintText = new Paint();
            paint.setColor(Color.BLACK);
            paintText.setColor(Color.RED);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(10);
            paintText.setStrokeWidth(10);
        }

        //在这里我们将测试canvas提供的绘制图形方法
        @Override
        protected void onDraw(Canvas canvas) {
            for (int i = 0; i < pointsN.size(); i++) {
                canvas.drawText("（" + pointsN.get(i).getPoint_x() + "," + pointsN.get(i).getPoint_y() + ")", pointsN.get(i).getPoint_x() * 10, pointsN.get(i).getPoint_y() * 10, paintText);
                if (i < pointsN.size() - 1) {
                    canvas.drawLine(pointsN.get(i).getPoint_x() * 10, pointsN.get(i).getPoint_y() * 10, pointsN.get(i + 1).getPoint_x() * 10, pointsN.get(i + 1).getPoint_y() * 10, paint);

                } else {
                    canvas.drawLine(pointsN.get(i).getPoint_x() * 10, pointsN.get(i).getPoint_y() * 10, pointsN.get(0).getPoint_x() * 10, pointsN.get(0).getPoint_y() * 10, paint);

                }
            }

        }

    }
}
