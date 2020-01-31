package raytracer.hitable.transforms;

import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.hitable.Aabb;
import raytracer.hitable.HitRecord;
import raytracer.hitable.Hitable;

public class RotateY implements Hitable {
	Hitable ptr;
	float sin_theta;
	float cos_theta;
	Optional<Aabb> hasbox;
	Aabb bbox;

	public RotateY(Hitable p, float angle) {
		ptr = p;
		float radians = (float) ((Math.PI / 180.0f) * angle);
		sin_theta = (float) Math.sin(radians);
		cos_theta = (float) Math.cos(radians);
		hasbox = ptr.bounding_box(0, 1);
		bbox = hasbox.orElse(new Aabb(new Vec(), new Vec()));

		Vec min = new Vec(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		Vec max = new Vec(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					float x = i * bbox.max().x() + (1 - i) * bbox.min().x();
					float y = j * bbox.max().y() + (1 - j) * bbox.min().y();
					float z = k * bbox.max().z() + (1 - k) * bbox.min().z();
					float newx = cos_theta * x + sin_theta * z;
					float newz = -sin_theta * x + cos_theta * z;
					Vec tester = new Vec(newx, y, newz);
					for (int c = 0; c < 3; c++) {
						if (tester.get(c) > max.get(c)) {
							max.set(c, tester.get(c));
						}
						if (tester.get(c) < min.get(c)) {
							min.set(c, tester.get(c));
						}
					}
				}

			}

		}
		bbox = new Aabb(min, max);
	}

	@Override
	public Optional<HitRecord> hit(final Ray r, float t_min, float t_max) {
		Vec origin = Vec.clone(r.origin());
		Vec direction = Vec.clone(r.direction());
		origin.set(0, cos_theta * r.origin().get(0) - sin_theta * r.origin().get(2));
		origin.set(2, sin_theta * r.origin().get(0) + cos_theta * r.origin().get(2));
		direction.set(0, cos_theta * r.direction().get(0) - sin_theta * r.direction().get(2));
		direction.set(2, sin_theta * r.direction().get(0) + cos_theta * r.direction().get(2));
		Ray rotated_r = new Ray(origin, direction, r.time());
		return ptr.hit(rotated_r, t_min, t_max).map(rec -> {
			rec = HitRecord.clone(rec);
			Vec p = Vec.clone(rec.p);
			Vec normal = Vec.clone(rec.normal);
			p.set(0, cos_theta * rec.p.get(0) + sin_theta * rec.p.get(2));
			p.set(2, -sin_theta * rec.p.get(0) + cos_theta * rec.p.get(2));
			normal.set(0, cos_theta * rec.normal.get(0) + sin_theta * rec.normal.get(2));
			normal.set(2, -sin_theta * rec.normal.get(0) + cos_theta * rec.normal.get(2));
			rec.p = p;
			rec.normal = normal;
			return Optional.of(rec);
		}).orElseGet(() -> {
			return Optional.empty();
		});
	}

	@Override
	public Optional<Aabb> bounding_box(float t0, float t1) {
		if (hasbox.isPresent()) {
			return Optional.of(bbox);
		} else {
			return Optional.empty();
		}
	}

}
