package com.example.paint_project;

import javafx.geometry.Point2D;
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
    // Flags
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


        // Graphics Context
        GFX = canvas.getGraphicsContext2D();

        // Graphics Context Setup
        GFX.setFill(Color.WHITE);
        GFX.fillRect(0, 0,
                canvas.getWidth(), canvas.getHeight());

    }

    public CanvasTab(Path workspace, File imageFile){

        // Open image from file
        String imageFilePath = imageFile.getAbsolutePath();

        try {
            this.initializeTab(workspace);
        } catch (IOException e) {
            logger.severe(e.toString());
            throw new RuntimeException(e);
        }

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

        // Set Canvas dimensions
        canvas.setHeight(image.getHeight());
        canvas.setWidth(image.getWidth());

        GFX = canvas.getGraphicsContext2D();

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
        // Variables
        // Flags
        // Handlers
        imageHandler = new ImageHandler();
        // Canvas
        canvas = new Canvas();

        // Canvas Setup
        if (imageHandler.getOpenImages().isEmpty()) {
            canvas.setHeight(CANVAS_HEIGHT);
            canvas.setWidth(CANVAS_WIDTH);
        }
        else {


            // resize canvas to fit image
            canvas.setHeight(imageHandler.getImage(
                    imageHandler.getOpenImage()).getHeight());
            canvas.setWidth(imageHandler.getImage(
                    imageHandler.getOpenImage()).getWidth());

        }

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


    /**
     * @param e Mouse event
     * @param dH DrawHandler
     * @return drawHandler for resetting Drawhandler
     */
    public DrawHandler drawFree(MouseEvent e, DrawHandler dH) {
        DrawHandler drawHandler = dH;
        if (drawHandler.isFirstClick()) {
            GFX.beginPath();
            drawHandler.click();
        }

        if (e.getEventType() == MouseEvent.MOUSE_DRAGGED){
            if (e.isPrimaryButtonDown()) {
                GFX.lineTo(e.getX(), e.getY());
                GFX.stroke();
            }
        }
        else if (e.getEventType() == MouseEvent.MOUSE_RELEASED){
            GFX.beginPath();
            GFX.moveTo(e.getX(), e.getY());
            GFX.stroke();

            // Reset Firstclick flag
            if (!drawHandler.isFirstClick()) drawHandler.click();

            // Push temp File to temp Directory
            try {
                imageHandler.pushTempFile(canvas, tempDir);
            } catch (IOException eX){
                logger.severe(eX.toString());
            }
        }
        return drawHandler;
    }

    /**
     * @param e Mouse event
     * @param dH DrawHandler
     * @return drawHandler for resetting Drawhandler
     */
    public DrawHandler drawLine(MouseEvent e, DrawHandler dH){
        DrawHandler drawHandler = dH;
        if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (drawHandler.isFirstClick()) {
                drawHandler.setPosA(e.getX(), e.getY());
                drawHandler.click();
            }
            else {
                GFX.setLineWidth(dH.getLineWidth());
                GFX.setStroke(dH.getCurrentColor());
                GFX.strokeLine(
                        drawHandler.getPosAX(), drawHandler.getPosAY(),
                        e.getX(), e.getY());
                drawHandler.click();

                try {
                    imageHandler.pushTempFile(canvas, tempDir);
                } catch (IOException eX){
                    logger.severe(eX.toString());
                }


            }
        }
        return drawHandler;
    }

    public DrawHandler drawShape(MouseEvent e, DrawHandler dH){
        DrawHandler drawHandler = dH;

        switch (drawHandler.getShapeType()){
            case TRIANGLE -> {
                if (drawHandler.isFirstClick()) {
                    drawHandler.setPoints(1);
                    drawHandler.click();
                }

                if (e.getEventType() == MouseEvent.MOUSE_CLICKED){
                    // Check that we havent finished the triangle
                    if (drawHandler.getPoints() < 3) {
                        drawHandler.pX[drawHandler.getPoints() - 1] = e.getX();
                        drawHandler.pY[drawHandler.getPoints() - 1] = e.getY();
                        // advance # of points
                        drawHandler.setPoints(drawHandler.getPoints() + 1);
                    }
                    else {
                        // Select last point
                        drawHandler.pX[2] = e.getX();
                        drawHandler.pY[2] = e.getY();
                        // set color
                        GFX.setFill(drawHandler.getCurrentColor());
                        GFX.setStroke(drawHandler.getCurrentColor());
                        if (drawHandler.getShapeStyle() == ShapeStyle.FILLED){
                            GFX.fillPolygon(
                                    drawHandler.pX, drawHandler.pY,
                                    3);

                        }
                        else {
                            if (drawHandler.getShapeStyle() == ShapeStyle.DASH) {
                                GFX.setLineDashes(20);
                            }
                            GFX.strokePolygon(
                                    drawHandler.pX, drawHandler.pY,
                                    3);
                        }
                        drawHandler.click();
                        drawHandler.setPoints(0);
                        drawHandler.resetShapeType();

                        try {
                            imageHandler.pushTempFile(canvas, tempDir);
                        } catch (IOException ex) {
                            logger.severe(e.toString());
                        }

                    }
                }
            }
            case SQUARE -> {
                if (e.getEventType() == MouseEvent.MOUSE_CLICKED){
                    // Get initial point
                    if (drawHandler.isFirstClick()) {
                        drawHandler.click();
                        drawHandler.pX[0] = e.getX();
                        drawHandler.pY[0] = e.getY();

                    }
                    else {
                        double topLeftX = 0;
                        double topLeftY = 0;
                        double width = 0;

                        if (e.getX() > drawHandler.pX[0]) {
                            // Second point RIGHT of first
                            width = e.getX() - drawHandler.pX[0];
                            topLeftX = drawHandler.pX[0];
                            if (e.getY() < drawHandler.pY[0]) {
                                // Second click is HIGHER than first
                                topLeftY = e.getY();
                            }
                            else topLeftY = drawHandler.pY[0];
                        }
                        else {
                            // Second point LEFT of first
                            width = drawHandler.pX[0] - e.getX();
                            topLeftX = e.getX();

                            if (e.getY() < drawHandler.pY[0]) {
                                // Second click HIGHER than first
                                topLeftY = e.getY();
                            }
                            else topLeftY = drawHandler.pY[0];
                        }

                        // Refigure width if width > height
                        if (e.getY() < drawHandler.pY[0]) width =
                                Math.max(width, (drawHandler.pY[0] - e.getY()));
                        else width = Math.max(width, (e.getY() - drawHandler.pY[0]));

                        if (drawHandler.getShapeStyle() == ShapeStyle.FILLED){
                            GFX.fillRect(
                                    topLeftX, topLeftY,
                                    width, width);
                        }
                        else {
                            if (drawHandler.getShapeStyle() == ShapeStyle.DASH){
                                GFX.setLineDashes(20);
                            }
                            GFX.strokeRect(
                                    topLeftX, topLeftY,
                                    width, width);
                        }

                        drawHandler.click();
                        drawHandler.resetShapeType();

                        try {
                            imageHandler.pushTempFile(canvas, tempDir);
                        } catch (IOException eX){
                            logger.severe(eX.toString());
                        }


                    }

                }
            }
            case CIRCLE -> {
                if (e.getEventType() == MouseEvent.MOUSE_CLICKED){
                    if (drawHandler.isFirstClick()){
                        drawHandler.click();
                        drawHandler.pX[0] = e.getX();
                        drawHandler.pY[0] = e.getY();


                    }
                    else{
                        double topLX = 0;
                        double topLY = 0;
                        double width = 0;
                        if (e.getX() > drawHandler.pX[0]){ // Second click to the right of first
                            width = e.getX() - drawHandler.pX[0];
                            topLX = drawHandler.pX[0];
                            if (e.getY() < drawHandler.pY[0]) { //Second click is Higher than first
                                topLY = e.getY();
                            }
                            else{
                                topLY = drawHandler.pY[0];

                            }

                        }
                        else{
                            width = drawHandler.pX[0] - e.getX();
                            topLX = e.getX();


                            if (e.getY() < drawHandler.pY[0]) { //Second click is Higher than first
                                topLY = e.getY();
                            }
                            else{
                                topLY = drawHandler.pY[0];

                            }
                        }
                        if (e.getY() < drawHandler.pY[0]) width = Math.max(width, (drawHandler.pY[0] - e.getY()));
                        else width = Math.max(width, (e.getY() - drawHandler.pY[0]));

                        if (drawHandler.getShapeStyle() == ShapeStyle.FILLED){
                            GFX.fillOval(
                                    topLX, topLY,
                                    width, width);

                        }
                        else {
                            if (drawHandler.getShapeStyle() == ShapeStyle.DASH) GFX.setLineDashes(20);;
                            GFX.strokeOval(
                                    topLX, topLY,
                                    width, width);
                        }




                        drawHandler.click();

                        drawHandler.resetShapeType();


                        try {
                            imageHandler.pushTempFile(canvas, tempDir);
                        } catch (IOException ex) {
                            logger.severe(ex.toString());
                        }


                    }

                }
                else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED){
                    if (!drawHandler.isFirstClick()){
                        // TODO add handling for dragging

                    }
                    else{ // add same handling fo second click from previous section

                    }

                }
            }
            case ELLIPSE -> {
                if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    if (drawHandler.isFirstClick()) {
                        drawHandler.click();
                        drawHandler.pX[0] = e.getX();
                        drawHandler.pY[0] = e.getY();

                    } else {
                        GFX.setFill(drawHandler.getCurrentColor());
                        if (e.getX() > drawHandler.pX[0]) {
                            // Second click is to the RIGHT of the first
                            if (e.getY() > drawHandler.pY[0]) {
                                // Second Click LOWER than First
                                if (drawHandler.getShapeStyle() == ShapeStyle.FILLED){

                                    GFX.fillOval(
                                            drawHandler.pX[0], drawHandler.pY[0],
                                            e.getX() - drawHandler.pX[0], e.getY() - drawHandler.pY[0]
                                    );

                                }
                                else {
                                    if (drawHandler.getShapeStyle() == ShapeStyle.DASH) GFX.setLineDashes(20);;
                                    GFX.strokeOval(
                                            drawHandler.pX[0], drawHandler.pY[0],
                                            e.getX() - drawHandler.pX[0], e.getY() - drawHandler.pY[0]
                                    );

                                }
                            } else {
                                if (drawHandler.getShapeStyle() == ShapeStyle.FILLED){

                                    GFX.fillOval(
                                            drawHandler.pX[0], e.getY(),
                                            e.getX() - drawHandler.pX[0], drawHandler.pY[0] - e.getY()
                                    );

                                }
                                else {
                                    if (drawHandler.getShapeStyle() == ShapeStyle.DASH) GFX.setLineDashes(20);;
                                    GFX.strokeOval(
                                            drawHandler.pX[0], e.getY(),
                                            e.getX() - drawHandler.pX[0], drawHandler.pY[0] - e.getY()
                                    );

                                }
                            }
                        } else {
                            // Second Click LEFT of first
                            if (e.getY() > drawHandler.pY[0]) {
                                // Second Click BELOW first
                                if (drawHandler.getShapeStyle() == ShapeStyle.FILLED){
                                    GFX.fillOval(
                                            e.getX(), drawHandler.pY[0],
                                            drawHandler.pX[0] - e.getX(), e.getY() - drawHandler.pY[0]
                                    );

                                }
                                else {
                                    if (drawHandler.getShapeStyle() == ShapeStyle.DASH) GFX.setLineDashes(20);;
                                    GFX.strokeOval(
                                            e.getX(), drawHandler.pY[0],
                                            drawHandler.pX[0] - e.getX(), e.getY() - drawHandler.pY[0]
                                    );

                                }
                            } else {
                                if (drawHandler.getShapeStyle() == ShapeStyle.FILLED){
                                    GFX.fillOval(
                                            e.getX(), e.getY(),
                                            drawHandler.pX[0] - e.getX(), drawHandler.pY[0] - e.getY()
                                    );

                                }
                                else {
                                    if (drawHandler.getShapeStyle() == ShapeStyle.DASH) GFX.setLineDashes(20);;
                                    GFX.strokeOval(
                                            e.getX(), e.getY(),
                                            drawHandler.pX[0] - e.getX(), drawHandler.pY[0] - e.getY()
                                    );


                                }
                            }

                        }
                        drawHandler.click();

                        drawHandler.resetShapeType();

                        try {
                            imageHandler.pushTempFile(canvas, tempDir);
                        } catch (IOException ex) {
                            logger.severe(ex.toString());
                            throw new RuntimeException(ex);
                        }
                    }
                }


            }
            case RECTANGLE -> {

                if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    if (drawHandler.isFirstClick()) {
                        drawHandler.click();
                        drawHandler.pX[0] = e.getX();
                        drawHandler.pY[0] = e.getY();

                    } else {
                        GFX.setFill(drawHandler.getCurrentColor());
                        if (e.getX() > drawHandler.pX[0]) {
                            if (e.getY() > drawHandler.pY[0]) {
                                if (drawHandler.getShapeStyle() == ShapeStyle.FILLED){
                                    GFX.fillRect(
                                            drawHandler.pX[0], drawHandler.pY[0],
                                            e.getX() - drawHandler.pX[0], e.getY() - drawHandler.pY[0]
                                    );

                                }
                                else {
                                    if (drawHandler.getShapeStyle() == ShapeStyle.DASH) GFX.setLineDashes(20);;
                                    GFX.strokeRect(
                                            drawHandler.pX[0], drawHandler.pY[0],
                                            e.getX() - drawHandler.pX[0], e.getY() - drawHandler.pY[0]
                                    );

                                }
                            } else {
                                if (drawHandler.getShapeStyle() == ShapeStyle.FILLED){
                                    GFX.fillRect(
                                            drawHandler.pX[0], e.getY(),
                                            e.getX() - drawHandler.pX[0], drawHandler.pY[0] - e.getY()
                                    );


                                }
                                else {
                                    if (drawHandler.getShapeStyle() == ShapeStyle.DASH) GFX.setLineDashes(20);;
                                    GFX.strokeRect(
                                            drawHandler.pX[0], e.getY(),
                                            e.getX() - drawHandler.pX[0], drawHandler.pY[0] - e.getY()
                                    );

                                }
                            }
                        } else {
                            if (e.getY() > drawHandler.pY[0]) {
                                if (drawHandler.getShapeStyle() == ShapeStyle.FILLED){
                                    GFX.fillRect(
                                            e.getX(), drawHandler.pY[0],
                                            drawHandler.pX[0] - e.getX(), e.getY() - drawHandler.pY[0]
                                    );


                                }
                                else {
                                    if (drawHandler.getShapeStyle() == ShapeStyle.DASH) GFX.setLineDashes(20);;
                                    GFX.strokeRect(
                                            e.getX(), drawHandler.pY[0],
                                            drawHandler.pX[0] - e.getX(), e.getY() - drawHandler.pY[0]
                                    );

                                }
                            } else {
                                if (drawHandler.getShapeStyle() == ShapeStyle.FILLED){
                                    GFX.fillRect(
                                            e.getX(), e.getY(),
                                            drawHandler.pX[0] - e.getX(), drawHandler.pY[0] - e.getY()
                                    );


                                }
                                else {
                                    if (drawHandler.getShapeStyle() == ShapeStyle.DASH) GFX.setLineDashes(20);;
                                    GFX.strokeRect(
                                            e.getX(), e.getY(),
                                            drawHandler.pX[0] - e.getX(), drawHandler.pY[0] - e.getY()
                                    );

                                }
                            }

                        }
                        drawHandler.click();

                        drawHandler.setShapeType(ShapeType.NONE);


                        try {
                            imageHandler.pushTempFile(canvas, tempDir);
                        } catch (IOException ex) {
                            logger.severe(ex.toString());
                            throw new RuntimeException(ex);
                        }

                    }
                }


            }
            default -> {
                logger.severe("Ever wonder why we're here?");
            }
        }


        return drawHandler;
    }

    public ImageHandler getImageHandler(){
        return imageHandler;
    }

    public Canvas getCanvas(){
        return canvas;
    }




}
