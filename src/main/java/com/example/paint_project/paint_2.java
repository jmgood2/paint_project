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
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
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
        //ButtonBar buttonB = new ButtonBar();
        //buttonB.setButtonMinWidth(50);

        // Create buttons
        ToggleButton freeDraw = new ToggleButton("FREE");
        ToggleButton lineDraw = new ToggleButton("LINE");
        ToggleButton shapes = new ToggleButton("Shapes");
        freeDraw.setPrefSize(65, 40);
        Circle c1 = new Circle();
        c1.setRadius(4);
        c1.setFill(Color.BLACK);

        freeDraw.setGraphic(c1);
        freeDraw.setToggleGroup(buttons);
        lineDraw.setPrefSize(55, 40);
        Line l1 = new Line();
        l1.setStrokeWidth(2);
        l1.setStartX(0);
        l1.setStartY(6);
        l1.setEndX(6);
        l1.setEndY(0);
        l1.setStroke(Color.BLACK);

        lineDraw.setGraphic(l1);
        lineDraw.setToggleGroup(buttons);
        shapes.setPrefSize(65, 40);
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                0.0, 6.0,
                3.0, 0.0,
                6.0, 6.0);

        shapes.setGraphic(triangle);
        shapes.setToggleGroup(buttons);
        ToggleButton colorPicker = new ToggleButton("Color\nPicker");
        colorPicker.setPrefSize(55, 40);
        colorPicker.setToggleGroup(buttons);

        GridPane buttonGrid = new GridPane();
        //buttonGrid.setGridLinesVisible(true);
        buttonGrid.setPrefSize(50, 50);

        buttonGrid.add(freeDraw, 0,0);
        buttonGrid.add(lineDraw, 1, 0);
        buttonGrid.add(shapes, 0, 1);
        buttonGrid.add(colorPicker, 1, 1);


        buttonGrid.getColumnConstraints().add(new ColumnConstraints(65));
        buttonGrid.getColumnConstraints().add(new ColumnConstraints(55));
        buttonGrid.getRowConstraints().add(new RowConstraints(40));
        buttonGrid.getRowConstraints().add(new RowConstraints(40));




        // COLOR Palette
        VBox vB1 = new VBox(5);
        vB1.setAlignment(Pos.CENTER);
        vB1.setPadding(new Insets(10));
        for (int i = 0; i < 8; i = i+2){
            //vB1.getChildren().add(pHandler.getRect(i));
            // Add rows of 2 colors x 4
            HBox h = new HBox(pHandler.getRect(i),
                    pHandler.getRect(i+1));
            h.setAlignment(Pos.CENTER);
            vB1.getChildren().add(h);
        }
        vB1.getChildren().add(pHandler.getCurrentColorRect());
        Label rgbHash = new Label(pHandler.getColorRGB());
        vB1.getChildren().add(rgbHash);

        // LINE Pane
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

        Slider lineWidthSlider = new Slider(0, 50, 5);
        lineWidthSlider.setOrientation(Orientation.VERTICAL);
        lineWidthSlider.setShowTickMarks(true);
        lineWidthSlider.setShowTickLabels(true);
        lineWidthSlider.setBlockIncrement(1);

        Rectangle widthPreviewBack = new Rectangle(50, 50);
        widthPreviewBack.setStroke(Color.BLACK);
        widthPreviewBack.setFill(Color.TRANSPARENT);
        Rectangle widthPreviewFore = new Rectangle(24,24,
                pHandler.getCurrentLine().getStrokeWidth(),
                pHandler.getCurrentLine().getStrokeWidth());
        widthPreviewFore.setStroke(Color.TRANSPARENT);
        widthPreviewFore.setFill(dHandler.getCurrentColor());

        Group widthPreviewRoot = new Group(widthPreviewBack, widthPreviewFore);




        TextField textW = new TextField(Double.toString(dHandler.getLineWidth()));
        textW.setMinWidth(20);
        textW.setMaxWidth(50);

        // Contextual Pane




        // Free Draw Pane
        VBox freeVBox = new VBox();
        freeVBox.getChildren().addAll(
                new Label("Brush Shape")
        );

        // Line Draw Pane
        VBox lineVBox = new VBox();
        lineVBox.getChildren().addAll(
                pBar);

        // Shapes Pane

        ToggleGroup shapeSelect = new ToggleGroup();
        VBox shapesVBox = new VBox();
        ToggleButton triangleSelect = new ToggleButton("",
          new Polygon(0,10, 5,0, 10,10));
        ToggleButton squareSelect = new ToggleButton("",
          new Rectangle(10,10));
        ToggleButton circleSelect = new ToggleButton("",
          new Circle(5));
        ToggleButton ellipseSelect = new ToggleButton("",
          new Ellipse(5, 3.5));
        ToggleButton rectangleSelect = new ToggleButton("",
                new Rectangle(10, 5));

        triangleSelect.setToggleGroup(shapeSelect);
        circleSelect.setToggleGroup(shapeSelect);
        squareSelect.setToggleGroup(shapeSelect);
        ellipseSelect.setToggleGroup(shapeSelect);
        rectangleSelect.setToggleGroup(shapeSelect);



        GridPane shapeGrid = new GridPane();
        //shapeGrid.setGridLinesVisible(true);
        shapeGrid.setPrefSize(50, 50);

        shapeGrid.add(triangleSelect, 0,0);
        shapeGrid.add(squareSelect, 1, 0);
        shapeGrid.add(circleSelect, 3, 0);
        shapeGrid.add(ellipseSelect, 0, 1);
        shapeGrid.add(rectangleSelect, 1, 1);

        Label shapeText = new Label("Shape: None");
        shapesVBox.getChildren().addAll(
                new Label("Shape Size"),
                shapeText,
                shapeGrid
        );

        // Color Picker Pane
        VBox pickerVBox = new VBox();
        int[] picker = new int[2];
        Rectangle pickerColor = new Rectangle(20, 20);
        pickerColor.setStroke(Color.BLACK);
        Label pickerXY = new Label("Coordinates: " + picker[0] + ", " + picker[1]);
        pickerVBox.getChildren().addAll(
                pickerXY,
                new HBox(
                        new Label("Pixel Color: "),
                        pickerColor
                )
        );






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
        // Free/Line/Shape root
        VBox FLSRoot = new VBox(
                new HBox(lineWidthSlider,
                new VBox(widthPreviewRoot, textW)),
                freeVBox);
        VBox vBRoot = new VBox(buttonGrid, vB1, FLSRoot);
        HBox hB1 = new HBox(menuB);
        //HBox hB2 = new HBox(buttonB);
        borderRoot.setTop(hB1);
        borderRoot.setCenter(canvasPane);
        //borderRoot.setBottom(hB2);
        borderRoot.setLeft(vBRoot);


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
                    System.out.println("DEBUG -- File extension of " + iFile.getAbsolutePath() + " is " + fType);
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
                        System.out.println("DEBUG -- file exists");
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
                bE -> {
                    dHandler.setDrawType(DrawType.FREE);

                    //vBRoot.getChildren().set(3, FLSRoot);
                    FLSRoot.getChildren().set(1, freeVBox);
                }

        );
        lineDraw.setOnAction(
                bE -> {
                    dHandler.setDrawType(DrawType.LINE);

                    //vBRoot.getChildren().set(3, FLSRoot);
                    FLSRoot.getChildren().set(1, lineVBox);
                }
        );
        shapes.setOnAction(
                bE -> {
                    dHandler.setDrawType(DrawType.SHAPE);

                    FLSRoot.getChildren().set(1, shapesVBox);
                }
        );
        colorPicker.setOnAction(
                bE -> {
                    dHandler.setDrawType(DrawType.PICKER);

                    FLSRoot.getChildren().set(1, pickerVBox);
                }
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
                        rgbHash.setText(pHandler.getColorRGB());
                    } catch (ClassCastException e){
                        e.printStackTrace();
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
                    textW.setText(Double.toString(dHandler.getLineWidth()));

                    widthPreviewFore.setX(24);
                    widthPreviewFore.setY(24);
                    widthPreviewFore.setWidth(1);
                    widthPreviewFore.setHeight(1);
                    lineWidthSlider.setValue(1);

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

                    textW.setText(Double.toString(dHandler.getLineWidth()));

                    widthPreviewFore.setX(23);
                    widthPreviewFore.setY(23);
                    widthPreviewFore.setWidth(5);
                    widthPreviewFore.setHeight(5);
                    lineWidthSlider.setValue(5);

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
                    textW.setText(Double.toString(dHandler.getLineWidth()));


                    widthPreviewFore.setX(19.5);
                    widthPreviewFore.setY(19.5);
                    widthPreviewFore.setWidth(10);
                    widthPreviewFore.setHeight(10);
                    lineWidthSlider.setValue(10);

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
                    String val = String.valueOf(c);

                    textW.setText(val);

                }
        );

        textW.setOnKeyPressed(
                kE -> {
                    if (kE.getCode() == KeyCode.ENTER){
                        dHandler.setLineWidth(Double.parseDouble(textW.getText()));
                        if (dHandler.getLineWidth() > 50) {
                            dHandler.setLineWidth(50);
                            textW.setText("50");
                        }
                        else if (dHandler.getLineWidth() < 1){
                            dHandler.setLineWidth(1);
                            textW.setText("1");
                        }

                        // Get the xy coords for the upper left of the preview rect
                        double pos = 25 - (dHandler.getLineWidth() / 2);
                        if (pos < 0) pos = 0;

                        widthPreviewFore.setX(pos);
                        widthPreviewFore.setY(pos);
                        widthPreviewFore.setWidth(dHandler.getLineWidth());
                        widthPreviewFore.setHeight(dHandler.getLineWidth());
                        lineWidthSlider.setValue(dHandler.getLineWidth());

                    }
                }
        );

        lineWidthSlider.setOnMouseReleased(
                sE -> {
                    if (lineWidthSlider.getValue() < 1) lineWidthSlider.setValue(1);
                    dHandler.setLineWidth(lineWidthSlider.getValue());

                    // Get the xy coords for the upper left of the preview rect
                    double w = dHandler.getLineWidth();
                    double pos = 25 - (w / 2);
                    if (pos < 0) pos = 0;

                    widthPreviewFore.setX(pos);
                    widthPreviewFore.setY(pos);
                    widthPreviewFore.setWidth(w);
                    widthPreviewFore.setHeight(w);
                    lineWidthSlider.setValue(w);
                    textW.setText(String.valueOf(w));

                }
        );

        // SHAPE selection
        triangleSelect.setOnAction(
                bE -> {
                    dHandler.setShapeType(ShapeType.TRIANGLE);
                    shapeText.setText("Shape: TRIANGLE");

                }
        );

        circleSelect.setOnAction(
                bE -> {
                    dHandler.setShapeType(ShapeType.CIRCLE);
                    shapeText.setText("Shape: CIRCLE");

                }
        );

        ellipseSelect.setOnAction(
                bE -> {
                    dHandler.setShapeType(ShapeType.ELLIPSE);
                    shapeText.setText("Shape: ELLIPSE");

                }
        );

        squareSelect.setOnAction(
                bE -> {
                    dHandler.setShapeType(ShapeType.SQUARE);
                    shapeText.setText("Shape: SQUARE");

                }
        );

        rectangleSelect.setOnAction(
                bE -> {
                    dHandler.setShapeType(ShapeType.RECTANGLE);
                    shapeText.setText("Shape: RECTANGLE");

                }
        );




        // DRAWING

        canvas.addEventHandler(MouseEvent.ANY,
                e -> {
                    FXC.setFill(pHandler.getCurrentColor());
                    FXC.setStroke(pHandler.getCurrentColor());
                    FXC.setLineWidth(dHandler.getLineWidth());
                    switch (dHandler.getDrawType()) {
                        case FREE -> {
                            //System.out.println("FREE");
                            if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                                if (e.isPrimaryButtonDown()) {
                                    /*FXC.setFill(dHandler.getCurrentColor());
                                    FXC.fillRect(e.getX() - 3,
                                            e.getY() - 3,
                                            5,
                                            5);*/
                                    FXC.lineTo(e.getX(), e.getY());
                                    FXC.stroke();



                                } else if (e.isSecondaryButtonDown()) {

                                    FXC.clearRect(e.getX() - 3,
                                            e.getY() - 3,
                                            5,
                                            5);

                                }
                            } else if (e.getEventType() == MouseEvent.MOUSE_PRESSED){
                                FXC.beginPath();
                                FXC.moveTo(e.getX(), e.getY());
                                FXC.stroke();
                            }
                        }
                        case LINE -> {
                            //System.out.println("LINE");
                            if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                                if (!dHandler.isFirstClick()) {
                                    dHandler.setPosA(e.getX(),
                                            e.getY());

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


                                }


                            }
                        }
                        case PICKER -> {
                            picker[0] = (int) e.getX();
                            picker[1] = (int) e.getY();
                            pickerXY.setText("Coordinates: " + picker[0] + ", " + picker[1]);
                            pickerColor.setFill(iHandler.getPixelColor(
                                    canvas.getGraphicsContext2D().getCanvas().snapshot(null,null),
                                    picker[0],
                                    picker[1]));
                            if (e.getEventType() == MouseEvent.MOUSE_CLICKED){

                                dHandler.setCurrentColor((Color) pickerColor.getFill());
                                pHandler.setCurrentColor(dHandler.getCurrentColor());
                                rgbHash.setText(pHandler.getColorRGB());

                            }
                        }
                        case SHAPE -> {
                            // TODO add preview of shape as it is being drawn
                            // TODO add orientation options

                            switch (dHandler.getShapeType()){
                                case TRIANGLE -> {
                                    if (dHandler.isFirstClick()){
                                        dHandler.setPoints(1);
                                        dHandler.click();

                                    }

                                    if(e.getEventType() == MouseEvent.MOUSE_CLICKED){
                                        System.out.println("Mouse Pressed -- points =" + dHandler.getPoints() + "/3");

                                        if (dHandler.getPoints() < 3){ // If this is not the third click
                                            System.out.println("We in it now\n" +
                                                    e.getX() + ", " + e.getY());
                                            dHandler.pX[dHandler.getPoints() - 1] = e.getX();
                                            dHandler.pY[dHandler.getPoints() - 1] = e.getY();
                                            System.out.println(dHandler.getPoints());
                                            dHandler.setPoints(dHandler.getPoints() + 1);
                                            System.out.println(dHandler.getPoints());
                                        }
                                        else {
                                            dHandler.pX[2] = e.getX();
                                            dHandler.pY[2] = e.getY();
                                            System.out.println("Point " + dHandler.getPoints() + "\n" +
                                                    "tX = [" + dHandler.pX[0] + "," + dHandler.pX[1] + "," + dHandler.pX[2] + "]\n" +
                                                    "tY = [" + dHandler.pY[0] + "," + dHandler.pY[1] + "," + dHandler.pY[2] + "]");
                                            FXC.setFill(dHandler.getCurrentColor());
                                            //FXC.strokePolygon(
                                            //        dHandler.pX, dHandler.pY, 3);
                                            FXC.fillPolygon(
                                                    dHandler.pX, dHandler.pY, 3);
                                            dHandler.click();
                                            dHandler.setPoints(0);

                                            dHandler.setShapeType(ShapeType.NONE);
                                            shapeSelect.getSelectedToggle().setSelected(false);
                                        }

                                    }
                                }
                                case SQUARE -> {
                                    if(e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                                        if (dHandler.isFirstClick()) {
                                            dHandler.click();
                                            dHandler.pX[0] = e.getX();
                                            dHandler.pY[0] = e.getY();

                                        }
                                        else {
                                            double topLX = 0;
                                            double topLY = 0;
                                            double width = 0;
                                            if (e.getX() > dHandler.pX[0]){ // Second click to the right of first
                                                width = e.getX() - dHandler.pX[0];
                                                topLX = dHandler.pX[0];
                                                if (e.getY() < dHandler.pY[0]) { //Second click is Higher than first
                                                    topLY = e.getY();
                                                }
                                                else{
                                                    topLY = dHandler.pY[0];

                                                }

                                            }
                                            else{
                                                width = dHandler.pX[0] - e.getX();
                                                topLX = e.getX();


                                                if (e.getY() < dHandler.pY[0]) { //Second click is Higher than first
                                                    topLY = e.getY();
                                                }
                                                else{
                                                    topLY = dHandler.pY[0];

                                                }
                                            }
                                            if (e.getY() < dHandler.pY[0]) width = Math.max(width, (dHandler.pY[0] - e.getY()));
                                            else width = Math.max(width, (e.getY() - dHandler.pY[0]));
                                            FXC.fillRect(
                                                    topLX, topLY,
                                                    width, width);




                                            dHandler.click();

                                            dHandler.setShapeType(ShapeType.NONE);
                                            shapeSelect.getSelectedToggle().setSelected(false);

                                        }
                                    }

                                }
                                case CIRCLE -> {
                                    if (e.getEventType() == MouseEvent.MOUSE_CLICKED){
                                        if (dHandler.isFirstClick()){
                                            dHandler.click();
                                            dHandler.pX[0] = e.getX();
                                            dHandler.pY[0] = e.getY();


                                        }
                                        else{
                                            double topLX = 0;
                                            double topLY = 0;
                                            double width = 0;
                                            if (e.getX() > dHandler.pX[0]){ // Second click to the right of first
                                                width = e.getX() - dHandler.pX[0];
                                                topLX = dHandler.pX[0];
                                                if (e.getY() < dHandler.pY[0]) { //Second click is Higher than first
                                                    topLY = e.getY();
                                                }
                                                else{
                                                    topLY = dHandler.pY[0];

                                                }

                                            }
                                            else{
                                                width = dHandler.pX[0] - e.getX();
                                                topLX = e.getX();


                                                if (e.getY() < dHandler.pY[0]) { //Second click is Higher than first
                                                    topLY = e.getY();
                                                }
                                                else{
                                                    topLY = dHandler.pY[0];

                                                }
                                            }
                                            if (e.getY() < dHandler.pY[0]) width = Math.max(width, (dHandler.pY[0] - e.getY()));
                                            else width = Math.max(width, (e.getY() - dHandler.pY[0]));
                                            FXC.fillOval(
                                                    topLX, topLY,
                                                    width, width);




                                            dHandler.click();

                                            dHandler.setShapeType(ShapeType.NONE);
                                            shapeSelect.getSelectedToggle().setSelected(false);


                                        }

                                    }
                                    else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED){
                                        if (!dHandler.isFirstClick()){
                                            // TODO add handling for dragging

                                        }
                                        else{ // add same handling fo second click from previous section

                                        }

                                    }
                                }
                                case ELLIPSE -> {
                                    if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                                        if (dHandler.isFirstClick()) {
                                            dHandler.click();
                                            dHandler.pX[0] = e.getX();
                                            dHandler.pY[0] = e.getY();

                                        } else {
                                            FXC.setFill(dHandler.getCurrentColor());
                                            if (e.getX() > dHandler.pX[0]) {
                                                if (e.getY() > dHandler.pY[0]) {
                                                    FXC.fillOval(
                                                            dHandler.pX[0], dHandler.pY[0],
                                                            e.getX() - dHandler.pX[0], e.getY() - dHandler.pY[0]
                                                    );
                                                } else {
                                                    FXC.fillOval(
                                                            dHandler.pX[0], dHandler.pY[0],
                                                            e.getY() - dHandler.pX[0], dHandler.pY[0] - e.getY()
                                                    );
                                                }
                                            } else {
                                                if (e.getY() > dHandler.pY[0]) {
                                                    FXC.fillOval(
                                                            e.getX(), dHandler.pY[0],
                                                            dHandler.pX[0] - e.getX(), e.getY() - dHandler.pY[0]
                                                    );
                                                } else {
                                                    FXC.fillOval(
                                                            e.getX(), e.getY(),
                                                            dHandler.pX[0] - e.getX(), dHandler.pY[0] - e.getY()
                                                    );
                                                }

                                            }
                                            dHandler.click();

                                            dHandler.setShapeType(ShapeType.NONE);
                                            shapeSelect.getSelectedToggle().setSelected(false);
                                        }
                                    }

                                }
                                case RECTANGLE -> {
                                    if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                                        if (dHandler.isFirstClick()) {
                                            dHandler.click();
                                            dHandler.pX[0] = e.getX();
                                            dHandler.pY[0] = e.getY();

                                        } else {
                                            FXC.setFill(dHandler.getCurrentColor());
                                            if (e.getX() > dHandler.pX[0]) {
                                                if (e.getY() > dHandler.pY[0]) {
                                                    FXC.fillRect(
                                                            dHandler.pX[0], dHandler.pY[0],
                                                            e.getX() - dHandler.pX[0], e.getY() - dHandler.pY[0]
                                                    );
                                                } else {
                                                    FXC.fillRect(
                                                            dHandler.pX[0], dHandler.pY[0],
                                                            e.getY() - dHandler.pX[0], dHandler.pY[0] - e.getY()
                                                    );
                                                }
                                            } else {
                                                if (e.getY() > dHandler.pY[0]) {
                                                    FXC.fillRect(
                                                            e.getX(), dHandler.pY[0],
                                                            dHandler.pX[0] - e.getX(), e.getY() - dHandler.pY[0]
                                                    );
                                                } else {
                                                    FXC.fillRect(
                                                            e.getX(), e.getY(),
                                                            dHandler.pX[0] - e.getX(), dHandler.pY[0] - e.getY()
                                                    );
                                                }

                                            }
                                            dHandler.click();

                                            dHandler.setShapeType(ShapeType.NONE);
                                            shapeSelect.getSelectedToggle().setSelected(false);

                                        }
                                    }
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