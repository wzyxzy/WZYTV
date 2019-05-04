package com.wzy.sandezi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.wzy.sandezi.R;
import com.wzy.sandezi.adapter.PointAdapter;
import com.wzy.sandezi.beans.Point;

import java.util.ArrayList;
import java.util.List;

public class PaintPointActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView pointList;
    private Button addPoint;
    private Button consult;
    private ArrayList<Point> points;
    private PointAdapter pointAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_point);
        initView();
        initData();

    }

    private void initData() {
        points = new ArrayList<>();
        points.add(new Point(0, 0));
        pointAdapter = new PointAdapter(points, this, R.layout.item_point);
        pointList.setAdapter(pointAdapter);
    }

    private void initView() {
        pointList = findViewById(R.id.pointList);
        addPoint = findViewById(R.id.addPoint);
        consult = findViewById(R.id.consult);

        addPoint.setOnClickListener(this);
        consult.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addPoint:
                points.add(new Point(0, 0));
                pointAdapter.notifyDataSetChanged();

                break;
            case R.id.consult:
                Log.d("points", points.toString());

                Intent intent = new Intent(this, MyLineActivity.class);
                intent.putParcelableArrayListExtra("pointList", points);
                startActivity(intent);
                break;
        }
    }
}
