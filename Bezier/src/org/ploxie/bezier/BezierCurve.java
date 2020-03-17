package org.ploxie.bezier;

import org.ploxie.bezier.math.Mathf;
import org.ploxie.bezier.math.Vector2f;

public class BezierCurve {

	private Vector2f firstAnchorNode;
	private Vector2f secondAnchorNode;
	
	private Vector2f firstControlNode;
	private Vector2f secondControlNode;
	
	public BezierCurve(Vector2f a, Vector2f b) {
		this.firstAnchorNode = a;
		this.secondAnchorNode = b;
		
		this.firstControlNode = Mathf.lerp(a, b, 0.25f);
		this.secondControlNode = Mathf.lerp(a, b, 0.75f);
	}
	
	public BezierCurve(Vector2f a, Vector2f b, Vector2f controlA, Vector2f controlB) {
		this.firstAnchorNode = a;
		this.secondAnchorNode = b;
		
		this.firstControlNode = controlA;
		this.secondControlNode = controlB;
	}
	
	public Vector2f[] getNodes() {
		return new Vector2f[] {getFirstAnchorNode(), getFirstControlNode(), getSecondAnchorNode(), getSecondControlNode()};
	}
	
	public Vector2f getPoint(float t) {
		return Mathf.cubicLerp(getFirstAnchorNode(), getFirstControlNode(), getSecondControlNode(), getSecondAnchorNode(), t);
	}
	
	public void setFirstAnchorNode(Vector2f firstAnchorNode) {
		this.firstAnchorNode = firstAnchorNode;
	}

	public void setSecondAnchorNode(Vector2f secondAnchorNode) {
		this.secondAnchorNode = secondAnchorNode;
	}

	public void setFirstControlNode(Vector2f firstControlNode) {
		this.firstControlNode = firstControlNode;
	}

	public void setSecondControlNode(Vector2f secondControlNode) {
		this.secondControlNode = secondControlNode;
	}
	
	public Vector2f getFirstAnchorNode() {
		return firstAnchorNode;
	}

	public Vector2f getSecondAnchorNode() {
		return secondAnchorNode;
	}

	public Vector2f getFirstControlNode() {
		return firstControlNode;
	}

	public Vector2f getSecondControlNode() {
		return secondControlNode;
	}
	
}
