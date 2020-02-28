package raytracer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import raytracer.hitable.Box;
import raytracer.hitable.BvhNode;
import raytracer.hitable.ConstantMedium;
import raytracer.hitable.HitRecord;
import raytracer.hitable.Hitable;
import raytracer.hitable.MovingSphere;
import raytracer.hitable.Sphere;
import raytracer.hitable.XYRect;
import raytracer.hitable.XZRect;
import raytracer.hitable.YZRect;
import raytracer.hitable.transforms.FlipNormals;
import raytracer.hitable.transforms.RotateY;
import raytracer.hitable.transforms.Translate;
import raytracer.image.PPMImage;
import raytracer.material.Dielectric;
import raytracer.material.DiffuseLight;
import raytracer.material.Lambertian;
import raytracer.material.Material;
import raytracer.material.Metal;
import raytracer.material.ScatterResult;
import raytracer.texture.CheckerTexture;
import raytracer.texture.ConstantTexture;
import raytracer.texture.ImageTexture;
import raytracer.texture.NoiseTexture;
import raytracer.texture.Texture;
import raytracer.util.Pixel;

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

		public Ticker(int max, int ns) {
			max_count = max;
			fiveper = (int) (max_count * 0.05f);
			elapsed_start = System.currentTimeMillis();
			time_start = elapsed_start;
			System.out.format("%s pixels, %.0f light rays", max_count, max_count * ns);
		}

		public void tick() {
			count = count + 1;
			if (count % fiveper == 0) {
				int percent = (int) (((count / max_count) * 10000.0f) / 100.0f);
				long elapsed_total_time = System.currentTimeMillis() - time_start;
				long estimated_total_time = (long) (elapsed_total_time * (100.0f / percent));
				long estimated_time_left = estimated_total_time - elapsed_total_time;
				long elapsed_time = System.currentTimeMillis() - elapsed_start;
				long elapsed_pixel = elapsed_time / fiveper;
				System.out.format("\r%2d%%\t%2dms per pixel\t%6dms\t%6dms", percent, elapsed_pixel, elapsed_total_time,
						estimated_time_left);
				elapsed_start = System.currentTimeMillis();
			}
		}

		public void reportElapsed() {
			long elapsed = (System.currentTimeMillis() - time_start) / 1000;
			System.out.print("\r" + elapsed + " seconds    ");
		}

		public void end() {
			reportElapsed();
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
		List<Hitable> list = new ArrayList<>();
		CheckerTexture checker = new CheckerTexture(new ConstantTexture(new Vec(0.2, 0.3, 0.1)),
				new ConstantTexture(new Vec(0.9, 0.9, 0.9)));
		list.add(new Sphere(new Vec(0, -1000.0f, 0.0f), 1000, new Lambertian(checker)));

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
		Texture pertext = new NoiseTexture(4);

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

	public static SetupResult earth(int nx, int ny) throws IOException {
		List<Hitable> list = new ArrayList<>();
		PPMImage image = PPMImage.fromFile("earthmap.ppm");
		Texture texture = new ImageTexture(image);

		list.add(new Sphere(new Vec(0, 0.0f, 0.0f), 4.5f, new Lambertian(texture)));

		Vec lookfrom = new Vec(0, 0, -28);
		Vec lookat = new Vec(0, 0, 0);
		Vec vup = new Vec(0, 1, 0);
		float dist_to_focus = 10.0f;// (Vec.sub(lookfrom, lookat)).length();
		float aperture = 0.0f;
		Camera cam = new Camera(lookfrom, lookat, vup, 20, (float) nx / (float) ny, aperture, dist_to_focus, 0.0f,
				1.0f);
		return new SetupResult(cam, list);
	}

	public static SetupResult simple_light(int nx, int ny) throws IOException {
		List<Hitable> list = new ArrayList<>();
		Texture pertext = new NoiseTexture(4);

		list.add(new Sphere(new Vec(0, -1000.0f, 0.0f), 1000f, new Lambertian(pertext)));
		list.add(new Sphere(new Vec(0, 2.0f, 0.0f), 2f, new Lambertian(pertext)));
		list.add(new Sphere(new Vec(0, 7.0f, 0.0f), 2f, new DiffuseLight(new ConstantTexture(new Vec(4, 4, 4)))));
		list.add(new XYRect(3, 5, 1, 3, -2, new DiffuseLight(new ConstantTexture(new Vec(4, 4, 4)))));

		Vec lookfrom = new Vec(20, 5, 14);
		Vec lookat = new Vec(0, 0, 0);
		Vec vup = new Vec(0, 1, 0);
		float dist_to_focus = 10.0f;// (Vec.sub(lookfrom, lookat)).length();
		float aperture = 0.0f;
		Camera cam = new Camera(lookfrom, lookat, vup, 20, (float) nx / (float) ny, aperture, dist_to_focus, 0.0f,
				1.0f);
		return new SetupResult(cam, list);
	}

	public static SetupResult cornell_box(int nx, int ny) throws IOException {
		List<Hitable> list = new ArrayList<>();
		Material red = new Lambertian(new ConstantTexture(new Vec(0.65, 0.05, 0.05)));
		Material white = new Lambertian(new ConstantTexture(new Vec(0.73, 0.73, 0.73)));
		Material green = new Lambertian(new ConstantTexture(new Vec(0.12, 0.45, 0.15)));
		Material light = new DiffuseLight(new ConstantTexture(new Vec(3, 3, 3)));

		list.add(new FlipNormals(new YZRect(0, 555, 0, 555, 555, green)));
		list.add(new YZRect(0, 555, 0, 555, 0, red));
		list.add(new XZRect(100, 455, 100, 455, 554, light));
		list.add(new FlipNormals(new XZRect(0, 555, 0, 555, 555, white)));
		list.add(new XZRect(0, 555, 0, 555, 0, white));
		list.add(new FlipNormals(new XYRect(0, 555, 0, 555, 555, white)));
		list.add(new Translate(new RotateY(new Box(new Vec(0, 0, 0), new Vec(165, 165, 165), white), -18),
				new Vec(130, 0, 65)));
		list.add(new Translate(new RotateY(new Box(new Vec(0, 0, 0), new Vec(165, 330, 165), white), 15),
				new Vec(265, 0, 295)));

		Vec lookfrom = new Vec(278, 278, -800);
		Vec lookat = new Vec(278, 278, 0);
		Vec vup = new Vec(0, 1, 0);
		float dist_to_focus = 10.0f;// (Vec.sub(lookfrom, lookat)).length();
		float aperture = 0.0f;
		float vfov = 40.0f;
		Camera cam = new Camera(lookfrom, lookat, vup, vfov, (float) nx / (float) ny, aperture, dist_to_focus, 0.0f,
				1.0f);
		return new SetupResult(cam, list);
	}

	public static SetupResult cornell_smoke(int nx, int ny) throws IOException {
		List<Hitable> list = new ArrayList<>();
		Material red = new Lambertian(new ConstantTexture(new Vec(0.65, 0.05, 0.05)));
		Material white = new Lambertian(new ConstantTexture(new Vec(0.73, 0.73, 0.73)));
		Material green = new Lambertian(new ConstantTexture(new Vec(0.12, 0.45, 0.15)));
		Material light = new DiffuseLight(new ConstantTexture(new Vec(4, 4, 4)));

		list.add(new FlipNormals(new YZRect(0, 555, 0, 555, 555, green)));
		list.add(new YZRect(0, 555, 0, 555, 0, red));
		list.add(new XZRect(100, 455, 100, 455, 554, light));
		list.add(new FlipNormals(new XZRect(0, 555, 0, 555, 555, white)));
		list.add(new XZRect(0, 555, 0, 555, 0, white));
		list.add(new FlipNormals(new XYRect(0, 555, 0, 555, 555, white)));

		Hitable b1 = new Translate(new RotateY(new Box(new Vec(0, 0, 0), new Vec(165, 165, 165), white), -18),
				new Vec(130, 0, 65));
		Hitable b2 = new Translate(new RotateY(new Box(new Vec(0, 0, 0), new Vec(165, 330, 165), white), 15),
				new Vec(265, 0, 295));
		list.add(new ConstantMedium(b1, 0.01f, new ConstantTexture(new Vec(1, 1, 1))));
		list.add(new ConstantMedium(b2, 0.01f, new ConstantTexture(new Vec(0, 0, 0))));

		Vec lookfrom = new Vec(278, 278, -800);
		Vec lookat = new Vec(278, 278, 0);
		Vec vup = new Vec(0, 1, 0);
		float dist_to_focus = 10.0f;// (Vec.sub(lookfrom, lookat)).length();
		float aperture = 0.0f;
		float vfov = 40.0f;
		Camera cam = new Camera(lookfrom, lookat, vup, vfov, (float) nx / (float) ny, aperture, dist_to_focus, 0.0f,
				1.0f);
		return new SetupResult(cam, list);
	}

	public static SetupResult final_scene(int nx, int ny) throws IOException {
		List<Hitable> list = new ArrayList<>();
		List<Hitable> boxlist = new ArrayList<>();
		List<Hitable> boxlist2 = new ArrayList<>();
		Material white = new Lambertian(new ConstantTexture(new Vec(0.73, 0.73, 0.73)));
		Material ground = new Lambertian(new ConstantTexture(new Vec(0.48, 0.83, 0.53)));
		Material light = new DiffuseLight(new ConstantTexture(new Vec(3, 3, 3)));

		for (var i = 0; i < 20; i++) {
			for (var j = 0; j < 20; j++) {
				float w = 100;
				float x0 = -1000 + i * w;
				float z0 = -1000 + j * w;
				float y0 = 0;
				float x1 = x0 + w;
				float y1 = 100.0f * ((float) Math.random() + 0.01f);
				float z1 = z0 + w;
				boxlist.add(new Box(new Vec(x0, y0, z0), new Vec(x1, y1, z1), ground));
			}

		}
		list.add(new BvhNode(boxlist, 0, 1));

		list.add(new XZRect(150, 405, 150, 405, 554, light));

		Vec center = new Vec(400, 400, 200);
		list.add(new MovingSphere(center, Vec.add(center, new Vec(30, 0, 0)), 0, 1, 50,
				new Lambertian(new ConstantTexture(new Vec(0.7, 0.3, 0.1)))));
		list.add(new Sphere(new Vec(260, 150, 45), 50, new Dielectric(1.5f)));
		list.add(new Sphere(new Vec(0, 150, 145), 50, new Metal(new Vec(0.8, 0.8, 0.9), 10.0f)));

		Hitable boundary = new Sphere(new Vec(360, 150, 145), 70, new Dielectric(1.5f));
		list.add(boundary);

		list.add(new ConstantMedium(boundary, 0.2f, new ConstantTexture(new Vec(0.2, 0.4, 0.9))));

		boundary = new Sphere(new Vec(0, 0, 0), 5000, new Dielectric(1.5f));
		list.add(new ConstantMedium(boundary, 0.0001f, new ConstantTexture(new Vec(1.0, 1.0, 1.0))));

		PPMImage image = PPMImage.fromFile("earthmap.ppm");
		Texture texture = new ImageTexture(image);
		Material emat = new Lambertian(texture);
		list.add(new Sphere(new Vec(400, 200, 400), 100, emat));

		Texture pertext = new NoiseTexture(1.1f);
		list.add(new Sphere(new Vec(220, 280, 300), 80, new Lambertian(pertext)));

		for (int j = 0; j < 1000; j++) {
			boxlist2.add(new Sphere(new Vec(165 * Math.random(), 165 * Math.random(), 165 * Math.random()), 10, white));
		}
		list.add(new Translate(new RotateY(new BvhNode(boxlist2, 0.0f, 1.0f), 15), new Vec(-100, 270, 395)));

		Vec lookfrom = new Vec(278, 278, -800);
		Vec lookat = new Vec(278, 278, 0);
		Vec vup = new Vec(0, 1, 0);
		float dist_to_focus = 10.0f;// (Vec.sub(lookfrom, lookat)).length();
		float aperture = 0.0f;
		float vfov = 40.0f;
		Camera cam = new Camera(lookfrom, lookat, vup, vfov, (float) nx / (float) ny, aperture, dist_to_focus, 0.0f,
				1.0f);
		return new SetupResult(cam, list);
	}

	public static Vec color(final Ray r, final Hitable world, int depth) {
		Optional<HitRecord> temp = world.hit(r, 0.001f, Float.MAX_VALUE);

		return temp.map(rec -> {
			Vec emitted = rec.mat.emitted(rec.u, rec.v, rec.p);
			Optional<ScatterResult> result = rec.mat.scatter(r, rec);
			if (depth < 50 && result.isPresent()) {
				ScatterResult sr = result.get();
				return Vec.add(emitted, Vec.mul(sr.attenuation, color(sr.scattered, world, depth + 1)));
				// return sr.attenuation;
			} else {
				return emitted;
			}

		}).orElseGet(() -> {
			return new Vec();
			/*
			 * Vec unit_direction = Vec.unit_vector(r.direction()); float t = 0.5f *
			 * (unit_direction.y() + 1.0f); return Vec.add(Vec.mul((1.0f - t), new Vec(1.0f,
			 * 1.0f, 1.0f)), Vec.mul(t, new Vec(0.5f, 0.7f, 1.0f)));
			 */
		});
	}

	public static void main(String[] args) throws IOException {
		BufferedWriter output = new BufferedWriter(new FileWriter("output.ppm"));
		int nx = 300;
		int ny = 300;
		int ns = 200;

		Ticker ticker = new Ticker(nx * ny, ns);

		SetupResult res = final_scene(nx, ny);

		// HitableList world = new HitableList(res.world);
		BvhNode world = new BvhNode(res.world, 0, 1);
		Camera cam = res.camera;

		Vec[][] buffer = new Vec[nx][ny];

		Pixel.genStream(nx, ny).parallel().forEach(p -> {
			int i = p.x;
			int j = p.y;

			Vec col = IntStream.range(0, ns).mapToObj(_p -> {
				float u = ((float) i + (float) Math.random()) / (float) nx;
				float v = ((float) j + (float) Math.random()) / (float) ny;
				Ray r = cam.get_ray(u, v);
				return color(r, world, 0);
			}).reduce(new Vec(), (prev, next) -> {
				return prev.add(next);
			}).div((float) ns).sqrt();

			buffer[p.x][p.y] = col;
			ticker.tick();
		});
		// ticker.reportElapsed();

		output.write("P3\n" + nx + " " + ny + "\n255\n");

		for (int j = ny - 1; j >= 0; j--) {
			for (int i = 0; i < nx; i++) {
				Vec col = buffer[i][j];
				col.clamp(0.0f, 0.999999f);
				int ir = (int) (255.99 * col.get(0));
				int ig = (int) (255.99 * col.get(1));
				int ib = (int) (255.99 * col.get(2));
				output.write(ir + " " + ig + " " + ib + "\n");
			}
		}

		output.close();
		ticker.end();
	}

}
