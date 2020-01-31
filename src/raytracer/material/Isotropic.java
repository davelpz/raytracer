package raytracer.material;

import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.hitable.HitRecord;
import raytracer.texture.Texture;

public class Isotropic implements Material {
	Texture albedo;

	public Isotropic(Texture a) {
		albedo = a;
	}

	@Override
	public Optional<ScatterResult> scatter(Ray r_in, HitRecord rec) {
		ScatterResult result = new ScatterResult();
		result.scattered = new Ray(rec.p, random_in_unit_sphere(), 0.0f);
		result.attenuation = albedo.value(rec.u, rec.v, rec.p);
		return Optional.of(result);
	}

	Vec random_in_unit_sphere() {
		Vec p;
		do {
			p = Vec.sub(Vec.mul(2.0f, new Vec(Math.random(), Math.random(), Math.random())), new Vec(1.0f, 1.0f, 1.0f));
		} while (p.squared_length() >= 1.0);

		return p;
	}

}
