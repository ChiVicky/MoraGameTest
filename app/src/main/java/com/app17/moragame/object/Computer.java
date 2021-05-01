package com.app17.moragame.object;

import java.util.Random;

public class Computer extends Player {

    private OnComputerCompletedListener onComputerCompletedListener;

    public Computer() {
        life = 1;
    }

    public void setOnComputerCompletedListener(OnComputerCompletedListener onComputerCompletedListener) {
        this.onComputerCompletedListener = onComputerCompletedListener;
    }

    public void ai() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().toString());
                setMora(getRandomMora());
                onComputerCompletedListener.complete();
            }
        }).start();
    }

    public int getRandomMora() {
        return new Random().nextInt(PAPER + 1);
    }


    public interface OnComputerCompletedListener {
        void complete();
    }
}


