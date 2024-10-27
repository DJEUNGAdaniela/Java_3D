public class Aeroport {
    private String nom;
    private double latitude;
    private double longitude;
    private String codeIATA;

    // Constructeur
    public Aeroport(String nom, double latitude, double longitude, String codeIATA) {
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
        this.codeIATA = codeIATA;
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCodeIATA() {
        return codeIATA;
    }

    // Méthode pour calculer la distance avec un autre aéroport
    public double calculDistance(Aeroport autre) {
        double deltaTheta = Math.toRadians(autre.getLatitude() - this.latitude);
        double deltaPhi = Math.toRadians(autre.getLongitude() - this.longitude);
        double moyenneTheta = Math.toRadians((autre.getLatitude() + this.latitude) / 2);

        // Utilisation de la formule Haversine pour calculer la distance
        return Math.sqrt(Math.pow(deltaTheta, 2) + Math.pow(deltaPhi * Math.cos(moyenneTheta), 2));
    }

    // Surcharge de la méthode toString
    @Override
    public String toString() {
        return "Aeroport{" +
                "nom='" + nom + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", codeIATA='" + codeIATA + '\'' +
                '}';
    }
}
