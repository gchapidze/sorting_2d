package com.play.sortinggui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
        var scene = buildScene(root);

        stage.setScene(scene);
    }

    private void configureStage(Stage stage) {
        stage.setWidth(STAGE_WIDTH);
        stage.setHeight(STAGE_HEIGHT);
        stage.setResizable(false);
        stage.show();
    }

    private Scene buildScene(Region root) {
        Scene scene = new Scene(root);
        return scene;
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

        ChoiceBox<String> algorithmOptions = new ChoiceBox<>();
        algorithmOptions.setLayoutX(0.0d);
        algorithmOptions.setLayoutY(0.0d);

        var algorithms = algorithmOptions.getItems();
        algorithms.add("BubbleSort");

        algorithmOptions.getSelectionModel().selectedItemProperty().addListener((algorithm, x, y) -> {
            String algo = algorithm.getValue();
            if (algo.equals("BubbleSort")) {
                bubbleSort(root, circleObjects);
            }
        });

        root.getChildren().addAll(line, algorithmOptions);
        stageCircleObjects(root);
    }

    private void stageCircleObjects(AnchorPane root) {
        circleValueLabelPane = new HBox();
        circleValueLabelPane.setSpacing(1.499999999999999d);
        circleValueLabelPane.setLayoutY(STAGE_HEIGHT - 70);
        circleValueLabelPane.setLayoutX(20);
        AtomicReference<Double> radius = new AtomicReference<>(7.0);
        AtomicReference<Double> xPos = new AtomicReference<>(25.0);
        AtomicReference<Double> yPos = new AtomicReference<>(350.0);

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
            label.setFont(new Font("consolas", 7));
            circleValueLabelPane.getChildren().add(label);
            labelCount--;

            root.getChildren().addAll(circle);

            circleObjects.add(circle);
            xPos.set(xPos.get() + 9.5);
            if (circleObjects.size() == 59) {
                root.getChildren().add(circleValueLabelPane);
                break;
            }
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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
