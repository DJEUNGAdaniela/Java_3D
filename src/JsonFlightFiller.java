import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JsonFlightFiller {
    private ArrayList<Flight> list = new ArrayList<>();

    public JsonFlightFiller(String jsonString, World w) {
        try {
            InputStream is = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            JsonReader rdr = Json.createReader(is);
            JsonObject obj = rdr.readObject();
            JsonArray results = obj.getJsonArray("data");

            // Définir le format de date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                try {
                    // Extraction des informations de la compagnie aérienne et du vol
                    JsonObject airline = result.getJsonObject("airline");
                    String airLineCode = airline.getString("iata", "Inconnu");
                    String airLineName = airline.getString("name", "Inconnu");
                    String departureCode = result.getJsonObject("departure").getString("iata");

                    // Extraction de l'heure de départ et d'arrivée depuis les sous-objets
                    JsonObject departure = result.getJsonObject("departure");
                    JsonObject arrival = result.getJsonObject("arrival");

                    LocalDateTime departureTime = parseDate(departure, "scheduled", formatter);
                    LocalDateTime arrivalTime = parseDate(arrival, "scheduled", formatter);

                    // Récupération du numéro de vol
                    JsonObject flightInfo = result.getJsonObject("flight");
                    int number = Integer.parseInt(flightInfo.getString("number", "0"));

                    // Création d'un nouvel objet Flight
                    if (departureTime != null && arrivalTime != null) {
                        Flight flight = new Flight(airLineCode, airLineName, arrivalTime, departureTime, number, departureCode);
                        list.add(flight); // Ajout à la liste
                    } else {
                        System.out.println("Vol ignoré : heure de départ ou d'arrivée manquante.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LocalDateTime parseDate(JsonObject obj, String key, DateTimeFormatter formatter) {
        if (obj.containsKey(key)) {
            String dateStr = obj.getString(key);
            // Retirer le fuseau horaire (+00:00) pour le formatage
            if (dateStr.contains("+")) {
                dateStr = dateStr.substring(0, dateStr.indexOf("+"));
            }
            return LocalDateTime.parse(dateStr, formatter);
        }
        return null;
    }

    // Méthode pour obtenir la liste des vols
    public ArrayList<Flight> getList() {
        return list;
    }
}
