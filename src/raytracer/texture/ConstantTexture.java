package raytracer.texture;

import raytracer.Vec;

public class ConstantTexture implements Texture {
	Vec color;

	public ConstantTexture(Vec c) {
		color = c;
	}

	@Override
	public Vec value(float u, float v, Vec p) {
		return color;
	}

}
