package de.jbee.panda;

public class Instruction
		implements CharSequence {

	public boolean isNone() {

		return false;
	}

	public boolean startsWith( String prefix ) {

		return false;
	}

	public Instruction skip( int length ) {

		return this;
	}

	public int parseInt( int def ) {

		return def;
	}

	@Override
	public char charAt( int index ) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CharSequence subSequence( int start, int end ) {
		// TODO Auto-generated method stub
		return null;
	}

}
