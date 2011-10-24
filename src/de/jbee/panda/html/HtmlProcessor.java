package de.jbee.panda.html;

import java.io.Reader;
import java.io.StringReader;

public class HtmlProcessor {

	static enum State {
		TAG_EXPECTANT,
		TAG_NAME,
		ATTR_EXPECTANT,
		ATTR_NAME,
		ATTR_VALUE,

	}

	public void render( Reader in, Appendable out )
			throws Exception {

		StringBuilder openTag = new StringBuilder( 50 );
		StringBuilder attrName = new StringBuilder( 15 );
		StringBuilder attrValue = new StringBuilder( 100 );
		int code = in.read();
		State state = State.TAG_EXPECTANT;
		boolean emptyTag = false;
		boolean closeTag = true;
		while ( code > 0 ) {
			char ch = (char) code;
			switch ( state ) {
				case TAG_EXPECTANT:
					if ( ch == '<' ) {
						state = State.TAG_NAME;
						openTag.setLength( 0 );
						openTag.append( ch );
						emptyTag = false;
					} else {
						out.append( ch );
					}
					break;
				case TAG_NAME:
					openTag.append( ch );
					if ( ch == '/' ) {
						closeTag = true;
					} else if ( ch == '>' ) {
						out.append( openTag );
						state = State.TAG_EXPECTANT;
					} else if ( Character.isWhitespace( ch ) ) {
						state = State.ATTR_EXPECTANT;
					}
					break;
				case ATTR_EXPECTANT:
					if ( Character.isLetter( ch ) ) {
						attrName.setLength( 0 );
						attrName.append( ch );
						state = State.ATTR_NAME;
					} else {
						openTag.append( ch );
						if ( ch == '/' ) {
							emptyTag = true;
						} else if ( ch == '>' ) {
							out.append( openTag );
							state = State.TAG_EXPECTANT;
						}
					}
					break;
				case ATTR_NAME:
					if ( Character.isLetterOrDigit( ch ) ) {
						attrName.append( ch );
					} else {
						if ( ch == '=' ) {
							if ( attrName.toString().equals( "are" ) ) {
								System.out.println( "We are" );
							}
							state = State.ATTR_VALUE;
							attrValue.setLength( 0 );
						}
					}
					break;
				case ATTR_VALUE:
					attrValue.append( ch );
					if ( ch == '\"' || ch == '\'' ) {
						if ( attrValue.length() > 1 ) {
							state = State.ATTR_EXPECTANT;
							openTag.append( attrName ).append( '=' ).append( attrValue );
						}
					}
					break;

			}
			code = in.read();
		}
	}

	public static void main( String[] args ) {
		HtmlProcessor p = new HtmlProcessor();
		try {
			p.render( new StringReader( "<we are='so cool'></we>" ), System.out );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
