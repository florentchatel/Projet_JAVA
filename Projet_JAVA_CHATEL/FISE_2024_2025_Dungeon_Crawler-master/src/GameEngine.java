import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;



public class GameEngine implements Engine, KeyListener {
    private DynamicSprite hero;
    private Playground playground;
    private List<TrapSprite> traps;
    Point exitPosition = Playground.getExitPosition();
    private int seconds = 45; //compteur de secondes
    private Timer timer;
    private JLabel timerLabel;
    private JFrame endGameFrame;
    private boolean gameEnded = false; //bouléen pour connaitre l'état du jeu



    public GameEngine(DynamicSprite hero, Playground playground) {

        this.hero = hero;
        this.playground = playground;
        this.exitPosition = Playground.getExitPosition();
        //on récupère la liste des sprites
        ArrayList<Displayable> spriteList = playground.getSpriteList();

        //on crée une nouvelle liste pour les pièges
        List<TrapSprite> trapList = new ArrayList<>();

        //on parcourt la liste des sprites afin de récupérer les pièges et on les met dans la liste crée au dessus
        for (Displayable sprite : spriteList) {
            if (sprite instanceof TrapSprite) { //vérifie si c'est un TrapSprite
                trapList.add((TrapSprite) sprite); //rajoute a la liste
            }
        }

        //on affecte la liste des piege à traps
        this.traps = trapList;

        //création du timer
        timerLabel = new JLabel("Temps : 45s", SwingConstants.CENTER); //afficher le timer à l'écran
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20)); //écriture du timer
        timer = new Timer(1000, e -> updateTimer()); //actualise la valeur du timer toutes les secondes
        timer.start();  //démarrer le chronomètre
    }

    //mettre à jour le chronomètre
    private void updateTimer() {
        seconds--;  //je décrémente le compteur de secondes
        timerLabel.setText("Temps restant : " +  seconds + "s");  //pour afficher le temps
        if (seconds == 0 && !gameEnded) {
                //System.exit(0); //lorsque le temps est écoulé on sort du jeu
            stopTimer();  //on arrête le chronomètre
            showEndGameWindow("Game Over", "Vous avez perdu !", Color.RED); //lorsque le temps est écoulé on affiche la fenêtre Game Over
        }
    }

    //création d'une méthode pour afficher les fenêtres de fin de jeu
    private void showEndGameWindow(String title, String message, Color color) {
        if (gameEnded) {
            return;
        } //verifie si la partie est terminée pour éviter d'afficher plein de fois la fenêtre gameover
        gameEnded = true;

        if (endGameFrame != null) {
            endGameFrame.dispose(); //enlève les fenêtres de fin de jeu
        }

        //nouvelle fenêtre
        endGameFrame = new JFrame(title);
        endGameFrame.setSize(400, 200);//taille de la fenêtre
        endGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        endGameFrame.setLocationRelativeTo(null);//position sur l'écran
        endGameFrame.setLayout(new BorderLayout());

        //message
        JLabel messageLabel = new JLabel(message, JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 24));//ecriture police et taille
        messageLabel.setForeground(color);//coleur de l'écriture
        endGameFrame.add(messageLabel, BorderLayout.CENTER);

        //bouton pour recommencer le jeu
        JButton restartButton = new JButton("Restart"); //bouton
        restartButton.setFont(new Font("Arial", Font.BOLD, 18));//police et taille ecriture
        restartButton.addActionListener(e -> {
            endGameFrame.dispose();//enlever la fenêtre de fin de jeu
            endGameFrame = null;
            Main.restartGame();//relancer le jeu
        });
        endGameFrame.add(restartButton, BorderLayout.SOUTH);

        endGameFrame.setVisible(true);
    }

    private static final int TOLERANCE = 5;

    @Override
    public void update() {
        if (gameEnded) {
            return;
        }

        handleVictory();
        checkCollisions();
        handleGameOver();
    }

    private void handleVictory() {
        if (isAtExit()) {  //vérifie si le héro est sur la sortie
            stopTimer();//arrete le timer
            SoundEffect.playWav("C:/Users/E7450/Downloads/FISE_2024_2025_Dungeon_Crawler-master/FISE_2024_2025_Dungeon_Crawler-master/sound/victoire.wav"); //son de la victoire
            BackgroundMusic.stopMusic();//arrete la musique de fond
            showEndGameWindow("Victoire !", "Bravo ! Vous avez gagné !", Color.GREEN);//affiche la fenêtre de victoire
        }
    }

    private void handleGameOver() {
        if (hero.getHealth() <= 0) {  //vérifie que le héro a plus de vie
            stopTimer(); //arrete le timer
            showEndGameWindow("Game Over", "Vous avez perdu !", Color.RED); //affiche la fenêtre gameover
        }
    }

    private boolean isAtExit() {
        return Math.abs(hero.getX() - exitPosition.x) <= TOLERANCE
                && Math.abs(hero.getY() - exitPosition.y) <= TOLERANCE; //compare les coordonnées du héro et de la sortie
    }

    // Vérifie les collisions entre le héros et les ennemis
    private void checkCollisions() {
        for (TrapSprite trap : traps) { //on parcourt les pièges
            if (isColliding(hero, trap)) { //regarde si il y a collision entre le héro et le piège
                hero.setHealth(hero.getHealth() - trap.getDamage()); //réduit la vie du héros
                SoundEffect.playWav("C:/Users/E7450/Downloads/FISE_2024_2025_Dungeon_Crawler-master/FISE_2024_2025_Dungeon_Crawler-master/sound/bruitpiege1.wav"); //joue un son lorsque le hero rencontre le piege
            }
        }
    }

    //vérifie si deux objets se chevauchent
    private boolean isColliding(DynamicSprite sprite1, TrapSprite sprite2) {
        return sprite1.getX() < sprite2.getX() + sprite2.getWidth() &&
                sprite1.getX() + sprite1.getWidth() > sprite2.getX() &&
                sprite1.getY() < sprite2.getY() + sprite2.getHeight() &&
                sprite1.getY() + sprite1.getHeight() > sprite2.getY();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP :
                hero.setDirection(Direction.NORTH);
                break;
            case KeyEvent.VK_DOWN:
                hero.setDirection(Direction.SOUTH);
                break;
            case KeyEvent.VK_LEFT:
                hero.setDirection(Direction.WEST);
                break;
            case KeyEvent.VK_RIGHT:
                hero.setDirection(Direction.EAST);
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    //pour pouvoir afficher le temps sur la fenêtre de jeu
    public JLabel getTimerLabel() {
        return timerLabel;
    }

    //pour arrêter le chronomètre
    public void stopTimer() {
        timer.stop();
        timer = null;
    }

    //pour obtenir le temps écoulé
    public int getElapsedTime() {
        return seconds;
    }
}

