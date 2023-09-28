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

enum ShapeType{
    NONE,
    CIRCLE,
    TRIANGLE,
    ELLIPSE,
    SQUARE,
    RECTANGLE
}

// Class for Objects that handle all drawing on the canvas
public class DrawHandler {
    private DrawType drawType;
    private ShapeType shapeType;

    Point2D posA;
    Point2D posB;
    private boolean firstClick;
    private double lineWidth;
    private int points;
    double[] pX;
    double[] pY;

    Color color = Color.BROWN;



    // METHODS
    //

    // Constructors
    public DrawHandler(){
        drawType = DrawType.NONE;
        shapeType = ShapeType.NONE;
        posA = new Point2D(0,0);
        posB = new Point2D(0,0);
        firstClick = true;
        lineWidth = 20;
        points = 0;
        pX = new double[4];
        pY = new double[4];
    }

    public void click(){
        firstClick = !firstClick;
    }

    public boolean isFirstClick(){
        return firstClick;
    }

    // GETTERS
    public double getLineWidth(){
        return lineWidth;
    }

    public Color getCurrentColor(){
        return color;
    }

    public DrawType getDrawType(){
        return drawType;
    }

    public ShapeType getShapeType(){return shapeType;}


    public Point2D getPosA(){
        return posA;
    }
    public Point2D getPosB() {
        return posB;
    }

    public double getPosAX(){
        return posA.getX();
    }

    public double getPosAY(){
        return posA.getY();
    }

    public int getPoints(){return points;}

    // SETTERS
    public void setLineWidth(double i) {
        lineWidth = i;
    }

    public void setCurrentColor(Color c){
        color = c;
    }

    public void setDrawType(DrawType d){
        drawType = d;
    }

    public void setShapeType(ShapeType s){shapeType = s;}

    public void resetShapeType(){shapeType = ShapeType.NONE;}

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

    public void setPosB(double x, double y){posB = new Point2D(x, y);}

    public void setPoints(int p){
        points = p;
    }




}
