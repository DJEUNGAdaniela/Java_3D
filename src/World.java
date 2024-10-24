import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class World {
    private List<Aeroport> listeAeroports;

    // Constructeur qui prend en paramètre un fichier .csv
    public World(String fileName) {
        listeAeroports = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();  // Ignorer la première ligne (en-tête)
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("\"", "");  // Retirer les guillemets superflus
                String[] champs = line.split(",");

                // Vérifie si l'aéroport est de type "large_airport" et que les données essentielles sont présentes
                if (champs.length > 9 && champs[1].equals("large_airport") && !champs[9].isEmpty()) {
                    String nom = champs[2].isEmpty() ? "Inconnu" : champs[2];  // Nom de l'aéroport
                    String codeIATA = champs[9];  // Le code IATA

                    // Vérification et conversion de la latitude et de la longitude
                    try {
                        double latitude = Double.parseDouble(champs[champs.length - 2]);
                        double longitude = Double.parseDouble(champs[champs.length - 1]);

                        // Création de l'objet Aeroport et ajout à la liste
                        Aeroport aeroport = new Aeroport(nom, latitude, longitude, codeIATA);
                        listeAeroports.add(aeroport);

                    } catch (NumberFormatException e) {
                        System.out.println("Erreur de format des coordonnées GPS pour l'aéroport : " + nom);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la lecture du fichier.");
        }
    }

    public Aeroport findNearest(double longitude, double latitude) {
        Aeroport aeroportLePlusProche = null;
        double distanceMin = Double.MAX_VALUE;

        for (Aeroport aeroport : listeAeroports) {
            double distance = Math.sqrt(Math.pow(Math.toRadians(aeroport.getLatitude() - latitude), 2) +
                    Math.pow(Math.toRadians(aeroport.getLongitude() - longitude) * Math.cos(Math.toRadians((aeroport.getLatitude() + latitude) / 2)), 2));

            if (distance < distanceMin) {
                distanceMin = distance;
                aeroportLePlusProche = aeroport;
            }
        }

        return aeroportLePlusProche;
    }

    // Trouver un aéroport par son code IATA
    public Aeroport findByCode(String codeIATA) {
        for (Aeroport aeroport : listeAeroports) {
            if (aeroport.getCodeIATA().equals(codeIATA)) {
                return aeroport;
            }
        }
        return null;
    }


    // Méthode pour retourner la liste des aéroports
    public List<Aeroport> getListeAeroports() {
        return listeAeroports;
    }

}
