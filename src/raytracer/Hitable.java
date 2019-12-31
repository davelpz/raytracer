package raytracer;

public interface Hitable {
	public HitResult hit(final Ray r, float t_min, float t_max);
}
