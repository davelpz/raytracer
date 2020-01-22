package raytracer.material;

import java.util.Optional;

import raytracer.Ray;
import raytracer.hitable.HitRecord;

public interface Material {

	Optional<ScatterResult> scatter(final Ray r_in, final HitRecord rec);
}
