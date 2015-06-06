import java.util.Random;

public class GameObject {
	private double x, y;
	private double vx, vy;
	private double ax, ay;
	private double max_vx, max_vy;
	
	private GameObject target;
	
	private int radius;
	private int width;
	private int height;
	
	private boolean invincible;
	
	private static Random random = new Random();
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @param width Gaming area width
	 * @param height Gaming area height
	 */
	public GameObject(double x, double y, int size, int width, int height) {
		this.x = x;
		this.y = y;
		this.vx = this.vy = 0d;
		this.radius = size;
		this.width = width;
		this.height = height;
		
		this.target = null;
		this.ax = this.ay = 0d;
		this.invincible = false;
	}
	
	public GameObject(GameObject go) {
		this.x = go.x;
		this.y = go.y;
		this.vx = go.vx;
		this.vy = go.vy;
		this.ax = go.ax;
		this.max_vx = go.max_vx;
		this.max_vy = go.max_vy;
		
		this.radius = go.radius;
		this.width = go.width;
		this.height = go.height;
		this.invincible = false;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getVx() {
		return vx;
	}
	public void setVx(double vx) {
		this.vx = vx;
	}
	public double getVy() {
		return vy;
	}
	public void setVy(double vy) {
		this.vy = vy;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public double getAx() {
		return ax;
	}
	public void setAx(double ax) {
		this.ax = ax;
	}
	public double getAy() {
		return ay;
	}
	public void setAy(double ay) {
		this.ay = ay;
	}
	
	public double getMax_vx() {
		return max_vx;
	}
	public void setMax_vx(double max_vx) {
		this.max_vx = max_vx;
	}
	public double getMax_vy() {
		return max_vy;
	}
	public void setMax_vy(double max_vy) {
		this.max_vy = max_vy;
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void setTarget(GameObject target) {
		this.target = target;
	}
	public boolean isInvincible() {
		return this.invincible;
	}
	public void invincible(final long millisecond) {
		this.invincible = true;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(millisecond);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				invincible = false;
			}
		}).start();
	}
	
	private void accelerate() {
		final double friction = 0.05;
		
		// 如果没有加速度，则根据摩擦力以一定加速度停下
		if (this.ax == 0d) {
			if (this.vx != 0) {
				if (this.vx < 0) {
					this.vx += friction;
					if (this.vx > 0)
						this.vx = 0.0;
				}
				if (this.vx > 0) {
					this.vx -= friction;
					if (this.vx < 0)
						this.vx = 0.0;
				}
			}
		} 
		if (this.ay == 0d) {
			if (this.vy != 0) {
				if (this.vy < 0) {
					this.vy += friction;
					if (this.vy > 0)
						this.vy = 0;
				}
				if (this.vy > 0) {
					this.vy -= friction;
					if (this.vy < 0)
						this.vy = 0;
				}
			}
		}
		
		this.vx += this.ax;
		this.vy += this.ay;
		if (this.vx < -this.max_vx)
			this.vx = -this.max_vx;
		else if (this.vx > this.max_vx)
			this.vx = this.max_vx;

		if (this.vy < -this.max_vy)
			this.vy = -this.max_vy;
		else if (this.vy > this.max_vy)
			this.vy = this.max_vy;

	}
	
	public void stepIgnoreAcceleration() {
		this.x += this.vx;
		this.y += this.vy;
		if (target == null) {
			if (this.x - this.radius <= 0) {
				this.x = 2 * this.radius - this.x;
				this.vx = -this.vx;
			} else if (this.x + this.radius >= this.width) {
				this.x = 2 * (this.width - this.radius) - this.x;
				this.vx = -this.vx;
			}
			if (this.y - this.radius <= 0) {
				this.y = 2 * this.radius - this.y;
				this.vy = -this.vy;
			} else if (this.y + this.radius >= this.height) {
				this.y = 2 * (this.height - this.radius) - this.y;
				this.vy = -this.vy;
			}
		} else {
			if (this.x - this.radius <= 0) {
				this.x = 2 * this.radius - this.x;
				this.vx = -this.vx;
				this.randomSpeed();
			} else if (this.x + this.radius >= this.width) {
				this.x = 2 * (this.width - this.radius) - this.x;
				this.vx = -this.vx;
				this.randomSpeed();
			}
			if (this.y - this.radius <= 0) {
				this.y = 2 * this.radius - this.y;
				this.vy = -this.vy;
				this.randomSpeed();
			} else if (this.y + this.radius >= this.height) {
				this.y = 2 * (this.height - this.radius) - this.y;
				this.vy = -this.vy;
				this.randomSpeed();
			}
		}
	}
	
	public void step() {
		this.accelerate();
		this.stepIgnoreAcceleration();
	}
	
	public void flash() {
		double xx = this.getX();
		double yy = this.getY();
		for (int i = 1; i < 40; ++i) {
			this.x += this.vx;
			this.y += this.vy;
			if (this.x < this.radius) {
				double dx = this.x - xx;
				double dy = this.y - yy;
				double dxx = this.radius-xx;
				this.y = dy/dx*dxx+yy;
				break;
			} else if (this.x > this.width-this.radius) {
				double dx = this.x - xx;
				double dy = this.y - yy;
				double dxx = this.width-this.radius-xx;
				this.y = dy/dx*dxx+yy;
				break;
			}
			if (this.y < this.radius) {
				double dx = this.x - xx;
				double dy = this.y - yy;
				double dyy = this.radius - yy;
				this.x = dx/dy*dyy + xx;
				break;
			} else if (this.y > this.height-radius) {
				double dx = this.x - xx;
				double dy = this.y - yy;
				double dyy = this.height - this.radius - yy;
				this.x = dx/dy*dyy + xx;
				break;
			}
		}
		this.ax = this.ay = this.vx = this.vy = 0;
	}
	public void randomSpeed() {
		// 重新计算方向
		if (this.target == null)
			return;
		double dx = this.target.x - this.x;
		double dy = this.target.y - this.y;
		double toOne = Math.abs(dx) > Math.abs(dy) ? Math.abs(dx) : Math.abs(dy);
		dx /= toOne;
		dy /= toOne;
		double f = random.nextDouble()/2+0.5;
		this.vx = f * dx * this.max_vx;
		this.vy = f * dy * random.nextDouble() * this.max_vy;
	}
	
	public boolean collision(GameObject o) {
		if (this.invincible)
			return false;
		
		double dx = this.x - o.x;
		double dy = this.y - o.y;
		double dis = o.radius+this.radius;
		dx *= dx;
		dy *= dy;
		
		return dx+dy < dis*dis;
	}
}
