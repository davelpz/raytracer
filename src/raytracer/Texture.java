package raytracer;

public interface Texture {
	public Vec value(float u, float v, final Vec p);
}
