package raytracer;

import java.util.ArrayList;
import java.util.List;

public class HitableList implements Hitable {
	List<Hitable> list = new ArrayList<>();

	public HitableList() {
	}

	public HitableList(List<Hitable> l) {
		this.list = new ArrayList<>(l);
	}

	@Override
	public HitResult hit(Ray r, float t_min, float t_max) {
		HitResult hit_result = new HitResult();
		float closest_so_far = t_max;
		for (Hitable h : list) {
			HitResult temp_hit_result = h.hit(r, t_min, closest_so_far);
			if (temp_hit_result.hit_anything) {
				hit_result.hit_anything = true;
				closest_so_far = temp_hit_result.hit_record.t;
				hit_result.hit_record = temp_hit_result.hit_record;
			}
		}
		return hit_result;
	}
}
