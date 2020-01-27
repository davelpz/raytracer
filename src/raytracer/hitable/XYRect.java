package raytracer.hitable;

import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.material.Material;

public class XYRect implements Hitable {
	float x0, x1, y0, y1, k;
	Material mp;

	public XYRect(float _x0, float _x1, float _y0, float _y1, float _k, Material mat) {
		x0 = _x0;
		x1 = _x1;
		y0 = _y0;
		y1 = _y1;
		k = _k;
		mp = mat;
	}

	@Override
	public Optional<HitRecord> hit(Ray r, float t_min, float t_max) {
		HitRecord rec = new HitRecord();
		float t = (k - r.origin().z()) / r.direction().z();
		if (t < t_min || t > t_max) {
			return Optional.empty();
		}

		float x = r.origin().x() + t * r.direction().x();
		float y = r.origin().y() + t * r.direction().y();
		if (x < x0 || x > x1 || y < y0 || y > y1) {
			return Optional.empty();
		}

		rec.u = (x - x0) / (x1 - x0);
		rec.v = (y - y0) / (y1 - y0);
		rec.t = t;
		rec.mat = mp;
		rec.p = r.point_at_parameter(t);
		rec.normal = new Vec(0, 0, 1);

		return Optional.of(rec);
	}

	@Override
	public Optional<Aabb> bounding_box(float t0, float t1) {
		return Optional.of(new Aabb(new Vec(x0, y0, k - 0.0001f), new Vec(x1, y1, k + 0.0001f)));
	}

}
