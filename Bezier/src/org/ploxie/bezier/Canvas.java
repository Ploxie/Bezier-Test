package org.ploxie.bezier;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import org.ploxie.bezier.math.Vector2f;

public class Canvas extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {

	private static final long serialVersionUID = 5762149068401439490L;
	
	private float nodeSize = 10.0f;
	private float stepSize = 5.0f;
	private int steps = 10;
	private boolean drawControls = true;

	private BezierPath path;
	private Vector2f selectedNode;

	public Canvas() {
		this.path = new BezierPath();

		path.add(new BezierCurve(new Vector2f(50, 300), new Vector2f(750, 300), new Vector2f(175, 150),
				new Vector2f(575, 450)));

		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (BezierCurve curve : path) {
			drawBezierCurve(curve, g, steps, true);
		}

	}

	private void drawBezierCurve(BezierCurve curve, Graphics g, int steps, boolean drawSteps) {
		g.setColor(Color.WHITE);

		Vector2f a = curve.getFirstAnchorNode();
		Vector2f b = curve.getFirstControlNode();
		Vector2f c = curve.getSecondAnchorNode();
		Vector2f d = curve.getSecondControlNode();

		Vector2f lastPoint = a;
		for (int i = 0; i < steps + 1; i++) {
			Vector2f currentPoint = curve.getPoint(i / (float) steps);
			g.drawLine((int) lastPoint.x, (int) lastPoint.y, (int) currentPoint.x, (int) currentPoint.y);

			if (drawControls) {
				int halfStepSize = (int) (stepSize * 0.5f);
				g.fillOval((int) currentPoint.x - halfStepSize, (int) currentPoint.y - halfStepSize, (int) stepSize,
						(int) stepSize);
			}
			lastPoint = currentPoint;
		}

		if (drawControls) {
			g.setColor(Color.gray);
			g.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
			g.drawLine((int) c.x, (int) c.y, (int) d.x, (int) d.y);

			for (Vector2f node : curve.getNodes()) {
				int halfNodeSize = (int) (nodeSize * 0.5f);
				g.fillOval((int) node.x - halfNodeSize, (int) node.y - halfNodeSize, (int) nodeSize, (int) nodeSize);
			}

			if (selectedNode != null) {
				g.setColor(Color.GREEN);
				int halfNodeSize = (int) (nodeSize * 0.5f);
				g.fillOval((int) selectedNode.x - halfNodeSize, (int) selectedNode.y - halfNodeSize, (int) nodeSize,
						(int) nodeSize);
			}
		}
	}
	
	
	
	private BezierCurve getSelectedCurve(Vector2f mousePos) {
		for (BezierCurve curve : path) {
			for (Vector2f node : curve.getNodes()) {
				if (node.distance(mousePos) <= (nodeSize)) {
					return curve;
				}
			}
		}
		return null;
	}

	private Vector2f getSelectedNode(Vector2f mousePos) {
		for (BezierCurve curve : path) {
			for (Vector2f node : curve.getNodes()) {
				if (node.distance(mousePos) <= (nodeSize)) {
					return node;
				}
			}
		}
		return null;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

		if (!drawControls) {
			return;
		}

		Vector2f mousePos = new Vector2f(e.getX(), e.getY());
		
		this.selectedNode = getSelectedNode(mousePos);

		Vector2f selectedNode = getSelectedNode(mousePos);
		BezierCurve selectedCurve = getSelectedCurve(mousePos);

		
		if(selectedNode == null || selectedCurve == null) {
			return;
		}
		
		
		Vector2f delta = mousePos.clone().subtract(selectedNode);
		
		boolean isControl = selectedNode.equals(selectedCurve.getFirstControlNode()) || selectedNode.equals(selectedCurve.getSecondControlNode());
		boolean isFirstNode = selectedNode.equals(selectedCurve.getFirstAnchorNode()) || selectedNode.equals(selectedCurve.getFirstControlNode());
		
		BezierCurve previousCurve = path.getPreviousCurve(selectedCurve);
		BezierCurve nextCurve = path.getNextCurve(selectedCurve);
		
		if(!isControl) {		
			if(isFirstNode) {
				if(previousCurve != null) {
					Vector2f oldPos = previousCurve.getFirstControlNode();
					previousCurve.setFirstControlNode(oldPos.add(delta));
				}
				
				selectedCurve.setFirstAnchorNode(mousePos);
				
				Vector2f oldPos = selectedCurve.getFirstControlNode();
				selectedCurve.setFirstControlNode(oldPos.add(delta));
			}else {
				if(nextCurve != null) {
					Vector2f oldPos = nextCurve.getFirstControlNode();
					nextCurve.setFirstControlNode(oldPos.add(delta));
					
					oldPos = nextCurve.getFirstAnchorNode();
					nextCurve.setFirstAnchorNode(oldPos.add(delta));
				}
				
				selectedCurve.setSecondAnchorNode(mousePos);
				
				Vector2f oldPos = selectedCurve.getSecondControlNode();
				selectedCurve.setSecondControlNode(oldPos.add(delta));
			}			
		}else {
			if(isFirstNode) {
				selectedCurve.setFirstControlNode(mousePos);
				Vector2f direction = selectedCurve.getFirstAnchorNode().clone().subtract(selectedCurve.getFirstControlNode()).normalized();
				
				if(previousCurve != null) {
					Vector2f oldPos = previousCurve.getSecondControlNode();
					float distance = mousePos.clone().subtract(oldPos).length();
					previousCurve.setSecondControlNode(selectedCurve.getFirstControlNode().clone().add(direction.clone().multiply(distance)));
				}
			}else {
				selectedCurve.setSecondControlNode(mousePos);
				Vector2f direction = selectedCurve.getSecondAnchorNode().clone().subtract(selectedCurve.getSecondControlNode()).normalized();
				
				
				if(nextCurve != null) {
					Vector2f oldPos = nextCurve.getFirstControlNode();
					float distance = mousePos.clone().subtract(oldPos).length();
					nextCurve.setFirstControlNode(selectedCurve.getSecondControlNode().clone().add(direction.clone().multiply(distance)));
				}
			}
		}
		

		repaint();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		Vector2f mousePos = new Vector2f(e.getX(), e.getY());
		this.selectedNode = getSelectedNode(mousePos);

		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON3) {
			drawControls = !drawControls;
		} else {
			Vector2f mousePos = new Vector2f(e.getX(), e.getY());
			if (this.selectedNode == null) {
				path.addNode(mousePos);
			}
		}

		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		this.steps += e.getWheelRotation();
		if (this.steps <= 0) {
			this.steps = 1;
		}
		repaint();
	}

}
