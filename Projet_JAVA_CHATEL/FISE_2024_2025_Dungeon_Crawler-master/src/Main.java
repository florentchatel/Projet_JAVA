import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    private static JFrame currentGameFrame;


    RenderEngine renderEngine;
    GameEngine gameEngine;
    PhysicEngine physicEngine;
    static Timer renderTimer;
    static Timer gameTimer;
    static Timer physicTimer;
    private static BackgroundMusic musicPlayer;

    public Main(DynamicSprite hero, Playground level) throws Exception{

        if (musicPlayer == null) {
            musicPlayer = new BackgroundMusic();
            musicPlayer.playMusic("C:/Users/E7450/Downloads/FISE_2024_2025_Dungeon_Crawler-master/FISE_2024_2025_Dungeon_Crawler-master/sound/musiquefond.wav");
        } //lance la musique de fond

        currentGameFrame = new JFrame("Java Labs"); //fenêtre de jeu
        currentGameFrame.setSize(970,890); //taille de la fenêtre
        currentGameFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        currentGameFrame.setLocationRelativeTo(null);

        hero = new DynamicSprite(75, 75, ImageIO.read(new File("C:/Users/E7450/Downloads/FISE_2024_2025_Dungeon_Crawler-master/FISE_2024_2025_Dungeon_Crawler-master/img/heroTileSheetLowRes.png")), 48, 50);

        renderEngine = new RenderEngine(currentGameFrame);
        physicEngine = new PhysicEngine();
        gameEngine = new GameEngine(hero,level);

        currentGameFrame.getContentPane().add(gameEngine.getTimerLabel(), BorderLayout.NORTH); //affichage du chronomètre en haut de la fenêtre


        renderTimer = new Timer(50,(time)-> renderEngine.update());
        gameTimer = new Timer(50,(time)-> gameEngine.update());
        physicTimer = new Timer(50,(time)-> physicEngine.update());

        renderTimer.start();
        gameTimer.start();
        physicTimer.start();

        currentGameFrame.getContentPane().add(renderEngine);
        currentGameFrame.setVisible(true);

        //SolidSprite testSprite = new DynamicSprite(100,100,test,0,0);
        renderEngine.addToRenderList(level.getSpriteList());
        renderEngine.addToRenderList(hero);
        physicEngine.addToMovingSpriteList(hero);
        physicEngine.setEnvironment(level.getSolidSpriteList());

        currentGameFrame.addKeyListener(gameEngine);
    }

        public static void main(String[] args) {
            new EcranTitre();
        }

    public static void restartGame() {
        SwingUtilities.invokeLater(() -> {
            try {
                if (currentGameFrame != null) {
                    currentGameFrame.dispose();
                }

                //sinon le timer continuait de tourner derrière même lorsque je perdais et recommencait une partie donc le héro avancait beaucoup moins vite au bout de 2 ou 3 parties
                Main.renderTimer.stop();
                Main.gameTimer.stop();
                Main.physicTimer.stop();

                Playground newLevel = new Playground("C:/Users/E7450/Downloads/FISE_2024_2025_Dungeon_Crawler-master/FISE_2024_2025_Dungeon_Crawler-master/data/level1.txt");
                DynamicSprite newHero = new DynamicSprite(75, 75, ImageIO.read(new File("C:/Users/E7450/Downloads/FISE_2024_2025_Dungeon_Crawler-master/FISE_2024_2025_Dungeon_Crawler-master/img/heroTileSheetLowRes.png")), 48, 50);
                new Main(newHero, newLevel);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }


}
