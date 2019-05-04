package com.wzy.sandezi.algorithm;

import com.wzy.sandezi.beans.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoCatch {

    private List<Point> points;
    private List<Point> pointsN;
    private Point pointZero;

    public VideoCatch(List<Point> points) {
        this.points = points;
    }


    private void searchZeroPoint() {
        pointZero = new Point();
        double sum = 0, lastSum = 0;
        int index = 0;
        for (int i = 0; i < points.size(); i++) {
            //取平方和最小的
            sum = points.get(i).getPoint_x() * points.get(i).getPoint_x() + points.get(i).getPoint_y() * points.get(i).getPoint_y();
            if (lastSum == 0 || lastSum > sum) {
                lastSum = sum;
                index = i;
            }
        }
        pointZero = points.get(index);
        points.remove(index);
    }

    public List<Point> getPointSort() {
        searchZeroPoint();
        pointsN = new ArrayList<>();
        int k = points.size();
        int j;
        boolean flag = true;
        while (flag) {
            flag = false;
            for (j = 1; j < k; j++) {
                if (degreeFor(points.get(j - 1)) > degreeFor(points.get(j))) {
                    Collections.swap(points, j - 1, j);
                    flag = true;


                }
            }
            k--;
        }
        pointsN.add(pointZero);
        pointsN.addAll(points);
        return pointsN;
    }

    private double degreeFor(Point point) {
        double d = 0;
        if (pointZero != null) {
            d = (point.getPoint_x() - pointZero.getPoint_x()) / point.getPoint_y();
        }
        return d;
    }

}
