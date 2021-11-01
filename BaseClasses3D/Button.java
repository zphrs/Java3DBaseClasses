package BaseClasses3D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class Button {
	private int left, right, top, bottom;
	private int margin;
	private Color border = Color.WHITE;
	private Color fill = Color.BLACK;
	private Color txtCol = Color.WHITE;
	private String str = "";

	public Button(int left, int top, int width, int height, String str, int margin)
	{
		this.left = left;
		this.right = left+width;
		this.top = top;
		this.bottom = top+height;
		this.str = str;
		this.margin = margin;
	}

	public Button(Vector3[] pts)
	{
		this.left = (int)Math.floor(pts[0].x);
		this.right = (int)Math.ceil(pts[1].x);
		this.top = (int)Math.floor(pts[0].y);
		this.bottom = (int)Math.ceil(pts[1].y);
	}

	public void updateBoundingBox(Vector3[] pts)
	{
		this.left = (int)Math.floor(pts[0].x);
		this.right = (int)Math.ceil(pts[1].x);
		this.top = (int)Math.floor(pts[0].y);
		this.bottom = (int)Math.ceil(pts[1].y);
	}

	public void setStr(String str) {
		this.str = str;
	}

	public void setFill(Color fill) {
		this.fill = fill;
	}
    public void setBorder(Color border) {
		this.border = border;
	}
	public boolean isClicked(int mouseX, int mouseY)
	{
		return (left<mouseX && right>mouseX && top<mouseY && bottom>mouseY);
	}
	public void draw(Graphics g)
	{
		if (this.border != null)
		{
			g.setColor(border);
			g.drawRect(this.left, this.top, this.right-this.left, this.bottom-this.top);
		}
		if (fill != null)
		{
			g.setColor(fill);
			g.fillRect(this.left+margin, this.top+margin, this.right-this.left-margin*2, this.bottom-this.top-margin*2);
		}
		if (this.str != null)
		{
			int fontSize = 20;
			g.setFont(new Font("Arial", Font.PLAIN, fontSize));
			g.setColor(txtCol);
			g.drawString(str, this.left+margin+(-str.length()*(fontSize/2)+this.right-this.left)/2, this.bottom-margin-(this.bottom-this.top-fontSize)/2);
		}
	}
}
