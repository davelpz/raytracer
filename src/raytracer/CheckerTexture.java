package raytracer;

public class CheckerTexture implements Texture {
	Texture odd;
	Texture even;

	public CheckerTexture(Texture t0, Texture t1) {
		even = t0;
		odd = t1;
	}

	@Override
	public Vec value(float u, float v, Vec p) {
		float sines = (float) (Math.sin(10 * p.x()) * Math.sin(10 * p.y()) * Math.sin(10 * p.z()));
		if (sines < 0) {
			return odd.value(u, v, p);
		} else {
			return even.value(u, v, p);
		}
	}

}
