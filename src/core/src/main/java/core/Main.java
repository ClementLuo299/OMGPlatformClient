package core;

import core.networking.DatabaseStub;

class Main{
    public static void main(String[] args){
        //Test database
        DatabaseStub db = new DatabaseStub();

        db.populateDB();
    }
}