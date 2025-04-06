import java.awt.*;
//je crée une classe TrapSprite, pour gérer les proprietes des pièges
public class TrapSprite extends Sprite {

    private int damage;

    public TrapSprite(double x, double y, Image image, double width, double height, int damage) {
        super(x, y, image, width, height);
        this.damage = damage;
    }

    public int getDamage() {
        return damage; //acceder à la valeur des dégats fait par un piège
    }
}
