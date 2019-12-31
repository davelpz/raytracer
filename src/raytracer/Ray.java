package raytracer;

public class Ray {
	public Vec a;
	public Vec b;

	public Ray() {
	}

	public Ray(Vec a, Vec b) {
		this.a = a;
		this.b = b;
	}

	public Vec origin() {
		return a;
	}

	public Vec direction() {
		return b;
	}

	public Vec point_at_parameter(float t) {
		return Vec.add(a, Vec.mul(b, t));
	}
}
