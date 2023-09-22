/**********************************************
 * Jonathan Good   ***********************
 * CS-250         *******************
 * PainT Project **************
 * Sprints 1/2  ***********
 * *******************/

package com.example.paint_project;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;  // The holy grail
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class paint_2 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Create Handler objects
        ImageHandler iHandler = new ImageHandler();
        DrawHandler dHandler = new DrawHandler();
        PaletteHandler pHandler = new PaletteHandler();

        // Create PATHs for directories
        Path workspace = Paths.get("Images");
        Path workdir = Files.createDirectories(workspace);
        Path tempDir = Files.createTempDirectory(workspace, "temp");



        // Create Stage
        stage.setTitle("Paint (t) 2.0");
        stage.setMaximized(true);

        //
        //MENU Bar
        //
        // File
        Menu menuF = new Menu("File");
        MenuItem openI = new MenuItem("Open Image");
        openI.setAccelerator(KeyCombination.keyCombination("Shortcut+Shift+N"));
        MenuItem saveI = new MenuItem("Save Image");
        saveI.setAccelerator(KeyCombination.keyCombination("Shortcut+S"));
        MenuItem saveIAs = new MenuItem("Save Image As");
        saveIAs.setAccelerator(KeyCombination.keyCombination("Shortcut+Shift+S"));
        MenuItem closeI = new MenuItem("Close Image");
        closeI.setAccelerator(KeyCombination.keyCombination("F1"));
        MenuItem exit = new MenuItem("Close");
        exit.setAccelerator(KeyCombination.keyCombination("F4"));
        menuF.getItems().add(openI);
        menuF.getItems().add(saveI);
        menuF.getItems().add(saveIAs);
        menuF.getItems().add(closeI);
        menuF.getItems().add(exit);

        //
        // Edit
        Menu editM = new Menu("Edit");
        MenuItem undo = new MenuItem("Undo");
        editM.getItems().add(undo);

        //
        // Help
        Menu helpM = new Menu("Help");
        MenuItem notes = new MenuItem("Release Notes");
        MenuItem about = new MenuItem("About");
        helpM.getItems().add(notes);
        helpM.getItems().add(about);

        //
        // Menu bar
        MenuBar menuB = new MenuBar(menuF, editM, helpM);

        //
        // LEFT Menu

        // Button Menu
        ToggleGroup buttons = new ToggleGroup();
        ButtonBar buttonB = new ButtonBar();
        buttonB.setButtonMinWidth(50);

        // Create buttons
        ToggleButton freeDraw = new ToggleButton("FREE");
        freeDraw.setToggleGroup(buttons);
        ButtonBar.setButtonData(freeDraw, ButtonBar.ButtonData.LEFT);
        ToggleButton lineDraw = new ToggleButton("LINE");
        lineDraw.setToggleGroup(buttons);
        ButtonBar.setButtonData(lineDraw, ButtonBar.ButtonData.LEFT);

        buttonB.getButtons().addAll(freeDraw, lineDraw);

        buttonB.setPrefHeight(30);
        BorderPane.setAlignment(buttonB, Pos.BOTTOM_CENTER);


        // COLOR Palette
        VBox vB1 = new VBox(5);
        vB1.setAlignment(Pos.CENTER);
        vB1.setPadding(new Insets(10));
        for (int i = 0; i < 8; i = i+2){
            //vB1.getChildren().add(pHandler.getRect(i));
            // Add rows of 2 colors x 4
            vB1.getChildren().add(new HBox(
                    pHandler.getRect(i),
                    pHandler.getRect(i+1)));
        }
        vB1.getChildren().add(pHandler.getCurrentColorRect());

        // LINE Selection
        Menu pMenu = new Menu("",
                pHandler.getMenuLine());
        MenuItem thinW = new MenuItem("Thin",
                pHandler.getLine(0));
        MenuItem defW = new MenuItem("Default",
                pHandler.getLine(1));
        MenuItem thickW = new MenuItem("Thick",
                pHandler.getLine(2));
        pMenu.getItems().add(thinW);
        pMenu.getItems().add(defW);
        pMenu.getItems().add(thickW);

        MenuBar pBar = new MenuBar(pMenu);
        vB1.getChildren().add(pBar);

        TextField textW = new TextField(Double.toString(dHandler.getLineWidth()));
        textW.setMinWidth(20);
        textW.setMaxWidth(40);
        vB1.getChildren().add(textW);


        vB1.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                new CornerRadii(2),
                new BorderWidths(2)
        )));



        // SCENE Setup
        BorderPane borderRoot = new BorderPane();
        Scene baseScene = new Scene(borderRoot,
                Screen.getPrimary().getVisualBounds().getWidth(),
                Screen.getPrimary().getVisualBounds().getHeight());

        // CANVAS Setup
        double canvasH = 500;
        double canvasW = 500;

        Canvas canvas = new Canvas();

        canvas.setHeight(canvasH);
        canvas.setWidth(canvasW);

        GraphicsContext FXC = canvas.getGraphicsContext2D();
        FXC.setFill(Color.WHITE);
        FXC.fillRect(0,
                0,
                canvas.getWidth(),
                canvas.getHeight());

        // Save initial Image to temp directory
        File tempImage = new File(String.valueOf(Files.createTempFile(
                tempDir,
                null,
                ".jpg")));

        newTempFiles(canvas, tempDir, tempImage, iHandler);

        // ScrollPane containing canvas
        ScrollPane canvasPane = new ScrollPane(canvas);

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

        BorderPane.setAlignment(canvasPane, Pos.TOP_LEFT);
        BorderPane.setMargin(canvasPane, new Insets(20,12,12,20));

        // LAYOUT Setup
        HBox hB1 = new HBox(menuB);
        HBox hB2 = new HBox(buttonB);
        borderRoot.setTop(hB1);
        borderRoot.setCenter(canvasPane);
        borderRoot.setBottom(hB2);
        borderRoot.setLeft(vB1);


        // IMAGE View

        ImageView imageV = new ImageView();
        imageV.setVisible(false);
        imageV.setX(5);
        imageV.setY(5);
        imageV.setFitWidth(600);
        imageV.setPreserveRatio(true);


        //LAYOUT Setup





         //SCENE creation and update


        stage.setScene(baseScene);

        stage.show();

        //Menu Item Actions

        //
        // MENU Items

        // OPEN image
        openI.setOnAction(
                aE -> {
                    //Open Image
                    File openFile = openImage(stage);
                    String openFilePath = openFile.getAbsolutePath();
                    System.out.println("DEBUG -- Opening " + openFilePath + "...");

                    // Add opened image to Image Handler
                    try {
                        iHandler.addImage(openFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Pull Image from file
                    Image image = null;
                    try {
                        image = new Image(new FileInputStream(openFilePath));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    // Resize canvas to fit image
                    canvas.setHeight(image.getHeight());
                    canvas.setWidth(image.getWidth());

                    // Clear canvas
                    FXC.clearRect(
                            0,0,
                            canvas.getWidth(),
                            canvas.getHeight());

                    FXC.drawImage(image,
                            0,
                            0);

                    iHandler.setCurrentImageFile(openFile);

                    String extension = openFile.getPath().substring(openFile.getPath().lastIndexOf('.'));

                    File tempFile = null;
                    try{
                        tempFile = new File(String.valueOf(
                                Files.createTempFile(
                                        tempDir,
                                        null,
                                        extension)));
                    } catch (IOException ex){
                        ex.printStackTrace();
                    }

                    try{
                        assert tempFile != null;
                        newTempFiles(canvas, tempDir, tempFile, iHandler);
                    } catch (IOException ex){
                        ex.printStackTrace();
                    }
                }
        );


        // Save Image
        saveI.setOnAction(
                aE -> {
                    File iFile = iHandler.getOpenImage();

                    String fType = iFile.getName().substring(
                            iFile.getName().lastIndexOf('.') + 1);
                    System.out.println("File extension of " + iFile.getAbsolutePath() + " is " + fType);
                    if (iFile == null){
                        File file = saveImage(stage, new File ("Images"));

                        System.out.println("DEBUG -- RUNNING SAVE IMAGE");
                        saveImageAs(canvas, file);
                    }
                    else {
                        System.out.println("DEBUG -- SAVING...");
                        saveImageAs(canvas, iFile);


                    }
                    System.out.println("DEBUG -- RUNNING SAVE IMAGE AS");

                }
        );



        // SAVE image AS
        saveIAs.setOnAction(
                aE -> {

                    File file = saveImage(stage, new File(workdir.toString()));
                    if (file == null) {
                        try {
                            Files.createFile(file.toPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("DEBUG -- RUNNING SAVE IMAGE");
                    saveImageAs(canvas, file);
                    System.out.println("DEBUG -- RUNNING SAVE IMAGE AS");



                });

        // CLOSE image
        closeI.setOnAction(
                aE -> {
                    // Close Image and clear canvas
                    iHandler.closeImage();
                    FXC.clearRect(0,
                            0,
                            canvas.getWidth(),
                            canvas.getHeight());

                    try {
                        clearTempFiles(tempDir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        );

        // EXIT program
        exit.setOnAction(
                aE -> {
                    try {
                        clearTemp(new File(tempDir.toString()), iHandler);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
        );

        //Undo action - Currently permanent
        // TODO Make it so we can undo AND redo
        // TODO Modify Drawing to save edits to temp directory
        undo.setOnAction(
                aE -> {
                    // Make sure we don't accidentally erase the original temp image
                    if (iHandler.getLatestTempImage() != iHandler.getOriginalImage()) {
                        try {
                            Files.delete(iHandler.getLatestTempImage().toPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        iHandler.backTempImage();
                        iHandler.setCurrentImageFile(iHandler.getLatestTempImage());


                        Image image = null;
                        try {
                            image = iHandler.getImage(iHandler.getLatestTempImage());
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
                    else {
                        System.out.println("ERROR -- Cannot erase initial temp Image");
                    }




                }
        );

        // OPEN release notes
        notes.setOnAction(
                aE -> {
                    File file = new File("src/main/Release-Notes.md");
                    if (file.exists()){
                        System.out.println("file exists");
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            desktop.open(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else System.out.println("file does not exist");
                }
        );

        // POPUP about page
        about.setOnAction(
                aE -> {
                    aboutPop();
                }
        );

        //
        // Button Items
        freeDraw.setOnAction(
                bE -> dHandler.setDrawType(DrawType.FREE)

        );
        lineDraw.setOnAction(
                bE -> dHandler.setDrawType(DrawType.LINE)
        );

        //
        // Palette Items

        // Color Selection
        vB1.addEventHandler(MouseEvent.MOUSE_CLICKED,
                pE -> {
                    try {

                        Rectangle target = (Rectangle) pE.getTarget();
                        dHandler.setCurrentColor((Color) target.getFill());
                        pHandler.setCurrentColor(dHandler.getCurrentColor());
                    } catch (ClassCastException cE){
                        cE.printStackTrace();
                    }

                });

        // Line Selection

        pMenu.setOnAction(
                aE -> pMenu.setGraphic(pHandler.getMenuLine())

        );

        thinW.setOnAction(
                aE -> {
                    dHandler.setLineWidth(pHandler.thin);
                    pHandler.setCurrentLine(1);
                    textW.setText(Double.toString(dHandler.lineWidth));
                    Line mLine = new Line(0,
                            10,
                            20,
                            10);
                    mLine.setStroke(pHandler.getCurrentColor());
                    mLine.setStrokeWidth(1);
                    pMenu.setGraphic(mLine);
                }
        );
        defW.setOnAction(
                aE -> {
                    dHandler.setLineWidth(pHandler.def);
                    pHandler.setCurrentLine(5);
                    textW.setText(Double.toString(dHandler.lineWidth));
                    Line mLine = new Line(0,
                            10,
                            20,
                            10);
                    mLine.setStroke(pHandler.getCurrentColor());
                    mLine.setStrokeWidth(5);
                    pMenu.setGraphic(mLine);
                }
        );
        thickW.setOnAction(
                aE -> {
                    dHandler.setLineWidth(pHandler.thick);
                    pHandler.setCurrentLine(10);
                    textW.setText(Double.toString(dHandler.lineWidth));
                    Line mLine = new Line(0,
                            10,
                            20,
                            10);
                    mLine.setStroke(pHandler.getCurrentColor());
                    mLine.setStrokeWidth(10);
                    pMenu.setGraphic(mLine);
                }
        );
        textW.setOnAction(
                aE -> {
                    char[] c = textW.getText().toCharArray();
                    System.out.println(String.valueOf(c));
                    boolean hasDecimal = false;
                    for (int i = 0; i < c.length; i++){
                        if (!Character.isDigit(c[i])){
                            boolean killChar = false;
                            for (int j = i; j + 1 < c.length; j++){
                                if (c[i] == '.' && !hasDecimal){
                                    hasDecimal = true;
                                    i++;
                                }
                                else  {
                                    c[j] = c[j+1];
                                    killChar = true;
                                }

                            }
                            if (killChar) {
                                c = Arrays.copyOfRange(c, 0, c.length - 1);
                                i--;
                            }
                            System.out.println(String.valueOf(c));

                        }


                    }
                    textW.setText(String.valueOf(c));
                    dHandler.setLineWidth(Double.parseDouble(textW.getText()));

                }
        );


        // DRAWING

        canvas.addEventHandler(MouseEvent.ANY,
                e -> {
                    switch (dHandler.getDrawType()) {
                        case FREE -> {
                            System.out.println("FREE");
                            if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                                if (e.isPrimaryButtonDown()) {
                                    FXC.setFill(dHandler.getCurrentColor());
                                    FXC.fillRect(e.getX() - 3,
                                            e.getY() - 3,
                                            5,
                                            5);
                                } else if (e.isSecondaryButtonDown()) {

                                    FXC.clearRect(e.getX() - 3,
                                            e.getY() - 3,
                                            5,
                                            5);

                                }
                            }
                        }
                        case LINE -> {
                            //System.out.println("LINE");
                            if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                                if (dHandler.isFirstClick()) {
                                    dHandler.setPosA(e.getX(),
                                            e.getY());
                                    System.out.println(dHandler.isFirstClick());
                                    dHandler.click();
                                } else {
                                    FXC.setLineWidth(5);
                                    FXC.setLineWidth(dHandler.getLineWidth());
                                    FXC.setStroke(dHandler.getCurrentColor());
                                    FXC.strokeLine(dHandler.getPosAX(),
                                            dHandler.getPosAY(),
                                            e.getX(),
                                            e.getY());
                                    dHandler.setPosA(0, 0);
                                    System.out.println(dHandler.isFirstClick());
                                    dHandler.click();


                                }


                            }
                        }
                        default -> {
                        }
                    }

                });



    }

    /**************************
     * METHODS
     ****************** ***/

    /* openImage
     * Launches a FileChooser explorer for locating an image
     * Images filtered by type
     * @param stage
     * @return File
     */
    public static File openImage(Stage stage){
        FileChooser f = new FileChooser();
        f.setTitle("Open Image");

        File init = new File("src/main/images");
        f.setInitialDirectory(init);
        f.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("TIFF", "*.tif", "*.tiff")
        );
        return f.showOpenDialog(stage);

    }

    /* saveImage
     * Launches a FileChooser explorer for saving an Image
     */
    public static File saveImage(Stage stage, File initial){
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Save Image");
        //fChooser.setInitialDirectory(initial);
        File init = new File("src/main/images");
        fChooser.setInitialDirectory(init);
        fChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("TIFF", "*.tif", "*.tiff")
        );

        if (fChooser.showSaveDialog(stage) != null) return fChooser.showSaveDialog(stage);
        else {
            System.out.println("ERROR -- returned file is Null!");
            return null;
        }

    }

    public static void saveImageAs(Canvas canvas, File file){

        try{
            System.out.println("SYSTEM Save Image As w/ " + file.getAbsolutePath());
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
            System.out.println("ERROR SAVING \n" + e);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            // Error LOG
            System.out.println("ERROR SAVING (NULLPOINTER \n" + ex);
        }


    }




    /* aboutPop
     * Opens a new Scene with About information
     */
    public static void aboutPop(){
        Stage aPop = new Stage();
        aPop.setTitle("About");

        Label aLabel = new Label("Pain(T) Alpha Build 2.1\n9/15/2023\n\nJonathan Good\nCS 250\n");

        VBox vB = new VBox(10);
        vB.getChildren().addAll(aLabel);
        vB.setAlignment(Pos.CENTER);

        Scene aScene = new Scene (vB, 100, 100);
        aPop.setScene(aScene);
        aPop.showAndWait();
    }

    public void clearTemp(File tempDir, ImageHandler iH) throws IOException {
        iH.clearTempList();
        File[] tempFiles = tempDir.listFiles();
        if (tempFiles != null) {
            for (File f: tempFiles){
                if (f.isDirectory()) clearTemp(f);
                else Files.delete(Paths.get(f.getPath()));
            }
        }
        Files.delete(Paths.get(tempDir.getPath()));
    }

    public void clearTemp(File tempDir) throws IOException {

        Files.delete(Paths.get(tempDir.getPath()));
    }

    public void newTempFiles(Canvas c, Path d, File f, ImageHandler iH) throws IOException {
        clearTempFiles(d);
        System.out.println(f.getAbsolutePath());
        iH.newTempList(f);
        saveImageAs(c, iH.getOriginalImage());
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


    /* main method
     * launches Pain(T)
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

}