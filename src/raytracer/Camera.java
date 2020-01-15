package raytracer;

public class Camera {
	Vec origin;
	Vec lower_left_corner;
	Vec horizontal;
	Vec vertical;
	Vec u, v, w;
	float lens_radius;

	public Camera(Vec lookfrom, Vec lookat, Vec vup, float vfov, float aspect, float aperture, float focus_dist) {
		lens_radius = aperture / 2;
		float theta = vfov * (float) Math.PI / 180.0f;
		float half_height = (float) Math.tan(theta / 2);
		float half_width = aspect * half_height;
		origin = lookfrom;
		w = Vec.unit_vector(Vec.sub(lookfrom, lookat));
		u = Vec.unit_vector(Vec.cross(vup, w));
		v = Vec.cross(w, u);
		lower_left_corner = Vec.sub(
				Vec.sub(Vec.sub(origin, Vec.mul(half_width * focus_dist, u)), Vec.mul(half_height * focus_dist, v)),
				Vec.mul(focus_dist, w));
		horizontal = Vec.mul(2 * half_width * focus_dist, u);
		vertical = Vec.mul(2 * half_height * focus_dist, v);
	}

	public Ray get_ray(float s, float t) {
		Vec rd = Vec.mul(lens_radius, random_in_unit_disk());
		Vec offset = Vec.add(Vec.mul(u, rd.x()), Vec.mul(v, rd.y()));

		return new Ray(Vec.add(origin, offset), Vec.sub(
				Vec.sub(Vec.add(lower_left_corner, Vec.add(Vec.mul(s, horizontal), Vec.mul(t, vertical))), origin),
				offset));
	}

	Vec random_in_unit_disk() {
		Vec p = new Vec();
		do {
			p = Vec.sub(Vec.mul(2, new Vec(Math.random(), Math.random(), 0)), new Vec(1, 1, 0));
		} while (Vec.dot(p, p) >= 1.0);
		return p;
	}
}
