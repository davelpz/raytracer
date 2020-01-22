package raytracer.material;

import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.hitable.HitRecord;
import raytracer.texture.Texture;

public class Lambertian implements Material {
	Texture albedo;

	public Lambertian(Texture a) {
		albedo = a;
	}

	@Override
	public Optional<ScatterResult> scatter(Ray r_in, HitRecord rec) {
		ScatterResult result = new ScatterResult();
		Vec target = Vec.add(Vec.add(rec.p, rec.normal), random_in_unit_sphere());
		result.scattered = new Ray(rec.p, Vec.sub(target, rec.p), r_in.time());
		result.attenuation = albedo.value(0, 0, rec.p);
		return Optional.of(result);
	}

	public Vec random_in_unit_sphere() {
		Vec p;
		do {
			p = Vec.sub(Vec.mul(2.0f, new Vec(Math.random(), Math.random(), Math.random())), new Vec(1.0f, 1.0f, 1.0f));
		} while (p.squared_length() >= 1.0);

		return p;
	}
}
