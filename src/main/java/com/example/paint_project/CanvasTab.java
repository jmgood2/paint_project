package com.example.paint_project;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/***
 *  This is a new Tab used for the Paint Project
 *  Each Tab will have its own:
 *  - Canvas
 *  - Image Handler
 *  -
 */
public class CanvasTab extends Tab {
    // Logger
    Logger logger;
    // Variables
    static double CANVAS_HEIGHT = 500;
    static double CANVAS_WIDTH = 500;
    // Handlers
    private ImageHandler imageHandler;
   //Canvas
    private Canvas canvas;
    // Graphics Context
    private GraphicsContext GFX;
    // Paths
    private Path tempDir;
    private Path workDirectory;
    // Files
    private File tempImage;
    // ScrollPane
    private ScrollPane canvasPane;

    public CanvasTab(Path workspace) {
        try {
            this.initializeTab(workspace);
        } catch (IOException e) {
            logger.severe(e.toString());
            throw new RuntimeException(e);
        }

        // Canvas Setup
        canvas.setHeight(CANVAS_HEIGHT);
        canvas.setWidth(CANVAS_WIDTH);

        // Graphics Context
        GFX = canvas.getGraphicsContext2D();

        // Graphics Context Setup
        GFX.setFill(Color.WHITE);
        GFX.fillRect(0, 0,
                canvas.getWidth(), canvas.getHeight());

    }

    public CanvasTab(Path workspace, File imageFile){
        try {
            this.initializeTab(workspace);
        } catch (IOException e) {
            logger.severe(e.toString());
            throw new RuntimeException(e);
        }

        // Open image from file
        String imageFilePath = imageFile.getAbsolutePath();

        try {
            imageHandler.addImage(imageFile);
        } catch (IOException e){
            logger.severe(e.toString());
        }

        // Pull image from File
        Image image = null;
        try {
            image = new Image(new FileInputStream(imageFilePath));
        } catch (FileNotFoundException e){
            logger.severe(e.toString());
        }

        // resize canvas to fit image
        canvas.setHeight(image.getHeight());
        canvas.setWidth(image.getWidth());

        // Set Canvas to transparent
        GFX.clearRect(
                0, 0,
                canvas.getWidth(), canvas.getHeight()
        );

        // Draw image to Canvas
        GFX.drawImage(image, 0, 0);

        // Set current Image File
        imageHandler.setCurrentImageFile(imageFile);

        // Get file extension
        String ext = imageFile.getPath().
                substring(imageFile.getPath().
                        lastIndexOf('.') + 1);

        // Save initial to temp directory
        tempImage = null;
        try {
            tempImage = new File(String.valueOf(
                    Files.createTempFile(
                            tempDir, null, ext
                    )
            ));
        } catch (IOException e){
            logger.severe(e.toString());
        }

        try {
            assert tempImage != null;
            imageHandler.newTempFiles(canvas, tempDir, tempImage);
        } catch (IOException e){
            logger.severe(e.toString());
        }
        imageHandler.notSaved();

    }

    private void initializeTab(Path w) throws IOException {
        // logger
        logger = Logger.getLogger("Paint");
        // Handlers
        imageHandler = new ImageHandler();
        // Canvas
        canvas = new Canvas();

        // Paths
        workDirectory = w;
        tempDir = Files.createTempDirectory(workDirectory, "temp");
        // Files
        tempImage = new File(String.valueOf(Files.createTempFile(
                tempDir,
                null,
                ".jpg"
        )));

        // Initialize tempFiles
        imageHandler.newTempFiles(canvas, tempDir, tempImage);



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

    // Save the current canvas to an image
    public void saveCanvas(Stage stage){
        logger.info("Saving Canvas Image");
        // Get current image
        File imageFile = imageHandler.getOpenImage();
        // Get image extension
        String fileType = imageFile.getName().substring(
                imageFile.getName().lastIndexOf('.') + 1
        );
        logger.info("File extension: " + fileType);
        File saveFile = ImageHandler.saveImage(
                stage, new File(workDirectory.toString()));
        assert saveFile != null;
        logger.info("Saving " + saveFile.getPath());
        ImageHandler.saveImageAs(canvas, saveFile);
        logger.info("Saved...");

        imageHandler.saved();
        this.setText(saveFile.getName());

    }

    // Save Canvas AS image
    public void saveCanvasAs(Stage stage) {
        logger.info("Saving Canvas Image As...");

        // Open FileChooser
        File saveFile = ImageHandler.saveImage(stage, new File(
                workDirectory.toString()
        ));

        // Create file for saving if none exists
        if (saveFile == null) {
            logger.info("Creating new file ");
            try {
                Files.createFile(saveFile.toPath());
                logger.info("Created " + saveFile.getPath());
            } catch (IOException e) {
                logger.severe(e.toString());
            }
        }
        logger.info("Running SaveImageAs...");
        ImageHandler.saveImageAs(canvas, saveFile);
        imageHandler.saved();
    }

    public void closeImage(){
        imageHandler.closeImage();
        GFX.clearRect(0, 0,
                CANVAS_WIDTH, CANVAS_HEIGHT);

        try {
            imageHandler.clearTempFiles(tempDir);
        } catch (IOException e){
            logger.severe(e.toString());
        }
    }

    public void closeTab(){
        try {
            imageHandler.clearTemp(new File(tempDir.toString()));
        } catch (IOException e) {
            logger.info(e.toString());
        }
    }

    public void undo(){
        canvas = imageHandler.undo(canvas);
    }

    public void redo(){
        canvas = imageHandler.redo(canvas);
    }

    public void pop(MouseEvent mE){
        imageHandler.notSaved();

        if (imageHandler.getUndoneStatus()
        && mE.getEventType() != MouseEvent.MOUSE_MOVED
        && mE.getEventType() != MouseEvent.MOUSE_ENTERED
        && mE.getEventType() != MouseEvent.MOUSE_EXITED){
            logger.info(mE.getEventType().toString());
            try{
                imageHandler.popTemp();
            } catch(IOException e){
                logger.severe(e.toString());
            }
        }
    }

    public void setGFX(Color c, DrawHandler dH){
        GFX.setFill(c);
        GFX.setStroke(c);
        GFX.setLineWidth((dH.getLineWidth()));

        if (dH.getLineType() == LineType.DASH)      GFX.setLineDashes(50d, 30d);
        else if (dH.getLineType() == LineType.DOT)  GFX.setLineDashes(20d, 15d);
        else                                        GFX.setLineDashes();
    }
    public void draw(DrawHandler dH){
        switch (dH.getDrawType()) {
            case FREE -> {
                if (dH.isFirstClick()){
                    GFX.beginPath();
                    dH.click();
                }
            }
        }


    }

    public ImageHandler getImageHandler(){
        return imageHandler;
    }

    public Canvas getCanvas(){
        return canvas;
    }




}
