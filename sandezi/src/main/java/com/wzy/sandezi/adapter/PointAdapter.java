package com.wzy.sandezi.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wzy.sandezi.R;
import com.wzy.sandezi.beans.Point;

import java.util.List;

public class PointAdapter extends WZYBaseAdapter<Point> {

    private final List<Point> data;
//    private final Context context;
    private int selectedEditTextPositionX = -1;
    private int selectedEditTextPositionY = -1;

    public PointAdapter(List<Point> data, Context context, int layoutRes) {
        super(data, context, layoutRes);
        this.data = data;
//
//        this.context = context;
    }

    @Override
    public void bindData(ViewHolder holder, Point point, final int indexPostion) {

        TextView id_num = (TextView) holder.getView(R.id.number);
        EditText x_point = (EditText) holder.getView(R.id.x_point);
        EditText y_point = (EditText) holder.getView(R.id.y_point);
        id_num.setText(indexPostion + 1 + "");

        x_point.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    EditText editText = (EditText) view;
                    selectedEditTextPositionX = (int) editText.getTag();
                }
                return false;
            }
        }); // 正确写法
        x_point.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                EditText editText = (EditText) view;
                if (b) {
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            data.get(indexPostion).setPoint_x(Float.valueOf(String.valueOf(editable)));

                        }
                    });
                }


            }
        });
        y_point.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    EditText editText = (EditText) view;
                    selectedEditTextPositionY = (int) editText.getTag();
                }
                return false;
            }
        }); // 正确写法
        y_point.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                EditText editText = (EditText) view;
                if (b) {
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            data.get(indexPostion).setPoint_y(Float.valueOf(String.valueOf(editable)));
                        }
                    });
                }


            }
        });
        x_point.setTag(indexPostion);
        y_point.setTag(indexPostion);

        if (selectedEditTextPositionX != -1 && indexPostion == selectedEditTextPositionX) { // 保证每个时刻只有一个EditText能获取到焦点
            x_point.requestFocus();
        } else if (selectedEditTextPositionY != -1 && indexPostion == selectedEditTextPositionY) { // 保证每个时刻只有一个EditText能获取到焦点
            y_point.requestFocus();
        } else {
            x_point.clearFocus();
            y_point.clearFocus();
        }

        x_point.setText(String.valueOf(data.get(indexPostion).getPoint_x()));
//        x_point.setSelection(x_text.length());

        y_point.setText(String.valueOf(data.get(indexPostion).getPoint_y()));
//        y_point.setSelection(y_text.length());


    }


}
