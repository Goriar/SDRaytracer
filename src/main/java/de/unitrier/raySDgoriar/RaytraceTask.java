package de.unitrier.raySDgoriar;

import java.util.concurrent.Callable;

class RaytraceTask implements Callable<Object> {
	SDRaytracer tracer;
	int id;
	RaytraceTask(SDRaytracer t, int ii) {
		tracer = t;
		id = ii;
	}

	public Light[] call() {
		Light[] col = new Light[tracer.height];
		for (int j = 0; j < tracer.height; j++) {
			tracer.image[id][j] = new Light(0, 0, 0);
			for (int k = 0; k < tracer.rayPerPixel; k++) {
				double di = id + (Math.random() / 2 - 0.25);
				double dj = j + (Math.random() / 2 - 0.25);
				if (tracer.rayPerPixel == 1) {
					di = id;
					dj = j;
				}
				Ray eye_ray = new Ray();
				eye_ray.setStart(tracer.startX, tracer.startY, tracer.startZ); 
				eye_ray.setDir((float) (((0.5 + di) * tracer.scene.tan_fovx * 2.0) / tracer.width - tracer.scene.tan_fovx),
						(float) (((0.5 + dj) * tracer.scene.tan_fovy * 2.0) / tracer.height - tracer.scene.tan_fovy), (float) 1f); // rd
				eye_ray.normalize();
				col[j] = Light.addColors(tracer.image[id][j], rayTrace(eye_ray, 0), 1.0f / tracer.rayPerPixel);
			}
		}
		return col;
	}

	Light rayTrace(Ray ray, int rec) {
		if (rec > tracer.scene.getMaxRec())
			return Light.black;
		IPoint ip = hitObject(ray);
		if (ip.dist > IPoint.epsilon)
			return lighting(ray, ip, rec);
		else
			return Light.black;
	}

	Light lighting(Ray ray, IPoint ip, int rec) {
		Vec3D point = ip.point;
		Triangle triangle = ip.triangle;
		Light color = Light.addColors(triangle.color, Light.ambient_color, 1);
		Ray shadow_ray = new Ray();
		for (Light light : tracer.scene.lights) {
			shadow_ray.start = point;
			shadow_ray.dir = light.position.minus(point).mult(-1);
			shadow_ray.dir.normalize();
			IPoint ip2 = hitObject(shadow_ray);
			if (ip2.dist < IPoint.epsilon) {
				float ratio = Math.max(0, shadow_ray.dir.dot(triangle.normal));
				color = Light.addColors(color, light, ratio);
			}
		}
		Ray reflection = new Ray();
		// R = 2N(N*L)-L) L ausgehender Vektor
		Vec3D L = ray.dir.mult(-1);
		reflection.start = point;
		reflection.dir = triangle.normal.mult(2 * triangle.normal.dot(L)).minus(L);
		reflection.dir.normalize();
		Light rcolor = rayTrace(reflection, rec + 1);
		float ratio = (float) Math.pow(Math.max(0, reflection.dir.dot(L)), triangle.shininess);
		color = Light.addColors(color, rcolor, ratio);
		return (color);
	}

	IPoint hitObject(Ray ray) {
		IPoint isect = new IPoint(null, null, -1);
		float idist = -1;
		for (Triangle t : tracer.scene.triangles) {
			IPoint ip = ray.intersect(t);
			if (ip.dist != -1 && ((idist == -1) || (ip.dist < idist))){ // save that intersection
					idist = ip.dist;
					isect.point = ip.point;
					isect.dist = ip.dist;
					isect.triangle = t;
				}
		}
		return isect; // return intersection point and normal
	}
}