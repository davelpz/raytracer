package raytracer.texture;

import raytracer.Vec;
import raytracer.image.PPMImage;
import raytracer.image.PPMImage.Pixel;

public class ImageTexture implements Texture {
	PPMImage image;

	public ImageTexture(PPMImage i) {
		image = i;
	}

	@Override
	public Vec value(float u, float v, Vec p) {
		//System.out.format("value: %s  %s  %s\n", u, v, p);
		int nx = image.getWidth();
		int ny = image.getHeight();
		int i = (int) ((u) * nx);
		// int j = (int) ((1 - v) * ny - 0.001f);
		int j = (int) ((1 - v) * ny);
		//System.out.format("i: %s  j: %s\n", i, j);
		if (i < 0)
			i = 0;
		if (j < 0)
			j = 0;
		if (i > (nx - 1))
			i = nx - 1;
		if (j > (ny - 1))
			j = ny - 1;
		//System.out.format("i: %s  j: %s\n", i, j);
		int index = i + (nx * j);
		//System.out.format("index: %s\n", index);
		Pixel pix = image.getData(i + (nx * j));
		//System.out.format("pixel: %s\n", pix);
		float r = pix.r / 255.0f;
		float g = pix.g / 255.0f;
		float b = pix.b / 255.0f;
		return new Vec(r, g, b);
	}

}
