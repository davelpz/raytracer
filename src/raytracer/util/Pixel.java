package raytracer.util;

import java.util.stream.Stream;

public class Pixel {
	public int x;
	public int y;

	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Stream<Pixel> genStream(int nx, int ny) {
		Stream<Pixel> stream = Stream.iterate(new Pixel(0, ny - 1), p -> {
			if (p.x == 0 && p.y == -1) {
				return false;
			} else {
				return true;
			}
		}, p -> {
			int tx = p.x;
			int ty = p.y;

			if (tx < nx - 1) {
				tx++;
			} else {
				tx = 0;
				ty--;
			}

			return new Pixel(tx, ty);
		});

		return stream;
	}
	
	public String toString() {
		return "Pixel(" + x + "," + y + ")";
	}
}
