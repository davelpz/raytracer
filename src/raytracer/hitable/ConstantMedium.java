package raytracer.hitable;

import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.material.Isotropic;
import raytracer.material.Material;
import raytracer.texture.Texture;

public class ConstantMedium implements Hitable {
	Hitable boundary;
	float density;
	Material phase_function;

	public ConstantMedium(Hitable b, float d, Texture a) {
		boundary = b;
		density = d;
		phase_function = new Isotropic(a);

	}

	@Override
	public Optional<HitRecord> hit(Ray r, float t_min, float t_max) {
		boolean db = (Math.random()) < 0.00001f;
		db = false;
		Optional<HitRecord> rec1_opt = boundary.hit(r, -Float.MAX_VALUE, Float.MAX_VALUE);
		if (rec1_opt.isPresent()) {
			HitRecord rec1 = rec1_opt.get();
			Optional<HitRecord> rec2_opt = boundary.hit(r, rec1.t + 0.0001f, Float.MAX_VALUE);
			if (rec2_opt.isPresent()) {
				HitRecord rec2 = rec2_opt.get();
				HitRecord rec = new HitRecord();
				if (rec1.t < t_min)
					rec1.t = t_min;
				if (rec2.t > t_max)
					rec2.t = t_max;
				if (rec1.t >= rec2.t)
					return Optional.empty();
				if (rec1.t < 0)
					rec1.t = 0;
				float distance_inside_boundary = (rec2.t - rec1.t) * r.direction().length();
				float hit_distance = -(1.0f / density) * (float) Math.log(Math.random());
				if (hit_distance < distance_inside_boundary) {
					rec.t = rec1.t + hit_distance / r.direction().length();
					rec.p = r.point_at_parameter(rec.t);
					rec.normal = new Vec(1, 0, 0); // arbitrary
					rec.mat = phase_function;
					return Optional.of(rec);
				}
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<Aabb> bounding_box(float t0, float t1) {
		return boundary.bounding_box(t0, t1);
	}

}
