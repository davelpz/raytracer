package raytracer;

import java.util.Optional;

public interface Hitable {
	public Optional<HitRecord> hit(final Ray r, float t_min, float t_max);
}
