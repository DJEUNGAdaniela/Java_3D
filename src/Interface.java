import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.PerspectiveCamera;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Interface extends Application {

    private World w; // Instance de World pour gérer les aéroports

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Modèle 3D de la Terre avec Interaction");

        // Création de l'objet World contenant les données des aéroports
        w = new World("C:\\Users\\djeun\\IdeaProjects\\DataFlight\\src\\airport-codes_no_comma.csv");

        // Création de la scène principale (la terre)
        Earth earth = new Earth();  // Objet Earth contenant la sphère
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
        addRightClickHandler(scene);

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
    private void addRightClickHandler(Scene scene) {
        scene.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                // Obtenir le résultat de la détection d'intersection
                PickResult pickResult = event.getPickResult();

                // Vérifiez que pickResult et les coordonnées de texture ne sont pas null
                if (pickResult != null && pickResult.getIntersectedNode() != null) {
                        //double x = pickResult.getIntersectedTexCoord().getX();
                        //double y = pickResult.getIntersectedTexCoord().getY();

                        double x = pickResult.getIntersectedPoint().getX();
                        double y = pickResult.getIntersectedPoint().getY();

                        // Conversion en latitude et longitude
                        double latitude = 180 * (0.5 - y);
                        double longitude = 360 * (x - 0.5);

                        // Recherche de l'aéroport le plus proche via World
                        Aeroport nearestAirport = w.findNearestAirport(longitude, latitude);

                        // Affiche l'aéroport le plus proche dans la console
                        System.out.println("Aéroport le plus proche : " + nearestAirport);}
                    else {
                        System.out.println("Clic en dehors de l'objet ou intersection non détectée.");
                    }
                }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
