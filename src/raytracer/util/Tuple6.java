package raytracer.util;

public final class Tuple6<A, B, C, D, E, F> {
	public final A a;
	public final B b;
	public final C c;
	public final D d;
	public final E e;
	public final F f;

	public Tuple6(final A _a, final B _b, final C _c, final D _d, final E _e, final F _f) {
		a = _a;
		b = _b;
		c = _c;
		d = _d;
		e = _e;
		f = _f;
	}

	@Override
	public String toString() {
		return "Tuple6(" + a + "," + b + "," + c + "," + d + "," + e + "," + f + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		result = prime * result + ((d == null) ? 0 : d.hashCode());
		result = prime * result + ((e == null) ? 0 : e.hashCode());
		result = prime * result + ((f == null) ? 0 : f.hashCode());
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
		Tuple6<A, B, C, D, E, F> other = (Tuple6<A, B, C, D, E, F>) obj;
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
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		if (d == null) {
			if (other.d != null)
				return false;
		} else if (!d.equals(other.d))
			return false;
		if (e == null) {
			if (other.e != null)
				return false;
		} else if (!e.equals(other.e))
			return false;
		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}

}
