package raytracer.hitable;

import raytracer.Vec;
import raytracer.material.Material;

public class HitRecord {
	public float t;
	public Vec p;
	public Vec normal;
	public Material mat;
	public float u;
	public float v;

	public HitRecord() {
		t = 0.0f;
		p = new Vec(0.0f, 0.0f, 0.0f);
		normal = new Vec(0.0f, 0.0f, 0.0f);
	}

	public HitRecord(float t, Vec p, Vec normal) {
		this.t = t;
		this.p = p;
		this.normal = normal;
	}

	public static HitRecord clone(HitRecord p) {
		var t = new HitRecord(p.t, p.p, p.normal);
		t.mat = p.mat;
		t.u = p.u;
		t.v = p.v;
		return t;
	}

	public String toString() {
		return "HitRecord(" + t + "," + p + "," + normal + "," + u + "," + v + ")";
	}
}