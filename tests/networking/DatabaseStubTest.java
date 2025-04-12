package networking;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        @Test
        void testSaveAndLoadPreservesData() {

        DatabaseStub db = new DatabaseStub();
        db.insertAccountData("player1", "password123", "Player main", "2005-05-05");
        db.saveDBState();

        DatabaseStub db2 = new DatabaseStub();
        db2.populateDB();

        String[] loaded = db2.getAccountData("player1");

        assertNotNull(loaded);
        assertEquals("player1", loaded[0]);
        assertEquals("password123", loaded[1]);
        assertEquals("Player main", loaded[2]);
        assertEquals("2005-05-05", loaded[3]);
        }

    @Test
    void testInsertDuplicateUsername() {
        DatabaseStub db = new DatabaseStub();

        // First insertion should succeed
        boolean first = db.insertAccountData("player1", "pass123", "Player One", "2000-01-01");

        // Second insertion with the same username should be rejected
        boolean second = db.insertAccountData("player1", "pass456", "Player Two", "2001-01-01");

        assertTrue(first, "The first insertion should succeed.");
        assertFalse(second, "The second insertion with a duplicate username should be rejected.");

        // Confirm only one user with username 'player1' exists
        List<String[]> duplicates = db.getAllAccountsByUsername("player1");
        assertEquals(1, duplicates.size(), "Only one account with username 'player1' should exist.");
        assertEquals("pass123", duplicates.get(0)[1], "The password should match the first inserted record.");
    }

    @Test
    void testInsertEmptyUsername() {
        DatabaseStub db = new DatabaseStub();

        boolean result = db.insertAccountData("", "pass123", "NoName", "1990-01-01");

        assertFalse(result, "Insertion should fail when username is empty.");
    }

}
