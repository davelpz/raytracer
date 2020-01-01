package raytracer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import raytracer.Vec;

public class Main {

	public static float hit_sphere(Vec center, float radius, Ray r) {
		Vec oc = Vec.sub(r.origin(), center);
		float a = Vec.dot(r.direction(), r.direction());
		float b = 2.0f * Vec.dot(oc, r.direction());
		float c = Vec.dot(oc, oc) - radius * radius;
		float discriminant = b * b - 4.0f * a * c;
		if (discriminant < 0) {
			return -1.0f;
		} else {
			return (-b - (float) Math.sqrt(discriminant)) / (2.0f * a);
		}
	}

	public static Vec color(Ray r, Hitable world) {
		Optional<HitRecord> temp = world.hit(r, 0.0f, Float.MAX_VALUE);
		if (temp.isPresent()) {
			HitRecord hit_record = temp.get();
			return Vec.mul(0.5f, new Vec(hit_record.normal.x() + 1.0f,
					hit_record.normal.y() + 1.0f, hit_record.normal.z() + 1.0f));
		}
		Vec unit_direction = Vec.unit_vector(r.direction());
		float t = 0.5f * (unit_direction.y() + 1.0f);
		return Vec.add(Vec.mul((1.0f - t), new Vec(1.0f, 1.0f, 1.0f)), Vec.mul(t, new Vec(0.5f, 0.7f, 1.0f)));
	}

	public static void main(String[] args) {
		int nx = 200;
		int ny = 100;
		Vec lower_left_corner = new Vec(-2.0f, -1.0f, -1.0f);
		Vec horizontal = new Vec(4.0f, 0.0f, 0.0f);
		Vec vertical = new Vec(0.0f, 2.0f, 0.0f);
		Vec origin = new Vec(0.0f, 0.0f, 0.0f);

		List<Hitable> list = new ArrayList<>();
		list.add(new Sphere(new Vec(0.0f, 0.0f, -1.0f), 0.5f));
		list.add(new Sphere(new Vec(0.0f, -100.5f, -1.0f), 100.f));
		HitableList world = new HitableList(list);

		System.out.println("P3\n" + nx + " " + ny + "\n255\n");
		for (int j = ny - 1; j >= 0; j--) {
			for (int i = 0; i < nx; i++) {
				float u = (float) i / (float) nx;
				float v = (float) j / (float) ny;
				Ray r = new Ray(origin,
						Vec.add(lower_left_corner, Vec.add(Vec.mul(u, horizontal), Vec.mul(v, vertical))));
				Vec col = color(r, world);
				int ir = (int) (255.99 * col.get(0));
				int ig = (int) (255.99 * col.get(1));
				int ib = (int) (255.99 * col.get(2));
				System.out.println("" + ir + " " + ig + " " + ib);
			}
		}
	}

}
