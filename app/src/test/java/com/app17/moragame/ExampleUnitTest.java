package com.app17.moragame;

import com.app17.moragame.object.Computer;
import com.app17.moragame.object.Player;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Computer computer = new Computer();

        for (int i = 0; i < 10; i++) {
            System.out.println("mora:" + computer.getRandomMora());
        }

        assertEquals(4, 2 + 2);
    }
}