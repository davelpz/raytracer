package raytracer.hitable;

public class HitResult {
	public boolean hit_anything;
	public HitRecord hit_record;

	public HitResult() {
		hit_anything = false;
		hit_record = new HitRecord();
	}

	public HitResult(boolean ha, HitRecord hr) {
		this.hit_anything = ha;
		this.hit_record = hr;
	}
}
