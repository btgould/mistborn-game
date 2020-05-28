package display;

import org.junit.Test;

public class BoardTest {
    private Board board = new Board();

    @Test
    public void boardHasLevel() {
        assert(board.getOne() == 1);
    }
}