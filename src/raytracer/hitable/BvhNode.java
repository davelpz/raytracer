package raytracer.hitable;

import java.util.List;
import java.util.Optional;

import raytracer.Ray;

public class BvhNode implements Hitable {
	Hitable left;
	Hitable right;
	Aabb box;

	public BvhNode(List<Hitable> l, float time0, float time1) {
		int axis = (int) (3 * Math.random());
		if (axis == 0) {
			l.sort((ah, bh) -> {
				Optional<Aabb> box_left_opt = ah.bounding_box(0, 0);
				Optional<Aabb> box_right_opt = bh.bounding_box(0, 0);
				if (box_left_opt.isEmpty() || box_right_opt.isEmpty()) {
					System.err.println("no bounding box in bvh_node constructor");
					throw new RuntimeException("no bounding box in bvh_node constructor");
				}
				Aabb box_left = box_left_opt.get();
				Aabb box_right = box_right_opt.get();
				if (box_left.min().x() - box_right.min().x() < 0.0) {
					return -1;
				} else {
					return 1;
				}
			});
		} else if (axis == 1) {
			l.sort((ah, bh) -> {
				Optional<Aabb> box_left_opt = ah.bounding_box(0, 0);
				Optional<Aabb> box_right_opt = bh.bounding_box(0, 0);
				if (box_left_opt.isEmpty() || box_right_opt.isEmpty()) {
					System.err.println("no bounding box in bvh_node constructor");
					throw new RuntimeException("no bounding box in bvh_node constructor");
				}
				Aabb box_left = box_left_opt.get();
				Aabb box_right = box_right_opt.get();
				if (box_left.min().y() - box_right.min().y() < 0.0) {
					return -1;
				} else {
					return 1;
				}
			});
		} else {
			l.sort((ah, bh) -> {
				Optional<Aabb> box_left_opt = ah.bounding_box(0, 0);
				Optional<Aabb> box_right_opt = bh.bounding_box(0, 0);
				if (box_left_opt.isEmpty() || box_right_opt.isEmpty()) {
					System.err.println("no bounding box in bvh_node constructor");
					throw new RuntimeException("no bounding box in bvh_node constructor");
				}
				Aabb box_left = box_left_opt.get();
				Aabb box_right = box_right_opt.get();
				if (box_left.min().z() - box_right.min().z() < 0.0) {
					return -1;
				} else {
					return 1;
				}
			});
		}

		if (l.size() == 1) {
			left = right = l.get(0);
		} else if (l.size() == 2) {
			left = l.get(0);
			right = l.get(1);
		} else {
			left = new BvhNode(l.subList(0, l.size() / 2), time0, time1);
			right = new BvhNode(l.subList(l.size() / 2, l.size()), time0, time1);
		}

		Optional<Aabb> box_left_opt = left.bounding_box(time0, time1);
		Optional<Aabb> box_right_opt = right.bounding_box(time0, time1);
		if (box_left_opt.isEmpty() || box_right_opt.isEmpty()) {
			System.err.println("no bounding box in bvh_node constructor");
			throw new RuntimeException("no bounding box in bvh_node constructor");
		}

		box = Aabb.surrounding_box(box_left_opt.get(), box_right_opt.get());
	}

	@Override
	public Optional<HitRecord> hit(Ray r, float t_min, float t_max) {
		if (box.hit(r, t_min, t_max)) {
			Optional<HitRecord> left_rec_opt = left.hit(r, t_min, t_max);
			Optional<HitRecord> right_rec_opt = right.hit(r, t_min, t_max);
			if (left_rec_opt.isPresent() && right_rec_opt.isPresent()) {
				HitRecord left_rec = left_rec_opt.get();
				HitRecord right_rec = right_rec_opt.get();
				if (left_rec.t < right_rec.t) {
					return Optional.of(left_rec);
				} else {
					return Optional.of(right_rec);
				}
			} else if (left_rec_opt.isPresent()) {
				return Optional.of(left_rec_opt.get());
			} else if (right_rec_opt.isPresent()) {
				return Optional.of(right_rec_opt.get());
			} else {
				return Optional.empty();
			}
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<Aabb> bounding_box(float t0, float t1) {
		return Optional.of(box);
	}

}
