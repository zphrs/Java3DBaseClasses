# Java 3D Base Classes
## Point
Base class for object3D - stores position along with other info.
## Object3D
Base class for all 3D objects. Includes rendering, minor culling, wrapping, and other methods. Just read through the class. Also it bakes rotation into the object's points so create a copy if you want to preserve the original.
## Camera
Passed into Object3D.render() to determine what should be displayed and distance and stuff.
## Plane3D
Makes a plane of N points. 
## RectPrism
Makes a rectangular prism. Extends Object3D. Good example of how to create a custom object.
## ObjFileTO3DObject
Static function that lets you import .obj files - imports geometry from .obj file and color from the .mtl file and returns and Object3D.
## Vector3 
A 3D vector class that does not mutate on operations. Operations return a new vector and leaves the passed in vectors unchanged.
