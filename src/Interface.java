import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.PerspectiveCamera;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class Interface extends Application {

    private World w; // Instance de World pour gérer les aéroports
    private Earth earth; // Instance d'Earth pour gérer les sphères colorées

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Modèle 3D de la Terre avec Interaction");

        // Création de l'objet World contenant les données des aéroports
        w = new World("C:\\Users\\djeun\\IdeaProjects\\DataFlight\\src\\airport-codes_no_comma.csv");

        // Création de la scène principale (la terre)
        earth = new Earth();  // Objet Earth contenant la sphère
        Pane root = new Pane(earth);  // Ajout de la sphère au root
        Scene scene = new Scene(root, 800, 600, true);

        // Ajout d'une caméra en 3D
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-1000);  // Recul initial de la caméra pour afficher la sphère
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        camera.setFieldOfView(35);
        scene.setCamera(camera);  // Assignation de la caméra à la scène

        // Ajout de la gestion des événements pour le zoom
        addZoomHandler(scene, camera);

        // Ajout de l'EventHandler pour le clic droit
        addRightClickHandler(scene, earth);

        // Appel de la méthode pour interroger l'API et afficher les boules jaunes pour les vols
        String apiUrl = "https://api.aviationstack.com/v1/flights?access_key=b4f5c2a40ec4189a73c224981274d1c9&arr_iata=CDG"; // Remplacer par l'URL de l'API
        queryAPIAndDisplayFlights(apiUrl, w);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Gestionnaire d'événements pour le zoom
    private void addZoomHandler(Scene scene, PerspectiveCamera camera) {
        Translate cameraTranslate = new Translate();
        camera.getTransforms().add(cameraTranslate);

        scene.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                System.out.println("Clicked on : (" + event.getSceneX() + ", " + event.getSceneY() + ")");
            }

            // Zoom sur l'axe Z en fonction du déplacement de la souris
            if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                double zoomFactor = event.getSceneY() > 300 ? 10 : -10;  // Ajuste le zoom selon la position Y
                cameraTranslate.setZ(cameraTranslate.getZ() + zoomFactor);
            }
        });
    }

    // Gestionnaire d'événements pour le clic droit
    private void addRightClickHandler(Scene scene, Earth earth) {
        scene.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                // Obtenir le résultat de la détection d'intersection
                PickResult pickResult = event.getPickResult();

                // Vérifiez que pickResult et les coordonnées de texture ne sont pas null
                if (pickResult != null && pickResult.getIntersectedNode() != null) {
                    double x = pickResult.getIntersectedPoint().getX();
                    double y = pickResult.getIntersectedPoint().getY();

                    // Conversion en latitude et longitude
                    double latitude = 180 * (0.5 - y);
                    double longitude = 360 * (x - 0.5);

                    // Recherche de l'aéroport le plus proche via World
                    Aeroport nearestAirport = w.findNearestAirport(longitude, latitude);

                    // Affiche l'aéroport le plus proche dans la console
                    System.out.println("Aéroport le plus proche : " + nearestAirport);
                    earth.displayRedSphere(nearestAirport);
                } else {
                    System.out.println("Clic en dehors de l'objet ou intersection non détectée.");
                }
            }
        });
    }

    // Méthode pour interroger l'API et afficher les boules jaunes
    public void queryAPIAndDisplayFlights(String apiUrl, World world) {
        try {
            // Initialiser HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Créer la requête
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            // Envoyer la requête et obtenir la réponse
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Vérifier si la requête est réussie
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();

                // Créer une instance de JsonFlightFiller pour analyser le JSON
                JsonFlightFiller jsonFlightFiller = new JsonFlightFiller(jsonResponse, world);

                // Récupérer la liste des vols
                ArrayList<Flight> flights = jsonFlightFiller.getList();

                // Afficher chaque aéroport de départ en utilisant displayYellowBall
                for (Flight flight : flights) {
                    Aeroport departureAirport = world.findByCode(flight.getDepartureCode());
                    if (departureAirport != null) {
                        earth.displayYellowSphere(departureAirport); // Utilise la méthode displayYellowSphere de Earth
                    }
                }
            } else {
                System.out.println("Erreur lors de la requête HTTP. Code : " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
