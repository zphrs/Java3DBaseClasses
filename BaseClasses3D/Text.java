package BaseClasses3D;
import java.awt.font.*;
import java.awt.Font;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.awt.RenderingHints;

public class Text extends Object3D {
	public static final int DEFAULT_COLOR = 0xFFFFFF;
	public static final int HORIZ_ALIGN_LEFT = 0;
	public static final int HORIZ_ALIGN_CENTER = 1;
	public static final int HORIZ_ALIGN_RIGHT = 2;
	public static final int VERT_ALIGN_BOTTOM = 0;
	public static final int VERT_ALIGN_CENTER = 1;
	public static final int VERT_ALIGN_TOP = 2;
	public static final int DEFAULT_ALIGNMENT = 0;
	public static final int CURVE_SEGMENTS = 5;
	private double resolution = CURVE_SEGMENTS;
	private int horizAlignment = DEFAULT_ALIGNMENT;
	private int vertAlignment = DEFAULT_ALIGNMENT;
	private int fontSize = 50;
	public Text(String text, String fontFile, int fontSize, int horizAlignment, int vertAlignment, double x, double y, double z, double resetDistance, double resolution) {
		super(x, y, z, resetDistance);
		this.resolution = resolution;
		this.horizAlignment = horizAlignment;
		this.vertAlignment = vertAlignment;
		this.fontSize = fontSize;
		this.initPlanes(text, new Font(fontFile, Font.PLAIN, fontSize));
		Vector3[] bounding = this.getBounding();
		switch (this.horizAlignment) {
			case HORIZ_ALIGN_LEFT:
				break;
			case HORIZ_ALIGN_CENTER:
				this.translate(new Vector3((bounding[0].x - bounding[1].x)/2, 0, 0));
				break;
			case HORIZ_ALIGN_RIGHT:
				this.translate(new Vector3(bounding[1].x - bounding[0].x, 0, 0));
				break;
		}
		switch(this.vertAlignment) {
			case VERT_ALIGN_BOTTOM:
				break;
			case VERT_ALIGN_CENTER:
				this.translate(new Vector3(0, (bounding[1].y - bounding[0].y)/2, 0));
				break;
			case VERT_ALIGN_TOP:
				this.translate(new Vector3(0, bounding[1].y - bounding[0].y, 0));
			break;
		}
	}
	public Text(String text) {
		this(text, "Arial", 50, DEFAULT_ALIGNMENT, DEFAULT_ALIGNMENT, 0.0, 0.0, 0.0, 0.0, CURVE_SEGMENTS);
	}
	// TODO: add triangulation algo to fix holes in text like for o and e
	// or you could maybe fix in a hacky way by somehow  going counterclockwise
	// for the inside points and clockwise for the outside points.
	// idk it's too much work rn and my current solution will work good enough
	// for the person lab.
	public void initPlanes(String text, Font font) {
		FontRenderContext frc = new FontRenderContext(
        null,
        RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT,
        RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
		this.setPlanes(new Plane3D[0]);
		PathIterator iterator = font.createGlyphVector(frc, text).getOutline().getPathIterator(null);
        
        double[] coordinates = new double[6];
        Vector3 currPt = new Vector3();
		Vector3 startPt = new Vector3();
        this.setPlanes(new Plane3D[0]);
		ArrayList<Vector3> out = new ArrayList<Vector3>();
        while (!iterator.isDone()) {
			int type = iterator.currentSegment(coordinates);
			// for all curves:
			double x1 = coordinates[0]; // the x coordinate of the current point
			double y1 = coordinates[1]; // the y coordinate of the current point
			// for quadratic curves + cubic curves:
			double x2 = coordinates[2]; // the control x coordinate of the current point
			double y2 = coordinates[3]; // the control y coordinate of the current point
			// for cubic curves:
			// double x3 = coordinates[4]; // the second control x coordinate of the current point
			// double y3 = coordinates[5]; // the second control y coordinate of the current point
			switch (type) {
            case PathIterator.SEG_QUADTO:
				System.out.println(fontSize*resolution);
				int lineSegments = (int)(fontSize*resolution);
				for (int i = 1; i < lineSegments; i++) {
					double t = (double)i / (double)(lineSegments);
					out.add(currPt.quadraticBezier(new Vector3(x1, y1, 0), new Vector3(x2, y2, 0), t));
				}
				currPt.x = x2;
				currPt.y = y2;
				out.add(currPt.copy());
				break;
    
            case PathIterator.SEG_CUBICTO:
                // TODO: do cubic curves - 
				// not needed for .ttf files because  
				// apple figured quadratic curves were cheaper
				// and who needs clean circle curves anyway?
				// Not apple I guess.
				System.out.println((char)27 + "[31m" + "Cubic curves not implemented");
                break;
            case PathIterator.SEG_LINETO:
				currPt.x = x1;
				currPt.y = y1;
				out.add(currPt.copy());
                break;
				case PathIterator.SEG_MOVETO:
				if (out.size() > 0)
				{
					Plane3D plane = new Plane3D(out, new Vector3(255, 255, 255), 1);
					
					this.addToPlanes(plane);
					out = new ArrayList<Vector3>();
				}
				currPt.x = x1;
				currPt.y = y1;
				startPt.x = x1;
				startPt.y = y1;
				out.add(currPt.copy());
                break;
			case PathIterator.SEG_CLOSE:
				out.add(new Vector3(startPt.x, startPt.y, 0));
				if (out.size() > 0)
				{
					Plane3D plane = new Plane3D(out, new Vector3(255, 255, 255), 1);

					this.addToPlanes(plane);
					out = new ArrayList<Vector3>();
				}
				break;
            }
			iterator.next();
        }
		Plane3D plane = new Plane3D(out, new Vector3(255, 255, 255), 1);
		this.addToPlanes(plane);
	}

}
