public class Bullet {
    private double x;
    private double y;
    private static final double STEP_SIZE = 25;
    private static final double ENERGY_CONSUMPTION = 3;
    private static final double SHOOTING_RANGE = 150;
    private static final double SHOT_DEAD_RANGE = 25;

    public Bullet(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static double getShotDeadRange() {
        return SHOT_DEAD_RANGE;
    }

    public static double getShootingRange() {
        return SHOOTING_RANGE;
    }

    public void moveTo(double directionX, double directionY) {
        x += STEP_SIZE * directionX;
        y += STEP_SIZE * directionY;
    }
}
