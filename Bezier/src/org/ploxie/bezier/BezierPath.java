package org.ploxie.bezier;

import java.util.ArrayList;

import org.ploxie.bezier.math.Vector2f;

public class BezierPath extends ArrayList<BezierCurve> {

	private static final long serialVersionUID = 6448027949974629493L;

	public void addNode(Vector2f point) {
		BezierCurve lastCurve = get(size() - 1);

		Vector2f lastAnchor = lastCurve.getSecondAnchorNode();		
		Vector2f lastControlLine = lastCurve.getSecondControlNode().clone().subtract(lastAnchor);

		Vector2f controlA = lastAnchor.clone().add(lastControlLine.clone().multiply(-1.0f));
		Vector2f currentControlLine = point.clone().subtract(controlA);
				
		Vector2f controlB = point.clone().add(currentControlLine.clone().multiply(-0.5f));

		BezierCurve current = new BezierCurve(lastAnchor, point, controlA, controlB);
		add(current);
	}

	public BezierCurve getPreviousCurve(BezierCurve current) {
		BezierCurve previous = null;

		for (BezierCurve curve : this) {
			if (curve.equals(current)) {
				return previous;
			}
			previous = curve;
		}

		return null;
	}

	public BezierCurve getNextCurve(BezierCurve current) {

		for (int i = 0; i < size() - 1; i++) {
			if (get(i).equals(current)) {
				return get(i + 1);
			}
		}

		return null;
	}
	
	
	

}
