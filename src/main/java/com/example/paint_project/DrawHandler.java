package com.example.paint_project;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

// Enumerated datatype for the TYPE of draw happening
enum DrawType{
    NONE,
    FREE,
    LINE,
    SHAPE,
    PICKER
}

// Class for Objects that handle all drawing on the canvas
public class DrawHandler {
    private DrawType drawType;

    Point2D posA;
    boolean firstClick;
    double lineWidth;

    Color color = Color.BROWN;




    public DrawHandler(){
        drawType = DrawType.NONE;
        posA = new Point2D(0,0);
        firstClick = true;
        lineWidth = 20;
    }

    public void click(){
        firstClick = !firstClick;
    }

    public boolean isFirstClick(){
        return firstClick;
    }

    public double getLineWidth(){
        return lineWidth;
    }

    public Color getCurrentColor(){
        return color;
    }

    public DrawType getDrawType(){
        return drawType;
    }

    public Point2D getPosA(){
        return posA;
    }

    public double getPosAX(){
        return posA.getX();
    }

    public double getPosAY(){
        return posA.getY();
    }


    public void setLineWidth(double i) {
        lineWidth = i;
    }

    public void setCurrentColor(Color c){
        color = c;
    }

    public void setDrawType(DrawType d){
        drawType = d;
    }

    public void setDrawType(String s){
        switch (s){
            case "FREE": drawType = DrawType.FREE;
                break;
            case "LINE": drawType = DrawType.LINE;
                break;
            case "SHAPE" : drawType = DrawType.SHAPE;
                break;
            default: drawType = DrawType.NONE;
                break;
        }
    }

    public void setPosA(double x, double y){
        posA = new Point2D(x, y);
    }




}
