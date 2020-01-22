package raytracer.hitable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import raytracer.Ray;

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
		boolean hit_anything = false;
		float closest_so_far = t_max;
		for (Hitable h : list) {
			Optional<HitRecord> temp = h.hit(r, t_min, closest_so_far);
			if (temp.isPresent()) {
				HitRecord temp_hit_record = temp.get();
				closest_so_far = temp_hit_record.t;
				hit_record = temp_hit_record;
				hit_anything = true;
			}
		}

		if (hit_anything) {
			return Optional.of(hit_record);
		} else {
			return Optional.empty();
		}
	}

	public Optional<Aabb> bounding_box(float t0, float t1) {
		if (list.size() < 1)
			return Optional.empty();
		
		Aabb temp_box;
		Optional<Aabb> temp_box_opt = list.get(0).bounding_box(t0, t1);
		
		if (temp_box_opt.isEmpty()) {
			return Optional.empty();
		} else {
			temp_box = temp_box_opt.get();
		}
		
		for (int i = 1; i < list.size(); i++) {
			temp_box_opt = list.get(i).bounding_box(t0, t1);
			if (temp_box_opt.isEmpty()) {
				return Optional.empty();
			} else {
				temp_box = Aabb.surrounding_box(temp_box_opt.get(), temp_box);
			}
		}

		return Optional.of(temp_box);
	}
}
