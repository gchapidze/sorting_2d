package com.play.sortinggui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class Sort_Gui extends Application {
    Double[][] coordinates;
    private List<Point> tmpPoints;
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
                bubbleSort(root, tmpPoints);
            }
        });

        root.getChildren().addAll(line, algorithmOptions);
        showPoints(root);
    }

    private void showPoints(AnchorPane root) {
        AtomicReference<Double> radius = new AtomicReference<>(7.0);
        AtomicReference<Double> x = new AtomicReference<>(25.0);
        AtomicReference<Double> y = new AtomicReference<>(350.0);

        tmpPoints = new ArrayList<>();
        while (true) {
            Point tmpPoint = new Point();
            tmpPoint.setRadius(radius.get());
            tmpPoint.setColor(randomColor());
            tmpPoint.setLayoutX(x.get());
            tmpPoint.setLayoutY(y.get());
            root.getChildren().add(tmpPoint);

            tmpPoints.add(tmpPoint);
            x.set(x.get() + 9.5);
            if (tmpPoints.size() == 59) {
                break;
            }
            radius.set(radius.get() - 0.1);
        }
    }

    private void bubbleSort(AnchorPane root, List<Point> point) {
        List<Point> points = new ArrayList<>(point.size());
        points.addAll(point);
        int size = points.size();

        // loop to access each points element
        for (int i = 0; i < size - 1; i++) {

            // loop to compare points elements
            for (int j = 0; j < size - i - 1; j++) {

                // compare two adjacent elements
                // change > to < to sort in descending order
                if (points.get(j).getLayoutX() < points.get(j + 1).getLayoutX()) {

                    // swapping occurs if elements
                    // are not in the intended order
                    var bigger = points.get(j);
                    Point smaller = points.get(j + 1);
                    double x = smaller.getLayoutX();
                    double y = smaller.getLayoutY();
                    smaller.setLayoutX(bigger.getLayoutX());
                    smaller.setLayoutY(bigger.getLayoutY());
                    bigger.setLayoutY(y);
                    bigger.setLayoutX(x);
                    points.set(j, smaller);
                    points.set(j + 1, bigger);
                }
            }
            points.forEach((p -> {
                root.getChildren().remove(p);
            }));
            points.forEach((p -> {
                root.getChildren().add(p);
            }));
        }
        parseCoordinates();
        int i = 0;
        for (Point p : points) {
            p.setLayoutX(coordinates[i][0]);
            p.setLayoutY(coordinates[i][1]);
            i++;
        }
    }

    private Color randomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r, g, b);
    }
    private void parseCoordinates() {
        coordinates = new Double[tmpPoints.size()][2];
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/play/sortinggui/coordinates.txt"));
            String fileIn;
            int i = 0;
            while ((fileIn = br.readLine()) != null) {
                String[] coordinate = fileIn.split(",");
                coordinates[i][0] = Double.parseDouble(coordinate[0]);
                coordinates[i][1] = Double.parseDouble(coordinate[1]);
                i++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
