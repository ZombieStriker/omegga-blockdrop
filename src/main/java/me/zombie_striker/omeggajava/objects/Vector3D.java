package me.zombie_striker.omeggajava.objects;

public class Vector3D {

    private double x,y,z;

    public Vector3D(double x, double y,  double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public double getZ() {
        return z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
