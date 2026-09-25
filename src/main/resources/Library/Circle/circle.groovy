import org.partmaker.scriptparams.*;

void defineParameters(Parameters parameters) {
	parameters.add(new RealParameter("radius")).min(5).max(200).required();
}

System.out.println("Hello world, will draw circle with radius: " + radius);

graphics.drawOval(-radius, -radius, radius*2, radius*2);
