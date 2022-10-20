package com.thales.qrapi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VcardParserTest {

	@Autowired
	private ApiVcardParser vCardParser;
	
	@Test
	public void parseVcard_3() {
		// vcard content to be parsed and matched with xml
		String strToParse = "BEGIN:VCARD\n"
				+ "VERSION:3.0\n"
				+ "N:Third;User3\n"
				+ "FN:User3 Third\n"
				+ "ORG:Thales\n"
				+ "TITLE:Engineer\n"
				+ "ADR:;;12 Ayer Rajah Crescent;Singapore;;139941;Singapore\n"
				+ "TEL;CELL:+6520002000\n"
				+ "EMAIL;WORK;INTERNET:user@thrid.sg\n"
				+ "END:VCARD";
		String expectedVal = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><vcards xmlns=\"urn:ietf:params:xml:ns:vcard-4.0\"><vcard><prodid><text>ez-vcard 0.11.3</text></prodid><n><surname>Third</surname><given>User3</given><additional/><prefix/><suffix/></n><fn><text>User3 Third</text></fn><org><text>Thales</text></org><title><text>Engineer</text></title><adr><pobox/><ext/><street>12 Ayer Rajah Crescent</street><locality>Singapore</locality><region/><code>139941</code><country>Singapore</country></adr><tel><parameters><type><text>CELL</text></type></parameters><text>+6520002000</text></tel><email><parameters><type><text>WORK</text><text>INTERNET</text></type></parameters><text>user@thrid.sg</text></email></vcard></vcards>";
		
		String parsedVal = vCardParser.parseStringContent(strToParse);
		
		String parsedName = vCardParser.getName(strToParse);
		System.out.println(parsedName + "\n\n");
		
		assertEquals(parsedVal, expectedVal);
	}
	
	@Test
	public void parseVcard_2() {
		// vcard content to be parsed and matched with xml
		String strToParse = "BEGIN:VCARD\n"
				+ "VERSION:3.0\n"
				+ "N:Second;User2\n"
				+ "FN:User2 Second\n"
				+ "ORG:Thales\n"
				+ "TITLE:Engineer\n"
				+ "ADR:;;12 Ayer Rajah Crescent;Singapore;;139941;Singapore\n"
				+ "TEL;CELL:+6510001000\n"
				+ "EMAIL;WORK;INTERNET:user@second.sg\n"
				+ "END:VCARD";
		String expectedVal = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><vcards xmlns=\"urn:ietf:params:xml:ns:vcard-4.0\"><vcard><prodid><text>ez-vcard 0.11.3</text></prodid><n><surname>Second</surname><given>User2</given><additional/><prefix/><suffix/></n><fn><text>User2 Second</text></fn><org><text>Thales</text></org><title><text>Engineer</text></title><adr><pobox/><ext/><street>12 Ayer Rajah Crescent</street><locality>Singapore</locality><region/><code>139941</code><country>Singapore</country></adr><tel><parameters><type><text>CELL</text></type></parameters><text>+6510001000</text></tel><email><parameters><type><text>WORK</text><text>INTERNET</text></type></parameters><text>user@second.sg</text></email></vcard></vcards>";
		
		String parsedVal = vCardParser.parseStringContent(strToParse);
		
		String parsedName = vCardParser.getName(strToParse);
		System.out.println(parsedName + "\n\n");
		
		assertEquals(parsedVal, expectedVal);
	}
	
	@Test
	public void parseVcard_1() {
		// vcard content to be parsed and matched with xml
		String strToParse = "BEGIN:VCARD\n"
				+ "VERSION:3.0\n"
				+ "N:First;User1\n"
				+ "FN:User1 First\n"
				+ "ORG:Thales\n"
				+ "TITLE:Engineer\n"
				+ "ADR:;;Blegdamsvej 17;København;;2100;Denmark\n"
				+ "TEL;CELL:+4510001000\n"
				+ "EMAIL;WORK;INTERNET:user@first.dk\n"
				+ "END:VCARD";
		String expectedVal = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><vcards xmlns=\"urn:ietf:params:xml:ns:vcard-4.0\"><vcard><prodid><text>ez-vcard 0.11.3</text></prodid><n><surname>First</surname><given>User1</given><additional/><prefix/><suffix/></n><fn><text>User1 First</text></fn><org><text>Thales</text></org><title><text>Engineer</text></title><adr><pobox/><ext/><street>Blegdamsvej 17</street><locality>København</locality><region/><code>2100</code><country>Denmark</country></adr><tel><parameters><type><text>CELL</text></type></parameters><text>+4510001000</text></tel><email><parameters><type><text>WORK</text><text>INTERNET</text></type></parameters><text>user@first.dk</text></email></vcard></vcards>";
		
		String parsedVal = vCardParser.parseStringContent(strToParse);
		
		String parsedName = vCardParser.getName(strToParse);
		System.out.println(parsedName + "\n\n");
		
		assertEquals(parsedVal, expectedVal);
	}
	
	@Test
	public void parseEmptyString() {
		String strToParse = "";
		String expectedVal = null;
		
		String parsedVal = vCardParser.parseStringContent(strToParse);
		
		assertEquals(parsedVal, expectedVal);
	}
	
	@Test
	public void parseUrl() {
		String strToParse = "http://www.wikipedia.org";
		String expectedVal = null;
		
		String parsedVal = vCardParser.parseStringContent(strToParse);
		
		assertEquals(parsedVal, expectedVal);
	}
	
	@Test
	public void parseText() {
		String strToParse = "This is some random text which, when parsed, should result to null";
		String expectedVal = null;
		
		String parsedVal = vCardParser.parseStringContent(strToParse);
		
		assertEquals(parsedVal, expectedVal);
	}
	
	@Test
	public void checkIfContainsName() {
		// NOTE: this method serves can serve as a proof of concept for the future improvements
		
		String strToParse = "BEGIN:VCARD\n"
				+ "VERSION:3.0\n"
				+ "N:Second;User2\n"
				+ "FN:User2 Second\n"
				+ "ORG:Thales\n"
				+ "TITLE:Engineer\n"
				+ "ADR:;;12 Ayer Rajah Crescent;Singapore;;139941;Singapore\n"
				+ "TEL;CELL:+6510001000\n"
				+ "EMAIL;WORK;INTERNET:user@second.sg\n"
				+ "END:VCARD";
		String expectedVal = "User2 Second";
		
		String parsedVal = vCardParser.getName(strToParse);
		
		assertEquals(parsedVal.contains(expectedVal), true);
	}
}
