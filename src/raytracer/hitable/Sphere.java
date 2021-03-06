package raytracer.hitable;

import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.material.Material;

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
				SphereUVResult uv = get_sphere_uv(hit_record.normal);
				hit_record.u = uv.u;
				hit_record.v = uv.v;
				return Optional.of(hit_record);
			}
			temp = (-b + (float) Math.sqrt(discriminant)) / a;
			if (temp < t_max && temp > t_min) {
				hit_record.t = temp;
				hit_record.p = r.point_at_parameter(hit_record.t);
				hit_record.normal = Vec.div((Vec.sub(hit_record.p, center)), radius);
				hit_record.mat = mat;
				SphereUVResult uv = get_sphere_uv(hit_record.normal);
				hit_record.u = uv.u;
				hit_record.v = uv.v;
				return Optional.of(hit_record);
			}
		}
		return Optional.empty();
	}

	public Optional<Aabb> bounding_box(float t0, float t1) {
		Aabb box = new Aabb(Vec.sub(center, new Vec(radius, radius, radius)),
				Vec.add(center, new Vec(radius, radius, radius)));
		return Optional.of(box);
	}

	static class SphereUVResult {
		public float u;
		public float v;

		public SphereUVResult(double u, double v) {
			this.u = (float) u;
			this.v = (float) v;
		}
	}

	public static SphereUVResult get_sphere_uv(final Vec p) {
		float phi = (float) Math.atan2(p.z(), p.x());
		float theta = (float) Math.asin(p.y());

		return new SphereUVResult(1.0f - (phi + Math.PI) / (2.0f * Math.PI), (theta + Math.PI / 2.0f) / Math.PI);
	}
}
