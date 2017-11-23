package de.unitrier.raySDgoriar;

class IPoint {
	public static final float epsilon = 0.0001f;
	Triangle triangle;
	Vec3D point;
	float dist;

	IPoint(Triangle tt, Vec3D ip, float d) {
		triangle = tt;
		point = ip;
		dist = d;
	}
}