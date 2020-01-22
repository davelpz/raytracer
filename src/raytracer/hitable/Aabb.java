package raytracer.hitable;

import raytracer.Ray;
import raytracer.Vec;

public class Aabb {
	Vec _min;
	Vec _max;

	public Aabb(Vec a, Vec b) {
		_min = a;
		_max = b;
	}

	public Vec min() {
		return _min;
	}

	public Vec max() {
		return _max;
	}

	public boolean hit(final Ray r, float tmin, float tmax) {
		for (int a = 0; a < 3; a++) {
			float t0 = ffmin((_min.get(a) - r.origin().get(a)) / r.direction().get(a),
					(_max.get(a) - r.origin().get(a)) / r.direction().get(a));

			float t1 = ffmax((_min.get(a) - r.origin().get(a)) / r.direction().get(a),
					(_max.get(a) - r.origin().get(a)) / r.direction().get(a));
			tmin = ffmax(t0, tmin);
			tmax = ffmin(t1, tmax);
			if (tmax <= tmin)
				return false;
		}
		return true;
	}

	public float ffmin(float a, float b) {
		return a < b ? a : b;
	}

	public float ffmax(float a, float b) {
		return a > b ? a : b;
	}
	
	public static Aabb surrounding_box(Aabb box0, Aabb box1) {
		Vec small = new Vec(Math.min(box0.min().x(), box1.min().x()), Math.min(box0.min().y(), box1.min().y()),
				Math.min(box0.min().z(), box1.min().z()));
		Vec big = new Vec(Math.max(box0.max().x(), box1.max().x()), Math.max(box0.max().y(), box1.max().y()),
				Math.max(box0.max().z(), box1.max().z()));
		return new Aabb(small, big);
	}

}
