package cl.chilefactory.core.util;

public class ReflectionObject {

	private Class type;
	private Object value;

	public Class getArrayType() [] {
		return new Class[] { type  };
	}
	
	public Object getArrayValue() [] {
		return new Object[] { value  };
	}
	
	public Class getType() {
		return type;
	}
	public void setType(Class type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
}
