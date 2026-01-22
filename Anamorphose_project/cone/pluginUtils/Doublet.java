package pluginUtils;

public class Doublet {
	
	/**
	 * @param a
	 * @param b
	 */
	public Doublet(int a, int b) {
		this.a = a;
		this.b = b;
	}

	private int a,b;

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + a;
		result = prime * result + b;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Doublet other = (Doublet) obj;
		if (a != other.a)
			return false;
		if (b != other.b)
			return false;
		return true;
	}

	/**
	 * @return the a
	 */
	public int getA() {
		return a;
	}

	/**
	 * @return the b
	 */
	public int getB() {
		return b;
	} 
	

}
