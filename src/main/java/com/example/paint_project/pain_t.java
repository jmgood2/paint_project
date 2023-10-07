package com.example.paint_project;

import javafx.application.Application;

public class pain_t {
    public static void main(String[] args){
        new Thread(() -> Application.launch(Painter.class)).start();



    }
}
