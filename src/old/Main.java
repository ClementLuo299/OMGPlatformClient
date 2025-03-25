package old;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        //Working code to create account
        /*
        final String URL = "http://127.0.0.1:8000/createaccount";
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            String data = "username=" + URLEncoder.encode("Bob", StandardCharsets.UTF_8) +
                    "&password=" + URLEncoder.encode("password", StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(data))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
        } catch (Exception e){
            e.printStackTrace();
        }
        */

        //Working code to get password from account
        /*
        final String URL = "http://127.0.0.1:8000/user";
        try{
            String username = "Bob";
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/" + username))
                    .header("Accept", "text/plain")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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
            e.printStackTrace();
        }
         */
    }
}