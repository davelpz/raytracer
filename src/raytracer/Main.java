package raytracer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import raytracer.Vec;

public class Main {

	public static Vec random_in_unit_square() {
		Vec p;
		do {
			p = Vec.sub(Vec.mul(2.0f, new Vec(Math.random(), Math.random(), Math.random())), new Vec(1.0f, 1.0f, 1.0f));
		} while (p.squared_length() >= 1.0);

		return p;
	}

	public static Vec color(Ray r, Hitable world) {
		Optional<HitRecord> temp = world.hit(r, 0.001f, Float.MAX_VALUE);

		return temp.map(rec -> {
			Vec target = Vec.add(Vec.add(rec.p, rec.normal), random_in_unit_square());
			return Vec.mul(0.5f, color(new Ray(rec.p, Vec.sub(target, rec.p)), world));

		}).orElseGet(() -> {
			Vec unit_direction = Vec.unit_vector(r.direction());
			float t = 0.5f * (unit_direction.y() + 1.0f);
			return Vec.add(Vec.mul((1.0f - t), new Vec(1.0f, 1.0f, 1.0f)), Vec.mul(t, new Vec(0.5f, 0.7f, 1.0f)));
		});
	}

	public static void main(String[] args) throws IOException {
		BufferedWriter output = new BufferedWriter(new FileWriter("output.ppm"));
		int nx = 400;
		int ny = 200;
		int ns = 100;

		List<Hitable> list = new ArrayList<>();
		list.add(new Sphere(new Vec(0.0f, 0.0f, -1.0f), 0.5f));
		list.add(new Sphere(new Vec(0.0f, -100.5f, -1.0f), 100.f));
		HitableList world = new HitableList(list);

		Camera cam = new Camera();

		output.write("P3\n" + nx + " " + ny + "\n255\n");
		for (int j = ny - 1; j >= 0; j--) {
			for (int i = 0; i < nx; i++) {
				Vec col = new Vec();
				for (int s = 0; s < ns; s++) {
					float u = ((float) i + (float) Math.random()) / (float) nx;
					float v = ((float) j + (float) Math.random()) / (float) ny;
					Ray r = cam.get_ray(u, v);
					col.add(color(r, world));
				}
				col.div((float) ns);
				col.sqrt();
				int ir = (int) (255.99 * col.get(0));
				int ig = (int) (255.99 * col.get(1));
				int ib = (int) (255.99 * col.get(2));
				output.write(ir + " " + ig + " " + ib + "\n");
			}
		}

		output.close();
	}

}
