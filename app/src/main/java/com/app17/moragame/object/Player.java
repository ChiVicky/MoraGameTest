package com.app17.moragame.object;

import java.util.Random;

public class Player {
    public static final int READY = -1;
    public static final int SCISSOR = 0;
    public static final int ROCK = 1;
    public static final int PAPER = 2;
    //PAPER+1==>3 nextInt(0,2) ==>0,1

    protected int mora;
    protected int life;

    public Player() {
        mora = READY;
        life = 3;
    }

    public int getMora() {
        return mora;
    }

    public void setMora(int mora) {
        this.mora = mora;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getRandomMora() {
        return new Random().nextInt(PAPER + 1);
    }
}
