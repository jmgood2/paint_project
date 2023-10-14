package com.example.paint_project;

import javafx.scene.control.TabPane;

public class CanvasTabPane extends TabPane {
    public CanvasTab getCurrentTab(){
        int index = this.getSelectionModel().getSelectedIndex();
        return (CanvasTab) this.getTabs().get(index);

    }

}
