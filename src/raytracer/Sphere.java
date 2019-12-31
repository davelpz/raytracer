package raytracer;

public class Sphere implements Hitable {

	Vec center = new Vec(0.0f, 0.0f, 0.0f);
	float radius = 0.0f;

	public Sphere() {
	}

	public Sphere(Vec cen, float r) {
		center = cen;
		radius = r;
	}

	@Override
	public HitResult hit(final Ray r, float t_min, float t_max) {
		HitResult hit_result = new HitResult();
		Vec oc = Vec.sub(r.origin(), center);
		float a = Vec.dot(r.direction(), r.direction());
		float b = Vec.dot(oc, r.direction());
		float c = Vec.dot(oc, oc) - radius * radius;
		float discriminant = b * b - a * c;
		if (discriminant > 0) {
			float temp = (-b - (float) Math.sqrt(discriminant)) / a;
			if (temp < t_max && temp > t_min) {
				hit_result.hit_record.t = temp;
				hit_result.hit_record.p = r.point_at_parameter(hit_result.hit_record.t);
				hit_result.hit_record.normal = Vec.div((Vec.sub(hit_result.hit_record.p, center)), radius);
				hit_result.hit_anything = true;
				return hit_result;
			}
			temp = (-b + (float) Math.sqrt(discriminant)) / a;
			if (temp < t_max && temp > t_min) {
				hit_result.hit_record.t = temp;
				hit_result.hit_record.p = r.point_at_parameter(hit_result.hit_record.t);
				hit_result.hit_record.normal = Vec.div((Vec.sub(hit_result.hit_record.p, center)), radius);
				hit_result.hit_anything = true;
				return hit_result;
			}
		}
		return hit_result;
	}

}
