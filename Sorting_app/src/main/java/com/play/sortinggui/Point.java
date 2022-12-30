package com.play.sortinggui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Point extends Circle {
    public Point() {
    }

    public void setColor(Color color) {
        this.setStroke(color);
    }
}
