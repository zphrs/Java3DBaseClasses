package BaseClasses3D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.awt.Graphics;

public class Object3D extends Point {
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
	private Vector3 posInterpTo;
	private Vector3 initInterpPos;
	private double interpDownAt;
	private double interpDuration;
	private boolean wireframe = false;
	protected Plane3D[] planeBuffer;
	private Vector3 rotInterpTo;
	private Vector3 initInterpRot;
	private double rotInterpDownAt;
	private double rotInterpDuration;
	/*Planes are relative to center position. See RectPrism class for example.*/
	public Object3D(double x, double y, double z, double resetDistance)
	{
		super(x, y, z, resetDistance);
		this.is3DObj = true;
	}
	public Object3D() {
		this(0, 0, 0, 0);
	}
	/**
	 * Makes Object3D with position and resetDistance. 
	 * @param pos Initial position of Object3D
	 * @param resetDistance how far away the object will be placed after the object passes the camera. use 0 for no transform.
	 */
	public Object3D(Vector3 pos, double resetDistance)
	{
		super(pos, resetDistance);
		this.is3DObj = true;
	}
	public String toString() {
		String out = "";
		out += "Object3D: " + this.getClass().getName() + "\n";
		if (planes != null) out += "Planes: " + Arrays.toString(planes) + "\n";
		if (childObjects.size() > 0) out += "ChildObjects: " + childObjects.toString() + "\n";
		if (parent != null) out += "Parent: " + parent + "\n";
		if (pos.x != 0 || pos.y != 0 || pos.z != 0) out += "Position: " + this.pos + "\n";
		if (resetDistance != 0) out += "ResetDistance: " + this.resetDistance + "\n";
		if (rotations.x != 0 || rotations.y != 0 || rotations.z != 0) out += "Rotation: " + this.rotations + "\n";
		if (velocity.x != 0 || velocity.y != 0 || velocity.z != 0) out += "Velocity: " + this.velocity + "\n";
		if (rotVelocity.x != 0 || rotVelocity.y != 0 || rotVelocity.z != 0) out += "RotVelocity: " + this.rotVelocity + "\n";
		if (acceleration.x != 0 || acceleration.y != 0 || acceleration.z != 0) out += "Acceleration: " + this.acceleration + "\n";
		if (forward.x != 0 || forward.y != 0 || forward.z != 0) out += "Forward: " + this.forward + "\n";
		if (right.x != 0 || right.y != 0 || right.z != 0) out += "Right: " + this.right + "\n";
		if (up.x != 0 || up.y != 0 || up.z != 0) out += "Up: " + this.up + "\n";
		if (render) out += "Render: true\n";
		else out += "Render: false\n";
		if (wireframe) out += "Wireframe: true\n";
		else out += "Wireframe: false\n";
		return out;
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
	public void setPlanes(ArrayList<Plane3D> inpPlanes)
	{
		planes = inpPlanes.toArray(new Plane3D[inpPlanes.size()]);
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
	public void setColor(int hex)
	{
		for (int i = 0; i<planes.length; i++)
		{
			planes[i].color = new Vector3(hex);
		}
	}
	public void replaceColor(Vector3 oldColor, Vector3 newColor)
	{
		if (planes != null) for (int i = 0; i<planes.length; i++)
		{

			if (planes[i].color.equals(oldColor))
			{
				planes[i].color = newColor;
			}
		}
		for (Object3D obj : childObjects)
		{
			obj.replaceColor(oldColor, newColor);
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
	public void addToPlanes(Plane3D inpPlane) {
		Plane3D[] newPlanes = new Plane3D[planes.length+1];
		int newPlanesIndex = 0;
		for (Plane3D plane : planes)
		{
			newPlanes[newPlanesIndex] = plane;
			newPlanesIndex++;
		}
		newPlanes[newPlanesIndex] = inpPlane;
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

	public void removeChild(Object3D child) 
	{
		childObjects.remove(child);
	}

	public int draw(Graphics g, Camera camera, double time, double ambientBrightness)
	{
		return this.draw(g, camera, time, ambientBrightness, new Vector3());
	}
	public void prepPlanes(Camera camera, double time, double ambientBrightness)
	{
		int planeCt = this.planes.length;
		for (Object3D child : childObjects)
		{
			child.prepPlanes(camera, time, ambientBrightness);
			planeCt += child.planeBuffer.length;
		}
		planeBuffer = new Plane3D[planeCt];
		int planeBufferInd = 0;
		for (int i = 0; i<planes.length; i++)
		{
			planeBuffer[planeBufferInd++] = planes[i];
		}
		for (Object3D child : childObjects)
		{
			for (int i = 0; i<child.planeBuffer.length; i++)
			{
				planeBuffer[planeBufferInd++] = child.planeBuffer[i];
			}
		}

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
	public void smoothTranslate(Vector3 amount, double time, double duration) {
		this.posInterpTo = amount.copy();
		this.initInterpPos = this.pos.copy();
		this.interpDownAt = time;
		this.interpDuration = duration;
		this.childObjects.forEach(obj -> obj.smoothTranslate(amount, time, duration));
	}
	public void smoothRotate(Vector3 amount, double time, double duration) {
		this.rotInterpTo = amount.copy();
		this.initInterpRot = this.getRotations().copy();
		this.rotInterpDownAt = time;
		this.rotInterpDuration = duration;
		this.childObjects.forEach(obj -> obj.smoothRotate(amount, time, duration));
	}
	public Vector3 getRotations() {
		return this.rotations;
	}
	public void scale(Vector3 amount) {
		for (Plane3D plane : planes) {
			plane.scale(amount);
		}	
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
			for (Point point : plane.points)
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
		if (this.posInterpTo != null)
		{
			this.pos = this.initInterpPos.lerp(this.posInterpTo, this.interpDownAt);
			if (this.pos.closeTo(this.posInterpTo, 0.3))
			{
				this.pos = this.posInterpTo;
				this.posInterpTo = null;
			}
		}
		this.pos = this.pos.add(this.velocity.multiply(deltaTime));

		this.rotate(this.rotVelocity.multiply(deltaTime));
	}
	public void updateInterps(double time) {
		if (this.posInterpTo != null)
		{
			double t = (time-this.interpDownAt)/this.interpDuration;
			this.pos = this.initInterpPos.lerp(this.posInterpTo, t);
			if (t >= 1)
			{
				this.pos = this.posInterpTo;
				this.posInterpTo = null;
			}
		}
		
		if (this.rotInterpTo != null)
		{
			double t = (time-this.rotInterpDownAt)/this.rotInterpDuration;
			this.setRotations(this.initInterpRot.lerp(this.rotInterpTo, t));
			if (t >= 1)
			{
				this.setRotations(this.rotInterpTo);
				this.rotInterpTo = null;
			}
		}
		for (Object3D child : childObjects)
		{
			child.updateInterps(time);
		}
	}

	public void clearInterps()
	{
		this.posInterpTo = null;
		this.rotInterpTo = null;
	}
	public void setRotations(Vector3 rot)
	{
		this.rotations = rot;
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
	
	public Object3D getParent()
	{
		return this.parent;
	}

	public Vector3 getInterpingTo()
	{
		return this.posInterpTo != null?this.posInterpTo:this.pos;
	}
}
