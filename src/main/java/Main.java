import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;
import org.json.JSONArray;

public class Main {

    private static final String api_key = "3a73cbe2c8a36c50dc1117d77d3d487b";
    private static final String base_url1 = "https://api.openweathermap.org/data/2.5/weather?lat=";
    private static final String base_url2 = "&lon=";
    private static final String base_url3 = "&appid=";
    private static final String base_url4 = "&units=metric";

    private static final String location_url1 = "http://api.openweathermap.org/geo/1.0/direct?q=";
    private static final String location_url2 = "&appid=";


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Ask for city
        System.out.println("Give a City:");
        String city = sc.nextLine();

        // Get location data
        JSONObject cityData = getCityData(city);
        assert cityData != null;
        double lat = cityData.getDouble("lat");
        double lon = cityData.getDouble("lon");

        // Get weather data
        JSONObject weatherData = getWeatherData(lat, lon);
        assert weatherData != null;
        System.out.println("It's currently " + (int) weatherData.getJSONObject("main").getDouble("temp") + "°C" + " in " + city);
        }

    private static JSONObject getWeatherData(double lat, double lon) {
        String fullUrl = base_url1 + lat + base_url2 + lon + base_url3 + api_key + base_url4;

        try {
            // Fetch the API
            HttpURLConnection conn = fetchAPI(fullUrl);

            // Read all data
            StringBuilder sb = readDataFromApi(conn);

            assert sb != null;
            return new JSONObject(sb.toString());
        }
        catch (Exception e) {
            System.out.println("Error.");
        }
        return null;
    }

    private static JSONObject getCityData(String city) {

        String fullUrl = location_url1 + city + location_url2 + api_key;

        try {
            // Fetch the API
            HttpURLConnection conn = fetchAPI(fullUrl);

            // Read all data
            StringBuilder sb = readDataFromApi(conn);

            // Return City data
            assert sb != null;
            JSONArray arr = new JSONArray(sb.toString());

            return new JSONObject(arr.getJSONObject(0).toString());
        }
        catch (Exception e) {
            System.out.println("Error.");
        }
        return null;
    }

    private static HttpURLConnection fetchAPI (String fullUrl) {
        try {
            // Connect to the API
            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            return conn;
        }
        catch (Exception e) {
            System.out.println("There was an error fetching.");
        }
        return null;
    }

    private static StringBuilder readDataFromApi(HttpURLConnection conn) {
        try {
            // Read the data
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();

            // Pump API data in String
            String data;
            while ((data = br.readLine()) != null) {
                sb.append(data);
            }
            return sb;
        }
        catch (Exception e) {
            System.out.println("There was an error reading the API.");
        }
        return null;
    }
}