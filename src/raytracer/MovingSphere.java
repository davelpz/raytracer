package raytracer;

import java.util.Optional;
import raytracer.HitRecord;

public class MovingSphere implements Hitable {

	Vec center0 = new Vec(0.0f, 0.0f, 0.0f);
	Vec center1 = new Vec(0.0f, 0.0f, 0.0f);
	float time0, time1;
	float radius = 0.0f;
	Material mat;

	public MovingSphere() {
	}

	public MovingSphere(Vec cen0, Vec cen1, float t0, float t1, float r, Material m) {
		center0 = cen0;
		center1 = cen1;
		time0 = t0;
		time1 = t1;
		radius = r;
		mat = m;
	}

	public Vec center(float time) {
		return Vec.add(center0, Vec.mul(((time - time0) / (time1 - time0)), Vec.sub(center1, center0)));
	}

	@Override
	public Optional<HitRecord> hit(final Ray r, float t_min, float t_max) {
		HitRecord hit_record = new HitRecord();
		Vec oc = Vec.sub(r.origin(), center(r.time()));
		float a = Vec.dot(r.direction(), r.direction());
		float b = Vec.dot(oc, r.direction());
		float c = Vec.dot(oc, oc) - radius * radius;
		float discriminant = b * b - a * c;
		if (discriminant > 0) {
			float temp = (-b - (float) Math.sqrt(discriminant)) / a;
			if (temp < t_max && temp > t_min) {
				hit_record.t = temp;
				hit_record.p = r.point_at_parameter(hit_record.t);
				hit_record.normal = Vec.div((Vec.sub(hit_record.p, center(r.time()))), radius);
				hit_record.mat = mat;
				return Optional.of(hit_record);
			}
			temp = (-b + (float) Math.sqrt(discriminant)) / a;
			if (temp < t_max && temp > t_min) {
				hit_record.t = temp;
				hit_record.p = r.point_at_parameter(hit_record.t);
				hit_record.normal = Vec.div((Vec.sub(hit_record.p, center(r.time()))), radius);
				hit_record.mat = mat;
				return Optional.of(hit_record);
			}
		}
		return Optional.empty();
	}

}