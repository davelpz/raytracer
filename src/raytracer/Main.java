package raytracer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import raytracer.Vec;

public class Main {

	public static Vec color(Ray r, Hitable world, int depth) {
		Optional<HitRecord> temp = world.hit(r, 0.001f, Float.MAX_VALUE);

		return temp.map(rec -> {
			Optional<ScatterResult> result = rec.mat.scatter(r, rec);
			if (depth < 50 && result.isPresent()) {
				ScatterResult sr = result.get();
				return Vec.mul(sr.attenuation, color(sr.scattered, world, depth + 1));
			} else {
				return new Vec();
			}

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
		list.add(new Sphere(new Vec(0.0f, 0.0f, -1.0f), 0.5f, new Lambertian(new Vec(0.8, 0.3, 0.3))));
		list.add(new Sphere(new Vec(0.0f, -100.5f, -1.0f), 100.f, new Lambertian(new Vec(0.8, 0.8, 0.0))));
		list.add(new Sphere(new Vec(1.0f, 0.0f, -1.0f), 0.5f, new Metal(new Vec(0.8, 0.6, 0.2), 1.0f)));
		list.add(new Sphere(new Vec(-1.0f, 0.0f, -1.0f), 0.5f, new Metal(new Vec(0.8, 0.8, 0.8), 0.3f)));
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
					col.add(color(r, world, 0));
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
