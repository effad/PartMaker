import org.partmaker.scriptparams.*;
import java.awt.geom.Path2D;
import java.awt.geom.AffineTransform;
import java.util.logging.Level;

void defineParameters(Parameters parameters) {
	parameters.add(new IntegerParameter("width")).min(5).max(2000).required().defaultValue(100);
	parameters.add(new IntegerParameter("width_fingers")).min(1).max(100).required().defaultValue(2);
	parameters.add(new IntegerParameter("height")).min(5).max(2000).required().defaultValue(50);
	parameters.add(new IntegerParameter("height_fingers")).min(1).max(100).required().defaultValue(1);
	parameters.add(new IntegerParameter("depth")).min(5).max(2000).required().defaultValue(200);
	parameters.add(new IntegerParameter("depth_fingers")).min(1).max(100).required().defaultValue(5);
	parameters.add(new IntegerParameter("material")).min(1).max(200).required().defaultValue(5);
	parameters.add(new IntegerParameter("gap")).min(1).max(100).required().defaultValue(10);
}

Path2D.Double createRidge(long nrFingers, long ridgeLength) {
	int sections = nrFingers * 2 + 1;
	double outerLength = ridgeLength + 2.0 * material;
	double sw = outerLength / sections;
	Path2D.Double ridge = new Path2D.Double();
	double xzero = 0.0;
	for (int i = 0; i < nrFingers; i++) {
		ridge.moveTo(xzero, 0d)
		ridge.lineTo(xzero + sw, 0d)
		ridge.lineTo(xzero + sw, material)
		ridge.lineTo(xzero + 2 * sw, material)
		ridge.lineTo(xzero + 2 * sw, 0d)
		xzero += sw * 2;
	}
	ridge.lineTo(xzero, 0d)
	ridge.lineTo(xzero + sw, 0d)	
	return ridge;
}


// bottom
topRidge = createRidge(width_fingers, width);
graphics.draw(topRidge);
AffineTransform tx = new AffineTransform();
tx.scale(1.0, -1.0);
tx.translate(0, -(depth + 2 * material));
bottomRidge = tx.createTransformedShape(topRidge);
graphics.draw(bottomRidge);


tx = new AffineTransform();
tx.quadrantRotate(1);
tx.scale(1.0, -1.0);
leftRidge = tx.createTransformedShape(createRidge(depth_fingers, depth));
graphics.draw(leftRidge);

tx = new AffineTransform();
tx.scale(-1.0, 1.0);
tx.translate(-(width + 2 * material), 0);
rightRidge = tx.createTransformedShape(leftRidge);
graphics.draw(rightRidge);

log.log(Level.INFO, "Done.");

