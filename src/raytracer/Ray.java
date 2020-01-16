package raytracer;

public class Ray {
	public Vec a;
	public Vec b;
	float _time;
	
	public Ray() {
	}

	/*public Ray(Vec a, Vec b) {
		this.a = a;
		this.b = b;
		this._time = 0.0f;
	}*/

	public Ray(Vec a, Vec b, float t) {
		this.a = a;
		this.b = b;
		this._time = t;
	}
	
	public Vec origin() {
		return a;
	}

	public Vec direction() {
		return b;
	}

	public float time() {
		return _time;
	}
	
	public Vec point_at_parameter(float t) {
		return Vec.add(a, Vec.mul(b, t));
	}
}
