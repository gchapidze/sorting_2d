package com.play.sortinggui;

public interface Random {
    static int random(int upperBound) {
        java.util.Random random = new java.util.Random();
        return random.nextInt(upperBound);
    }
}
