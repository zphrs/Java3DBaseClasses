package BaseClasses3D;
import java.awt.Color;

public class Vector3 {
	public double x, y, z;

	public Vector3()
	{
		this.x = this.y = this.z = 0;
	}

	public Vector3(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3 multiply(Vector3 a)
	{
		return new Vector3(this.x*a.x, this.y*a.y, this.z*a.z);
	}
	public Vector3 multiply(double a)
	{
		return new Vector3(this.x*a, this.y*a, this.z*a);
	}
	public Vector3 transition(Vector3 a, double amount)
	{
		double oppAmount = 1-amount;
		return new Vector3(this.x*oppAmount+a.x*amount, this.y*oppAmount +a.y*amount, this.z*oppAmount +a.z*amount);
	}
	public Vector3 transition(Vector3[] a, double amount)
	{
		Vector3[] temp = new Vector3[a.length+1];
		temp[0] = this;
		for (int i = 0; i<a.length; i++)
		{
			temp[i+1]= a[i];
		}
		if (amount == 0)
		{
			return temp[0];
		}
		if (amount == 1)
		{
			return temp[temp.length-1];
		}
		double colorDistance = (double)1/(temp.length-1);
		double transitioningBetween = amount/colorDistance;
		int index = (int)Math.floor(transitioningBetween);
		return temp[index].transition(temp[index+1], transitioningBetween-index);
	}
	public Vector3 crossProduct(Vector3 a)
	{
		return new Vector3(a.y*z - a.z*y, a.z*x - a.x*z, a.x*y - a.y*x);
	}
	public Vector3 add(Vector3 a)
	{
		return new Vector3(this.x+a.x, this.y+a.y, this.z+a.z);
	}
	public Vector3[] add(Vector3[] a)
	{
		Vector3[] output = new Vector3[a.length];
		for (int i = 0; i<a.length; i++)
		{
			output[i] = this.add(a[i]);
		}
		return output;
	}
	public Vector3 subtract(Vector3 a)
	{
		return new Vector3(x-a.x, y-a.y, z-a.z);
	}
	public String toString()
	{
		return "x:" + this.x + "\ty:" + this.y + "\tz:" + this.z;
	}
	public Color toColor()
	{
		return new Color(Math.min((int)this.x, 255), Math.min((int)this.y, 255), Math.min((int)this.z, 255));
	}
	public Color toColor(double opacity)
	{
		return new Color(Math.min((int)this.x, 255), Math.min((int)this.y, 255), Math.min((int)this.z, 255), Math.min((int)(opacity*255), 255));
	}
	public Vector3 copy()
	{
		return new Vector3(this.x, this.y, this.z);
	}
	public double distanceFrom(Vector3 a)
	{
		return Math.sqrt(Math.pow(a.x-x, 2) + Math.pow(a.y-y, 2) + Math.pow(a.z-z, 2));
	}
	public Vector3 normalized()
	{
		double length = Math.sqrt( x*x + y*y + z*z );
		//System.out.println(output);
		if (length == 0)
		{
			return new Vector3(0, 0, 0);
		}
		return new Vector3(x/length, y/length, z/length);
	}
	public Vector3 radToVector()
	{
		double cosa = Math.cos(z);
		double sina = Math.sin(z);
	
		double cosb = Math.cos(y);
		double sinb = Math.sin(y);
	
		double cosc = Math.cos(x);
		double sinc = Math.sin(x);
	
		double Axx = cosa*cosb;
		double Axy = cosa*sinb*sinc - sina*cosc;
		double Axz = cosa*sinb*cosc + sina*sinc;
	
		double Ayx = sina*cosb;
		double Ayy = sina*sinb*sinc + cosa*cosc;
		double Ayz = sina*sinb*cosc - cosa*sinc;
	
		double Azx = -sinb;
		double Azy = cosb*sinc;
		double Azz = cosb*cosc;


		Vector3 newPt = new Vector3();
			double px = 0;
			double py = 0;
			double pz = 1;
	
			newPt.x = Axx*px + Axy*py + Axz*pz;
			newPt.y = Ayx*px + Ayy*py + Ayz*pz;
			newPt.z = Azx*px + Azy*py + Azz*pz;
			
		return newPt;
	}
	public Vector3 rotate(Vector3 amount)
	{
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


		Vector3 newPt = new Vector3();
			double px = this.x;
			double py = this.y;
			double pz = this.z;
	
			newPt.x = Axx*px + Axy*py + Axz*pz;
			newPt.y = Ayx*px + Ayy*py + Ayz*pz;
			newPt.z = Azx*px + Azy*py + Azz*pz;
			
		return newPt;
	}

	public Vector3 rotateAround(Vector3 around, Vector3 amount) {
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
		Vector3 newPt = this.subtract(around);
		double px = newPt.x;
		double py = newPt.y;
		double pz = newPt.z;

		newPt.x = Axx*px + Axy*py + Axz*pz;
		newPt.y = Ayx*px + Ayy*py + Ayz*pz;
		newPt.z = Azx*px + Azy*py + Azz*pz;
		return newPt;
	}

	public double[] getRotMatrix()
	{
		Vector3 amount = this;
		double[] m = new double[9];
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
		return m;
	}

	public Vector3 VectorToRad()
	{
		return new Vector3(Math.atan2(Math.sqrt(y*y + z*z), x), Math.atan2(Math.sqrt(z*z + x*x), y), Math.atan2(Math.sqrt(x*x + y*y), z));
	}
	public Vector3 right()
	{
		return new Vector3(Math.cos(y)*Math.cos(x), Math.cos(y)*Math.sin(x), -Math.sin(y));
	}
	public boolean closeTo(Vector3 a, double dist)
	{
		return this.distanceFrom(a) < dist;
	}

	public boolean isZeros()
	{
		return x == 0 && y == 0 && z == 0;
	}

	public int[] signOfVector()
	{
		return new int[]{(int)Math.signum(x), (int)Math.signum(y), (int)Math.signum(z)};
	}
}
