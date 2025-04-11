package networking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseStubTest {

    class DatabaseStubTest {

        @Test
        void testAccountDataForNonexistentUser() {
            DatabaseStub db = new DatabaseStub();
            db.insertAccountData("player1", "password123");
            String[] result = db.getAccountData("doesNotExist");

            assertNull(result);
        }

        @Test
        void testAccountExist() {
            DatabaseStub db = new DatabaseStub();
            db.insertAccountData("player1", "password123");

            assertTrue(db.checkAccountExists("player1"));
            assertFalse(db.checkAccountExists("object"));
        }

        @Test
        void testInsertWithFullNameAndDOB() {
            DatabaseStub db = new DatabaseStub();
            db.insertAccountData("player2", "pass123", "main player", "2005-05-05");

            String[] data = db.getAccountData("player2");

            assertNotNull(data);
            assertEquals("player2", data[0]);
            assertEquals("pass123", data[1]);
            assertEquals("main player", data[2]);
            assertEquals("2005-05-05", data[3]);
        }
}
