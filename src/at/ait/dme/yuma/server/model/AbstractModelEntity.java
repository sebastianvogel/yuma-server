package at.ait.dme.yuma.server.model;

/**
 * Abstract base class that contains general 
 * utility methods shared across annotation data 
 * model entities.
 * 
 * @author Rainer Simon
 */
public class AbstractModelEntity {
	
	/**
	 * Checks for equality on objects that may be null
	 * @param a object A
	 * @param b object B
	 * @return
	 */
	protected boolean equalsNullable(Object a, Object b) {
		if (a == null)
			return b == null;
		
		if (b == null)
			return a == null;
		
		return a.equals(b);
	}

}
