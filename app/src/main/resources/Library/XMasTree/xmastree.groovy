import org.partmaker.scriptparams.*;
import java.awt.Polygon;

void defineParameters(Parameters parameters) {
	parameters.add(new IntegerParameter("basesize")).min(50).max(400).defaultValue(140).required();
	parameters.add(new IntegerParameter("height")).min(4).max(100).defaultValue(10).required();
	parameters.add(new RealParameter("thickness")).min(1).max(50).defaultValue(6.9).required();
	parameters.add(new IntegerParameter("shrink")).min(1).max(50).defaultValue(6).required();
	parameters.add(new IntegerParameter("distance")).min(1).max(50).defaultValue(4).required();
}

double y = 0;
double size = basesize;

while (size - 2*shrink > thickness) {
	double x0 = 0; 
	double x1 = x0 + shrink;
	double x2 = x0 + size / 2.0 - thickness / 2.0;
	double x3 = x2 + thickness;
	double x4 = size - shrink;
	double x5 = size;
	double y0 = y;
	double y1 = y + height / 2.0;
	double y2 = y + height;
	graphics.drawPolygon(
		new double[] { x0, x5, x4, x3, x3, x2, x2, x1},
		new double[] { y0, y0, y2, y2, y1, y1, y2, y2},
		8
	);
	y = y2 + distance;
	y0 = y;
	y1 = y + height / 2.0;
	y2 = y + height;
	graphics.drawPolygon(
		new double[] { x0, x2, x2, x3, x3, x5, x4, x1},
		new double[] { y0, y0, y1, y1, y0, y0, y2, y2},
		8
	);
	y = y2 + distance;
	size -= 2 * shrink;
}

