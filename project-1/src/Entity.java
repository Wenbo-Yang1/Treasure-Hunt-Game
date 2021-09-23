import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import org.lwjgl.system.CallbackI;


import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Entity {
    ArrayList<Point> ZOMBIE = new ArrayList<>();
    ArrayList<Point> SANDWICH = new ArrayList<>();
    private Point TREASURE;
    private Player player;
    private Bullet bullet;
    private int zombieNum = 0;
    private int sandwichNum = 0;
    private final Image zombie = new Image("project-1/res/images/zombie.png");
    private final Image sandwich = new Image("project-1/res/images/sandwich.png");
    private final Image background = new Image("project-1/res/images/background.png");
    private final Image playerI = new Image("project-1/res/images/player.png");
    private final Image treasure = new Image("project-1/res/images/treasure.png");
    private final Image bulletI = new Image("project-1/res/images/shot.png");
    private final Font f = new Font("project-1/res/font/DejaVuSans-Bold.ttf", 20);


    public Entity(){
        this.loadEnvironment("project-1/res/IO/environment.csv");
    }

    private void loadEnvironment(String filename) {
        // Code here to read from the file and set up the environment
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",");
                if (info[0].contains("Player")) {
                    player = new Player(Double.parseDouble(info[1]), Double.parseDouble(info[2]), Integer.parseInt(info[3]));
                } else if (info[0].equals("Zombie")) {
                    ZOMBIE.add(new Point(Double.parseDouble(info[1]), Double.parseDouble(info[2])));
                    zombieNum += 1;
                } else if (info[0].equals("Sandwich")) {
                    SANDWICH.add(new Point(Double.parseDouble(info[1]), Double.parseDouble(info[2])));
                    sandwichNum += 1;
                } else if (info[0].equals("Treasure")) {
                    TREASURE = new Point(Double.parseDouble(info[1]), Double.parseDouble(info[2]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean shot = false;
    private int zombieIndex;
    private int sandwichIndex;
    private boolean special_Case;

    public void specialCase() {
        if (zombieNum > 0 && player.getEnergy() < 3 && sandwichNum == 0 && shot == true) {
            if (new Point(bullet.getX(), bullet.getY()).distanceTo(ZOMBIE.get(zombieIndex)) >= bullet.getShotDeadRange()) {
                player.moveTo(player.getDirectionX(), player.getDirectionY());
                bullet.moveTo(player.getDirectionX(), player.getDirectionY());
            } else {
                zombieNum --;
                ZOMBIE.remove(zombieIndex);
                if (zombieNum == 0) {
                    player.setDirectionTo(TREASURE);
                    player.moveTo(player.getDirectionX(), player.getDirectionY());
                } else {
                    System.out.println(player.getEnergy());
                    Window.close();
                }

            }
            special_Case = true;
        }
    }


    private double closest_Z = 100000;
    private double closest_S = 100000;

    public void writeCsv(double bulletX, double bulletY) throws IOException {
        PrintWriter output = new PrintWriter(new FileWriter("project-1/res/IO/output.csv", true));
        output.println(bulletX + ", " + bulletY);
        output.close();
    }

    public void algorithmOne() throws IOException {
        specialCase();
        if (new Point(player.getX(), player.getY()).distanceTo(TREASURE) < 50) {
            System.out.println(player.getEnergy() + ",success!");
            Window.close();
        }
        if (zombieNum == 0) {
            shot = false;
            player.setDirectionTo(TREASURE);
            player.moveTo(player.getDirectionX(), player.getDirectionY());
        }
        if (!special_Case) {

            if ((player.getEnergy() >= 3 || shot) && zombieNum > 0) {
                for (Point zombie: ZOMBIE) {
                    double distanceToZ = zombie.distanceTo(new Point(player.getX(), player.getY()));
                    if (closest_Z > distanceToZ) {
                        closest_Z = distanceToZ;
                        zombieIndex = ZOMBIE.indexOf(zombie);
                    }
                }
                closest_Z = 100000;
                player.setDirectionTo(ZOMBIE.get(zombieIndex));
                player.moveTo(player.getDirectionX(), player.getDirectionY());
            } else if (player.getEnergy() < 3){
                for (Point sandwich: SANDWICH) {
                    double distanceToS = sandwich.distanceTo(new Point(player.getX(), player.getY()));
                    if (closest_S > distanceToS) {
                        closest_S = distanceToS;
                        sandwichIndex = SANDWICH.indexOf(sandwich);
                    }
                }
                closest_S = 100000;
                player.setDirectionTo(SANDWICH.get(sandwichIndex));
                player.moveTo(player.getDirectionX(), player.getDirectionY());
            }
            if (sandwichNum > 0 && new Point(player.getX(), player.getY()).distanceTo(SANDWICH.get(sandwichIndex)) < 50) {
                SANDWICH.remove(sandwichIndex);
                player.setEnergy(player.getEnergy() + 5);
                sandwichNum --;
                sandwichIndex = 0;
            } else if (zombieNum > 0 && new Point(player.getX(), player.getY()).distanceTo(ZOMBIE.get(zombieIndex)) < bullet.getShootingRange()) {
                if (!shot) {
                    player.setEnergy(player.getEnergy() - 3);
                    bullet = new Bullet(player.getX(), player.getY());
                    shot = true;
                    writeCsv(bullet.getX(), bullet.getY());
                } else {
                    bullet.moveTo(player.getDirectionX(), player.getDirectionY());
                    writeCsv(bullet.getX(), bullet.getY());
                }
            }
            if (shot) {
                if (new Point(bullet.getX(), bullet.getY()).distanceTo(ZOMBIE.get(zombieIndex)) < bullet.getShotDeadRange()) {
                    ZOMBIE.remove(zombieIndex);
                    zombieNum --;
                    shot = false;
                    zombieIndex = 0;
                }
            }
        }
        drawGame();
    }
    public void drawGame () {
        background.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        playerI.draw(player.getX(), player.getY());
        for (Point z: ZOMBIE) {
            zombie.draw(z.x, z.y);
        }
        for (Point s: SANDWICH) {
            sandwich.draw(s.x, s.y);
        }
        if (shot) {
            bulletI.draw(bullet.getX(), bullet.getY());
        }
        treasure.draw(TREASURE.x, TREASURE.y);
        f.drawString("energy: " + player.getEnergy(), 20, 760);
    }

}
