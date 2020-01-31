package raytracer.hitable.transforms;

import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.hitable.Aabb;
import raytracer.hitable.HitRecord;
import raytracer.hitable.Hitable;

public class FlipNormals implements Hitable {
	Hitable ptr;

	public FlipNormals(Hitable p) {
		ptr = p;
	}

	@Override
	public Optional<HitRecord> hit(Ray r, float t_min, float t_max) {
		return ptr.hit(r, t_min, t_max).map(rec -> {
			rec.normal = Vec.neg(rec.normal);
			return Optional.of(rec);
		}).orElseGet(() -> {
			return Optional.empty();
		});
	}

	@Override
	public Optional<Aabb> bounding_box(float t0, float t1) {
		return ptr.bounding_box(t0, t1);
	}

}
