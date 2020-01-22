package raytracer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import raytracer.Vec;
import raytracer.hitable.BvhNode;
import raytracer.hitable.HitRecord;
import raytracer.hitable.Hitable;
import raytracer.hitable.MovingSphere;
import raytracer.hitable.Sphere;
import raytracer.material.Dielectric;
import raytracer.material.Lambertian;
import raytracer.material.Metal;
import raytracer.material.ScatterResult;
import raytracer.texture.CheckerTexture;
import raytracer.texture.ConstantTexture;
import raytracer.texture.NoiseTexture;
import raytracer.texture.Texture;

public class Main {

	static class SetupResult {
		public Camera camera;
		public List<Hitable> world;

		public SetupResult(Camera camera, List<Hitable> world) {
			this.camera = camera;
			this.world = world;
		}
	}

	static class Ticker {
		float max_count;
		int count;
		int fiveper;
		long elapsed_start;
		long time_start;

		public Ticker(int max) {
			max_count = max;
			fiveper = (int) (max_count * 0.05f);
			elapsed_start = System.currentTimeMillis();
			time_start = elapsed_start;
			System.out.println("" + max_count + " pixels");
		}

		public void tick() {
			count = count + 1;
			if (count % fiveper == 0) {
				int percent = (int) (((count / max_count) * 10000.0f) / 100.0f);
				long elapsed = (System.currentTimeMillis() - elapsed_start) / fiveper;
				System.out.print("\r" + percent + "%    " + elapsed + "ms per pixel");
				elapsed_start = System.currentTimeMillis();
			}
		}

		public void end() {
			long elapsed = (System.currentTimeMillis() - time_start) / 1000;
			System.out.print("\r" + elapsed + " seconds    ");
		}
	}

	public static SetupResult setup(int nx, int ny) {
		List<Hitable> list = new ArrayList<>();
		list.add(new Sphere(new Vec(0.0f, 0.0f, -1.0f), 0.5f,
				new Lambertian(new ConstantTexture(new Vec(0.1, 0.2, 0.5)))));
		list.add(new Sphere(new Vec(0.0f, -100.5f, -1.0f), 100.f,
				new Lambertian(new ConstantTexture(new Vec(0.8, 0.8, 0.0)))));
		list.add(new Sphere(new Vec(1.0f, 0.0f, -1.0f), 0.5f, new Metal(new Vec(0.8, 0.6, 0.2), 0.2f)));
		list.add(new Sphere(new Vec(-1.0f, 0.0f, -1.0f), 0.5f, new Dielectric(1.5f)));
		list.add(new Sphere(new Vec(-1.0f, 0.0f, -1.0f), -0.45f, new Dielectric(1.5f)));

		Vec lookfrom = new Vec(3, 3, 2);
		Vec lookat = new Vec(0, 0, -1);
		Vec vup = new Vec(0, 1, 0);
		float dist_to_focus = (Vec.sub(lookfrom, lookat)).length();
		float aperture = 2.0f;
		Camera cam = new Camera(lookfrom, lookat, vup, 20, (float) nx / (float) ny, aperture, dist_to_focus, 0.0f,
				1.0f);

		return new SetupResult(cam, list);
	}

	public static SetupResult setup2(int nx, int ny) {
		float R = (float) Math.cos(Math.PI / 4);
		List<Hitable> list = new ArrayList<>();
		list.add(new Sphere(new Vec(-R, 0.0f, -1.0f), R, new Lambertian(new ConstantTexture(new Vec(0.0, 0.0, 1)))));
		list.add(new Sphere(new Vec(R, 0.0f, -1.0f), R, new Lambertian(new ConstantTexture(new Vec(1.0, 0.0, 0)))));

		Vec lookfrom = new Vec(3, 3, 2);
		Vec lookat = new Vec(0, 0, -1);
		Vec vup = new Vec(0, 1, 0);
		float dist_to_focus = (Vec.sub(lookfrom, lookat)).length();
		float aperture = 2.0f;
		Camera cam = new Camera(lookfrom, lookat, vup, 20, (float) nx / (float) ny, aperture, dist_to_focus, 0.0f,
				1.0f);

		return new SetupResult(cam, list);
	}

	public static SetupResult random_scene(int nx, int ny) {
		int n = 500;
		List<Hitable> list = new ArrayList<>();
		CheckerTexture checker = new CheckerTexture(new ConstantTexture(new Vec(0.2, 0.3, 0.1)),
				new ConstantTexture(new Vec(0.9, 0.9, 0.9)));
		list.add(new Sphere(new Vec(0, -1000.0f, 0.0f), 1000, new Lambertian(checker)));

		int i = 1;
		for (int a = -11; a < 11; a++) {
			for (int b = -11; b < 11; b++) {
				float choose_mat = (float) Math.random();
				Vec center = new Vec(a + 0.9 * Math.random(), 0.2, b + 0.9 * Math.random());
				if (Vec.sub(center, new Vec(4, 0.2, 0)).length() > 0.9) {
					if (choose_mat < 0.7) {
						list.add(new MovingSphere(center, Vec.add(center, new Vec(0, 0.5 * Math.random(), 0)), 0.0f,
								1.0f, 0.2f, new Lambertian(new ConstantTexture(new Vec(Math.random() * Math.random(),
										Math.random() * Math.random(), Math.random() * Math.random())))));
					}
				} else if (choose_mat < 0.85) {
					list.add(new Sphere(center, 0.2f, new Metal(
							new Vec(0.5 * (1 + Math.random()), 0.5 * (1 + Math.random()), 0.5 * (1 + Math.random())),
							0.5f * (float) Math.random())));
				} else {
					list.add(new Sphere(center, 0.2f, new Dielectric(1.5f)));
				}
			}
		}

		list.add(new Sphere(new Vec(0, 1, 0), 1.0f, new Dielectric(1.5f)));
		list.add(new Sphere(new Vec(-4, 1, 0), 1.0f, new Lambertian(new ConstantTexture(new Vec(0.4, 0.2, 0.1)))));
		list.add(new Sphere(new Vec(4, 1, 0), 1.0f, new Metal(new Vec(0.7, 0.6, 0.5), 0.0f)));

		Vec lookfrom = new Vec(13, 2, 3);
		Vec lookat = new Vec(0, 0, 0);
		Vec vup = new Vec(0, 1, 0);
		float dist_to_focus = 10.0f;// (Vec.sub(lookfrom, lookat)).length();
		float aperture = 0.1f;
		Camera cam = new Camera(lookfrom, lookat, vup, 20, (float) nx / (float) ny, aperture, dist_to_focus, 0.0f,
				1.0f);
		return new SetupResult(cam, list);
	}

	public static SetupResult two_spheres(int nx, int ny) {
		List<Hitable> list = new ArrayList<>();
		CheckerTexture checker = new CheckerTexture(new ConstantTexture(new Vec(0.2, 0.3, 0.1)),
				new ConstantTexture(new Vec(0.9, 0.9, 0.9)));
		list.add(new Sphere(new Vec(0, -10.0f, 0.0f), 10, new Lambertian(checker)));
		list.add(new Sphere(new Vec(0, 10.0f, 0.0f), 10, new Lambertian(checker)));

		Vec lookfrom = new Vec(13, 2, 3);
		Vec lookat = new Vec(0, 0, 0);
		Vec vup = new Vec(0, 1, 0);
		float dist_to_focus = 10.0f;// (Vec.sub(lookfrom, lookat)).length();
		float aperture = 0.0f;
		Camera cam = new Camera(lookfrom, lookat, vup, 20, (float) nx / (float) ny, aperture, dist_to_focus, 0.0f,
				1.0f);
		return new SetupResult(cam, list);
	}

	public static SetupResult two_perlin_spheres(int nx, int ny) {
		List<Hitable> list = new ArrayList<>();
		Texture pertext = new NoiseTexture();

		list.add(new Sphere(new Vec(0, -1000.0f, 0.0f), 1000, new Lambertian(pertext)));
		list.add(new Sphere(new Vec(0, 2.0f, 0.0f), 2, new Lambertian(pertext)));

		Vec lookfrom = new Vec(13, 2, 3);
		Vec lookat = new Vec(0, 0, 0);
		Vec vup = new Vec(0, 1, 0);
		float dist_to_focus = 10.0f;// (Vec.sub(lookfrom, lookat)).length();
		float aperture = 0.0f;
		Camera cam = new Camera(lookfrom, lookat, vup, 20, (float) nx / (float) ny, aperture, dist_to_focus, 0.0f,
				1.0f);
		return new SetupResult(cam, list);
	}

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

	public static class Pixel {
		public int x;
		public int y;

		public Pixel(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public String toString() {
			return "Pixel(" + x + "," + y + ")";
		}
	}

	public static class RayCastResult {
		public Pixel pixel;
		public Vec color;

		public RayCastResult(Pixel p, Vec c) {
			this.pixel = p;
			this.color = c;
		}
	}

	public static Stream<Pixel> genStream(int nx, int ny) {
		Stream<Pixel> stream = Stream.iterate(new Pixel(0, ny - 1), p -> {
			if (p.x == 0 && p.y == -1) {
				return false;
			} else {
				return true;
			}
		}, p -> {
			int tx = p.x;
			int ty = p.y;

			if (tx < nx - 1) {
				tx++;
			} else {
				tx = 0;
				ty--;
			}

			return new Pixel(tx, ty);
		});

		return stream;
	}

	public static void main(String[] args) throws IOException {
		BufferedWriter output = new BufferedWriter(new FileWriter("output.ppm"));
		int nx = 400;
		int ny = 200;
		int ns = 100;

		Ticker ticker = new Ticker(nx * ny);

		SetupResult res = two_perlin_spheres(nx, ny);
		// HitableList world = new HitableList(res.world);
		BvhNode world = new BvhNode(res.world, 0, 1);
		Camera cam = res.camera;

		output.write("P3\n" + nx + " " + ny + "\n255\n");
		Vec[][] buffer = new Vec[nx][ny];

		genStream(nx, ny).parallel().map(p -> {
			int i = p.x;
			int j = p.y;
			Vec col = new Vec();
			for (int s = 0; s < ns; s++) {
				float u = ((float) i + (float) Math.random()) / (float) nx;
				float v = ((float) j + (float) Math.random()) / (float) ny;
				Ray r = cam.get_ray(u, v);
				col.add(color(r, world, 0));
			}
			col.div((float) ns);
			col.sqrt();
			return new RayCastResult(p, col);
		}).forEach(r -> {
			Vec col = r.color;
			Pixel p = r.pixel;
			buffer[p.x][p.y] = col;
			ticker.tick();
		});

		for (int j = ny - 1; j >= 0; j--) {
			for (int i = 0; i < nx; i++) {
				Vec col = buffer[i][j];
				int ir = (int) (255.99 * col.get(0));
				int ig = (int) (255.99 * col.get(1));
				int ib = (int) (255.99 * col.get(2));
				output.write(ir + " " + ig + " " + ib + "\n");
			}
		}

		if (false) {
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
				}
			}
		}

		output.close();
		ticker.end();
	}

}
