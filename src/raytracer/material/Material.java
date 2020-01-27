package raytracer.material;

import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.hitable.HitRecord;

public interface Material {

	Optional<ScatterResult> scatter(final Ray r_in, final HitRecord rec);

	default Vec emitted(float u, float v, final Vec p) {
		return new Vec(0, 0, 0);
	}
}
