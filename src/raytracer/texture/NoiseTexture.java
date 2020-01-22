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
		return Vec.mul(new Vec(1, 1, 1), noise.noise(Vec.mul(scale, p)));
	}

}
