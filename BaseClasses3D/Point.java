package BaseClasses3D;

import java.awt.Graphics;

public class Point implements Comparable<Point>{
	public Vector3 pos;
	protected int screenWidth = 800;
	protected int screenHeight = 600;
	private Vector3 distanceScale;
	private Vector3 cameraCalcedAt;
	private Vector3 posCalcedAt;
	private Vector3 centerScreenPos;
	private double timeCalcedAt = -1;
	protected double resetDistance;
	protected boolean render;
	protected boolean is3DObj;
	protected Point(double xCoord, double yCoord, double zCoord, double resetDistance)
	{
		this.resetDistance = resetDistance;
		this.pos = new Vector3(xCoord, yCoord, zCoord);
	}
	protected Point(Vector3 inpPos, double resetDistance)
	{
		this.pos = inpPos.copy();
	}
	public Point copy()
	{
		Point out = new Point(this.pos.copy(), resetDistance);
		return out;
	}
	public Vector3 getCenterScreenPos(Vector3 camera, double maxSizeInPx, double time)
	{
		if (time!=timeCalcedAt || centerScreenPos == null)
		{
			centerScreenPos = new Vector3((pos.x-camera.x)*distanceScale(camera).z + screenWidth/2, (pos.y-camera.y)*distanceScale(camera).z + screenHeight/2, maxSizeInPx*distanceScale(camera).z);
			timeCalcedAt = time;
		}
		else
		{
		}
		return centerScreenPos;
	}
	protected Vector3 distanceScale(Vector3 camera)
	{

		if (camera != cameraCalcedAt || pos!=posCalcedAt)
		{
			cameraCalcedAt = camera.copy();
			posCalcedAt = pos.copy();
			distanceScale = new Vector3(calcScaleFromDist(pos.x-camera.x), calcScaleFromDist(pos.y-camera.y), calcScaleFromDist((pos.z-camera.z)*(.001)));
		}
		return distanceScale;
	}
	private double calcScaleFromDist(double distance)
	{
		return Math.PI/2 - Math.atan(distance);
	}
	public int draw(Graphics g, Camera camera, double time, double ambientBrightness, Vector3 offset)
	{
		if (resetDistance > 0 && camera.pos.z > this.pos.z)
		{
			this.render = true;
			this.pos.z+=resetDistance;
			return 1;
		}
		return 0;
	}

	@Override
	public int compareTo(Point object)
	{
		if (object.pos.z > this.pos.z)
		{
			return 1;
		} else if (object.pos.z < this.pos.z)
		{
			return -1;
		} else {
			return 0;
		}
	}
}
