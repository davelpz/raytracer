package raytracer;

public class Vec {
	float[] e = new float[3];

	public Vec() {
	}

	public Vec(float e0, float e1, float e2) {
		e[0] = e0;
		e[1] = e1;
		e[2] = e2;
	}

	public Vec(double e0, double e1, double e2) {
		e[0] = (float) e0;
		e[1] = (float) e1;
		e[2] = (float) e2;
	}

	public float x() {
		return e[0];
	}

	public float y() {
		return e[1];
	}

	public float z() {
		return e[2];
	}

	public float r() {
		return e[0];
	}

	public float g() {
		return e[1];
	}

	public float b() {
		return e[2];
	}

	public static Vec plus(Vec a) {
		return a;
	}

	public static Vec neg(Vec a) {
		return new Vec(-a.x(), -a.y(), -a.z());
	}

	public float get(int i) {
		return e[i];
	}

	public void set(int i, float value) {
		e[i] = value;
	}

	public float length() {
		return (float) Math.sqrt(e[0] * e[0] + e[1] * e[1] + e[2] * e[2]);
	}

	public float squared_length() {
		return e[0] * e[0] + e[1] * e[1] + e[2] * e[2];
	}

	public String toString() {
		return "Vec(" + e[0] + "," + e[1] + "," + e[2] + ")";
	}

	public void make_unit_vector() {
		float k = 1.0f / length();
		e[0] *= k;
		e[1] *= k;
		e[2] *= k;
	}

	public static Vec add(Vec a, Vec b) {
		return new Vec(a.x() + b.x(), a.y() + b.y(), a.z() + b.z());
	}

	public static Vec sub(Vec a, Vec b) {
		return new Vec(a.x() - b.x(), a.y() - b.y(), a.z() - b.z());
	}

	public static Vec mul(Vec a, Vec b) {
		return new Vec(a.x() * b.x(), a.y() * b.y(), a.z() * b.z());
	}

	public static Vec div(Vec a, Vec b) {
		return new Vec(a.x() / b.x(), a.y() / b.y(), a.z() / b.z());
	}

	public static Vec mul(Vec a, float t) {
		return new Vec(a.x() * t, a.y() * t, a.z() * t);
	}

	public static Vec mul(float t, Vec a) {
		return new Vec(a.x() * t, a.y() * t, a.z() * t);
	}

	public static Vec div(Vec a, float t) {
		return new Vec(a.x() / t, a.y() / t, a.z() / t);
	}

	public static float dot(Vec a, Vec b) {
		return a.x() * b.x() + a.y() * b.y() + a.z() * b.z();
	}

	public static Vec cross(Vec a, Vec b) {
		return new Vec(a.y() * b.z() - a.z() * b.y(), -(a.x() * b.z() - a.z() * b.x()), a.x() * b.y() - a.y() * b.x());
	}

	public Vec add(Vec a) {
		e[0] += a.x();
		e[1] += a.y();
		e[2] += a.z();
		return this;
	}

	public Vec mul(Vec a) {
		e[0] *= a.x();
		e[1] *= a.y();
		e[2] *= a.z();
		return this;
	}

	public Vec div(Vec a) {
		e[0] /= a.x();
		e[1] /= a.y();
		e[2] /= a.z();
		return this;
	}

	public Vec sub(Vec a) {
		e[0] -= a.x();
		e[1] -= a.y();
		e[2] -= a.z();
		return this;
	}

	public Vec mul(float t) {
		e[0] *= t;
		e[1] *= t;
		e[2] *= t;
		return this;
	}

	public Vec div(float t) {
		e[0] /= t;
		e[1] /= t;
		e[2] /= t;
		return this;
	}

	public Vec sqrt() {
		e[0] = (float) Math.sqrt(e[0]);
		e[1] = (float) Math.sqrt(e[1]);
		e[2] = (float) Math.sqrt(e[2]);
		return this;
	}

	public static Vec unit_vector(Vec v) {
		return Vec.div(v, v.length());
	}
}
