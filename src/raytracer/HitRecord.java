package raytracer;

public class HitRecord {
	public float t;
	public Vec p;
	public Vec normal;
	public Material mat;

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
}