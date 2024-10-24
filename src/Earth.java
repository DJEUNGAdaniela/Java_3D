import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class Earth extends Group {
    private Sphere sph;
    private Rotate ry;  // Rotation sur l'axe Y

    public Sphere getSph() {
        return sph;
    }

    // Constructeur
    public Earth() {
        // Création de la sphère de 300 pixels de rayon
        sph = new Sphere(300);

        // Création du matériau PhongMaterial
        PhongMaterial earthMaterial = new PhongMaterial();

        // Chargement de la texture de la Terre
        Image earthTexture = new Image("file:/C:/Users/djeun/IdeaProjects/DataFlight/src/earth_lights_4800.png");
        earthMaterial.setDiffuseMap(earthTexture);

        // Application du matériau à la sphère
        sph.setMaterial(earthMaterial);

        // Création de la rotation sur l'axe Y
        ry = new Rotate(0, Rotate.Y_AXIS);
        sph.getTransforms().add(ry);

        // Ajout de la sphère au groupe
        this.getChildren().add(sph);

        // Animation pour faire tourner la Terre
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long time) {
                // Calcul de l'angle : un tour complet (360 degrés) en 15 secondes
                double angle = (time / 1_000_000_000.0) * (360 / 15);  // Conversion en secondes, puis calcul de l'angle
                ry.setAngle(angle % 360);  // Faire tourner en boucle
            }
        };
        animationTimer.start();

    }
}
