package raytracer;

import java.util.Optional;

public class Dielectric implements Material {
	Vec albedo;
	float fuzz;
	float ref_idx;

	public Dielectric(float i) {
		ref_idx = i;
	}

	@Override
	public Optional<ScatterResult> scatter(Ray r_in, HitRecord rec) {
		ScatterResult result = new ScatterResult();
		Vec outward_normal;
		Vec reflected = reflect(r_in.direction(), rec.normal);
		Vec refracted = new Vec();
		float ni_over_nt;
		result.attenuation = new Vec(1.0, 1.0, 1.0);
		float reflect_prob;
		float cosine;

		if (Vec.dot(r_in.direction(), rec.normal) > 0) {
			outward_normal = Vec.neg(rec.normal);
			ni_over_nt = ref_idx;
			cosine = ref_idx * Vec.dot(r_in.direction(), rec.normal) / r_in.direction().length();
		} else {
			outward_normal = rec.normal;
			ni_over_nt = 1.0f / ref_idx;
			cosine = -Vec.dot(r_in.direction(), rec.normal) / r_in.direction().length();
		}

		Optional<Vec> refract_result = refract(r_in.direction(), outward_normal, ni_over_nt);

		if (refract_result.isPresent()) {
			reflect_prob = schlick(cosine, ref_idx);
			refracted = refract_result.get();
		} else {
			reflect_prob = 1;
		}

		if (Math.random() < reflect_prob) {
			result.scattered = new Ray(rec.p, reflected, r_in.time());
		} else {
			result.scattered = new Ray(rec.p, refracted, r_in.time());
		}

		return Optional.of(result);
	}

	float schlick(float cosine, float ref_idx) {
		float r0 = (1 - ref_idx) / (1 + ref_idx);
		r0 = r0 * r0;
		return r0 + (1.0f - r0) * (float) Math.pow((1.0f - cosine), 5.0f);
	}

	Optional<Vec> refract(final Vec v, final Vec n, float ni_over_nt) {
		Vec uv = Vec.unit_vector(v);
		float dt = Vec.dot(uv, n);
		float discriminant = 1.0f - ni_over_nt * ni_over_nt * (1.0f - dt * dt);
		if (discriminant > 0) {
			Vec refracted = Vec.sub(Vec.mul(ni_over_nt, (Vec.sub(uv, Vec.mul(n, dt)))),
					Vec.mul(n, (float) Math.sqrt(discriminant)));
			return Optional.of(refracted);
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
