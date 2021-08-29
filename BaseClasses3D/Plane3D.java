package BaseClasses3D;

import java.awt.Graphics;

import java.util.ArrayList;


public class Plane3D implements Comparable<Plane3D> {
	public Point[] points;
	public Vector3 avg = new Vector3(0, 0, 0);
	public Vector3 closestToCamera;
	public Vector3 velocity = new Vector3(),  acceleration = new Vector3();
	private Vector3 camera;
	Vector3 color;
	Vector3 normal;
	double opacity = 1;
	boolean luminous = false;
	boolean wireframe = false;
	public Plane3D(Vector3[] inpPoints, Vector3 col, double opacity)
	{
		this.color = col;
		points = new Point[inpPoints.length];
		for (int i = 0; i<inpPoints.length; i++)
		{
			points[i] = new Point(inpPoints[i], 0);
		}
		this.opacity = opacity;
		for (int i = 0; i<points.length; i++)
		{
			avg = avg.add(points[i].pos);
		}
		avg = avg.multiply(1/((double)points.length));
	}

	public Plane3D(ArrayList<Vector3> inpPoints, Vector3 col, double opacity)
	{
		this.color = col;
		points = new Point[inpPoints.size()];
		for (int i = 0; i<inpPoints.size(); i++)
		{
			points[i] = new Point(inpPoints.get(i), 0);
		}
		this.opacity = opacity;
		for (int i = 0; i<points.length; i++)
		{
			avg = avg.add(points[i].pos);
		}
		avg = avg.multiply(1/((double)points.length));
	}

	public Plane3D(Vector3[] inpPoints, Vector3 col, double opacity, boolean luminous)
	{
		this.color = col;
		points = new Point[inpPoints.length];
		for (int i = 0; i<inpPoints.length; i++)
		{
			points[i] = new Point(inpPoints[i], 0);
		}
		this.opacity = opacity;
		this.luminous = luminous;
		for (int i = 0; i<points.length; i++)
		{
			avg = avg.add(points[i].pos);
		}
		avg = avg.multiply(1/((double)points.length));
	}

	public Plane3D(Vector3[] inpPoints, Vector3 col)
	{
		this.color = col;
		points = new Point[inpPoints.length];
		for (int i = 0; i<inpPoints.length; i++)
		{
			points[i] = new Point(inpPoints[i], 0);
		}
		for (int i = 0; i<points.length; i++)
		{
			avg = avg.add(points[i].pos);
		}
		avg = avg.multiply(1/((double)points.length));
	}
	public Vector3[] getPoints() {
		Vector3[] ret = new Vector3[points.length];
		for (int i = 0; i<points.length; i++)
		{
			ret[i] = points[i].pos;
		}
		return ret;
	}
	public Plane3D copy()
	{
		Vector3[] ptCpys = new Vector3[this.points.length];
		for (int i = 0; i<ptCpys.length; i++)
		{
			ptCpys[i] = this.points[i].pos;
		}
		Plane3D out = new Plane3D(ptCpys, this.color.copy());
		return this.transferSettings(out);
	}
	public Plane3D transferSettings(Plane3D p)
	{
		p.avg = this.avg.copy();
		p.color = color.copy();
		p.normal = this.normal;
		p.opacity = this.opacity;
		p.luminous = this.luminous;
		p.wireframe = this.wireframe;
		return p;
	}
	public void calcNormal()
	{
		Vector3 v1 = this.points[0].pos.subtract(this.points[1].pos);
		Vector3 v2 = this.points[2].pos.subtract(this.points[1].pos);
		this.normal = v1.crossProduct(v2).normalized();
	}
	public void draw(Graphics g, Camera camera, double time, double ambientBrightness, Vector3 xyOffset)
	{
		int[] xPoints = new int[points.length];
		int[] yPoints = new int[points.length];
		Point[] rotatedPts = this.rotateAround(camera);
		boolean inFrame = true;
		for (int i = 0; i<rotatedPts.length; i++)
		{
			if (rotatedPts[i].pos.z<camera.pos.z)
			{
				inFrame = false;
				break;
			}
			Vector3 toScreen = rotatedPts[i].getCenterScreenPos(camera.pos, 1, time);
			xPoints[i] = (int)Math.round(toScreen.x+xyOffset.x);
			yPoints[i] = (int)Math.round(toScreen.y+xyOffset.y);

		}
		if (inFrame)
		{
			if (!luminous)
				g.setColor(color.multiply(ambientBrightness).toColor(this.opacity));
			else
				g.setColor(color.toColor(this.opacity*(1-ambientBrightness)));
			if (wireframe)
			{
				g.drawPolygon(xPoints, yPoints, points.length);
			}
			else 
			{
				g.fillPolygon(xPoints, yPoints, points.length);
			}
		}
	}
	public Plane3D get2dProjection(Camera camera, double time)
	{
		Point[] rotatedPts = this.rotateAround(camera);
		Vector3[] pts = new Vector3[points.length];
		for (int i = 0; i<rotatedPts.length; i++)
		{
			Vector3 toScreen = rotatedPts[i].getCenterScreenPos(camera.pos, 1, time);
			pts[i] = new Vector3(toScreen.x-camera.screenWidth/2, toScreen.y-camera.screenHeight/2, 0);
		}
		return this.transferSettings(new Plane3D(pts, this.color));
	}

	public void updatePoints(Vector3[] inpPoints)
	{
		if (points.length != inpPoints.length)
		{
			throw new IndexOutOfBoundsException("lists don't match in length");
		}
		avg = new Vector3(0, 0, 0);
		for (int i = 0; i<inpPoints.length; i++)
		{
			points[i] = new Point(inpPoints[i], 0);
		}
		avg = avg.multiply(1/(double)points.length);
	}

	public void rotateAround(Vector3 around, Vector3 amount) {
		double cosa = Math.cos(amount.z);
		double sina = Math.sin(amount.z);
	
		double cosb = Math.cos(amount.y);
		double sinb = Math.sin(amount.y);
	
		double cosc = Math.cos(amount.x);
		double sinc = Math.sin(amount.x);
	
		double Axx = cosa*cosb;
		double Axy = cosa*sinb*sinc - sina*cosc;
		double Axz = cosa*sinb*cosc + sina*sinc;
	
		double Ayx = sina*cosb;
		double Ayy = sina*sinb*sinc + cosa*cosc;
		double Ayz = sina*sinb*cosc - cosa*sinc;
	
		double Azx = -sinb;
		double Azy = cosb*sinc;
		double Azz = cosb*cosc;
	
		for (int i = 0; i < points.length; i++) {
			Vector3 newPt = points[i].pos.subtract(around);
			double px = newPt.x;
			double py = newPt.y;
			double pz = newPt.z;
	
			newPt.x = Axx*px + Axy*py + Axz*pz;
			newPt.y = Ayx*px + Ayy*py + Ayz*pz;
			newPt.z = Azx*px + Azy*py + Azz*pz;
			
			points[i].pos = newPt.add(around);
		}
	}
	public void applyRotMatrix(double[] m)
	{
		for (int i = 0; i < points.length; i++) {
			Vector3 newPt = points[i].pos;
			double px = newPt.x;
			double py = newPt.y;
			double pz = newPt.z;
	
			newPt.x = m[0]*px + m[1]*py + m[2]*pz;
			newPt.y = m[3]*px + m[4]*py + m[5]*pz;
			newPt.z = m[6]*px + m[7]*py + m[8]*pz;
			
			points[i].pos = newPt;
		}
	}
	public Point[] rotateAround(Camera camera) {
		if (camera.getRotations().isZeros())
		{
			return points;
		}
		double[] m = camera.getMatrix();
		Vector3 around = camera.pos;
		Point[] outPts = new Point[points.length];
		for (int i = 0; i < points.length; i++) {
			Vector3 newPt = points[i].pos.subtract(around);
			double px = newPt.x;
			double py = newPt.y;
			double pz = newPt.z;
	
			newPt.x = m[0]*px + m[1]*py + m[2]*pz;
			newPt.y = m[3]*px + m[4]*py + m[5]*pz;
			newPt.z = m[6]*px + m[7]*py + m[8]*pz;
			
			outPts[i] = new Point(newPt.add(around), 0);
		}
		return outPts;
	}
	public void scale(Vector3 amount) {
		for (int i = 0; i < points.length; i++) {
			points[i].pos = points[i].pos.multiply(amount);
		}
	}
	public void setCamera(Camera camera) {
		this.camera = camera.pos;
		double maxDist = 0;
		avg = new Vector3(0, 0, 0);
		Point[] newPts = new Point[points.length];
		newPts = this.rotateAround(camera);
		for (int i = 0; i<points.length; i++)
		{
			avg = avg.add(newPts[i].pos);
			double dist = newPts[i].pos.distanceFrom(camera.pos);

			if (maxDist<dist)
			{
				maxDist = dist;
				closestToCamera = newPts[i].pos;
			}
		}
		avg = avg.multiply(1/((double)newPts.length));
	}
	public void setWireframe(boolean wireframe) {
		this.wireframe = wireframe;
	}
	@Override
	public int compareTo(Plane3D plane)
	{
		if (plane.closestToCamera == null || this.closestToCamera == null) {
			return -1;
		}
		double planeDist = plane.closestToCamera.distanceFrom(camera);
		double thisDist = this.closestToCamera.distanceFrom(camera);

		if (planeDist > thisDist)
		{
			return 1;
		} else if (planeDist < thisDist)
		{
			return -1;
		} else {
			return 0;
		}
	}

	public Plane3D add(Vector3 pos)
	{
		Vector3[] posOfPts = new Vector3[this.points.length];
		for (int i = 0; i<posOfPts.length; i++)
		{
			posOfPts[i] = this.points[i].pos;
		}
		return this.transferSettings(new Plane3D(pos.add(posOfPts), this.color));
	}
	public String toString()
	{
		String output = "";
		for (int i = 0; i<points.length; i++)
		{
			output += "point "+i+": "+points[i].pos+"\n";
		}
		return output;
	}
	public Plane3D[] subdivide()
	{
		return subdivide(0, true, false);
	}
	public Plane3D[] subdivide(double yExtrusion, boolean squaresToSquares, boolean starting)
	{
		double sideLength = points[0].pos.distanceFrom(points[1].pos);
		if (!(points.length==4 || points.length==3))
			throw new IndexOutOfBoundsException("The plane must have either 3 or 4 points");
		avg = new Vector3(0, 0, 0);
		for (int i = 0; i<points.length; i++)
		{
			avg = avg.add(points[i].pos);
		}
		double rand;
		if (starting)
		{
			rand = .5+Math.random()*.5;
		}
		else{
			rand = Math.random();
		}
		Vector3 midPt = avg.multiply(1/((double)points.length)).add(new Vector3(0, yExtrusion*(rand)*sideLength, 0));
		if (midPt.y>0)
		{
			midPt.y = 0;
		}
		if (points.length == 3)
		{
			return new Plane3D[]{
				new Plane3D(new Vector3[]{
					points[0].pos,
					midPt,
					points[1].pos
				}, this.color, this.opacity),
				new Plane3D(new Vector3[]{
					points[0].pos,
					midPt,
					points[2].pos
				}, this.color, this.opacity),
				new Plane3D(new Vector3[]{
					points[1].pos,
					midPt,
					points[2].pos
				}, this.color, this.opacity),
			};	
		}
		//for 4 points:
		Vector3 avg01 = points[0].pos.add(points[1].pos).multiply(.5);
		Vector3 avg12 = points[1].pos.add(points[2].pos).multiply(.5);
		Vector3 avg23 = points[2].pos.add(points[3].pos).multiply(.5);
		Vector3 avg30 = points[3].pos.add(points[0].pos).multiply(.5);
		if (squaresToSquares)
		{
			return new Plane3D[]{
				new Plane3D(new Vector3[]{
					avg01,
					points[1].pos,
					avg12,
					midPt
				}, this.color, this.opacity),
				new Plane3D(new Vector3[]{
					avg12,
					points[2].pos,
					avg23,
					midPt
				}, this.color, this.opacity),
				new Plane3D(new Vector3[]{
					avg23,
					points[3].pos,
					avg30,
					midPt
				}, this.color, this.opacity),
				new Plane3D(new Vector3[]{
					avg30,
					points[0].pos,
					avg01,
					midPt
				}, this.color, this.opacity),
			};
		}
		return new Plane3D[]{
			new Plane3D(new Vector3[]{
				avg01,
				points[0].pos,
				midPt
			}, this.color, this.opacity),
			new Plane3D(new Vector3[]{
				avg01,
				points[1].pos,
				midPt
			}, this.color, this.opacity),
			new Plane3D(new Vector3[]{
				avg12,
				points[1].pos,
				midPt
			}, this.color, this.opacity),
			new Plane3D(new Vector3[]{
				avg12,
				points[2].pos,
				midPt
			}, this.color, this.opacity),
			new Plane3D(new Vector3[]{
				avg23,
				points[2].pos,
				midPt
			}, this.color, this.opacity),
			new Plane3D(new Vector3[]{
				avg23,
				points[3].pos,
				midPt
			}, this.color, this.opacity),
			new Plane3D(new Vector3[]{
				avg30,
				points[3].pos,
				midPt
			}, this.color, this.opacity),
			new Plane3D(new Vector3[]{
				avg30,
				points[0].pos,
				midPt
			}, this.color, this.opacity),
		};
	}
	public void update(double deltaTime)
	{
		this.velocity = this.velocity.add(this.acceleration.multiply(deltaTime));
		for (int i = 0; i<points.length; i++)
		{
			this.points[i].pos = this.points[i].pos.add(this.velocity.multiply(deltaTime));
		}
	}
}
