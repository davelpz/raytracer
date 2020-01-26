package raytracer.material;

import raytracer.Ray;
import raytracer.Vec;

public class ScatterResult {
	public Vec attenuation;
	public Ray scattered;
	
	public String toString() {
		return "ScatterResult("+attenuation+","+scattered+")";
	}
}
