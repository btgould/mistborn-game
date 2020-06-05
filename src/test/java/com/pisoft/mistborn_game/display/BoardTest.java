package com.pisoft.mistborn_game.display;

import org.junit.Test;

public class BoardTest {
    private Board board = new Board();

    @Test
    public void testOne() {
        assert(board.getOne() == 1);
    }
}