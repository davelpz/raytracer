package raytracer.hitable;

import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.material.Material;

public class XZRect implements Hitable {
	float x0, x1, z0, z1, k;
	Material mp;

	public XZRect(float _x0, float _x1, float _z0, float _z1, float _k, Material mat) {
		x0 = _x0;
		x1 = _x1;
		z0 = _z0;
		z1 = _z1;
		k = _k;
		mp = mat;
	}

	@Override
	public Optional<HitRecord> hit(Ray r, float t_min, float t_max) {
		HitRecord rec = new HitRecord();
		float t = (k - r.origin().y()) / r.direction().y();
		if (t < t_min || t > t_max) {
			return Optional.empty();
		}

		float x = r.origin().x() + t * r.direction().x();
		float z = r.origin().z() + t * r.direction().z();
		if (x < x0 || x > x1 || z < z0 || z > z1) {
			return Optional.empty();
		}

		rec.u = (x - x0) / (x1 - x0);
		rec.v = (z - z0) / (z1 - z0);
		rec.t = t;
		rec.mat = mp;
		rec.p = r.point_at_parameter(t);
		rec.normal = new Vec(0, 1, 0);

		return Optional.of(rec);
	}

	@Override
	public Optional<Aabb> bounding_box(float t0, float t1) {
		return Optional.of(new Aabb(new Vec(x0, k - 0.0001f, z0), new Vec(x1, k + 0.0001f, z1)));
	}

}
