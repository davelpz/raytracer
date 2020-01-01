package raytracer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HitableList implements Hitable {
	List<Hitable> list = new ArrayList<>();

	public HitableList() {
	}

	public HitableList(List<Hitable> l) {
		this.list = new ArrayList<>(l);
	}

	@Override
	public Optional<HitRecord> hit(Ray r, float t_min, float t_max) {
		HitRecord hit_record = new HitRecord();
		float closest_so_far = t_max;
		for (Hitable h : list) {
			Optional<HitRecord> temp = h.hit(r, t_min, closest_so_far);
			if (temp.isPresent()) {
				HitRecord temp_hit_record = temp.get();
				closest_so_far = temp_hit_record.t;
				hit_record = temp_hit_record;
			}
		}
		return Optional.of(hit_record);
	}
}
