import bagel.util.Point;

public class Player {
    private double X;
    private double Y;
    private int energy;
    private static final double STEP_SIZE = 10;
    private double DirectionX = 0;
    private double DirectionY = 0;

    public Player(double x, double y, int energy) {
        X = x;
        Y = y;
        this.energy = energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public int getEnergy() {
        return energy;
    }

    public double getDirectionX() {
        return DirectionX;
    }

    public double getDirectionY() {
        return DirectionY;
    }

    public void setDirectionTo(Point Entity) {
        double distance = new Point(X, Y).distanceTo(Entity);
        DirectionX = (Entity.x - X)/distance;
        DirectionY = (Entity.y - Y)/distance;
    }

    public void moveTo(double directionX, double directionY) {
        X += STEP_SIZE * directionX;
        Y += STEP_SIZE * directionY;
    }
}
