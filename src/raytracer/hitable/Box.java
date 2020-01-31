package raytracer.hitable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import raytracer.Ray;
import raytracer.Vec;
import raytracer.hitable.transforms.FlipNormals;
import raytracer.material.Material;

public class Box implements Hitable {
	Vec pmin, pmax;
	HitableList list;

	public Box(final Vec p0, final Vec p1, Material ptr) {
		pmin = p0;
		pmax = p1;
		List<Hitable> tlist = new ArrayList<>();
		tlist.add(new XYRect(p0.x(), p1.x(), p0.y(), p1.y(), p1.z(), ptr));
		tlist.add(new FlipNormals(new XYRect(p0.x(), p1.x(), p0.y(), p1.y(), p0.z(), ptr)));
		tlist.add(new XZRect(p0.x(), p1.x(), p0.z(), p1.z(), p1.y(), ptr));
		tlist.add(new FlipNormals(new XZRect(p0.x(), p1.x(), p0.z(), p1.z(), p0.y(), ptr)));
		tlist.add(new YZRect(p0.y(), p1.y(), p0.z(), p1.z(), p1.x(), ptr));
		tlist.add(new FlipNormals(new YZRect(p0.y(), p1.y(), p0.z(), p1.z(), p0.x(), ptr)));
		list = new HitableList(tlist);
	}

	@Override
	public Optional<HitRecord> hit(Ray r, float t_min, float t_max) {
		return list.hit(r, t_min, t_max);
	}

	@Override
	public Optional<Aabb> bounding_box(float t0, float t1) {
		return Optional.of(new Aabb(pmin, pmax));
	}

}
