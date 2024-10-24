import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.input.PickResult;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Interface extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Modèle 3D de la Terre avec Interaction");

        // Création de la scène principale
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

        primaryStage.setScene(scene);
        primaryStage.show();
    }



    // Gestionnaire d'événements pour le zoom
    private void addZoomHandler(Scene scene, PerspectiveCamera camera) {
        // Créer une transformation pour zoomer
        Translate cameraTranslate = new Translate();
        camera.getTransforms().add(cameraTranslate);

        scene.addEventHandler(MouseEvent.ANY, event -> {
            // Capture le clic de la souris
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                System.out.println("Clicked on : (" + event.getSceneX() + ", " + event.getSceneY() + ")");
            }

            // Capture le déplacement de la souris avec le bouton enfoncé pour zoomer
            if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                // Zoomer sur l'axe Z en fonction de la direction du déplacement de la souris
                double zoomFactor = event.getSceneY() > 300 ? 10 : -10;  // Ajuster selon la position Y
                cameraTranslate.setZ(cameraTranslate.getZ() + zoomFactor);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
