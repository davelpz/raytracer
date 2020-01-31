package raytracer.hitable.transforms;

import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.hitable.Aabb;
import raytracer.hitable.HitRecord;
import raytracer.hitable.Hitable;

public class Translate implements Hitable {
	Hitable ptr;
	Vec offset;

	public Translate(Hitable p, final Vec displacement) {
		ptr = p;
		offset = displacement;
	}

	@Override
	public Optional<HitRecord> hit(Ray r, float t_min, float t_max) {
		Ray moved_r = new Ray(Vec.sub(r.origin(), offset), r.direction(), r.time());
		return ptr.hit(moved_r, t_min, t_max).map(rec -> {
			//rec = HitRecord.clone(rec);
			rec.p = Vec.add(rec.p, offset);
			return Optional.of(rec);
		}).orElseGet(() -> {
			return Optional.empty();
		});
	}

	@Override
	public Optional<Aabb> bounding_box(float t0, float t1) {
		return ptr.bounding_box(t0, t1).map((box) -> {
			return Optional.of(new Aabb(Vec.add(box.min(), offset), Vec.add(box.max(), offset)));
		}).orElseGet(() -> {
			return Optional.empty();
		});
	}

}
