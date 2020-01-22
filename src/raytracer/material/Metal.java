package raytracer.material;

import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.hitable.HitRecord;

public class Metal implements Material {
	Vec albedo;
	float fuzz;

	public Metal(Vec a) {
		albedo = a;
	}

	public Metal(Vec a, float f) {
		albedo = a;
		fuzz = f;
	}
	
	@Override
	public Optional<ScatterResult> scatter(Ray r_in, HitRecord rec) {
		ScatterResult result = new ScatterResult();
		Vec reflected = reflect(Vec.unit_vector(r_in.direction()), rec.normal);
		result.scattered = new Ray(rec.p, Vec.add(reflected, Vec.mul(fuzz,random_in_unit_sphere())), r_in.time());
		result.attenuation = albedo;
		if (Vec.dot(result.scattered.direction(), rec.normal) > 0) {
			return Optional.of(result);
		} else {
			return Optional.empty();
		}
	}

	Vec reflect(final Vec v, final Vec n) {
		return Vec.sub(v, Vec.mul(2.0f, Vec.mul(Vec.dot(v, n), n)));
	}
	
	Vec random_in_unit_sphere() {
		Vec p;
		do {
			p = Vec.sub(Vec.mul(2.0f, new Vec(Math.random(), Math.random(), Math.random())), new Vec(1.0f, 1.0f, 1.0f));
		} while (p.squared_length() >= 1.0);

		return p;
	}
	

}
