package org.ploxie.bezier.math;

public class Mathf {

	public static Vector2f lerp(Vector2f a, Vector2f b, float t) {
		return a.clone().add(b.clone().subtract(a).multiply(t));
	}
	
	public static Vector2f quadraticLerp(Vector2f a, Vector2f b, Vector2f c, float t) {
		Vector2f p = lerp(a,b, t);
		Vector2f p2 = lerp(b,c,t);
		
		return lerp(p, p2, t);
	}
	
	public static Vector2f cubicLerp(Vector2f a, Vector2f b, Vector2f c, Vector2f d, float t) {
		Vector2f p = quadraticLerp(a, b, c, t);
		Vector2f p2 = quadraticLerp(b,c,d, t);
		return lerp(p, p2, t);
	}
	
	public static float sqrt(float value) {
		return (float) Math.sqrt(value);
	}
	
}
