package com.wzy.sandezi.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class Point implements Parcelable {
    private float point_x;
    private float point_y;

    public Point(float point_x, float point_y) {
        this.point_x = point_x;
        this.point_y = point_y;
    }

    public Point() {
    }

    protected Point(Parcel in) {
        point_x = in.readFloat();
        point_y = in.readFloat();
    }

    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

    public float getPoint_x() {
        return point_x;
    }

    public void setPoint_x(float point_x) {
        this.point_x = point_x;
    }

    public float getPoint_y() {
        return point_y;
    }

    public void setPoint_y(float point_y) {
        this.point_y = point_y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "point_x=" + point_x +
                ", point_y=" + point_y +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(point_x);
        parcel.writeFloat(point_y);
    }
}
