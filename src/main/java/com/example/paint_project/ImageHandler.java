package com.example.paint_project;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

// Class for handling images in a Map
public class ImageHandler {
    // Map for image file (key) and Image object (value)
    private Map<File, Image> imageMap;
    // List of currently open images
    private List<File> openImagesFileList;
    // Directory for temp image files
    Path tempDir;
    // List of temp images
    private List<File> tempImagesList;
    // Currently opened image File
    File openImage;

    // Constructors
    public ImageHandler(){
        openImage = null;
        imageMap = new HashMap<>();
        openImagesFileList = new ArrayList<>();
        tempDir = FileSystems.getDefault().getPath("/images", "/temp");

    }

    public ImageHandler(File f) throws IOException {
        openImage = null;
        imageMap = new HashMap<>();
        openImagesFileList = new ArrayList<>();
        // Add file and corresponding image to imageMap
        this.addImage(f);
        // Add Image to list of open images
        openImagesFileList.add(0, f);
        tempDir = FileSystems.getDefault().getPath("/images", "/temp");
    }

    public ImageHandler(Map<File, Image> map){
        openImage = null;
        imageMap = map;
        openImagesFileList = new ArrayList<>();

        tempDir = FileSystems.getDefault().getPath("/images", "/temp");

    }


    public ImageHandler(Map<File, Image> map, List<File> list){
        openImage = null;
        imageMap = map;
        openImagesFileList = list;
        tempDir = FileSystems.getDefault().getPath("/images", "temp");
    }

    // Methods

    // Set the current open Image
    public void setOpenImage(File f){
        openImage = f;
    }

    // Close open image
    public void closeImage(){
        openImage = null;
        imageMap.remove(openImagesFileList.get(0));
        openImagesFileList.remove(0);
    }


    // Add a new file to Image Map
    public void addNewImage(File f) throws IOException {
        // Check that given File (and corresponding Image) are not already Mapped
        if (!imageMap.containsKey(f)) {
            // Get File Input Stream for given File
            FileInputStream stream = new FileInputStream(f);
            //Get Image from file location
            Image i = new Image(stream);
            // Close File input Stream
            stream.close();
            // Add File location and matching Image to Map

            imageMap.put(f, i);
        }
        else System.out.println("WARNING -- Image at \"" + f.getAbsolutePath() + "\" already exists!");
    }

    // Add a new file using passed Image and File Path
    // @params Image i and Path p
    public void addNewImage(Image i, Path p){
        // Createnew File at destination path
        File file = new File(p.toString());
        // Enter File and Image into Map if not already there
        if (!imageMap.containsKey(file)) imageMap.put(file, i);
        else System.out.println("WARNING -- Image at \"" + file.getAbsolutePath() + "\" already exists!");

    }

    // Add image to Map
    // @param File image to be added
    public void addImage(File f) throws IOException{
        if (!imageMap.containsKey(f)) this.addNewImage(f);
        openImagesFileList.add(0, f);

    }

    // Create new list of Temp Images based on File f (this is the saved original)
    public void newTempList(File f) throws NullPointerException{
        try {
            System.out.println("Empty List? " + tempImagesList.isEmpty());
            System.out.println("adding " + f.getAbsolutePath() + " to list");
            tempImagesList.add(0, f);
            System.out.println("done");
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void addTempImage(File f){
        tempImagesList.add(f);
    }

    public void backTempImage(){
        if (tempImagesList.size() > 1) {
            tempImagesList.remove(tempImagesList.size() -1);
        }
        else System.out.println("ERROR -- Unable to remove Temp Image from List");
    }

    public void clearTempList(){
        tempImagesList.clear();
    }

    // SETTERS
    public void setCurrentImageFile(File f){
        openImage = f;
    }

    // GETTERS

    // Get image
    //@param File image file to be gotten
    public Image getImage(File f) throws IOException{
        if(imageMap.containsKey(f)) {
            if (!openImagesFileList.contains(f)) openImagesFileList.add(0, f);
            return imageMap.get(f);
        }
        else {
            System.out.println("DEBUG -- Image at \"" + f.getAbsolutePath() + "\" is Not in map -- Adding to map");
            this.addImage(f);
            return imageMap.get(f);
        }
    }

    public List<File> getOpenImages(){
        return openImagesFileList;
    }

    public File getCurrentImageFile(){
        return openImage;
    }


    // Get the file for current Open Image
    public File getOpenImage(){
        return openImage;
    }

    public List<File> getTempList(){
        return tempImagesList;
    }

    public File getLatestTempImage(){
        return tempImagesList.get(tempImagesList.size() -1);
    }

    public File getOriginalImage(){

        try {
            return tempImagesList.get(0);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }

    // Get the color of a pixel on the canvas image by
    // taking a snapshot of the canvas and the mouse coordinates
    //@params Image snapshot of the canvas, int x and int y positions of the mouse relative to the image
    // returns the color value at the specified coordinates
    public Color getPixelColor(Image snapshot, int mouseX, int mouseY){
        return snapshot.getPixelReader().getColor(mouseX, mouseY);
    }




}