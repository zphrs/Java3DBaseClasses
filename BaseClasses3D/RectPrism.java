package BaseClasses3D;

import java.awt.Graphics;

public class RectPrism extends Object3D {
	private Plane3D[] sides;
	public RectPrism(double x, double y, double z, double width, double height, double depth, Vector3 color, double opacity)
	{
		super(x, y, z, 0);
		init(width, height, depth, color);
	}
	public RectPrism(double x, double y, double z, double width, double height, double depth, Vector3 color)
	{
		super(x, y, z, 0);
		init(width, height, depth, color);
		
	}
	public Object3D toObject()
	{
		Object3D output = new Object3D(this.pos, 0);
		output.setPlanes(this.toPlanes());
		return output;
	}
	public void init(double width, double height, double depth, Vector3 color)
	{
		sides = new Plane3D[6];
		sides[0] = new Plane3D(new Vector3[]{
			new Vector3(-width/2, -height/2, -depth/2),
			new Vector3(-width/2, height/2, -depth/2),
			new Vector3(-width/2, height/2, depth/2),
			new Vector3(-width/2, -height/2, depth/2)
		}, color);
		sides[1] = new Plane3D(new Vector3[]{
			new Vector3(+width/2, -height/2, +depth/2),
			new Vector3(+width/2, +height/2, +depth/2),
			new Vector3(-width/2, +height/2, +depth/2),
			new Vector3(-width/2, -height/2, +depth/2)
		}, color);
		sides[2] = new Plane3D(new Vector3[]{
			new Vector3(+width/2, -height/2, -depth/2),
			new Vector3(+width/2, +height/2, -depth/2),
			new Vector3(+width/2, +height/2, +depth/2),
			new Vector3(+width/2, -height/2, +depth/2)
		}, color);
		sides[3] = new Plane3D(new Vector3[]{
			new Vector3(+width/2, -height/2, -depth/2),
			new Vector3(+width/2, +height/2, -depth/2),
			new Vector3(-width/2, +height/2, -depth/2),
			new Vector3(-width/2, -height/2, -depth/2)
		}, color);
		sides[4] = new Plane3D(new Vector3[]{
			new Vector3(+width/2, -height/2, -depth/2),
			new Vector3(+width/2, -height/2, +depth/2),
			new Vector3(-width/2, -height/2, +depth/2),
			new Vector3(-width/2, -height/2, -depth/2)
		}, color);
		sides[5] = new Plane3D(new Vector3[]{
			new Vector3(+width/2, +height/2, -depth/2),
			new Vector3(+width/2, +height/2, +depth/2),
			new Vector3(-width/2, +height/2, +depth/2),
			new Vector3(-width/2, +height/2, -depth/2)
		}, color);
		setPlanes(sides);
	}
	@Override
	public int draw(Graphics g, Camera camera, double time, double ambientBrightness)
	{
		if (super.draw(g, camera, time, ambientBrightness) != 0)
		{
			return 1;
		}
		return 0;
	}
}
