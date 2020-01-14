package raytracer;

public class Camera {
	Vec origin;
	Vec lower_left_corner;
	Vec horizontal;
	Vec vertical;

	public Camera() {
		lower_left_corner = new Vec(-2.0f, -1.0f, -1.0f);
		horizontal = new Vec(4.0f, 0.0f, 0.0f);
		vertical = new Vec(0.0f, 2.0f, 0.0f);
		origin = new Vec(0.0f, 0.0f, 0.0f);
	}

	public Ray get_ray(float u, float v) {
		return new Ray(origin,
				Vec.sub(Vec.add(lower_left_corner, Vec.add(Vec.mul(u, horizontal), Vec.mul(v, vertical))), origin));
	}
}
