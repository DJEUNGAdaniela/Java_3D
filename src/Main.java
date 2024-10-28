import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        // Création d'un objet Aeroport
        Aeroport aeroport1 = new Aeroport("Charles de Gaulle", 49.0097, 2.5479, "CDG");

        // Test de l'affichage de l'objet avec toString
        System.out.println(aeroport1);

        // Crée une instance de World en pointant vers le fichier csv
        World world = new World("C:\\Users\\djeun\\IdeaProjects\\DataFlight\\src/airport-codes_no_comma.csv");

        try {
            // Lire le fichier de test contenant la réponse JSON
            StringBuilder jsonBuilder = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\djeun\\IdeaProjects\\DataFlight\\src/JsonOrly.txt"));
            String line;

            // Lire chaque ligne et l'ajouter au StringBuilder
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }

            // Convertir le StringBuilder en chaîne de caractères
            String test = jsonBuilder.toString();

            // Créer une instance de JsonFlightFiller avec le JSON lu
            JsonFlightFiller jsonFlightFiller = new JsonFlightFiller(test, world);

            // Afficher la liste des vols récupérés
            for (Flight flight : jsonFlightFiller.getList()) {
                System.out.println(flight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Affiche le nombre d'aéroports trouvés
        System.out.println("Nombre d'aéroports trouvés : " + world.getListeAeroports().size());

        // Affiche les aéroports
        for (Aeroport aeroport2 : world.getListeAeroports()) {
            System.out.println(aeroport2);
        }

        // Trouver l'aéroport le plus proche de Paris
        Aeroport aeroportProcheParis = world.findNearestAirport(2.316, 48.866);
        System.out.println("Aéroport le plus proche de Paris : " + aeroportProcheParis);

        // Trouver l'aéroport par son code IATA
        Aeroport aeroportCDG = world.findByCode("CDG");
        if (aeroportCDG != null) {
            System.out.println("Aéroport avec code CDG : " + aeroportCDG);
        } else {
            System.out.println("Aucun aéroport trouvé avec le code CDG.");
        }
    }
}