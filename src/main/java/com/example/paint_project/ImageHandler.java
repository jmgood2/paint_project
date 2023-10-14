package com.example.paint_project;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

// Class for handling images in a Map
public class ImageHandler {
    // Flags
    private boolean isSaved;
    private boolean isPainting;
    private boolean isUndone;
    // Iterator for cycling through the TempImagesList
    private int tempIterator;
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
        // Flags
        isSaved = false;
        isPainting = false;
        isUndone = false;
        tempIterator = 0;
        openImage = null;
        imageMap = new HashMap<>();
        openImagesFileList = new ArrayList<>();
        tempImagesList = new ArrayList<>();
        tempDir = FileSystems.getDefault().getPath("/images", "/temp");

    }

    public ImageHandler(File f) throws IOException {
        // Flags
        isSaved = false;
        isPainting = false;
        isUndone = false;
        tempIterator = 0;
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
        // Flags
        isSaved = false;
        isPainting = false;
        isUndone = false;
        tempIterator = 0;
        openImage = null;
        imageMap = map;
        openImagesFileList = new ArrayList<>();

        tempDir = FileSystems.getDefault().getPath("/images", "/temp");

    }


    public ImageHandler(Map<File, Image> map, List<File> list){
        // Flags
        isSaved = false;
        isPainting = false;
        isUndone = false;
        tempIterator = 0;
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
        tempIterator++;
    }

    public void backTempImage(){
        //if (tempImagesList.size() > 1) {
        //    tempImagesList.remove(tempImagesList.size() -1);
        //}
        //else System.out.println("ERROR -- Unable to remove Temp Image from List");

        tempIterator--;
    }
    public void nextTempImage(){
        if (tempIterator < (tempImagesList.size())) tempIterator++;
        else System.out.println("ERRROR Already at last temp image");
    }

    public void clearTempList(){
        tempImagesList.clear();
        
    }

    public void clearTemp(File tempDir) throws IOException {
        this.clearTempList();
        File[] tempFiles = tempDir.listFiles();
        if (tempFiles != null) {
            for (File f: tempFiles){
                if (f.isDirectory()) this.clearTemp(f);
                else Files.delete(Paths.get(f.getPath()));
            }
        }
        Files.delete(Paths.get(tempDir.getPath()));
    }



    public void newTempFiles(Canvas c, Path d, File f) throws IOException {
        clearTempFiles(d);
        System.out.println(f.getAbsolutePath());
        this.newTempList(f);
        ImageHandler.saveImageAs(c, this.getOriginalImage());
    }

    public void pushTempFile(Canvas c, Path d) throws IOException {
        File f = new File(String.valueOf(Files.createTempFile(
                d,
                null,
                this.getOriginalImage().getPath().substring(
                        this.getOriginalImage().getPath().lastIndexOf(".")
                ))));
        this.addTempImage(f);
        ImageHandler.saveImageAs(c, f);
    }

    public void clearTempFiles(Path d) throws IOException {
        File tempDir = new File(d.toString());
        File[] tempFiles = tempDir.listFiles();
        if (tempFiles != null) {
            for (File f: tempFiles){
                if (f.isDirectory()) clearTempFiles(f.toPath());
                else Files.delete(Paths.get(f.getPath()));
            }
        }
    }

    public void popTemp() throws IOException{
        while (tempIterator < (tempImagesList.size() - 1)){
            Files.delete(Paths.get(tempImagesList.get(tempImagesList.size() - 1).getPath()));
            tempImagesList.remove(tempImagesList.size() -1);
        }
    }

    public static File openImage(Stage stage){
        Logger logger = Logger.getLogger("Paint");
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Open Image");

        String imageDir = "";
        if (System.getProperty("os.name").equalsIgnoreCase("windows")) imageDir = "images";
        else {
            String userDir = System.getProperty("user.home");
            logger.info("User Directory = " + userDir);
            File userDirF = new File(userDir);
            if (!userDirF.canRead()) {
                logger.info("Cannot read " + userDirF.getAbsolutePath());
                logger.info("Trying to create folder");
                userDirF = new File("c:/");
            }

            imageDir = userDirF.getPath() + "/Documents/Images";
        }
        logger.info("Initial Directory = " + imageDir);
        File init = new File(imageDir);
        fChooser.setInitialDirectory(init);
        fChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("TIFF", "*.tif", "*.tiff")
        );
        File saveFile = fChooser.showOpenDialog(stage);
        return saveFile;

    }

    /* saveImage
     * Launches a FileChooser explorer for saving an Image
     */
    public static File saveImage(Stage stage, File initial){
        Logger logger = Logger.getLogger("Paint");

        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Save Image");

        String imageDir = "";
        if (System.getProperty("os.name").equalsIgnoreCase("windows")) imageDir = "images";
        else {
            String userDir = System.getProperty("user.home");
            logger.info("User Directory = " + userDir);
            File userDirF = new File(userDir);
            if (!userDirF.canRead()) {
                logger.info("Cannot read " + userDirF.getAbsolutePath());
                logger.info("Trying to create folder");
                userDirF = new File("c:/");
            }

            imageDir = userDirF.getPath() + "/Documents/Images";
        }
        logger.info("Initial Directory = " + imageDir);
        File init = new File(imageDir);
        fChooser.setInitialDirectory(init);
        fChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("TIFF", "*.tif", "*.tiff")
        );

        File saveFile = fChooser.showSaveDialog(stage);

        if (saveFile != null) return saveFile;
        else {
            logger.severe("ERROR -- returned file is Null!");
            return null;
        }

    }

    public static void saveImageAs(Canvas canvas, File file){
        Logger logger = Logger.getLogger("Paint");

        try{
            logger.info("SYSTEM Save Image As w/ " + file.getAbsolutePath());
            logger.info("Canvas Height = " + canvas.getHeight() + " Width = " + canvas.getWidth());
            WritableImage wImage = new WritableImage((int) canvas.getWidth(),
                    (int) canvas.getHeight());
            canvas.snapshot(null, wImage);

            BufferedImage bImage1 = SwingFXUtils.fromFXImage(wImage, null);
            String ext = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);

            BufferedImage bImage2 = bImage1;

            if (ext.equals("jpg") || ext.equals("jpeg")){
                bImage2 = new BufferedImage(
                        bImage1.getWidth(),
                        bImage1.getHeight(),
                        BufferedImage.OPAQUE);
            }
            Graphics2D graphics = bImage2.createGraphics();
            graphics.drawImage(bImage1, 0, 0, null);

            ImageIO.write(bImage2,
                    ext,
                    file);
            graphics.dispose();


        } catch (IOException e) {
            e.printStackTrace();
            // Error LOG
            logger.warning("ERROR SAVING \n" + e);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            // Error LOG
            logger.warning("ERROR SAVING (NULLPOINTER \n" + ex);
        }


    }

    public Canvas undo(Canvas canvas){
        // Make sure we don't accidentally erase the original temp image
        if (this.getLatestTempImage() != this.getOriginalImage()) {
            this.backTempImage();
            this.setCurrentImageFile(this.getLatestTempImage());

            GraphicsContext FXC = canvas.getGraphicsContext2D();

            Image image = null;
            try {
                image = this.getImage(this.getLatestTempImage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Set canvas to old values
            canvas.setHeight(image.getHeight());
            canvas.setWidth(image.getWidth());

            FXC.clearRect(
                    0, 0,
                    canvas.getWidth(),
                    canvas.getHeight());

            FXC.drawImage(image,
                    0,
                    0);
            isUndone = true;

        }
        else {

            System.out.println("ERROR -- Cannot erase initial temp Image");
        }

        return canvas;
    }

    public Canvas redo(Canvas canvas){
        this.nextTempImage();
        if (tempIterator == (tempImagesList.size() -1)) isUndone = false;
        else {
            this.setCurrentImageFile(this.getLatestTempImage());

            GraphicsContext FXC = canvas.getGraphicsContext2D();


            Image image = null;
            try {
                image = this.getImage(this.getLatestTempImage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Set canvas to old values
            canvas.setHeight(image.getHeight());
            canvas.setWidth(image.getWidth());

            FXC.clearRect(
                    0, 0,
                    canvas.getWidth(),
                    canvas.getHeight());

            FXC.drawImage(image,
                    0,
                    0);
        }
        return canvas;
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
        //return tempImagesList.get(tempImagesList.size() -1);
        return tempImagesList.get(tempIterator - 1);
    }

    public File getNextTempImage(){
        tempIterator ++;
        return getLatestTempImage();
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

    public boolean getUndoneStatus(){ return isUndone;}


    public void notSaved() {
        isSaved = false;
    }

    public void saved() {
        isSaved = true;
    }

    public boolean getSaveStatus() {
        return isSaved;
    }
}