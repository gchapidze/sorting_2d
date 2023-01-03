package com.play.sortinggui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ApplicationStage extends Application {
    HBox circleValueLabelPane;
    Double[][] properCoordinatesAfterSorting;
    private List<Circle> circleObjects;
    private static final double STAGE_WIDTH = 600.0d;
    public static final double STAGE_HEIGHT = 430.0d;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        setUp(stage);
    }

    private void setUp(Stage stage) {
        configureStage(stage);
        var root = initializeAndFillRoot();
        var scene = new Scene(root);

        stage.setScene(scene);
    }

    private void configureStage(Stage stage) {
        stage.setWidth(STAGE_WIDTH);
        stage.setHeight(STAGE_HEIGHT);
        stage.setResizable(false);
        stage.show();
    }

    private Region initializeAndFillRoot() {
        AnchorPane root = new AnchorPane();
        attachDisplayThings(root);
        return root;
    }

    private void attachDisplayThings(AnchorPane root) {
        Line line = new Line();
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(2.0d);
        line.setStartX(0.0d);
        line.setStartY(25.00);
        line.setEndY(25.00);
        line.setEndX(STAGE_WIDTH);

        MenuButton algorithmOptions = new MenuButton("Algorithms");
        algorithmOptions.setLayoutX(0.0d);
        algorithmOptions.setLayoutY(0.0d);


        var algorithms = algorithmOptions.getItems();
        MenuItem bubbleSort = new MenuItem("BubbleSort");
        algorithms.add(bubbleSort);


        AlgorithmTask algorithmService = new AlgorithmTask(bubbleSort.getText(), () -> {
            bubbleSort(root, circleObjects);
            return null;
        });

        bubbleSort.setOnAction((evt) -> {
            Thread thread = new Thread(algorithmService.createTask());
            thread.start();
        });

        Button reset = new Button("Reset");
        reset.setLayoutX(110.0d);
        reset.setLayoutY(0.0);
        reset.addEventHandler(ActionEvent.ACTION, (evt) -> {
            for (Circle circleObject : circleObjects) {
                root.getChildren().remove(circleObject);
            }
            root.getChildren().remove(circleValueLabelPane);
            stageCircleObjects(root);
        });

        root.getChildren().addAll(line, algorithmOptions, reset);
        stageCircleObjects(root);
    }

    private void stageCircleObjects(AnchorPane root) {
        circleValueLabelPane = new HBox();
        circleValueLabelPane.setSpacing(1.499999999999999d);
        circleValueLabelPane.setLayoutY(STAGE_HEIGHT - 70);
        circleValueLabelPane.setLayoutX(20);
        AtomicReference<Double> radius = new AtomicReference<>(7.0d);
        AtomicReference<Double> xPos = new AtomicReference<>(24.0d);
        AtomicReference<Double> yPos = new AtomicReference<>(350.0d);

        circleObjects = new ArrayList<>();
        int labelCount = 59;
        while (true) {
            Circle circle = new Circle();
            circle.setRadius(radius.get());
            ColorGenerator color = () -> {
                int r = Random.random(256);
                int g = Random.random(256);
                int b = Random.random(256);
                return Color.rgb(r, g, b);
            };
            circle.setFill(color.generate());
            circle.setLayoutX(xPos.get());
            circle.setLayoutY(yPos.get());

            Label label = new Label(String.valueOf(labelCount));
            label.setFont(Font.font("verdana", 7));
            labelCount--;

            circleObjects.add(circle);
            circleValueLabelPane.getChildren().add(label);
            xPos.set(xPos.get() + 9.59);
            if (circleObjects.size() == 59) {
                root.getChildren().add(circleValueLabelPane);
                break;
            }
            root.getChildren().add(circle);
            radius.set(radius.get() - 0.1);
        }
    }

    private void bubbleSort(AnchorPane layoutPane, List<Circle> circles) {
        layoutPane.getChildren().remove(circleValueLabelPane);
        List<Circle> copiedCircleList = new ArrayList<>(circles.size());
        copiedCircleList.addAll(circles);
        int size = circles.size();

        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (copiedCircleList.get(j).getLayoutX() < copiedCircleList.get(j + 1).getLayoutX()) {
                    var biggerCircle = copiedCircleList.get(j);
                    var smallerCircle = copiedCircleList.get(j + 1);

                    double circleXCoordinate = smallerCircle.getLayoutX();
                    double circleYCoordinate = smallerCircle.getLayoutY();

                    smallerCircle.setLayoutX(biggerCircle.getLayoutX());
                    smallerCircle.setLayoutY(biggerCircle.getLayoutY());
                    biggerCircle.setLayoutY(circleYCoordinate);
                    biggerCircle.setLayoutX(circleXCoordinate);

                    copiedCircleList.set(j, smallerCircle);
                    copiedCircleList.set(j + 1, biggerCircle);
                }
            }
            copiedCircleList.forEach((p -> layoutPane.getChildren().remove(p)));
            copiedCircleList.forEach((p -> layoutPane.getChildren().add(p)));
        }
        parseCoordinates();
        int i = 0;
        for (Circle p : copiedCircleList) {
            p.setLayoutX(properCoordinatesAfterSorting[i][0]);
            p.setLayoutY(properCoordinatesAfterSorting[i][1]);
            i++;
        }
    }

    private void parseCoordinates() {
        properCoordinatesAfterSorting = new Double[circleObjects.size()][2];
        try (FileReader reader = new FileReader("Sorting_app/src/main/java/com/play/sortinggui/coordinates.txt"); BufferedReader fileReader = new BufferedReader(reader)) {
            String fileInput;
            int rowIncrementer = 0;
            while ((fileInput = fileReader.readLine()) != null) {
                String[] coordinate = fileInput.split(",");
                properCoordinatesAfterSorting[rowIncrementer][0] = Double.parseDouble(coordinate[0]);
                properCoordinatesAfterSorting[rowIncrementer][1] = Double.parseDouble(coordinate[1]);
                rowIncrementer++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
