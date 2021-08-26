package BaseClasses3D;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.Collections;

import java.util.ArrayList;

public class Object3D extends Object {
	Plane3D[] planes;
	ArrayList<Object3D> childObjects = new ArrayList<Object3D>();
	Object3D parent;
	Vector3 rotations = new Vector3(0, 0, 0);
	boolean render = true;
	Vector3 velocity = new Vector3();
	Vector3 rotVelocity = new Vector3();
	Vector3 forward = new Vector3(0, 0, 1);
	Vector3 right = new Vector3(1, 0, 0);
	Vector3 up = new Vector3(0, 1, 0);
	Vector3 acceleration = new Vector3();
	private boolean wireframe = false;
	/*Planes are relative to center position. See SquarePrism class for example.*/
	public Object3D(double x, double y, double z, double resetDistance)
	{
		super(x, y, z, resetDistance);
		this.is3DObj = true;
	}
	public Object3D(Vector3 pos, double resetDistance)
	{
		super(pos, resetDistance);
		this.is3DObj = true;
	}
	public void setWireframe(boolean wireframe) {
		this.wireframe = wireframe;
		if (planes != null) for (Plane3D plane : planes)
		{
			plane.setWireframe(wireframe);
		}
		if (childObjects != null) for (Object3D child : childObjects)
		{
			child.setWireframe(wireframe);
		}
	}
	public void setPlanes(Plane3D[] inpPlanes)
	{
		planes = inpPlanes;
	}
	public void setChildren(Object3D[] children)
	{
		childObjects = new ArrayList<Object3D>();
		for (Object3D child : children)
		{
			childObjects.add(child);
			child.parent = this;
		}
	}
	public Object3D getChild(int index)
	{
		return childObjects.get(index).copy();
	}
	public void setChildren(ArrayList<Object3D> children)
	{
		childObjects = children;
		for (Object3D child : childObjects)
		{
			child.parent = this;
		}
	}
	public void setColor(Vector3 color)
	{
		for (int i = 0; i<planes.length; i++)
		{
			planes[i].color = color;
		}
	}
	public boolean getWireframe()
	{
		return wireframe;
	}
	public Object3D copy()
	{
		Object3D copy = new Object3D(this.pos, this.resetDistance);
		if (this.planes != null)
		{
			Plane3D[] planeCpy = new Plane3D[this.planes.length];
			for (int i = 0; i<planeCpy.length; i++)
			{
				planeCpy[i] = this.planes[i].copy();
			}
			copy.setPlanes(planeCpy);
		}
		else
		{
			copy.setPlanes(this.planes);
		}
		if (childObjects != null)
		{
			Object3D[] childrenCpys = new Object3D[childObjects.size()];
			for (int i = 0; i<childrenCpys.length; i++)
			{
				childrenCpys[i] = childObjects.get(i).copy();
			}
			copy.setChildren(childrenCpys);
		}
		else
		{
			copy.setChildren(childObjects);
		}
		copy.setWireframe(wireframe);
		return copy;
	}
	public void addToPlanes(Plane3D[] inpPlanes)
	{
		Plane3D[] newPlanes = new Plane3D[inpPlanes.length+planes.length];
		int newPlanesIndex = 0;
		for (Plane3D plane : planes)
		{
			newPlanes[newPlanesIndex] = plane;
			newPlanesIndex++;
		}
		for (Plane3D plane : inpPlanes)
		{
			newPlanes[newPlanesIndex] = plane;
			newPlanesIndex++;
		}
		planes = newPlanes;
	}
	public void addToChildren(Object3D[] inpChildren)
	{
		Collections.addAll(childObjects, inpChildren);
		for (Object3D child : inpChildren)
		{
			child.parent = this;
		}
	}

	public void addToChildren(Object3D inpChild)
	{
		this.addToChildren(new Object3D[]{inpChild});
	}

	public int draw(Graphics g, Camera camera, double time, double ambientBrightness)
	{
		return this.draw(g, camera, time, ambientBrightness, new Vector3());
	}
	@Override
	public int draw(Graphics g, Camera camera, double time, double ambientBrightness, Vector3 xyOffset) {
		onDraw(camera, time, ambientBrightness);
		if (super.draw(g, camera, time, ambientBrightness, xyOffset) != 0 || !render)
		{
			return 1;
		}
		if (this.childObjects.size() != 0)
		{
			Object3D allPlanes = new Object3D(this.pos, 0);
			if (planes != null)
			{
				Plane3D[] temp = new Plane3D[planes.length];
				for (int i = 0; i<planes.length; i++)
				{
					temp[i] = planes[i];
				}
				allPlanes.setPlanes(temp);
			}
			else {
				allPlanes.setPlanes(new Plane3D[]{});
			}
			for (int i = 0; i<childObjects.size(); i++)
			{
				allPlanes.addToPlanes(childObjects.get(i).toPlanes());
			}
			allPlanes.draw(g, camera, time, ambientBrightness, xyOffset);
			return 0;
		}
		if (planes != null)
		{
			Plane3D[] temp = new Plane3D[planes.length];
			for (int i = 0; i<planes.length; i++)
			{
				temp[i] = planes[i].add(this.pos);
				temp[i].setCamera(camera);
			}
			Arrays.sort(temp);
			for (int i = 0; i<planes.length; i++)
			{
				temp[i].draw(g, camera, time, ambientBrightness, xyOffset);
			}
		}
		return 0;
	}
	public Object3D get2dProjection(Camera camera, double time) {
		Plane3D[] planes = this.toPlanes();
		Plane3D[] flattened = new Plane3D[planes.length];
		for (int i = 0; i<planes.length; i++)
		{
			flattened[i] = planes[i].get2dProjection(camera, time);
		}
		Object3D out = new Object3D(new Vector3(), resetDistance);
		out.setPlanes(flattened);
		return out;
	}

	public void onDraw(Camera camera, double time, double ambientBrightness)
	{
		return;
	}
	public void rotate(Vector3 around, Vector3 amount)
	{
		rotations.add(amount);
		this.pos = this.pos.rotateAround(new Vector3(), amount);
		if (planes != null) for (Plane3D plane : planes)
		{
			plane.rotateAround(new Vector3(), amount);
		}
		if (childObjects.size() != 0)
		{
			for (Object3D obj : childObjects)
			{
				obj.rotate(obj.pos.multiply(-1), amount);
			}
		}
	}

	public void rotate(Vector3 amount)
	{
		if (amount.x == 0 && amount.y == 0 && amount.z == 0)
		{
			return;
		}
		rotations = rotations.add(amount);
		forward = forward.rotate(amount);
		right = right.rotate(amount);
		up = up.rotate(amount);
		double[] rotMatrix = amount.getRotMatrix();
		if (planes != null)
		{
			for (Plane3D plane : planes)
			{
				plane.applyRotMatrix(rotMatrix);
			}
		}
		if (childObjects != null)
		{
			for (Object3D obj : childObjects)
			{
				obj.rotate(obj.pos.multiply(-1), amount);
			}
		}
	}
	public void translate(Vector3 amount) {
		this.pos = this.pos.add(amount);
	}

	public Vector3 forward()
	{
		//return new Vector3(Math.cos(rotations.y)*Math.cos(rotations.x), Math.cos(rotations.y)*Math.sin(rotations.x), -Math.sin(rotations.y));
		return forward;
	}
	/**
	 * 
	 * @return Vector3[]: ind 0 is min, ind 1 is max
	 */
	public Vector3[] getBounding()
	{
		Vector3[] bounding = new Vector3[2];
		bounding[0] = new Vector3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
		bounding[1] = new Vector3(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
		for(Plane3D plane : this.toPlanes())
		{
			for (Object point : plane.points)
			{
				point.pos = point.pos.subtract(this.pos);
				bounding[0].x = Math.min(bounding[0].x, point.pos.x);
				bounding[0].y = Math.min(bounding[0].y, point.pos.y);
				bounding[0].z = Math.min(bounding[0].z, point.pos.z);

				bounding[1].x = Math.max(bounding[1].x, point.pos.x);
				bounding[1].y = Math.max(bounding[1].y, point.pos.y);
				bounding[1].z = Math.max(bounding[1].z, point.pos.z);

			}
		}
		return bounding;
	}
	public Vector3[] getScBounding(Camera c)
	{
		Object3D obj2D = this.get2dProjection(c, -1);
		Vector3[] bounding = obj2D.getBounding();
		for (Vector3 pt : bounding)
		{
			pt.x += c.screenWidth/2;
			pt.y += c.screenHeight/2;
		}
		return bounding;
	}
	public Plane3D[] toPlanes()
	{
		return toPlanes(new Vector3());
	}
	public Plane3D[] toPlanes(Vector3 offset)
	{
		Plane3D[] output;
		offset = this.pos.add(offset);
		if (planes != null)
		{
			output = new Plane3D[planes.length];
			for (int i = 0; i<planes.length; i++)
			{
				output[i] = planes[i].add(offset);
			}
		}
		else
		{
			output = new Plane3D[0];
		}
		if (childObjects!=null)
			for (int i = 0; i<childObjects.size(); i++)
			{
				Plane3D[] temp = childObjects.get(i).toPlanes(offset);
				Plane3D[] temp2 = new Plane3D[output.length + temp.length];
				int j = 0;
				while (j<output.length)
				{
					temp2[j] = output[j];
					j++;
				}
				for (int k = 0; k<temp.length; j++, k++)
				{
					temp2[j] = temp[k];
				}
				output = temp2; 
			}
		return output;
	}
	public Plane3D[] toPlanes(double opacity)
	{
		Plane3D[] output = new Plane3D[planes.length];
		for (int i = 0; i<planes.length; i++)
		{
			output[i] = planes[i].add(this.pos);
			output[i].opacity = opacity;
		}
		return output;
	}
	public Plane3D[] toPlanes(double opacity, boolean luminous)
	{
		Plane3D[] output = new Plane3D[planes.length];
		for (int i = 0; i<planes.length; i++)
		{
			output[i] = planes[i].add(this.pos);
			output[i].opacity = opacity;
			output[i].luminous = true;
		}
		return output;
	}
	public void checkIfPast(Vector3 camera)
	{
		this.pos = new Vector3(camera.x + Math.random() * 1600 - 800, 100, camera.z + 5000 + Math.random() * 30000);
	}
	public void update(double deltaTime)
	{
		this.velocity = this.velocity.add(this.acceleration.multiply(deltaTime));
		this.pos = this.pos.add(this.velocity.multiply(deltaTime));
		this.rotate(this.rotVelocity.multiply(deltaTime));
	}
	public Vector3 getGlobalPos()
	{
		Vector3 out = this.pos;
		if (this.parent != null)
			out.add(this.parent.getGlobalPos());
		return out;
	}

	public Vector3 getGlobalPos(Vector3 local)
	{
		Vector3 out = this.pos.add(local);
		if (this.parent != null)
			out.add(this.parent.getGlobalPos());
		return out;
	}

	public Vector3 getLocalPos(Vector3 global)
	{
		Vector3 out = getGlobalPos();
		return global.subtract(out);
	}
}
