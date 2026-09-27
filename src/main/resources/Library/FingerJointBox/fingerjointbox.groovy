import org.partmaker.scriptparams.*;
import java.awt.geom.Path2D;

void defineParameters(Parameters parameters) {
	parameters.add(new IntegerParameter("width")).min(5).max(2000).required().defaultValue(100);
	parameters.add(new IntegerParameter("width_fingers")).min(1).max(100).required().defaultValue(2);
	parameters.add(new IntegerParameter("height")).min(5).max(2000).required().defaultValue(50);
	parameters.add(new IntegerParameter("depth")).min(5).max(2000).required().defaultValue(200);
	parameters.add(new IntegerParameter("material")).min(1).max(200).required().defaultValue(5);
	parameters.add(new IntegerParameter("gap")).min(1).max(100).required().defaultValue(10);
}

int sections = width_fingers * 2 + 1;
double outer_width = width + 2 * material;
double sw = outer_width / sections;

poly = new Path2D.Double();
double xzero;
for (int i = 0; i < width_fingers; i++) {
	xzero += sw * 2;
	poly.moveTo(xzero, 0d)
	poly.lineTo(xzero + sw, 0d)
	poly.lineTo(xzero + sw, material)
	poly.lineTo(xzero + 2 * sw, material)
	poly.lineTo(xzero + 2 * sw, 0d)
}
xzero += sw * 2;
poly.lineTo(xzero, 0d)
poly.lineTo(xzero + sw, 0d)

// bottom
graphics.draw(poly);

