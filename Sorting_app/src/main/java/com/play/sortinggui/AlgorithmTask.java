package com.play.sortinggui;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.function.Supplier;

public class AlgorithmTask extends Service<Void> {
    private Supplier<?> supplier;
    private String algorithm;

    public AlgorithmTask(String algorithmName, Supplier<?> supplier) {
        this.supplier = supplier;
        algorithm = algorithmName;
    }

    @Override
    protected Task<Void> createTask() {
        if (algorithm.equals("BubbleSort")) {
            Platform.runLater(() -> supplier.get());
        }

        return null;
    }
}
