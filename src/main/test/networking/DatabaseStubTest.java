package com.network;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseStubTest {

    // Tests the account data for a username that does not exist
        @Test
        void testAccountDataForNonexistentUser() {
            DatabaseStub db = new DatabaseStub();
            db.insertAccountData("player1", "password123");
            String[] result = db.getAccountData("doesNotExist");

            assertNull(result);
        }
    // Tests whether checkAccountExists correctly identifies an existing account.
        @Test
        void testAccountExist() {
            DatabaseStub db = new DatabaseStub();
            db.insertAccountData("player1", "password123");

            assertTrue(db.checkAccountExists("player1"));
            assertFalse(db.checkAccountExists("object"));
        }

    //Tests database reaction to multiple accounts being created at once
    void testMultipleAccountInsertionCount() {
        DatabaseStub db = new DatabaseStub();

        db.insertAccountData("player1", "password22", "player main", "2005-05-05");
        db.insertAccountData("player2", "password22", "player secondary", "2007-07-07");
        db.insertAccountData("player3", "password23", "player last", "2006-06-06");

        List<String[]> all = db.getAllAccounts();

        assertEquals(3, all.size(), "There should be exactly 3 accounts in the database.");
    }
    // Tests the validity of information after account is created
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
    // Tests that account data is preserved properly after saving and loading data
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

        // Tests the outcome when theres a duplicate username inserted
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

     //Tests database reaction to accounts with no username
    @Test
    void testInsertEmptyUsername() {
        DatabaseStub db = new DatabaseStub();

        boolean result = db.insertAccountData("", "pass123", "NoName", "1990-01-01");

        assertFalse(result, "Insertion should fail when username is empty.");
    }

    //Tests actions of deleting account, verifying it's removed from the database
    @Test
    void testDeleteAccount() {
        DatabaseStub db = new DatabaseStub();

        db.insertAccountData("player1", "pass123", "Player One", "2000-01-01");

        // Ensure the account exists before deletion
        assertNotNull(db.getAccountData("player1"));

        // Perform deletion
        boolean deleted = db.deleteAccount("player1");

        // Ensure deletion was successful
        assertTrue(deleted, "Account should be successfully deleted.");

        // Ensure account no longer exists
        assertNull(db.getAccountData("player1"), "Deleted account should return null on retrieval.");
    }

    //Tests reaction after attempt to delete an account that does not exist
    @Test
    void testDeletenonexistentAccount() {
        DatabaseStub db = new DatabaseStub();

        boolean result = db.deleteAccount("p1");

        assertFalse(result, "Error, this account doesnt exist.");
        }

    //Testing checkAccountExists function, whether it returns falls for a null username

    @Test
    void testCheckAccountExistsWithNull() {
        DatabaseStub db = new DatabaseStub();

        assertFalse(db.checkAccountExists(null), "checkAccountExists returns false.");
    }

    //Tests reaction from database to special characters in the fields
    @Test
    void testInsertWithSpecialCharacters() {
        DatabaseStub db = new DatabaseStub();

        String username = "_player_Xx34#";
        String password = "p@ass0word;!";
        String fullName = "_player_Xx34# m4in";
        String dob = "2005-05-05";

        boolean inserted = db.insertAccountData("_player_Xx34#", "p@ass0word;!", "_player_Xx34# m4in", "2005-05-05");
        assertTrue(inserted, "Should allow insertion with special characters.");

        String[] data = db.getAccountData(username);
        assertNotNull(data);
        assertEquals("_player_Xx34#", data[0]);
        assertEquals("p@ass0word;!", data[1]);
        assertEquals("_player_Xx34# m4in", data[2]);
        assertEquals("2005-05-05", data[3]);
    }


}
