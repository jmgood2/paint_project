package com.example.paint_project;



import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

// Class for choosing Colors. Currently gives options across ~10 colors
public class PaletteHandler {
    private Map<Rectangle, Color> cMap = new HashMap<>();

    Rectangle[] pallete = new Rectangle[8];
    Rectangle currentColor = new Rectangle (50,50);

    Line[] line = new Line[3];
    Line menuLine;
    public double thin, def, thick;
    private int currentLine;

    public PaletteHandler(){
        thin = 1.0;
        def = 5.0;
        thick = 10.0;

        for (int i = 0; i < pallete.length; i++){
            pallete[i] = new Rectangle(20,20);
            currentLine = 1;
            switch (i) {
                case 0 -> {
                    pallete[i].setFill(Color.WHITE);
                    pallete[i].setStroke(Color.BLACK);
                }
                case 1 -> {
                    pallete[i].setFill(Color.RED);
                    pallete[i].setStroke(Color.BLACK);
                }
                case 2 -> {
                    pallete[i].setFill(Color.YELLOW);
                    pallete[i].setStroke(Color.BLACK);
                }
                case 3 -> {
                    pallete[i].setFill(Color.ORANGE);
                    pallete[i].setStroke(Color.BLACK);
                }
                case 4 -> {
                    pallete[i].setFill(Color.BLACK);
                    pallete[i].setStroke(Color.BLACK);
                }
                case 5 -> {
                    pallete[i].setFill(Color.BLUE);
                    pallete[i].setStroke(Color.BLACK);
                }
                case 6 -> {
                    pallete[i].setFill(Color.PURPLE);
                    pallete[i].setStroke(Color.BLACK);
                }
                case 7 -> {
                    pallete[i].setFill(Color.GREEN);
                    pallete[i].setStroke(Color.BLACK);
                }
                default -> {
                    pallete[i].setFill(Color.BROWN);
                    pallete[i].setStroke(Color.BLACK);
                }
            }
            cMap.put(pallete[i], (Color) pallete[i].getFill());
            this.setCurrentColor(Color.BROWN);
            currentColor.setStrokeWidth(2);
            currentColor.setStroke(Color.BLACK);

            if ( i < line.length ){
                line[i] = new Line(0,10,20,10);
                switch (i){
                    case 0:
                        line[i].setStrokeWidth(thin);
                        break;
                    case 1:
                        line[i].setStrokeWidth(def);
                        break;
                    case 2:
                        line[i].setStrokeWidth(thick);
                        break;
                    default:
                        break;
                }
            }

        }

        menuLine = this.getCurrentLine();
        menuLine.setStartX(0);
        menuLine.setStartY(10);
        menuLine.setEndX(20);
        menuLine.setEndY(10);

    }

    // GETTERS

    //Line getters
    public Line getCurrentLine(){
        switch (currentLine){
            case 1:
                return line[0];
            case 2:
                return line[1];
            case 3:
                return line[2];
            default:
                break;
        }
        return line[0];

    }
    public Line getMenuLine(){
        return menuLine;
    }
    public Line getLine(int i){
        if (i>2) {
            currentLine = 2;
            return line[2];
        }
        currentLine = i;
        return line[i];

    }

    // Colors

    public Color getCurrentColor(){
        return (Color) currentColor.getFill();
    }

    public Rectangle getCurrentColorRect(){
        return currentColor;
    }

    public Rectangle getRect(int i) {
        return pallete[i];
    }

    public String getColorRGB(){

        return String.format("#%02X%02X%02X",
                (int) (((Color) currentColor.getFill()).getRed() * 255),
                (int) (((Color) currentColor.getFill()).getGreen() * 255),
                (int) (((Color) currentColor.getFill()).getBlue() * 255));

    }

    // Setters

    // Lines
    public void setMenuLine(){
        menuLine = getCurrentLine();
    }

    public void setCurrentLine(int i){
        if (i != thin && i != def && i != thick) currentLine = 1;
        else currentLine = i;
        this.setMenuLine();
    }

    // Colors
    public void setCurrentColor(Color c){
        currentColor.setFill(c);
    }




}
