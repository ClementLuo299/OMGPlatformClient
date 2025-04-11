package gamelogic;

import java.io.*;
import java.util.*;
import src.gamelogic.*;
import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of Whist Games
 *
 * @authors Dylan Shiels, Scott Brown
 * @date April 6, 2025
 */

class WhistTests {

    private PlayerMock player1;
    private PlayerMock player2;
    private WhistGame game;

    @BeforeEach
    void setUp() {
        player1 = new PlayerMock(true);
        player2 = new PlayerMock(true);
        List<Player> playerList = {player1, player2};
        game = new WhistGame(GameType.WHIST, playerList, 8);
    }

    @Test
    void testTheSetUp() {
        assertEquals(game.getRound(), 0);
        assertEquals(game.getTrickWinner(), null);
    }


