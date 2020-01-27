package raytracer.material;

import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.hitable.HitRecord;
import raytracer.texture.Texture;

public class DiffuseLight implements Material {
	Texture emit;

	public DiffuseLight(Texture a) {
		emit = a;
	}

	@Override
	public Optional<ScatterResult> scatter(Ray r_in, HitRecord rec) {
		return Optional.empty();
	}

	@Override
	public Vec emitted(float u, float v, final Vec p) {
		return emit.value(u, v, p);
	}

}
