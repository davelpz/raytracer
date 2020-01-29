package raytracer.hitable;

import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.material.Material;

public class YZRect implements Hitable {
	float y0, y1, z0, z1, k;
	Material mp;

	public YZRect(float _y0, float _y1, float _z0, float _z1, float _k, Material mat) {
		y0 = _y0;
		y1 = _y1;
		z0 = _z0;
		z1 = _z1;
		k = _k;
		mp = mat;
	}

	@Override
	public Optional<HitRecord> hit(Ray r, float t_min, float t_max) {
		HitRecord rec = new HitRecord();
		float t = (k - r.origin().x()) / r.direction().x();
		if (t < t_min || t > t_max) {
			return Optional.empty();
		}

		float y = r.origin().y() + t * r.direction().y();
		float z = r.origin().z() + t * r.direction().z();
		if (y < y0 || y > y1 || z < z0 || z > z1) {
			return Optional.empty();
		}

		rec.u = (y - y0) / (y1 - y0);
		rec.v = (z - z0) / (z1 - z0);
		rec.t = t;
		rec.mat = mp;
		rec.p = r.point_at_parameter(t);
		rec.normal = new Vec(1, 0, 0);

		return Optional.of(rec);
	}

	@Override
	public Optional<Aabb> bounding_box(float t0, float t1) {
		return Optional.of(new Aabb(new Vec(k - 0.0001f, y0, z0), new Vec(k + 0.0001f, y1, z1)));
	}

}
