import java.time.LocalDateTime;

public class Flight {
    private String airLineCode;  // Code de la compagnie aérienne
    private String airLineName;  // Nom de la compagnie aérienne
    private LocalDateTime arrivalTime;  // Heure d'arrivée
    private LocalDateTime departureTime;  // Heure de départ
    private int number;  // Numéro de vol
    private String departureCode; // Code de l'aéroport de départ (IATA)

    // Constructeur
    public Flight(String airLineCode, String airLineName, LocalDateTime arrivalTime, LocalDateTime departureTime, int number, String departureCode) {
        this.airLineCode = airLineCode;
        this.airLineName = airLineName;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.number = number;
        this.departureCode = departureCode;
    }

    // Getters
    public String getAirLineCode() {
        return airLineCode;
    }

    public String getAirLineName() {
        return airLineName;
    }

    public LocalDateTime getArrival() {
        return arrivalTime;
    }

    public LocalDateTime getDeparture() {
        return departureTime;
    }

    public int getNumber() {
        return number;
    }

    public String getDepartureCode() {
        return departureCode;
    }

    // Surcharge de la méthode toString
    @Override
    public String toString() {
        return "Flight{" +
                "airLineCode='" + airLineCode + '\'' +
                ", airLineName='" + airLineName + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                ", number=" + number +
                ", departureCode='" + departureCode + '\'' +
                '}';
    }
}
