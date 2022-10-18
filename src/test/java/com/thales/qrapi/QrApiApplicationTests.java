package com.thales.qrapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QrApiApplicationTests {

	@Test
	void contextLoads() {
	}
	
//	// NOTE: Use this method in unit testing qrCodes
//		public String testMethod() {
//			
//			String sol = "";
//			try {
//				String projectPath = System.getProperty("user.dir");
//				byte[] bytes = Files.readAllBytes(Paths.get(projectPath + "/src/test/resources/static/qrcodes/url5.png"));
////				byte[] bytes = file.getBytes();
//				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//				BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
//				BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
//						new BufferedImageLuminanceSource(bufferedImage)));
//		        // QRCodeReader reader = new QRCodeReader();
//		        MultiFormatReader multiFormatReader = new MultiFormatReader();
//		        
//		        Result res = multiFormatReader.decode(binaryBitmap);
//		        sol = res.getText();
//			} catch (Exception exc) {
//				exc.printStackTrace();
//			}
//
//			System.out.println(sol);
//			VCard vcard = Ezvcard.parse(sol).first();
//
//			String xmlStringForm = Ezvcard.writeXml(vcard).go();
//
//			return xmlStringForm;
//
//		}

}
