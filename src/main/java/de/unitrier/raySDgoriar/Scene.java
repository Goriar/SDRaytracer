package de.unitrier.raySDgoriar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Scene {

	double tan_fovx;
	double tan_fovy;
	Light mainLight = new Light(new Vec3D(0, 100, 0), 0.1f, 0.1f, 0.1f);
	Light lights[] = new Light[] { mainLight, new Light(new Vec3D(100, 200, 300), 0.5f, 0, 0.0f),
			new Light(new Vec3D(-100, 200, 300), 0.0f, 0, 0.5f)
			// ,new Light(new Vec3D(-100,0,0), new RGB(0.0f,0.8f,0.0f))
	};
	public static final float FOVX = (float) 0.628;
	public static final float FOVY = (float) 0.628;
	List<Triangle> triangles;
	private int maxRec = 3;
	int y_angle_factor = 4;
	int x_angle_factor = -4;

	public int getMaxRec() {
		return maxRec;
	}
	void createScene(SDRaytracer sdRaytracer) {
		triangles = new ArrayList<Triangle>();

		addCube(0, 35, 0, 10, 10, 10, new Light(0.3f, 0, 0), 0.4f); // rot, klein
		addCube(-70, -20, -20, 20, 100, 100, new Light(0f, 0, 0.3f), .4f);
		addCube(-30, 30, 40, 20, 20, 20, new Light(0, 0.4f, 0), 0.2f); // grün, klein
		addCube(50, -20, -40, 10, 80, 100, new Light(.5f, .5f, .5f), 0.2f);
		addCube(-70, -26, -40, 130, 3, 40, new Light(.5f, .5f, .5f), 0.2f);

		Matrix mRx = Matrix.createXRotation((float) (x_angle_factor * Math.PI / 16));
		Matrix mRy = Matrix.createYRotation((float) (y_angle_factor * Math.PI / 16));
		Matrix mT = Matrix.createTranslation(0, 0, 200);
		Matrix m = mT.mult(mRx).mult(mRy);
		m.print();
		m.apply(triangles);
	}

	void profileRenderImage(SDRaytracer sdRaytracer) {
		long end, start, time;

		sdRaytracer.scene.renderImage(sdRaytracer); // initialisiere Datenstrukturen, erster Lauf verfälscht sonst
													// Messungen

		for (int procs = 1; procs < 6; procs++) {

			maxRec = procs - 1;
			System.out.print(procs);
			for (int i = 0; i < 10; i++) {
				start = System.currentTimeMillis();

				sdRaytracer.scene.renderImage(sdRaytracer);

				end = System.currentTimeMillis();
				time = end - start;
				System.out.print(";" + time);
			}
			System.out.println("");
		}
	}

	void renderImage(SDRaytracer sdRaytracer) {
		tan_fovx = Math.tan(FOVX);
		tan_fovy = Math.tan(FOVY);
		for (int i = 0; i < sdRaytracer.width; i++) {
			sdRaytracer.futureList[i] = sdRaytracer.eservice.submit(new RaytraceTask(sdRaytracer, i));
		}

		for (int i = 0; i < sdRaytracer.width; i++) {
			try {
				Light[] col = (Light[]) sdRaytracer.futureList[i].get();
				for (int j = 0; j < sdRaytracer.height; j++)
					sdRaytracer.image[i][j] = col[j];
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			}
		}
	}

	void addCube(int x, int y, int z, int w, int h, int d, Light c, float sh) { // front
		triangles.add(new Triangle(new Vec3D(x, y, z), new Vec3D(x + w, y, z), new Vec3D(x, y + h, z), c, sh));
		triangles.add(new Triangle(new Vec3D(x + w, y, z), new Vec3D(x + w, y + h, z), new Vec3D(x, y + h, z), c, sh));
		// left
		triangles.add(new Triangle(new Vec3D(x, y, z + d), new Vec3D(x, y, z), new Vec3D(x, y + h, z), c, sh));
		triangles.add(new Triangle(new Vec3D(x, y + h, z), new Vec3D(x, y + h, z + d), new Vec3D(x, y, z + d), c, sh));
		// right
		triangles.add(
				new Triangle(new Vec3D(x + w, y, z), new Vec3D(x + w, y, z + d), new Vec3D(x + w, y + h, z), c, sh));
		triangles.add(new Triangle(new Vec3D(x + w, y + h, z), new Vec3D(x + w, y, z + d),
				new Vec3D(x + w, y + h, z + d), c, sh));
		// top
		triangles.add(new Triangle(new Vec3D(x + w, y + h, z), new Vec3D(x + w, y + h, z + d), new Vec3D(x, y + h, z),
				c, sh));
		triangles.add(new Triangle(new Vec3D(x, y + h, z), new Vec3D(x + w, y + h, z + d), new Vec3D(x, y + h, z + d),
				c, sh));
		// bottom
		triangles.add(new Triangle(new Vec3D(x + w, y, z), new Vec3D(x, y, z), new Vec3D(x, y, z + d), c, sh));
		triangles.add(new Triangle(new Vec3D(x, y, z + d), new Vec3D(x + w, y, z + d), new Vec3D(x + w, y, z), c, sh));
		// back
		triangles.add(
				new Triangle(new Vec3D(x, y, z + d), new Vec3D(x, y + h, z + d), new Vec3D(x + w, y, z + d), c, sh));
		triangles.add(new Triangle(new Vec3D(x + w, y, z + d), new Vec3D(x, y + h, z + d),
				new Vec3D(x + w, y + h, z + d), c, sh));

	}

}
