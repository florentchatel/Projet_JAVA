import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class EcranTitre {
    private JFrame frame;

    public EcranTitre() {
        //je crée une nouvelle fenêtre
        frame = new JFrame();
        frame.setTitle("Dungeon Crawler"); //titre de la fenêtre
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300); //taille de la fenêtre
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); //position de la fenêtre

        //j'écris dans la fenêtre
        JLabel titleLabel = new JLabel("Bienvenue dans Dungeon Crawler", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(20f));
        frame.add(titleLabel, BorderLayout.NORTH); //position du texte

        //je crée un bouton pour lancer le jeu
        JButton startButton = new JButton("Start");
        startButton.setFont(startButton.getFont().deriveFont(20f));
        frame.add(startButton, BorderLayout.CENTER); //position du bouton

        startButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                //on relance une partie avec un nouveau main
                try {
                    Playground level = new Playground("C:/Users/E7450/Downloads/FISE_2024_2025_Dungeon_Crawler-master/FISE_2024_2025_Dungeon_Crawler-master/data/level1.txt");
                    DynamicSprite hero = new DynamicSprite(75, 75, ImageIO.read(new File("C:/Users/E7450/Downloads/FISE_2024_2025_Dungeon_Crawler-master/FISE_2024_2025_Dungeon_Crawler-master/img/heroTileSheetLowRes.png")), 48, 50);
                    new Main(hero, level);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        frame.setVisible(true);
    }
}

