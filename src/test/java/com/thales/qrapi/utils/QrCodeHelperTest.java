package com.thales.qrapi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.thales.qrapi.dtos.enums.QrCodeContentType;
import com.thales.qrapi.entities.QrCode;
import com.thales.qrapi.entities.Vcard;
import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.DbApiException;
import com.thales.qrapi.exceptions.ServerApiException;

// We are interested in ContentType, textContent, and vCard behavior after QrCodeHelper generates a QrCode object
// If an exception occurs during a test the test is set to fail. That is unless the exception is expected within the test.
@SpringBootTest
public class QrCodeHelperTest {
	
	@Autowired
	private ApiQrCodeHelper qrCodeHelper;
	
	@Test
	public void readQrCodesContainingText() {
		// 3 QrCode files to be decoded with their content of type TEXT
		Map<String, String> expectedTexts = Map.of(
			"qrcode7.png", "This is the first text created for testing",
			"qrcode8.png", "This is the second text created for testing",
			"qrcode9.png", "This is the third text created for testing"
		);
		QrCodeContentType expectedContentType = QrCodeContentType.TEXT;
		Vcard expectedVcard = null;
		
		try {
			for (Entry<String, String> entry : expectedTexts.entrySet()) {
				QrCode qrCode = generateQrCode(entry.getKey());
				
				assertEquals(qrCode.getContentType(), expectedContentType.getValue());
				assertEquals(qrCode.getTextContent(), entry.getValue());
				assertEquals(qrCode.getvCard(), expectedVcard);
			}
		} catch (Exception exc) {
			assertEquals(true, false);
		}
	}
	
	@Test
	public void readQrCodesContainingUrl() {
		// 3 QrCode files to be decoded with their content of type URL
		Map<String, String> expectedUrls = Map.of(
			"qrcode1.png", "https://en.wikipedia.org/wiki/Bird",
			"qrcode2.png", "https://en.wikipedia.org/wiki/Sun",
			"qrcode3.png", "https://en.wikipedia.org/wiki/Moon"
		);
		QrCodeContentType expectedContentType = QrCodeContentType.URL;
		Vcard expectedVcard = null;
		
		try {
			for (Entry<String, String> entry : expectedUrls.entrySet()) {
				QrCode qrCode = generateQrCode(entry.getKey());
				
				assertEquals(qrCode.getContentType(), expectedContentType.getValue());
				assertEquals(qrCode.getTextContent(), entry.getValue());
				assertEquals(qrCode.getvCard(), expectedVcard);
			}
		} catch (Exception exc) {
			assertEquals(true, false);
		}
	}
	
	@Test
	public void readQrCodesContainingVcard() {
		// 3 QrCode files to be decoded with their content VCARD
		Map<String, Vcard> expectedVcards = Map.of(
			"qrcode4.png", new Vcard("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><vcards xmlns=\"urn:ietf:params:xml:ns:vcard-4.0\"><vcard><prodid><text>ez-vcard 0.11.3</text></prodid><n><surname>First</surname><given>User1</given><additional/><prefix/><suffix/></n><fn><text>User1 First</text></fn><org><text>Thales</text></org><title><text>Engineer</text></title><adr><pobox/><ext/><street>Blegdamsvej 17</street><locality>København</locality><region/><code>2100</code><country>Denmark</country></adr><tel><parameters><type><text>CELL</text></type></parameters><text>+4510001000</text></tel><email><parameters><type><text>WORK</text><text>INTERNET</text></type></parameters><text>user@first.dk</text></email></vcard></vcards>"),
			"qrcode5.png", new Vcard("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><vcards xmlns=\"urn:ietf:params:xml:ns:vcard-4.0\"><vcard><prodid><text>ez-vcard 0.11.3</text></prodid><n><surname>Second</surname><given>User2</given><additional/><prefix/><suffix/></n><fn><text>User2 Second</text></fn><org><text>Thales</text></org><title><text>Engineer</text></title><adr><pobox/><ext/><street>12 Ayer Rajah Crescent</street><locality>Singapore</locality><region/><code>139941</code><country>Singapore</country></adr><tel><parameters><type><text>CELL</text></type></parameters><text>+6510001000</text></tel><email><parameters><type><text>WORK</text><text>INTERNET</text></type></parameters><text>user@second.sg</text></email></vcard></vcards>"),
			"qrcode6.png", new Vcard("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><vcards xmlns=\"urn:ietf:params:xml:ns:vcard-4.0\"><vcard><prodid><text>ez-vcard 0.11.3</text></prodid><n><surname>Third</surname><given>User3</given><additional/><prefix/><suffix/></n><fn><text>User3 Third</text></fn><org><text>Thales</text></org><title><text>Engineer</text></title><adr><pobox/><ext/><street>12 Ayer Rajah Crescent</street><locality>Singapore</locality><region/><code>139941</code><country>Singapore</country></adr><tel><parameters><type><text>CELL</text></type></parameters><text>+6520002000</text></tel><email><parameters><type><text>WORK</text><text>INTERNET</text></type></parameters><text>user@thrid.sg</text></email></vcard></vcards>")
		);
		QrCodeContentType expectedContentType = QrCodeContentType.VCARD;
		String expectedText = null;
		
		try {
			for (Entry<String, Vcard> entry : expectedVcards.entrySet()) {
				QrCode qrCode = generateQrCode(entry.getKey());
				
				assertEquals(qrCode.getContentType(), expectedContentType.getValue());
				assertEquals(qrCode.getTextContent(), expectedText);
				assertEquals(qrCode.getvCard(), entry.getValue());
			}
		} catch (Exception exc) {
			assertEquals(true, false);
		}
	}
	
	@Test
	public void readFaultyQrCode() {
		
		String qrCodeFaulty = "bird.jpeg";
		Class[] allowedExceptions = new Class[] {BadRequestApiException.class, ServerApiException.class, DbApiException.class} ;
		
		try {
			generateQrCode(qrCodeFaulty);
			
			assertEquals(true, false);	// NOTE: if the qrCode is read without exception being thrown, the test should fail
		} catch (Exception exc) {
			// NOTE: exception must be one of the following for the test to pass: BadRequestApiException, ServerApiException, DbApiException
			boolean isValidException = Arrays.asList(allowedExceptions).contains(exc.getClass());
			assertEquals(isValidException, true);
		}
	}
	
	@Test
	public void readFaultyFile() {
		String qrCodeFaulty = "faultyFile.txt";
		Class[] allowedExceptions = new Class[] {BadRequestApiException.class, ServerApiException.class, DbApiException.class} ;
		
		try {
			generateQrCode(qrCodeFaulty);
			
			assertEquals(true, false);	// NOTE: if the qrCode is read without exception being thrown, the test should fail
		} catch (Exception exc) {
			// NOTE: exception must be one of the following for the test to pass: BadRequestApiException, ServerApiException, DbApiException
			boolean isValidException = Arrays.asList(allowedExceptions).contains(exc.getClass());
			System.out.println(exc.getClass());
			assertEquals(isValidException, true);
		}
	}
	
	// Private functions
	private QrCode generateQrCode(String fileName) throws BadRequestApiException, ServerApiException, DbApiException {
		
		try {
			String projectPath = System.getProperty("user.dir");
			byte[] bytes = Files.readAllBytes(Paths.get(projectPath + "/src/test/resources/static/qrcodes/" + fileName));
			
			QrCode qrCode = qrCodeHelper.generateNewQrCode(bytes, fileName);
			return qrCode;
			
		} catch(IOException exc) {			// NOTE: specific errors/exceptions will be logged on lower levels
			throw new BadRequestApiException();
		}
	}
}
