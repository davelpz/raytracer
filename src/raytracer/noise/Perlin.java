package raytracer.noise;

import raytracer.Vec;

public class Perlin {

	public float noise(final Vec p) {
		float u = (float) (p.x() - Math.floor(p.x()));
		float v = (float) (p.y() - Math.floor(p.y()));
		float w = (float) (p.z() - Math.floor(p.z()));
		u = u * u * (3 - 2 * u);
		v = v * v * (3 - 2 * v);
		w = w * w * (3 - 2 * w);

		int i = (int) Math.floor(p.x());
		int j = (int) Math.floor(p.y());
		int k = (int) Math.floor(p.z());
		float[][][] c = new float[2][2][2];
		for (int di = 0; di < 2; di++) {
			for (int dj = 0; dj < 2; dj++) {
				for (int dk = 0; dk < 2; dk++) {
					c[di][dj][dk] = ranfloat[perm_x[(i + di) & 255] ^ perm_y[(j + dj) & 255] ^ perm_z[(k + dk) & 255]];
				}
			}
		}
		return trilinear_interp(c, u, v, w);
	}

	static float[] ranfloat = perlin_generate();
	static int[] perm_x = perlin_generate_perm();
	static int[] perm_y = perlin_generate_perm();
	static int[] perm_z = perlin_generate_perm();

	public static float trilinear_interp(float[][][] c, float u, float v, float w) {
		float accum = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					accum += (i * u + (1 - i) * (1 - u)) * (j * v + (1 - j) * (1 - v)) * (k * w + (1 - k) * (1 - w))
							* c[i][j][k];
				}
			}
		}

		return accum;
	}

	public static float[] perlin_generate() {
		float[] p = new float[256];
		for (int i = 0; i < 256; i++) {
			p[i] = (float) Math.random();
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
