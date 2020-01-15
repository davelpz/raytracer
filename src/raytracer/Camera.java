package raytracer;

public class Camera {
	Vec origin;
	Vec lower_left_corner;
	Vec horizontal;
	Vec vertical;

	public Camera(Vec lookfrom, Vec lookat, Vec vup, float vfov, float aspect) {
		Vec u;
		Vec v;
		Vec w;
		float theta = vfov * (float) Math.PI / 180.0f;
		float half_height = (float) Math.tan(theta / 2);
		float half_width = aspect * half_height;
		origin = lookfrom;
		w = Vec.unit_vector(Vec.sub(lookfrom, lookat));
		u = Vec.unit_vector(Vec.cross(vup, w));
		v = Vec.cross(w, u);
		lower_left_corner = Vec.sub(Vec.sub(Vec.sub(origin, Vec.mul(half_width, u)), Vec.mul(half_height, v)), w);
		horizontal = Vec.mul(2 * half_width, u);
		vertical = Vec.mul(2 * half_height, v);
	}

	public Ray get_ray(float s, float t) {
		return new Ray(origin,
				Vec.sub(Vec.add(lower_left_corner, Vec.add(Vec.mul(s, horizontal), Vec.mul(t, vertical))), origin));
	}
}
