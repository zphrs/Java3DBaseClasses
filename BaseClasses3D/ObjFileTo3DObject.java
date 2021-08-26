package BaseClasses3D;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Hashtable;
import java.util.Scanner; // Import the Scanner class to read text files

public class ObjFileTo3DObject {
    public static Object3D convert(String fileName, double scaleAmount)
    {
        Object3D output = new Object3D(0, 0, 0, 0); 
        output.setPlanes(new Plane3D[0]);
        output.setChildren(new Object3D[0]);
        Vector3[] points = new Vector3[0];
        String material;
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (!myReader.hasNext("mtllib")){
                myReader.next();
            }
            myReader.next();
			String filePath = fileName.substring(0, fileName.lastIndexOf("/")+1);
            Hashtable<String, Vector3> matDict = getMatDict(filePath+myReader.next());
            while (myReader.hasNext())
            {
                Object3D child = new Object3D(0, 0, 0, 0);
                child.setPlanes(new Plane3D[0]);
                while (!myReader.hasNext("v")) {
                    myReader.next();
                }
                do 
                {
                    myReader.next();
                    Vector3 point = new Vector3();
                    point.x = myReader.nextDouble()*scaleAmount;
                    point.y = myReader.nextDouble()*scaleAmount;
                    point.z = myReader.nextDouble()*scaleAmount;
                    points = addToVector3Array(points, point);
                    myReader.nextLine();
                } while (myReader.hasNext("v"));
                while (!myReader.hasNext("usemtl")){
                    myReader.next();
                }
				while (myReader.hasNext("usemtl"))
				{
					myReader.next();
					material = myReader.next();
					Vector3 matCol = matDict.getOrDefault(material, new Vector3(255, 255, 255));
					while (!myReader.hasNext("f"))
					{
						myReader.next();
					}
					while (myReader.hasNext() && myReader.hasNext("f")){
						myReader.next(); // gets rid of "f"
						Vector3[] planePts = new Vector3[0];
						while(true)
						{
							String n = myReader.next();
							planePts = addToVector3Array(planePts, points[strToInt(n.split("/")[0])-1]);
							if (myReader.hasNext("f") || !myReader.hasNext() || myReader.hasNext("o") || myReader.hasNext("l") || myReader.hasNext("usemtl"))
							{
								break;
							}
						}
						Plane3D plane = new Plane3D(planePts, matCol);
						child.addToPlanes(new Plane3D[]{plane});
					}
				}
                output.addToChildren(new Object3D[]{child});
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return output;
    }
    private static Vector3[] addToVector3Array(Vector3[] arr, Vector3 newVect)
    {
        Vector3[] output = new Vector3[arr.length+1];
        for (int i = 0; i<arr.length; i++)
        {
            output[i] = arr[i];
        }
        output[arr.length] = newVect;
        return output;
    }
    private static int strToInt(String inp)
    {
        int output = 0;
        for (int i = 0; i<inp.length(); i++)
        {
            if (inp.charAt(i) - 48 < 0 || inp.charAt(i) - 48 > 9)
            {
                return -1; 
            }else{
                output += (inp.charAt(i) - 48)*Math.pow(10, inp.length() - 1 - i);
            }
        }
        return output;
    }
    private static Hashtable<String, Vector3> getMatDict(String fileName)
    {
        Hashtable<String, Vector3> dict = new Hashtable<String, Vector3>();
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while(myReader.hasNext())
            {
                while(!myReader.hasNext("newmtl"))
                {
                    myReader.next();
                };
                myReader.next();
                String key = myReader.next();
                while(!myReader.hasNext("Kd"))
                {
                    myReader.next();
                };
                myReader.next();
                //System.out.println(myReader.next() + "," + myReader.next() + "," + myReader.next());
                Vector3 value = new Vector3(myReader.nextDouble()*255, myReader.nextDouble()*255, myReader.nextDouble()*255);
                dict.put(key, value);
                while(!myReader.hasNext("newmtl") && myReader.hasNext())
                {
                    myReader.next();
                };
            }
			myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return dict;
    }
    
}
