package BaseClasses3D;

public class Camera extends Object3D {
	private double[] m = new double[9];
	private Vector3 rotations;
	public Camera(double x, double y, double z)
	{
		super(x, y, z, 0);
		this.rotations = new Vector3();
		updateMatrix();

	}
	public Vector3 getRotations()
	{
		return rotations.copy();
	}
	public void setRotations(Vector3 rotations)
	{
		this.rotations = rotations;
		updateMatrix();
	}
	private void updateMatrix()
	{
		Vector3 amount = this.rotations.multiply(-1);
		double cosa = Math.cos(amount.z);
		double sina = Math.sin(amount.z);
	
		double cosb = Math.cos(amount.y);
		double sinb = Math.sin(amount.y);
	
		double cosc = Math.cos(amount.x);
		double sinc = Math.sin(amount.x);
	
		m[0] = cosa*cosb;
		m[1] = cosa*sinb*sinc - sina*cosc;
		m[2] = cosa*sinb*cosc + sina*sinc;
	
		m[3] = sina*cosb;
		m[4] = sina*sinb*sinc + cosa*cosc;
		m[5] = sina*sinb*cosc - cosa*sinc;
	
		m[6] = -sinb;
		m[7] = cosb*sinc;
		m[8] = cosb*cosc;
	}
	/**
	 * do not set values in array returned - I could clone array but that is expensive
	 */
	public double[] getMatrix() {
		return m;
	}
	public Vector3 forward() {
		return new Vector3(-m[2], -m[5], m[8]);
	}
	/*@Override
	public void rotate(Vector3 amount) {
		rotations = rotations.add(amount);
	}
	@Override
	public void rotate(Vector3 amount, Vector3 around) {
		Vector3 offset = this.pos.subtract(around);
		Vector3 newPts = new Vector3(offset.x, offset.y*Math.cos(-amount.x) + offset.z*Math.sin(-amount.x), -offset.y*Math.sin(-amount.x) + offset.z*Math.cos(-amount.x)); //x rotation
		newPts = new Vector3(newPts.x*Math.cos(-amount.y)+ -newPts.z*Math.sin(-amount.y), newPts.y, newPts.x*Math.sin(-amount.y)+newPts.z*Math.cos(-amount.y)); // y rotation
		newPts = new Vector3(newPts.x*Math.cos(amount.z)+newPts.y*Math.sin(amount.z), -newPts.x*Math.sin(amount.z) + newPts.y*Math.cos(amount.z), newPts.z); // z rotation
		this.pos = newPts;
		rotations = rotations.add(amount);
	}*/
	public void pointTowards(Vector3 point)
	{
		//this.rotations = new Vector3(-Math.atan2(point.y-this.pos.y, point.z-this.pos.z), Math.atan2(point.x-this.pos.x, point.z-this.pos.z), 0);
		this.rotations.y = Math.atan2(point.x-this.pos.x, point.z-this.pos.z);
		this.rotations.x = -Math.atan2(point.y-this.pos.y, point.z-this.pos.z);
		//this.rotations.z = -Math.atan2(point.y-this.pos.y, point.x-this.pos.x);
		//this.rotations = point.subtract(this.pos).VectorToRad().multiply(-1);
	}
}
