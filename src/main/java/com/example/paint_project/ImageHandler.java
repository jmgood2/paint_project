package com.example.paint_project;

import javafx.scene.image.Image;

import java.io.*;
import java.util.*;

// Class for handling images in a Map
public class ImageHandler {
    // Map for image file (key) and Image object (value)
    private final Map<File, Image> imageMap = new HashMap<>();
    // Currently opened image File
    File openImage = null;

    // Constructors
    public ImageHandler(){

    }
    public ImageHandler(File f) throws FileNotFoundException {
        openImage = f;
        addImage(f);
    }
    public ImageHandler(String s) throws FileNotFoundException {
        openImage = new File(s);
        addImage(openImage);
    }

    // Methods

    // Set the current open Image
    public void setOpenImage(File f){
        openImage = f;
    }

    // Close open image
    public void closeImage(){
        imageMap.remove(openImage);
        openImage = null;
    }

    // Add image to Map
    // @param String path to file
    public void addImage(String s) throws FileNotFoundException{
        File file = new File(s);
        openImage = file;
        if (!imageMap.containsKey(file)) imageMap.put(openImage, new Image(new FileInputStream(openImage)));


    }


    // Add image to Map
    // @param File image to be added
    public void addImage(File f) throws FileNotFoundException{
        openImage = f;
        if (!imageMap.containsKey(f)) imageMap.put(openImage, new Image(new FileInputStream(openImage)));

    }

    // GETTERS

    // Get Image
    public Image getImage(){
        return imageMap.get(openImage);
    }
    // Get image
    //@param File image file to be gotten
    public Image getImage(File f) throws FileNotFoundException{
        if(!imageMap.containsKey(f)) imageMap.put(f, new Image(new FileInputStream(f)));
        if (openImage != f) openImage = f;
        return imageMap.get(openImage);
    }
    // Get image
    //@param String path to image file to be gotten
    public Image getImage(String s) throws FileNotFoundException{
        File file = new File(s);
        if(!imageMap.containsKey(file)) imageMap.put(file, new Image(new FileInputStream(file)));
        if (openImage != file) openImage = file;
        return imageMap.get(openImage);
    }

    // Get the file for current Open Image
    public File getOpenImage(){
        return openImage;
    }





}