package raytracer.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PPMImage {
	int width;
	int height;
	int colordepth;
	Pixel[] data;

	public PPMImage(int width, int height, int colordepth) {
		this.width = width;
		this.height = height;
		this.colordepth = colordepth;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getDepth() {
		return colordepth;
	}

	public Pixel[] getData() {
		return data;
	}

	public Pixel getData(int i) {
		return data[i];
	}

	public void setData(Pixel[] pixels) {
		this.data = pixels;
	}

	public static PPMImage fromFile(String fileName) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get(fileName)));
		String[] tokens = content.split("\\s+");
		int token_index = 0;
		String token = tokens[token_index];
		assert (token.equals("P3"));
		token_index++;
		int width = Integer.parseInt(tokens[token_index]);
		token_index++;
		int height = Integer.parseInt(tokens[token_index]);
		token_index++;
		int maxcolordepth = Integer.parseInt(tokens[token_index]);

		PPMImage image = new PPMImage(width, height, maxcolordepth);
		Pixel[] pixels = new Pixel[width * height];
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				token_index++;
				int r = Integer.parseInt(tokens[token_index]);
				token_index++;
				int g = Integer.parseInt(tokens[token_index]);
				token_index++;
				int b = Integer.parseInt(tokens[token_index]);
				pixels[i + (j * width)] = new Pixel(r, g, b);
			}
		}

		image.setData(pixels);
		return image;
	}

	public void dump() {
		for (Pixel r : data) {
			System.out.println(r);
		}
		System.out.format("P3\n%s %s\n%s\n", width, height, colordepth);
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int r = data[i + (j * width)].r;
				int g = data[i + (j * width)].g;
				int b = data[i + (j * width)].b;
				System.out.format("%s %s %s ", r, g, b);
			}
			System.out.println("");
		}
	}

	public static class Pixel {
		public int r;
		public int g;
		public int b;

		public Pixel(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}

		public String toString() {
			return "Pixel(" + r + "," + g + "," + b + ")";
		}
	}

}
