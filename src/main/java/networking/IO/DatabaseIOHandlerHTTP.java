<<<<<<<< HEAD:src/old/networking/DatabaseIOHandlerHTTP.java
<<<<<<<< HEAD:src/old/networking/IO/DatabaseIOHandlerHTTP.java
package old.networking.IO;
========
package old.networking;
>>>>>>>> gamelogic:src/old/networking/DatabaseIOHandlerHTTP.java
========
package core.networking.IO;
>>>>>>>> GUI:src/main/java/networking/IO/DatabaseIOHandlerHTTP.java

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * Handles communication between the database and the program.
 * Compatible with a web server.
 *
 * @author Clement Luo
 */
public class DatabaseIOHandlerHTTP {
    //URL of the web server
    private static final String BASE_URL = "http://127.0.0.1:8000";

    //Http client
    private final HttpClient httpClient = HttpClient.newHttpClient();

    //Constructor
    public DatabaseIOHandlerHTTP() {}

    /**
     * Register an account to the database.
     *
     * @param username
     *            The username of the account.
     * @param password
     *            The password of the account.
     * @return boolean
     *             true if registration is successful,
     *             false otherwise
     */
    public boolean registerAccount(String username, String password){
        //
        return true;
    }

    /**
     * Retrieve the password from an account.
     *
     * @param username
     *            The username of the account.
     * @return String
     *            The password of the account, null if the account is not found
     */
    private String getPassword(String username){
        //
        return null;
    }

    /**
     * Verify password.
     *
     * @param usernameInput
     *            The username of the account entered.
     * @param passwordInput
     *            The username of the account entered.
     * @return boolean
     *             true if the password matches,
     *             false otherwise
     */
    public boolean login(String usernameInput, String passwordInput){
        //
        return false;
    }

    //This method adds an account with username "Bob" and password "password"
    public void sendMessage(String message){
        try {
            String url = BASE_URL + "/createaccount";
            String data = "username=" + URLEncoder.encode("Bob", StandardCharsets.UTF_8) +
                    "&password=" + URLEncoder.encode("password", StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(data))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //This method prints the username and password of Bob's account
    public void getMessage(String message){
        String username = "Bob";
        String url = BASE_URL + "/user/" + username;

        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/" + username))
                    .header("Accept", "text/plain")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + response.statusCode());

            if(response.statusCode() == 404){
                System.out.println("Account not found");
                return;
            }

            //Parse response
            String[] parts = response.body().split("&");
            for (String part : parts){
                String[] keyValue = part.split("=");
                if (keyValue.length == 2) {
                    System.out.println(keyValue[0] + ": " + keyValue[1]);
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
