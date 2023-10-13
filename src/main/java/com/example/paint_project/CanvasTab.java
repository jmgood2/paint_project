package com.example.paint_project;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;

/***
 *  This is a new Tab used for the Paint Project
 *  Each Tab will have its own:
 *  - Canvas
 *  - Image Handler
 *  -
 */
public class CanvasTab extends Tab {
    // Variables
    static double CANVAS_HEIGHT = 500;
    static double CANVAS_WIDTH = 500;
    // Handlers
    private ImageHandler imageHandler;
   //Canvas
    private Canvas canvas;
    // Graphics Context
    private GraphicsContext FXC;
    // ScrollPane
    private ScrollPane canvasPane;

    public CanvasTab(){
        // Handlers
        imageHandler = new ImageHandler();
        // Canvas
        canvas = new Canvas();

        // Canvas Setup
        canvas.setHeight(CANVAS_HEIGHT);
        canvas.setWidth(CANVAS_WIDTH);

        // Graphics Context
        FXC = canvas.getGraphicsContext2D();
        FXC.setFill(Color.BLUEVIOLET);
        FXC.fill();
        FXC.setFill(Color.GOLDENROD);
        FXC.fillRect(0,0,CANVAS_HEIGHT,CANVAS_WIDTH);
        // ScrollPane
        canvasPane = new ScrollPane(canvas);
        // Scale Scrolling
        canvasPane.getContent().setOnScroll(scrollEvent ->{
            double y0 = scrollEvent.getDeltaY();
            double cHeight = canvasPane.getContent().getBoundsInLocal().getHeight();
            double sHeight = canvasPane.getHeight();
            double y1 = 1;
            if (cHeight != sHeight) y1 = (cHeight - sHeight);
            double vVal = canvasPane.getVvalue();
            canvasPane.setVvalue(vVal + -y0/y1);

            double x0 = scrollEvent.getDeltaX();
            double cWidth = canvasPane.getContent().getBoundsInLocal().getWidth();
            double sWidth = canvasPane.getWidth();
            double x1 = 1;
            if (cWidth != cWidth) x1 = (cWidth - sWidth);
            double hVal = canvasPane.getHvalue();
            canvasPane.setHvalue(hVal + -x0/x1);

        });

        canvasPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        this.setContent(canvasPane);
    }



}
