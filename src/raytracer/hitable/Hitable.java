package raytracer.hitable;

import java.util.Optional;

import raytracer.Ray;

public interface Hitable {
	public Optional<HitRecord> hit(final Ray r, float t_min, float t_max);
	public Optional<Aabb> bounding_box(float t0, float t1);
}
