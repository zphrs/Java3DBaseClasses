import javax.swing.JFrame;
public class TestRunner {
	public static void main( String[] args ){
		JFrame frame = new JFrame("TEST");
		
		Test sc = new Test();
		frame.add(sc);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		sc.animate();	
	}
}
