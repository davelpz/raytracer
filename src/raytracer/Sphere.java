package raytracer;

import java.util.Optional;
import raytracer.HitRecord;

public class Sphere implements Hitable {

	Vec center = new Vec(0.0f, 0.0f, 0.0f);
	float radius = 0.0f;
	Material mat;

	public Sphere() {
	}

	public Sphere(Vec cen, float r, Material m) {
		center = cen;
		radius = r;
		mat = m;
	}

	@Override
	public Optional<HitRecord> hit(final Ray r, float t_min, float t_max) {
		HitRecord hit_record = new HitRecord();
		Vec oc = Vec.sub(r.origin(), center);
		float a = Vec.dot(r.direction(), r.direction());
		float b = Vec.dot(oc, r.direction());
		float c = Vec.dot(oc, oc) - radius * radius;
		float discriminant = b * b - a * c;
		if (discriminant > 0) {
			float temp = (-b - (float) Math.sqrt(discriminant)) / a;
			if (temp < t_max && temp > t_min) {
				hit_record.t = temp;
				hit_record.p = r.point_at_parameter(hit_record.t);
				hit_record.normal = Vec.div((Vec.sub(hit_record.p, center)), radius);
				hit_record.mat = mat;
				return Optional.of(hit_record);
			}
			temp = (-b + (float) Math.sqrt(discriminant)) / a;
			if (temp < t_max && temp > t_min) {
				hit_record.t = temp;
				hit_record.p = r.point_at_parameter(hit_record.t);
				hit_record.normal = Vec.div((Vec.sub(hit_record.p, center)), radius);
				hit_record.mat = mat;
				return Optional.of(hit_record);
			}
		}
		return Optional.empty();
	}

}
