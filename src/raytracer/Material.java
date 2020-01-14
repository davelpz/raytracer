package raytracer;

import java.util.Optional;

public interface Material {

	Optional<ScatterResult> scatter(final Ray r_in, final HitRecord rec);
}
