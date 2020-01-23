package raytracer.noise;

import raytracer.Vec;

public class Perlin {

	public float noise(final Vec p) {
		float u = (float) (p.x() - Math.floor(p.x()));
		float v = (float) (p.y() - Math.floor(p.y()));
		float w = (float) (p.z() - Math.floor(p.z()));

		int i = (int) Math.floor(p.x());
		int j = (int) Math.floor(p.y());
		int k = (int) Math.floor(p.z());
		Vec[][][] c = new Vec[2][2][2];
		for (int di = 0; di < 2; di++) {
			for (int dj = 0; dj < 2; dj++) {
				for (int dk = 0; dk < 2; dk++) {
					c[di][dj][dk] = ranvec[perm_x[(i + di) & 255] ^ perm_y[(j + dj) & 255] ^ perm_z[(k + dk) & 255]];
				}
			}
		}
		return perlin_interp(c, u, v, w);
	}

	static Vec[] ranvec = perlin_generate();
	static int[] perm_x = perlin_generate_perm();
	static int[] perm_y = perlin_generate_perm();
	static int[] perm_z = perlin_generate_perm();

	public static float perlin_interp(Vec[][][] c, float u, float v, float w) {
		float uu = u * u * (3 - 2 * u);
		float vv = v * v * (3 - 2 * v);
		float ww = w * w * (3 - 2 * w);
		float accum = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					Vec weight_v = new Vec(u - i, v - j, w - k);
					accum += (i * uu + (1 - i) * (1 - uu)) * (j * vv + (1 - j) * (1 - vv))
							* (k * ww + (1 - k) * (1 - ww)) * Vec.dot(c[i][j][k], weight_v);
				}
			}
		}

		return accum;
	}

	public static Vec[] perlin_generate() {
		Vec[] p = new Vec[256];
		for (int i = 0; i < 256; i++) {
			p[i] = Vec.unit_vector(new Vec(-1 + 2 * Math.random(), -1 + 2 * Math.random(), -1 + 2 * Math.random()));
		}

		return p;
	}

	public static void permute(int[] p, int n) {
		for (int i = n - 1; i > 0; i--) {
			int target = (int) (Math.random() * (i + 1));
			int tmp = p[i];
			p[i] = p[target];
			p[target] = tmp;
		}
	}

	public static int[] perlin_generate_perm() {
		int[] p = new int[256];
		for (int i = 0; i < 256; i++) {
			p[i] = i;
		}
		permute(p, 256);
		return p;
	}
}
