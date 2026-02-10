package dp.abstractFactory1;

public abstract class AbstractFactory {

	abstract Shape getShape(String shapeType);
	
	abstract Color getColor(String shapeType);
	
}
