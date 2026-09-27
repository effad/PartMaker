import org.partmaker.scriptparams.*;

void defineParameters(Parameters parameters) {
	parameters.add(new IntegerParameter("width")).min(5).max(2000).required().defaultValue(100);
	parameters.add(new IntegerParameter("height")).min(5).max(2000).required().defaultValue(50);
	parameters.add(new IntegerParameter("depth")).min(5).max(2000).required().defaultValue(200);
	parameters.add(new IntegerParameter("material")).min(1).max(200).required().defaultValue(5);
	parameters.add(new IntegerParameter("gap")).min(1).max(100).required().defaultValue(10);
}

// bottom
graphics.drawRect(0, 0, width, depth);

// front
graphics.drawRect(-material, -gap, width + 2 * material, -height - material);

// back
graphics.drawRect(-material, depth + gap, width + 2 * material, height + material);

// left
graphics.drawRect(-gap, 0, -height - material, depth);

// right*
graphics.drawRect(width + material, 0, height + material, depth);

