package raytracer;

import java.util.Optional;

public class Lambertian implements Material {
	Vec albedo;

	public Lambertian(Vec a) {
		albedo = a;
	}

	@Override
	public Optional<ScatterResult> scatter(Ray r_in, HitRecord rec) {
		ScatterResult result = new ScatterResult();
		Vec target = Vec.add(Vec.add(rec.p, rec.normal), random_in_unit_sphere());
		result.scattered = new Ray(rec.p, Vec.sub(target, rec.p));
		result.attenuation = albedo;
		return Optional.of(result);
	}

	public  Vec random_in_unit_sphere() {
		Vec p;
		do {
			p = Vec.sub(Vec.mul(2.0f, new Vec(Math.random(), Math.random(), Math.random())), new Vec(1.0f, 1.0f, 1.0f));
		} while (p.squared_length() >= 1.0);

		return p;
	}
}
