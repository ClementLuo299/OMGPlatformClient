package statistics;

import statistics.*;
import gamelogic.Player;
import com.entities.accounts.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevellingTest {

    private Player winner1;
    private Player winner2;
    private Player winner3;
    private Player player1;
    private Player player2;
    private Player player3;
    private List<Player> players1;
    private List<Player> players2;
    private ExperienceHandler eh1;
    private ExperienceHandler eh2;
    private ExperienceHandler eh3;


    @BeforeEach
    void setUp() {
        UserAccount ua1 = new UserAccount("Kyleigh", "abc123");
        UserAccount ua2 = new UserAccount("Brayden", "password");
        UserAccount ua3 = new UserAccount("Emma", "1July2017");
        UserAccount ua4 = new UserAccount("Rylan", "<3MyWife");
        UserAccount ua5 = new UserAccount("Ohliviah", "Cookie100!");
        UserAccount ua6 = new UserAccount("Ahmed", "X24uhf;86hj_#dwu3");
        UserAccount ua7 = new UserAccount("Khaleesi", "password123");
        UserAccount ua8 = new UserAccount("Zara", "i_i_i_ufinhjf8484398");

        winner2 = new Player(ua1);
        player2 = new Player(ua2);
        winner1 = new Player(ua3);
        player1 = new Player(ua4);
        winner3 = new Player(ua6);
        player3 = new Player(ua5);


        winner1.getAccount().setExpInLevel(0);
        winner1.setPlays(20);
        player1.getAccount().setExpInLevel(9);


        winner2.getAccount().setExpInLevel(5);
        winner2.setPlays(1);

        players1 = Arrays.asList(winner1, player1);
        winner1.setPlays(20);
        player1.setPlays(20);
        eh1 = new ExperienceHandler(players1, winner1, 20);

        players2 = Arrays.asList(winner2, player2);
        eh2 = new ExperienceHandler(players2, winner2, 20);

        winner3.setPlays(100);
        player3.setPlays(100);
        eh3 = new ExperienceHandler(Arrays.asList(winner3, player3), winner3, 10);

    }

    @Test
    void testAwardWinNormalGame() {
        eh1.awardWin();
        assertEquals(1, winner1.getAccount().getExpInLevel());
    }

    @Test
    void testAwardParticipation() {
       eh2.awardParticipation();
       assertEquals(6, winner2.getAccount().getExpInLevel());
       assertEquals(1, player2.getAccount().getExpInLevel());

    }

    @Test
    void testAwardLongGame() {
        eh3.awardLongGame();
        assertEquals(1, winner3.getAccount().getExpInLevel());
        assertEquals(1, player3.getAccount().getExpInLevel());
    }

    @Test
    void testAwardWinQuickGame() {
        eh2.awardWin();
        assertEquals(7, winner2.getAccount().getExpInLevel());
    }

    @Test
    void testIncreaseLevel() {
        eh1.awardParticipation();
        assertEquals(1, player1.getAccount().getLevel());
    }

}
