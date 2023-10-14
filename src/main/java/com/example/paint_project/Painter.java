package com.example.paint_project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class Painter extends Application {
    // Logging Utilities
    private static Logger logger;
    private static StreamHandler streamH;
    // Private Variables

    int[] picker;
    double canvasH;
    double canvasW;
    // Handlers
    private ImageHandler iHandler;
    private DrawHandler dHandler;
    private PaletteHandler pHandler;
    // Paths
    private Path workspace, workdir, tempDir;
    // Labels
    Label shapeText;
    Label pickerXY;
    Label lineType;
    Label shapeStyle;
    // ImageView
    ImageView imageV;
    // Shapes
    Rectangle pickerColor;
    // Widgets
    private Slider lineWidthSlider;
    private TextField textW;
    // Menu Shapes
    private Rectangle widthPreviewBack;
    private Rectangle widthPreviewFore;
    // Menus
    private Menu menuF;
    private Menu editM;
    private Menu helpM;
    private Menu lineMenu;
    private Menu shapeMenu;
    // Menu Bar
    private MenuBar lineBar;
    private MenuBar shapeBar;
    // Menu Items
    private MenuItem openI;
    private MenuItem saveI;
    private MenuItem saveIAs;
    private MenuItem closeI;
    private MenuItem exit;
    private MenuItem undo;
    private MenuItem redo;
    private MenuItem notes;
    private MenuItem about;
    private MenuItem solidLine;
    private MenuItem dashLine;
    private MenuItem dotLine;
    private MenuItem shapeFill;
    private MenuItem shapeOutline;
    private MenuItem shapeOutlineDash;

    // Buttons
    private ToggleButton freeDraw;
    private ToggleButton lineDraw;
    private ToggleButton shapes;
    private ToggleButton colorPicker;
    ToggleButton triangleSelect;
    ToggleButton squareSelect;
    ToggleButton circleSelect;
    ToggleButton ellipseSelect;
    ToggleButton rectangleSelect;

    // Groups
    private MenuBar menuB;
    private ToggleGroup buttons;
    VBox colorVBox;
    private Group widthPreviewRoot;
    VBox freeVBox;
    VBox lineVBox;
    ToggleGroup shapeSelect;
    VBox shapesVBox;
    VBox pickerVBox;
    VBox FLSRoot;
    VBox vBRoot;
    HBox hB1;


    // Canvas
    //Canvas canvas;
    // Panes
    private GridPane buttonGrid;
    GridPane shapeGrid;
    BorderPane borderRoot;
    //ScrollPane canvasPane;
    CanvasTabPane tabPane;
    // Scenes
    Scene baseScene;
    // GraphicsContext
    //GraphicsContext FXC;
    // Files
    File tempImage;





    //Constructor
    @Override
    public void start(Stage stage) throws IOException {

        // Variables
        picker = new int[2];
        canvasH = 500;
        canvasW = 500;
        // Handlers
        iHandler = new ImageHandler();
        dHandler = new DrawHandler();
        pHandler = new PaletteHandler();

        // Paths
        workspace = Paths.get("Images");
        workdir = Files.createDirectories(workspace);
        tempDir = Files.createTempDirectory(workspace, "temp");

        // Labels
        shapeText = new Label("Shape: None");
        pickerXY = new Label("Coordinates: " + picker[0] + ", " + picker[1]);
        lineType = new Label(dHandler.getCurrentLineType());
        shapeStyle = new Label(dHandler.getCurrentShapeStyle());

        // ImageView
        imageV = new ImageView();
        imageV.setVisible(false);
        imageV.setX(5);
        imageV.setY(5);
        imageV.setFitWidth(600);
        imageV.setPreserveRatio(true);

        // Imageview Setup



        // Canvas
        //canvas = new Canvas();

        // Canvas Setup
        //canvas.setHeight(canvasH);
        //canvas.setWidth(canvasW);

        // Shapes
        pickerColor = new Rectangle(20, 20);

        // Widgets
        lineWidthSlider = new Slider(0, 50, 5);
        lineWidthSlider.setOrientation(Orientation.VERTICAL);
        lineWidthSlider.setShowTickMarks(true);
        lineWidthSlider.setShowTickLabels(true);
        lineWidthSlider.setBlockIncrement(1);

        textW = new TextField(Double.toString(dHandler.getLineWidth()));
        textW.setMinWidth(20);
        textW.setMaxWidth(50);

        // Menu Shapes
        widthPreviewBack = new Rectangle(50, 50);
        widthPreviewBack.setStroke(Color.BLACK);
        widthPreviewBack.setFill(Color.TRANSPARENT);
        widthPreviewFore = new Rectangle(24,24,
                pHandler.getCurrentLine().getStrokeWidth(),
                pHandler.getCurrentLine().getStrokeWidth());
        widthPreviewFore.setStroke(Color.TRANSPARENT);
        widthPreviewFore.setFill(dHandler.getCurrentColor());

        // Menus
        menuF = new Menu("File");
        editM = new Menu("Edit");
        helpM = new Menu("Help");
        lineMenu = new Menu("", lineType);
        shapeMenu = new Menu("", shapeStyle);

        // Menu Bars
        lineBar = new MenuBar(lineMenu);
        shapeBar = new MenuBar(shapeMenu);

        // Menu Items
        openI = new MenuItem("Open Image");
        openI.setAccelerator(KeyCombination.keyCombination("Shortcut+Shift+N"));
        saveI = new MenuItem("Save Image");
        saveI.setAccelerator(KeyCombination.keyCombination("Shortcut+S"));
        saveIAs = new MenuItem("Save Image As");
        saveIAs.setAccelerator(KeyCombination.keyCombination("Shortcut+Shift+S"));
        closeI = new MenuItem("Close Image");
        closeI.setAccelerator(KeyCombination.keyCombination("F1"));
        exit = new MenuItem("Close");
        exit.setAccelerator(KeyCombination.keyCombination("F4"));

        undo = new MenuItem("Undo");
        undo.setAccelerator(KeyCombination.keyCombination("Shortcut+Z"));
        redo = new MenuItem("Redo");
        redo.setAccelerator(KeyCombination.keyCombination("Shortcut+Y"));

        notes = new MenuItem("Release Notes");
        about = new MenuItem("About");

        solidLine = new MenuItem("Solid");
        dashLine = new MenuItem("Dash");
        dotLine = new MenuItem("Dot");

        shapeFill = new MenuItem("Filled");
        shapeOutline = new MenuItem("Outline");
        shapeOutlineDash = new MenuItem("Dashed");

        // Buttons
        freeDraw = new ToggleButton("FREE");
        lineDraw = new ToggleButton("LINE");
        shapes = new ToggleButton("Shapes");
        colorPicker = new ToggleButton("Color\nPicker");
        triangleSelect = new ToggleButton("",
                new Polygon(0,10, 5,0, 10,10));
        squareSelect = new ToggleButton("",
                new Rectangle(10,10));
        circleSelect = new ToggleButton("",
                new Circle(5));
        ellipseSelect = new ToggleButton("",
                new Ellipse(5, 3.5));
        rectangleSelect = new ToggleButton("",
                new Rectangle(10, 5));


        // Create buttons
        freeDraw.setPrefSize(65, 40);
        Circle c1 = new Circle();
        c1.setRadius(4);
        c1.setFill(Color.BLACK);
        freeDraw.setGraphic(c1);

        lineDraw.setPrefSize(55, 40);
        Line l1 = new Line();
        l1.setStrokeWidth(2);
        l1.setStartX(0);
        l1.setStartY(6);
        l1.setEndX(6);
        l1.setEndY(0);
        l1.setStroke(Color.BLACK);
        lineDraw.setGraphic(l1);shapes.setPrefSize(65, 40);

        Polygon triangle = new Polygon();   // Does this need moved to private?
        triangle.getPoints().addAll(
                0.0, 6.0,
                3.0, 0.0,
                6.0, 6.0);
        shapes.setGraphic(triangle);

        colorPicker.setPrefSize(55, 40);


        // Panes
        buttonGrid = new GridPane();
        shapeGrid = new GridPane();
        borderRoot = new BorderPane();
        //canvasPane = new ScrollPane(canvas);
        tabPane = (CanvasTabPane) new TabPane(new CanvasTab(workspace));

        // Groups
        menuB = new MenuBar(menuF, editM, helpM);
        buttons = new ToggleGroup();
        colorVBox = new VBox(5);
        widthPreviewRoot = new Group(widthPreviewBack, widthPreviewFore);
        freeVBox = new VBox();
        lineVBox = new VBox();
        shapeSelect = new ToggleGroup();
        shapesVBox = new VBox();
        pickerVBox = new VBox();
        FLSRoot = new VBox(
                new HBox(lineWidthSlider,
                        new VBox(widthPreviewRoot, textW)),
                freeVBox);
        vBRoot = new VBox(buttonGrid, colorVBox, FLSRoot);
        hB1 = new HBox(menuB);

        // Group Assignments
        // Menus
        menuF.getItems().add(openI);
        menuF.getItems().add(saveI);
        menuF.getItems().add(saveIAs);
        menuF.getItems().add(closeI);
        menuF.getItems().add(exit);

        editM.getItems().add(undo);
        editM.getItems().add(redo);

        helpM.getItems().add(notes);
        helpM.getItems().add(about);

        lineMenu.getItems().add(solidLine);
        lineMenu.getItems().add(dashLine);
        lineMenu.getItems().add(dotLine);

        shapeMenu.getItems().addAll(shapeFill,
                shapeOutline,
                shapeOutlineDash);


        triangleSelect.setToggleGroup(shapeSelect);
        circleSelect.setToggleGroup(shapeSelect);
        squareSelect.setToggleGroup(shapeSelect);
        ellipseSelect.setToggleGroup(shapeSelect);
        rectangleSelect.setToggleGroup(shapeSelect);

        // Groups
        freeDraw.setToggleGroup(buttons);
        lineDraw.setToggleGroup(buttons);
        shapes.setToggleGroup(buttons);
        colorPicker.setToggleGroup(buttons);

        // VBoxes
        /// Color Palette
        colorVBox.setAlignment(Pos.CENTER);
        colorVBox.setPadding(new Insets(10));
        for (int i = 0; i < 8; i = i+2){
            //vB1.getChildren().add(pHandler.getRect(i));
            // Add rows of 2 colors x 4
            HBox h = new HBox(pHandler.getRect(i),
                    pHandler.getRect(i+1));
            h.setAlignment(Pos.CENTER);
            colorVBox.getChildren().add(h);
        }
        colorVBox.getChildren().add(pHandler.getCurrentColorRect());
        Label rgbHash = new Label(pHandler.getColorRGB());
        colorVBox.getChildren().add(rgbHash);
        colorVBox.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                new CornerRadii(2),
                new BorderWidths(2)
        )));

        freeVBox.getChildren().addAll(
                new Label("Brush Shape",
                        lineBar)
        );

        lineVBox.getChildren().addAll(
                lineBar);

        shapesVBox.getChildren().addAll(
                shapeBar,
                new Label("Shape Size"),
                shapeText,
                shapeGrid
        );

        pickerColor.setStroke(Color.BLACK);
        pickerVBox.getChildren().addAll(
                pickerXY,
                new HBox(
                        new Label("Pixel Color: "),
                        pickerColor
                )
        );

        // HBoxes


        // Create Panes
        // ButtonGrid
        buttonGrid.setPrefSize(50, 50);
        buttonGrid.add(freeDraw, 0,0);
        buttonGrid.add(lineDraw, 1, 0);
        buttonGrid.add(shapes, 0, 1);
        buttonGrid.add(colorPicker, 1, 1);
        buttonGrid.getColumnConstraints().add(new ColumnConstraints(65));
        buttonGrid.getColumnConstraints().add(new ColumnConstraints(55));
        buttonGrid.getRowConstraints().add(new RowConstraints(40));
        buttonGrid.getRowConstraints().add(new RowConstraints(40));

        // ShapeGrid
        shapeGrid.setPrefSize(50, 50);
        shapeGrid.add(triangleSelect, 0,0);
        shapeGrid.add(squareSelect, 1, 0);
        shapeGrid.add(circleSelect, 3, 0);
        shapeGrid.add(ellipseSelect, 0, 1);
        shapeGrid.add(rectangleSelect, 1, 1);

        // Scale Scrolling
//        canvasPane.getContent().setOnScroll(scrollEvent ->{
//            double y0 = scrollEvent.getDeltaY();
//            double cHeight = canvasPane.getContent().getBoundsInLocal().getHeight();
//            double sHeight = canvasPane.getHeight();
//            double y1 = 1;
//            if (cHeight != sHeight) y1 = (cHeight - sHeight);
//            double vVal = canvasPane.getVvalue();
//            canvasPane.setVvalue(vVal + -y0/y1);
//
//            double x0 = scrollEvent.getDeltaX();
//            double cWidth = canvasPane.getContent().getBoundsInLocal().getWidth();
//            double sWidth = canvasPane.getWidth();
//            double x1 = 1;
//            if (cWidth != cWidth) x1 = (cWidth - sWidth);
//            double hVal = canvasPane.getHvalue();
//            canvasPane.setHvalue(hVal + -x0/x1);
//
//        });
//
//        canvasPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        //BorderPane.setAlignment(canvasPane, Pos.TOP_LEFT);
        //BorderPane.setMargin(canvasPane, new Insets(20,12,12,20));
        //borderRoot.setTop(hB1);
        //borderRoot.setCenter(canvasPane);
        //borderRoot.setLeft(vBRoot);
        BorderPane.setAlignment(tabPane, Pos.TOP_LEFT);
        BorderPane.setMargin(tabPane, new Insets(20,12,12,20));
        borderRoot.setTop(hB1);
        borderRoot.setCenter(tabPane);
        borderRoot.setLeft(vBRoot);
        logger.info(tabPane.getTabs().toString());
        // Scenes
        baseScene = new Scene(borderRoot,
                Screen.getPrimary().getVisualBounds().getWidth(),
                Screen.getPrimary().getVisualBounds().getHeight());

        // Create Stage
        stage.setTitle("Paint (t) 2.0");
        stage.setMaximized(true);
        stage.setScene(baseScene);
        stage.show();


//        // GraphicsContext
//        FXC = canvas.getGraphicsContext2D();
//
//        // GraphicsContext Setup
//        FXC.setFill(Color.WHITE);
//        FXC.fillRect(0,
//                0,
//                canvas.getWidth(),
//                canvas.getHeight());

//        // Files
//        tempImage = new File(String.valueOf(Files.createTempFile(
//                tempDir,
//                null,
//                ".jpg")));

        // Initialize Temp Files
        //iHandler.newTempFiles(canvas, tempDir, tempImage);


        // Actions
        // OPEN image
        openI.setOnAction(
                aE -> {
                    // TODO Opening a new image should create a new canvasTab with that image
                    //Open Image
                    File openFile = ImageHandler.openImage(stage);
                    tabPane.getTabs().add(new CanvasTab(workspace, openFile));
                    //String openFilePath = openFile.getAbsolutePath();
                    //System.out.println("DEBUG -- Opening " + openFilePath + "...");

                    // Add opened image to Image Handler
//                    try {
//                        iHandler.addImage(openFile);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    // Pull Image from file
//                    Image image = null;
//                    try {
//                        image = new Image(new FileInputStream(openFilePath));
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }

                    // Resize canvas to fit image
//                    canvas.setHeight(image.getHeight());
//                    canvas.setWidth(image.getWidth());
//
//                    // Clear canvas
//                    FXC.clearRect(
//                            0,0,
//                            canvas.getWidth(),
//                            canvas.getHeight());
//
//                    FXC.drawImage(image,
//                            0,
//                            0);

//                    iHandler.setCurrentImageFile(openFile);
//
//                    String extension = openFile.getPath().substring(openFile.getPath().lastIndexOf('.'));
//
//                    File tempFile = null;
//                    try{
//                        tempFile = new File(String.valueOf(
//                                Files.createTempFile(
//                                        tempDir,
//                                        null,
//                                        extension)));
//                    } catch (IOException ex){
//                        ex.printStackTrace();
//                    }
//
//                    try{
//                        assert tempFile != null;
//                        iHandler.newTempFiles(canvas, tempDir, tempFile);
//                    } catch (IOException ex){
//                        ex.printStackTrace();
//                    }
                    iHandler.notSaved();
                }
        );
        // Save Image
        saveI.setOnAction(
                aE -> {
                    tabPane.getCurrentTab().saveCanvas(stage);
//
//                    File iFile = iHandler.getOpenImage();
//
//                    String fType = iFile.getName().substring(
//                            iFile.getName().lastIndexOf('.') + 1);
//                    System.out.println("DEBUG -- File extension of " + iFile.getAbsolutePath() + " is " + fType);
//                    if (iFile == null){
//                        //File file = saveImage(stage, new File ("Images"));
//                        File file = ImageHandler.saveImage(stage, new File(workdir.toString()));
//
//                        System.out.println("DEBUG -- RUNNING SAVE IMAGE");
//                        ImageHandler.saveImageAs(canvas, file);
//                    }
//                    else {
//                        System.out.println("DEBUG -- SAVING...");
//                        //saveImageAs(canvas, iFile);
//                        ImageHandler.saveImageAs(canvas, iFile);
//
//
//                    }
//                    System.out.println("DEBUG -- RUNNING SAVE IMAGE AS");
//                    iHandler.saved();

                }
        );



        // SAVE image AS
        saveIAs.setOnAction(
                aE -> {
                    tabPane.getCurrentTab().saveCanvasAs(stage);
//
//                    //File file = saveImage(stage, new File(workdir.toString()));
//                    File file = ImageHandler.saveImage(stage, new File(workdir.toString()));
//                    if (file == null) {
//                        try {
//                            Files.createFile(file.toPath());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    System.out.println("DEBUG -- RUNNING SAVE IMAGE");
//                    ImageHandler.saveImageAs(canvas, file);
//                    System.out.println("DEBUG -- RUNNING SAVE IMAGE AS");
//
//
//                    iHandler.saved();

                });

        // CLOSE image
        closeI.setOnAction(
                aE -> {
                    // Close Image and clear canvas
                    tabPane.getCurrentTab().closeImage();
                    tabPane.getTabs().remove(tabPane.getCurrentTab());

//
//                    iHandler.closeImage();
//                    FXC.clearRect(0,
//                            0,
//                            canvas.getWidth(),
//                            canvas.getHeight());
//
//                    try {
//                        iHandler.clearTempFiles(tempDir);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }

        );

        // EXIT program
        exit.setOnAction(
                aE -> {
                    // TODO have it cycle through each open tab anad repeat this
                    if (tabPane.getCurrentTab().getImageHandler().getSaveStatus()) {
                        tabPane.getCurrentTab().closeTab();
                    } else {
                        saveWarning(stage);
                    }
                }
        );

        //Undo action
        // TODO Modify Drawing to erase temp files when redo + new draw (delete temp files AND pop off tempImage list
        undo.setOnAction(
                aE -> {
                    tabPane.getCurrentTab().undo();
                }
        );

        // Redo action
        redo.setOnAction(
                aE -> {
                    tabPane.getCurrentTab().redo();
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
                    dHandler.resetClick();
                    dHandler.setDrawType(DrawType.FREE);

                    //vBRoot.getChildren().set(3, FLSRoot);
                    FLSRoot.getChildren().set(1, freeVBox);
                }

        );
        lineDraw.setOnAction(
                bE -> {
                    dHandler.resetClick();
                    dHandler.setDrawType(DrawType.LINE);

                    //vBRoot.getChildren().set(3, FLSRoot);
                    FLSRoot.getChildren().set(1, lineVBox);
                }
        );
        shapes.setOnAction(
                bE -> {
                    dHandler.resetClick();
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
        colorVBox.addEventHandler(MouseEvent.MOUSE_CLICKED,
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

        lineMenu.setOnAction(
                aE -> lineMenu.setGraphic(lineType)

        );

        solidLine.setOnAction(
                aE -> {
                    dHandler.setLineType(LineType.SOLID);
                    lineType.setText(dHandler.getCurrentLineType());
                }
        );
        dashLine.setOnAction(
                aE -> {

                    dHandler.setLineType(LineType.DASH);
                    lineType.setText(dHandler.getCurrentLineType());
                }
        );
        dotLine.setOnAction(
                aE -> {
                    dHandler.setLineType(LineType.DOT);
                    lineType.setText(dHandler.getCurrentLineType());
                }
        );

        shapeFill.setOnAction(
                aE ->{
                    dHandler.setShapeStyle(ShapeStyle.FILLED);
                    shapeStyle.setText(dHandler.getCurrentShapeStyle());
                }
        );
        shapeOutline.setOnAction(
                aE ->{
                    dHandler.setShapeStyle(ShapeStyle.OUTLINE);
                    shapeStyle.setText(dHandler.getCurrentShapeStyle());

                }
        );
        shapeOutlineDash.setOnAction(
                aE ->{
                    dHandler.setShapeStyle(ShapeStyle.DASH);
                    shapeStyle.setText(dHandler.getCurrentShapeStyle());

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
                    dHandler.resetClick();
                    dHandler.setShapeType(ShapeType.TRIANGLE);
                    shapeText.setText("Shape: TRIANGLE");

                }
        );

        circleSelect.setOnAction(
                bE -> {
                    dHandler.resetClick();
                    dHandler.setShapeType(ShapeType.CIRCLE);
                    shapeText.setText("Shape: CIRCLE");

                }
        );

        ellipseSelect.setOnAction(
                bE -> {
                    dHandler.resetClick();
                    dHandler.setShapeType(ShapeType.ELLIPSE);
                    shapeText.setText("Shape: ELLIPSE");

                }
        );

        squareSelect.setOnAction(
                bE -> {
                    dHandler.resetClick();
                    dHandler.setShapeType(ShapeType.SQUARE);
                    shapeText.setText("Shape: SQUARE");

                }
        );

        rectangleSelect.setOnAction(
                bE -> {
                    dHandler.resetClick();
                    dHandler.setShapeType(ShapeType.RECTANGLE);
                    shapeText.setText("Shape: RECTANGLE");

                }
        );

        // DRAWING

        tabPane.getCurrentTab().getCanvas().addEventHandler(MouseEvent.ANY,
                e -> {


                    if (dHandler.getDrawType() != DrawType.PICKER) {
                        tabPane.getCurrentTab().pop(e);
//                        iHandler.notSaved();
//
//                        if (iHandler.getUndoneStatus()
//                                && e.getEventType() != MouseEvent.MOUSE_MOVED
//                                && e.getEventType() != MouseEvent.MOUSE_ENTERED
//                                && e.getEventType() != MouseEvent.MOUSE_EXITED) {
//                            System.out.println(e.getEventType());
//                            try {
//                                iHandler.popTemp();
//                            } catch (IOException ex) {
//                                throw new RuntimeException(ex);
//                            }
//                        }
                    }

                    FXC.setFill(pHandler.getCurrentColor());
                    FXC.setStroke(pHandler.getCurrentColor());
                    FXC.setLineWidth(dHandler.getLineWidth());
                    if (dHandler.getLineType() == LineType.DASH){
                        FXC.setLineDashes(50d, 30d);
                    }
                    else if (dHandler.getLineType() == LineType.DOT){
                        FXC.setLineDashes(20d, 15d);

                    }
                    else{
                        FXC.setLineDashes();
                    }
                    switch (dHandler.getDrawType()) {
                        case FREE -> {
                            if (dHandler.isFirstClick()){
                                FXC.beginPath();
                                dHandler.click();
                            }
                            //System.out.println("FREE");
                            if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                                if (e.isPrimaryButtonDown()) {
                                    /*FXC.setFill(dHandler.getCurrentColor());
                                    FXC.fillRect(e.getX() - 3,
                                            e.getY() - 3,
                                            5,
                                            5);*/
                                    //FXC.beginPath();
                                    FXC.lineTo(e.getX(), e.getY());
                                    FXC.stroke();



                                } else if (e.isSecondaryButtonDown()) {

                                    FXC.clearRect(e.getX() - 3,
                                            e.getY() - 3,
                                            5,
                                            5);

                                }
                                //try {
                                //    pushTempFile(canvas, tempDir, iHandler);
                                //} catch (IOException ex) {
                                //    throw new RuntimeException(ex);
                                //}
                            } else if (e.getEventType() == MouseEvent.MOUSE_RELEASED){
                                FXC.beginPath();
                                FXC.moveTo(e.getX(), e.getY());
                                FXC.stroke();
                                if (!dHandler.isFirstClick()) dHandler.click();

                                try {
                                    iHandler.pushTempFile(canvas, tempDir);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                        case LINE -> {
                            if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                                if (dHandler.isFirstClick()) {
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
                                    dHandler.click();

                                    try {
                                        iHandler.pushTempFile(canvas, tempDir);
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }


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
                                            FXC.setFill(dHandler.getCurrentColor());
                                            //FXC.strokePolygon(
                                            //        dHandler.pX, dHandler.pY, 3);
                                            if (dHandler.getShapeStyle() == ShapeStyle.FILLED) {
                                                FXC.fillPolygon(
                                                        dHandler.pX, dHandler.pY, 3);
                                            }
                                            else {
                                                if (dHandler.getShapeStyle() == ShapeStyle.DASH){
                                                    FXC.setLineDashes(20);
                                                }
                                                FXC.strokePolygon(
                                                        dHandler.pX,
                                                        dHandler.pY, 3
                                                );
                                            }
                                            dHandler.click();
                                            dHandler.setPoints(0);

                                            dHandler.setShapeType(ShapeType.NONE);
                                            shapeSelect.getSelectedToggle().setSelected(false);

                                            try {
                                                iHandler.pushTempFile(canvas, tempDir);
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }
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

                                            if (dHandler.getShapeStyle() == ShapeStyle.FILLED) {
                                                FXC.fillRect(
                                                        topLX, topLY,
                                                        width, width);
                                            }
                                            else {
                                                if (dHandler.getShapeStyle() == ShapeStyle.DASH){
                                                    FXC.setLineDashes(20);
                                                }
                                                FXC.strokeRect(
                                                        topLX, topLY,
                                                        width, width);
                                            }




                                            dHandler.click();

                                            dHandler.setShapeType(ShapeType.NONE);
                                            shapeSelect.getSelectedToggle().setSelected(false);

                                            try {
                                                iHandler.pushTempFile(canvas, tempDir);
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }

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

                                            if (dHandler.getShapeStyle() == ShapeStyle.FILLED){
                                                FXC.fillOval(
                                                        topLX, topLY,
                                                        width, width);

                                            }
                                            else {
                                                if (dHandler.getShapeStyle() == ShapeStyle.DASH) FXC.setLineDashes(20);;
                                                FXC.strokeOval(
                                                        topLX, topLY,
                                                        width, width);
                                            }




                                            dHandler.click();

                                            dHandler.setShapeType(ShapeType.NONE);
                                            shapeSelect.getSelectedToggle().setSelected(false);

                                            try {
                                                iHandler.pushTempFile(canvas, tempDir);
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }


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
                                                    if (dHandler.getShapeStyle() == ShapeStyle.FILLED){

                                                        FXC.fillOval(
                                                                dHandler.pX[0], dHandler.pY[0],
                                                                e.getX() - dHandler.pX[0], e.getY() - dHandler.pY[0]
                                                        );

                                                    }
                                                    else {
                                                        if (dHandler.getShapeStyle() == ShapeStyle.DASH) FXC.setLineDashes(20);;
                                                        FXC.strokeOval(
                                                                dHandler.pX[0], dHandler.pY[0],
                                                                e.getX() - dHandler.pX[0], e.getY() - dHandler.pY[0]
                                                        );

                                                    }
                                                } else {
                                                    if (dHandler.getShapeStyle() == ShapeStyle.FILLED){

                                                        FXC.fillOval(
                                                                dHandler.pX[0], dHandler.pY[0],
                                                                e.getY() - dHandler.pX[0], dHandler.pY[0] - e.getY()
                                                        );

                                                    }
                                                    else {
                                                        if (dHandler.getShapeStyle() == ShapeStyle.DASH) FXC.setLineDashes(20);;
                                                        FXC.strokeOval(
                                                                dHandler.pX[0], dHandler.pY[0],
                                                                e.getY() - dHandler.pX[0], dHandler.pY[0] - e.getY()
                                                        );

                                                    }
                                                }
                                            } else {
                                                if (e.getY() > dHandler.pY[0]) {
                                                    if (dHandler.getShapeStyle() == ShapeStyle.FILLED){
                                                        FXC.fillOval(
                                                                e.getX(), dHandler.pY[0],
                                                                dHandler.pX[0] - e.getX(), e.getY() - dHandler.pY[0]
                                                        );

                                                    }
                                                    else {
                                                        if (dHandler.getShapeStyle() == ShapeStyle.DASH) FXC.setLineDashes(20);;
                                                        FXC.strokeOval(
                                                                e.getX(), dHandler.pY[0],
                                                                dHandler.pX[0] - e.getX(), e.getY() - dHandler.pY[0]
                                                        );

                                                    }
                                                } else {
                                                    if (dHandler.getShapeStyle() == ShapeStyle.FILLED){
                                                        FXC.fillOval(
                                                                e.getX(), e.getY(),
                                                                dHandler.pX[0] - e.getX(), dHandler.pY[0] - e.getY()
                                                        );

                                                    }
                                                    else {
                                                        if (dHandler.getShapeStyle() == ShapeStyle.DASH) FXC.setLineDashes(20);;
                                                        FXC.strokeOval(
                                                                e.getX(), e.getY(),
                                                                dHandler.pX[0] - e.getX(), dHandler.pY[0] - e.getY()
                                                        );


                                                    }
                                                }

                                            }
                                            dHandler.click();

                                            dHandler.setShapeType(ShapeType.NONE);
                                            shapeSelect.getSelectedToggle().setSelected(false);


                                            try {
                                                iHandler.pushTempFile(canvas, tempDir);
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }
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
                                                    if (dHandler.getShapeStyle() == ShapeStyle.FILLED){
                                                        FXC.fillRect(
                                                                dHandler.pX[0], dHandler.pY[0],
                                                                e.getX() - dHandler.pX[0], e.getY() - dHandler.pY[0]
                                                        );

                                                    }
                                                    else {
                                                        if (dHandler.getShapeStyle() == ShapeStyle.DASH) FXC.setLineDashes(20);;
                                                        FXC.strokeRect(
                                                                dHandler.pX[0], dHandler.pY[0],
                                                                e.getX() - dHandler.pX[0], e.getY() - dHandler.pY[0]
                                                        );

                                                    }
                                                } else {
                                                    if (dHandler.getShapeStyle() == ShapeStyle.FILLED){
                                                        FXC.fillRect(
                                                                dHandler.pX[0], e.getY(),
                                                                e.getX() - dHandler.pX[0], dHandler.pY[0] - e.getY()
                                                        );


                                                    }
                                                    else {
                                                        if (dHandler.getShapeStyle() == ShapeStyle.DASH) FXC.setLineDashes(20);;
                                                        FXC.strokeRect(
                                                                dHandler.pX[0], e.getY(),
                                                                e.getX() - dHandler.pX[0], dHandler.pY[0] - e.getY()
                                                        );

                                                    }
                                                }
                                            } else {
                                                if (e.getY() > dHandler.pY[0]) {
                                                    if (dHandler.getShapeStyle() == ShapeStyle.FILLED){
                                                        FXC.fillRect(
                                                                e.getX(), dHandler.pY[0],
                                                                dHandler.pX[0] - e.getX(), e.getY() - dHandler.pY[0]
                                                        );


                                                    }
                                                    else {
                                                        if (dHandler.getShapeStyle() == ShapeStyle.DASH) FXC.setLineDashes(20);;
                                                        FXC.strokeRect(
                                                                e.getX(), dHandler.pY[0],
                                                                dHandler.pX[0] - e.getX(), e.getY() - dHandler.pY[0]
                                                        );

                                                    }
                                                } else {
                                                    if (dHandler.getShapeStyle() == ShapeStyle.FILLED){
                                                        FXC.fillRect(
                                                                e.getX(), e.getY(),
                                                                dHandler.pX[0] - e.getX(), dHandler.pY[0] - e.getY()
                                                        );


                                                    }
                                                    else {
                                                        if (dHandler.getShapeStyle() == ShapeStyle.DASH) FXC.setLineDashes(20);;
                                                        FXC.strokeRect(
                                                                e.getX(), e.getY(),
                                                                dHandler.pX[0] - e.getX(), dHandler.pY[0] - e.getY()
                                                        );

                                                    }
                                                }

                                            }
                                            dHandler.click();

                                            dHandler.setShapeType(ShapeType.NONE);
                                            shapeSelect.getSelectedToggle().setSelected(false);


                                            try {
                                                iHandler.pushTempFile(canvas, tempDir);
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }

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



    public void closePaint(ImageHandler iH, Path temp){

        try {
            iHandler.clearTemp(new File(temp.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.exit();
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

    public void saveWarning(Stage stage){
        // Pop up if current image is not saved
        Stage sPop = new Stage();
        sPop.setTitle("WARNING");

        VBox root = new VBox();
        Label warning = new Label("WARNING!\n-------\nYou are about to exit without saving!");
        HBox options = new HBox();

        Button sQuit = new Button("Save and Exit");
        Button quit = new Button("Exit Without Saving");
        Button canc = new Button("Cancel");

        options.getChildren().addAll(sQuit, quit, canc);

        root.getChildren().addAll(warning, options);

        sQuit.setOnAction(
                sA -> {
                    File file = ImageHandler.saveImage(stage, new File(workdir.toString()));

                    System.out.println("DEBUG -- RUNNING SAVE IMAGE");
                    ImageHandler.saveImageAs(canvas, file);

//
//                    File iFile = iHandler.getOpenImage();
//
//                    String fType = iFile.getName().substring(
//                            iFile.getName().lastIndexOf('.') + 1);
//                    System.out.println("DEBUG -- File extension of " + iFile.getAbsolutePath() + " is " + fType);
//                    if (iFile == null){
//                    }
//                    else {
//                        System.out.println("DEBUG -- SAVING...");
//                        //saveImageAs(canvas, iFile);
//                        ImageHandler.saveImageAs(canvas, iFile);
//
//
//                    }
                    System.out.println("DEBUG -- RUNNING SAVE IMAGE AS");
                    closePaint(iHandler, tempDir);
                }
        );

        quit.setOnAction(
                qA -> closePaint(iHandler, tempDir)
        );

        canc.setOnAction(
                cA -> sPop.close()
        );

        Scene aScene = new Scene (root, 350, 80);
        sPop.setScene(aScene);
        sPop.showAndWait();
    }



    /* main method
     * launches Pain(T)
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // Logging Utilies
        logger = Logger.getLogger("Paint");
        streamH = new StreamHandler(System.out, new SimpleFormatter());

        logger.addHandler(streamH);
        logger.setLevel(Level.ALL);
        logger.info("test");
        launch();

    }






}
