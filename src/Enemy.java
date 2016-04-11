public class Enemy {
	int width = 40;
	int height = 40;
	double x;
	double y;
	public Enemy(int width, int height, double x, double y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public void moveUp() {             
    	y = y - 5;
    }
    public void moveDown() {
    	y = y + 5;
    }
    public void moveRight() {
    	x = x + 5;
    }
    public void moveLeft() {
    	x = x - 5;
    }
    public void move(double dx, double dy)
    {
    	x = (x + dx);
    	y = (y + dy);
    }
}