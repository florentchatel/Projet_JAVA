import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class RenderEngine extends JPanel implements Engine{
    private ArrayList<Displayable> renderList;

    public RenderEngine(JFrame jFrame) {
        renderList = new ArrayList<>();
    }

    public void addToRenderList(Displayable displayable){
        if (!renderList.contains(displayable)){
            renderList.add(displayable);
        }
    }

    public void addToRenderList(ArrayList<Displayable> displayable){
        if (!renderList.contains(displayable)){
            renderList.addAll(displayable);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Displayable renderObject:renderList) {
            renderObject.draw(g);

            if (renderObject instanceof DynamicSprite) {
                drawHealthBar(g, (DynamicSprite) renderObject);
            }
        }
    }

    //barre de vie
    private void drawHealthBar(Graphics g, DynamicSprite sprite) {
        int barWidth = 50;  //largeur de la barre de vie
        int barHeight = 5;  //hauteur de la barre de vie
        double x = sprite.getX() + sprite.getWidth() / 2 - barWidth / 2; //centrer sur le perso
        double y = sprite.getY() - 10;  //position au dessus de la tete du perso
        double healthPercentage = (double) sprite.getHealth() / sprite.getMaxHealth(); //pourcentage de vie

        //design de la barre
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y, barWidth, barHeight);//barre en rouge
        g.setColor(Color.GREEN);
        g.fillRect((int) x, (int) y, (int) (barWidth * healthPercentage), barHeight);//barre en vert par dessus de taille variant selon la vie du héro
        g.setColor(Color.BLACK);
        g.drawRect((int) x, (int) y, barWidth, barHeight); //contour
    }

    @Override
    public void update(){
        this.repaint();
    }
}
