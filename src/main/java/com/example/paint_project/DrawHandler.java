package com.example.paint_project;


import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import javax.sound.sampled.Line;
import java.io.IOException;
import java.nio.file.Path;

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

enum LineType{
    SOLID,
    DASH,
    DOT
}
enum ShapeStyle{
    FILLED,
    OUTLINE,
    DASH
}

// Class for Objects that handle all drawing on the canvas
public class DrawHandler {
    private DrawType drawType;
    private ShapeType shapeType;
    private LineType lineType;
    private ShapeStyle shapeStyle;

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
        lineType = LineType.SOLID;
        shapeStyle = ShapeStyle.FILLED;
        posA = new Point2D(0,0);
        posB = new Point2D(0,0);
        firstClick = true;
        lineWidth = 10;
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
    public LineType getLineType(){ return lineType;}
    public ShapeStyle getShapeStyle(){return shapeStyle;}
    public String getCurrentLineType(){
        switch (this.getLineType()){
            case SOLID -> {
                return "SOLID";
            }
            case DASH -> {
                return "DASH";
            }
            case DOT -> {
                return "DOT";
            }
            default -> {
                return "NULL";
            }
        }
    }


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
    public void setShapeStyle(ShapeStyle s) {shapeStyle = s;}

    public void resetShapeType(){shapeType = ShapeType.NONE;}
    public void setLineType(LineType l){lineType = l;}
    public void resetClick(){ firstClick = true;}

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

    public void setFirstClick(boolean f){ firstClick = f; }

    public String getCurrentShapeStyle() {
        switch (this.getShapeStyle()){
            case FILLED -> {
                return "FILLED";
            }
            case OUTLINE -> {
                return "OUTLINE";
            }
            case DASH -> {
                return "OUTLINE(DASH)";
            }
            default -> {
                return "NULL";
            }
        }
    }

    // Drawing
//    public Canvas draw(Canvas canvas, MouseEvent e, ImageHandler iH, PaletteHandler pH, Path p){
//        GraphicsContext FXC = canvas.getGraphicsContext2D();
//
//        FXC.setFill(pH.getCurrentColor());
//        FXC.setStroke(pH.getCurrentColor());
//        FXC.setLineWidth(this.getLineWidth());
//        switch (this.getDrawType()) {
//            case FREE -> {
//                if (this.isFirstClick()){
//                    FXC.beginPath();
//                    this.click();
//                }
//                //System.out.println("FREE");
//                if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
//                    if (e.isPrimaryButtonDown()) {
//                                    /*FXC.setFill(dHandler.getCurrentColor());
//                                    FXC.fillRect(e.getX() - 3,
//                                            e.getY() - 3,
//                                            5,
//                                            5);*/
//                        //FXC.beginPath();
//                        FXC.lineTo(e.getX(), e.getY());
//                        FXC.stroke();
//
//
//
//                    } else if (e.isSecondaryButtonDown()) {
//
//                        FXC.clearRect(e.getX() - 3,
//                                e.getY() - 3,
//                                5,
//                                5);
//
//                    }
//                    //try {
//                    //    pushTempFile(canvas, tempDir, iHandler);
//                    //} catch (IOException ex) {
//                    //    throw new RuntimeException(ex);
//                    //}
//                } else if (e.getEventType() == MouseEvent.MOUSE_RELEASED){
//                    FXC.beginPath();
//                    FXC.moveTo(e.getX(), e.getY());
//                    FXC.stroke();
//                    if (!this.isFirstClick()) this.click();
//
//                    try {
//                        iH.pushTempFile(canvas, p);
//                    } catch (IOException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }
//            }
//            case LINE -> {
//                if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
//                    if (this.isFirstClick()) {
//                        this.setPosA(e.getX(),
//                                e.getY());
//
//                        this.click();
//                    } else {
//                        FXC.setLineWidth(5);
//                        FXC.setLineWidth(this.getLineWidth());
//                        FXC.setStroke(this.getCurrentColor());
//                        FXC.strokeLine(this.getPosAX(),
//                                this.getPosAY(),
//                                e.getX(),
//                                e.getY());
//                        this.setPosA(0, 0);
//                        this.click();
//
//                        try {
//                            iH.pushTempFile(canvas, p);
//                        } catch (IOException ex) {
//                            throw new RuntimeException(ex);
//                        }
//
//
//                    }
//
//
//                }
//            }
//            case SHAPE -> {
//                // TODO add preview of shape as it is being drawn
//                // TODO add orientation options
//
//                switch (this.getShapeType()){
//                    case TRIANGLE -> {
//                        if (this.isFirstClick()){
//                            this.setPoints(1);
//                            this.click();
//
//                        }
//
//                        if(e.getEventType() == MouseEvent.MOUSE_CLICKED){
//                            System.out.println("Mouse Pressed -- points =" + this.getPoints() + "/3");
//
//                            if (this.getPoints() < 3){ // If this is not the third click
//                                System.out.println("We in it now\n" +
//                                        e.getX() + ", " + e.getY());
//                                this.pX[this.getPoints() - 1] = e.getX();
//                                this.pY[this.getPoints() - 1] = e.getY();
//                                System.out.println(this.getPoints());
//                                this.setPoints(this.getPoints() + 1);
//                                System.out.println(this.getPoints());
//                            }
//                            else {
//                                this.pX[2] = e.getX();
//                                this.pY[2] = e.getY();
//                                System.out.println("Point " + this.getPoints() + "\n" +
//                                        "tX = [" + this.pX[0] + "," + this.pX[1] + "," + this.pX[2] + "]\n" +
//                                        "tY = [" + this.pY[0] + "," + this.pY[1] + "," + this.pY[2] + "]");
//                                FXC.setFill(this.getCurrentColor());
//                                //FXC.strokePolygon(
//                                //        dHandler.pX, dHandler.pY, 3);
//                                FXC.fillPolygon(
//                                        this.pX, this.pY, 3);
//                                this.click();
//                                this.setPoints(0);
//
//                                this.setShapeType(ShapeType.NONE);
//
//                                try {
//                                    iH.pushTempFile(canvas, p);
//                                } catch (IOException ex) {
//                                    throw new RuntimeException(ex);
//                                }
//                            }
//
//                        }
//                    }
//                    case SQUARE -> {
//                        if(e.getEventType() == MouseEvent.MOUSE_CLICKED) {
//                            if (this.isFirstClick()) {
//                                this.click();
//                                this.pX[0] = e.getX();
//                                this.pY[0] = e.getY();
//
//                            }
//                            else {
//                                double topLX = 0;
//                                double topLY = 0;
//                                double width = 0;
//                                if (e.getX() > this.pX[0]){ // Second click to the right of first
//                                    width = e.getX() - this.pX[0];
//                                    topLX = this.pX[0];
//                                    if (e.getY() < this.pY[0]) { //Second click is Higher than first
//                                        topLY = e.getY();
//                                    }
//                                    else{
//                                        topLY = this.pY[0];
//
//                                    }
//
//                                }
//                                else{
//                                    width = this.pX[0] - e.getX();
//                                    topLX = e.getX();
//
//
//                                    if (e.getY() < this.pY[0]) { //Second click is Higher than first
//                                        topLY = e.getY();
//                                    }
//                                    else{
//                                        topLY = this.pY[0];
//
//                                    }
//                                }
//                                if (e.getY() < this.pY[0]) width = Math.max(width, (this.pY[0] - e.getY()));
//                                else width = Math.max(width, (e.getY() - this.pY[0]));
//                                FXC.fillRect(
//                                        topLX, topLY,
//                                        width, width);
//
//
//
//
//                                this.click();
//
//                                this.setShapeType(ShapeType.NONE);
//
//                                try {
//                                    iH.pushTempFile(canvas, p);
//                                } catch (IOException ex) {
//                                    throw new RuntimeException(ex);
//                                }
//
//                            }
//                        }
//
//                    }
//                    case CIRCLE -> {
//                        if (e.getEventType() == MouseEvent.MOUSE_CLICKED){
//                            if (this.isFirstClick()){
//                                this.click();
//                                this.pX[0] = e.getX();
//                                this.pY[0] = e.getY();
//
//
//                            }
//                            else{
//                                double topLX = 0;
//                                double topLY = 0;
//                                double width = 0;
//                                if (e.getX() > this.pX[0]){ // Second click to the right of first
//                                    width = e.getX() - this.pX[0];
//                                    topLX = this.pX[0];
//                                    if (e.getY() < this.pY[0]) { //Second click is Higher than first
//                                        topLY = e.getY();
//                                    }
//                                    else{
//                                        topLY = this.pY[0];
//
//                                    }
//
//                                }
//                                else{
//                                    width = this.pX[0] - e.getX();
//                                    topLX = e.getX();
//
//
//                                    if (e.getY() < this.pY[0]) { //Second click is Higher than first
//                                        topLY = e.getY();
//                                    }
//                                    else{
//                                        topLY = this.pY[0];
//
//                                    }
//                                }
//                                if (e.getY() < this.pY[0]) width = Math.max(width, (this.pY[0] - e.getY()));
//                                else width = Math.max(width, (e.getY() - this.pY[0]));
//                                FXC.fillOval(
//                                        topLX, topLY,
//                                        width, width);
//
//
//
//
//                                this.click();
//
//                                this.setShapeType(ShapeType.NONE);
//
//                                try {
//                                    iH.pushTempFile(canvas, p);
//                                } catch (IOException ex) {
//                                    throw new RuntimeException(ex);
//                                }
//
//
//                            }
//
//                        }
//                        else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED){
//                            if (!this.isFirstClick()){
//                                // TODO add handling for dragging
//
//                            }
//                            else{ // add same handling fo second click from previous section
//
//                            }
//
//                        }
//                    }
//                    case ELLIPSE -> {
//                        if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
//                            if (this.isFirstClick()) {
//                                this.click();
//                                this.pX[0] = e.getX();
//                                this.pY[0] = e.getY();
//
//                            } else {
//                                FXC.setFill(this.getCurrentColor());
//                                if (e.getX() > this.pX[0]) {
//                                    if (e.getY() > this.pY[0]) {
//                                        FXC.fillOval(
//                                                this.pX[0], this.pY[0],
//                                                e.getX() - this.pX[0], e.getY() - this.pY[0]
//                                        );
//                                    } else {
//                                        FXC.fillOval(
//                                                this.pX[0], this.pY[0],
//                                                e.getY() - this.pX[0], this.pY[0] - e.getY()
//                                        );
//                                    }
//                                } else {
//                                    if (e.getY() > this.pY[0]) {
//                                        FXC.fillOval(
//                                                e.getX(), this.pY[0],
//                                                this.pX[0] - e.getX(), e.getY() - this.pY[0]
//                                        );
//                                    } else {
//                                        FXC.fillOval(
//                                                e.getX(), e.getY(),
//                                                this.pX[0] - e.getX(), this.pY[0] - e.getY()
//                                        );
//                                    }
//
//                                }
//                                this.click();
//
//                                this.setShapeType(ShapeType.NONE);
//
//
//                                try {
//                                    iH.pushTempFile(canvas, p);
//                                } catch (IOException ex) {
//                                    throw new RuntimeException(ex);
//                                }
//                            }
//                        }
//
//                    }
//                    case RECTANGLE -> {
//                        if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
//                            if (this.isFirstClick()) {
//                                this.click();
//                                this.pX[0] = e.getX();
//                                this.pY[0] = e.getY();
//
//                            } else {
//                                FXC.setFill(this.getCurrentColor());
//                                if (e.getX() > this.pX[0]) {
//                                    if (e.getY() > this.pY[0]) {
//                                        FXC.fillRect(
//                                                this.pX[0], this.pY[0],
//                                                e.getX() - this.pX[0], e.getY() - this.pY[0]
//                                        );
//                                    } else {
//                                        FXC.fillRect(
//                                                this.pX[0], e.getY(),
//                                                e.getX() - this.pX[0], this.pY[0] - e.getY()
//                                        );
//                                    }
//                                } else {
//                                    if (e.getY() > this.pY[0]) {
//                                        FXC.fillRect(
//                                                e.getX(), this.pY[0],
//                                                this.pX[0] - e.getX(), e.getY() - this.pY[0]
//                                        );
//                                    } else {
//                                        FXC.fillRect(
//                                                e.getX(), e.getY(),
//                                                this.pX[0] - e.getX(), this.pY[0] - e.getY()
//                                        );
//                                    }
//
//                                }
//                                this.click();
//
//                                this.setShapeType(ShapeType.NONE);
//
//
//                                try {
//                                    iH.pushTempFile(canvas, p);
//                                } catch (IOException ex) {
//                                    throw new RuntimeException(ex);
//                                }
//
//                            }
//                        }
//                    }
//                }
//            }
//
//
//            default -> {
//            }
//        }
//
//
//        return canvas;
//    }



}
