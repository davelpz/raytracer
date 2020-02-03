package raytracer.util;

public final class Tuple2<A, B> {
	public final A a;
	public final B b;

	public Tuple2(final A _a, final B _b) {
		a = _a;
		b = _b;
	}

	@Override
	public String toString() {
		return "Tuple2(" + a + "," + b + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Tuple2<A, B> other = (Tuple2<A, B>) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}
}
