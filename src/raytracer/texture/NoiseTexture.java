package raytracer.texture;

import raytracer.Vec;
import raytracer.noise.Perlin;

public class NoiseTexture implements Texture {
	Perlin noise = new Perlin();
	float scale;

	public NoiseTexture(float sc) {
		scale = sc;
	}

	@Override
	public Vec value(float u, float v, Vec p) {
		// return Vec.mul(new Vec(1, 1, 1), turb(Vec.mul(scale, p)));
		// return Vec.mul(new Vec(1, 1, 1), 0.5f * (1 + turb(Vec.mul(scale, p))));
		return Vec.mul(new Vec(1, 1, 1),
				0.5f * (1 + (float) Math.sin(scale * p.z() + 10.0f * turb(p))));
	}

	float turb(final Vec p) {
		return turb(p, 7);
	}

	float turb(final Vec p, int depth) {
		float accum = 0;
		Vec temp_p = p;
		float weight = 1.0f;
		for (int i = 0; i < depth; i++) {
			accum += weight * noise.noise(temp_p);
			weight *= 0.5;
			temp_p = Vec.mul(temp_p, 2);
		}

		return Math.abs(accum);
	}
}
