package main.java.gamelogic;

public class Player {

    private String username;
    private String password;
    private double gamesPlayed;
    private double wins;
    private double draws;
    private double losses;
    private boolean online;

    public Player(String username, String password){
        this.username = username;
        this.password = password;
        this.gamesPlayed = 0.0;
        this.wins = 0.0;
        this.draws = 0.0;
        this.losses = 0.0;
        this.online = true;

    }

    public void changePasswordTo(String newPassword){
        this.password = newPassword;
    }
    
    public void changeUsernameTo(String newUsername){
        this.username = newUsername;
    }

    public void won(){
        this.wins+=1;
        this.gamesPlayed+=1;
    }

    public void lost(){
        this.losses+=1;
        this.gamesPlayed+=1;
    }

    public void drew(){
        this.draws+=1;
        this.gamesPlayed+=1;
    }

    public void loggedIn(){
        this.online = true;
    }

    public void loggedOff(){
        this.online = false;
    }

    public boolean isOnline(){
        return this.online;
    }

    public double getWinRatio(){
        // return wins over games played as a percent
        return 100*this.wins/this.gamesPlayed;
    }


    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public double getWins(){
        return this.wins;
    }

    public double getLosses(){
        return this.losses;
    }

    public double getDraws(){
        return this.draws;
    }

    public double getGamesPlayed(){
        return this.gamesPlayed;
    }



}
