package com.adesso.digitalwash.model;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.adesso.digitalwash.model.Category;

public class PdfFactory {

	private final float headerFontSize = 8.5f, fontSize = 11, windowHeaderFontSize = 6.8f, lineHeight = 10.5f,
			distanceToRight = mm2pt(45), distanceToLeft = mm2pt(26), distanceTopRight = mm2pt(27),
			distanceTopLeft = mm2pt(49), distanceToTopFullLength = mm2pt(210);
	private final String place = "Köln", address = "Agrippinawerft 26", zip = "50678", phone = "0221-27850-0",
			fax = "0221-27850-500";
	private float pageHeight, pageWidth;
	private PDFont fontNorm, fontBold, fontLight;
	private Color adessoBlue = new Color(0, 110, 199), warmGrey = new Color(136, 125, 117);

	/**
	 * Creates a file containing a table with the headlines and content provided.
	 * The returned OutputStream contains the pdf file as a byte array.
	 *
	 * @param headers The table headlines
	 * @param content The table content
	 * @return
	 */
	public ByteArrayOutputStream createPdf(String[] headers, ArrayList<String[]> content, LocalDate date,
			Collection<Category> categories, String owner, boolean isShirts, boolean showMailContent) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (PDDocument doc = new PDDocument()) {
			String fontResourceNorm = PdfFactory.class.getResource("/arial.ttf").getFile();
			String fontResourceBold = PdfFactory.class.getResource("/arialbd.ttf").getFile();
			String fontResourceLight = PdfFactory.class.getResource("/arialn.ttf").getFile();
			fontNorm = PDType0Font.load(doc, new File(fontResourceNorm));
			fontBold = PDType0Font.load(doc, new File(fontResourceBold));
			fontLight = PDType0Font.load(doc, new File(fontResourceLight));
			// do not place more than 35 entries on one DIN A4 page
			for (int i = 0; i < content.size(); i += 35) {
				PDPage page = new PDPage(PDRectangle.A4);
				doc.addPage(page);
				pageHeight = page.getMediaBox().getHeight();
				pageWidth = page.getMediaBox().getWidth();
				PDPageContentStream contentStream = new PDPageContentStream(doc, page);
				contentStream.setLeading(14);
				contentStream.setFont(fontLight, headerFontSize);
				drawRectCI(contentStream);
				contentStream.setNonStrokingColor(warmGrey);
				contentStream.setStrokingColor(warmGrey);
				drawContactInformation(contentStream, showMailContent);
				drawLogo(contentStream, doc, 1);
				drawWindowHeader(contentStream);

				drawDate(contentStream, date, showMailContent);
				if (isShirts == false) {
					drawPrices(contentStream, categories);
				}
				drawTable(contentStream, distanceToLeft, pageHeight - distanceTopRight * 2.25f,
						(pageWidth - distanceToLeft - distanceToRight) * 0.9f, headers,
						content.subList(i, Math.min(content.size(), i + 34)));
				if (showMailContent) {
					drawOwner(contentStream, owner);
				}
				contentStream.close();
				doc.save(baos);
				doc.close();
			}
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		return baos;
	}

	private void drawOwner(PDPageContentStream contentStream, String owner) throws IOException {
		contentStream.beginText();
		contentStream.newLineAtOffset(distanceToLeft, pageHeight - mm2pt(55));
		contentStream.setFont(fontNorm, fontSize);
		contentStream.showText(owner);
		contentStream.endText();
	}

	private void drawRectCI(PDPageContentStream contentStream) throws IOException {
		contentStream.moveTo(0, pageHeight);
		contentStream.setStrokingColor(adessoBlue);
		contentStream.setNonStrokingColor(adessoBlue);
		contentStream.lineTo(0, pageHeight - mm2pt(105));
		contentStream.lineTo(mm2pt(4), pageHeight - mm2pt(101));
		contentStream.lineTo(mm2pt(4), pageHeight);
		contentStream.lineTo(0, pageHeight);
		contentStream.closeAndFillAndStroke();
	}

	private void drawWindowHeader(PDPageContentStream contentStream) throws IOException {
		contentStream.beginText();
		contentStream.newLineAtOffset(distanceToLeft, pageHeight - distanceTopLeft);
		contentStream.setFont(fontBold, windowHeaderFontSize);
		contentStream.showText("adesso AG");
		contentStream.setFont(fontLight, windowHeaderFontSize);
		contentStream.showText(" / " + address + " / " + zip + " " + place);
		contentStream.endText();
	}

	private void drawContactInformation(PDPageContentStream contentStream, boolean showMailContent) throws IOException {
		float startpositionX = pageWidth - distanceToRight;
		drawContactHeader(contentStream, startpositionX);
		if (showMailContent) {
			drawBranchContact(contentStream, startpositionX);
			drawCompanyInformation(contentStream, startpositionX);
		}
	}

	private void drawCompanyInformation(PDPageContentStream contentStream, float startpositionX) throws IOException {
		contentStream.beginText();
		contentStream.newLineAtOffset(startpositionX, pageHeight - distanceToTopFullLength);
		contentStream.newLine();
		contentStream.setFont(fontNorm, headerFontSize);
		contentStream.showText("Vorstand");
		contentStream.setFont(fontLight, headerFontSize);
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("Christoph Junge");
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("Michael Kenfenheuer");
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("Rainer Rudolf (Vorsitzender)");
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("Dr. Rüdiger Striemer");
		contentStream.newLine();
		contentStream.setFont(fontNorm, headerFontSize);
		contentStream.showText("Aufsichtsratvorsitzender");
		contentStream.setFont(fontLight, headerFontSize);
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("Prof. Dr. Volker Gruhn");
		contentStream.newLine();
		contentStream.showText("Amtsgericht Dortmund");
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("HRB 20663");
		contentStream.newLine();
		contentStream.showText("Sparkasse Dortmund");
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("BLZ 440 501 99");
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("Kto. 741 002 809");
		contentStream.newLine();
		contentStream.showText("Dresdner Bank Dortmund");
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("BLZ 440 800 50");
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("Kto. 107 697 800");
		contentStream.newLine();
		contentStream.showText("Ust.-IdNr DE 208 318 527");
		contentStream.endText();
	}

	private void drawBranchContact(PDPageContentStream contentStream, float startpositionX) throws IOException {
		contentStream.beginText();
		contentStream.setFont(fontLight, headerFontSize);
		contentStream.newLineAtOffset(startpositionX, pageHeight - distanceToTopFullLength);
		contentStream.showText("Telefax: " + fax);
		contentStream.newLineAtOffset(0, lineHeight);
		contentStream.showText("Telefon: " + phone);
		contentStream.newLineAtOffset(0, lineHeight);
		contentStream.showText(zip + " " + place);
		contentStream.newLineAtOffset(0, lineHeight);
		contentStream.showText(address);
		contentStream.newLineAtOffset(0, lineHeight);
		contentStream.setFont(fontNorm, headerFontSize);
		contentStream.showText("Geschäftstelle " + place);
		contentStream.setFont(fontLight, headerFontSize);
		contentStream.endText();
	}

	private void drawContactHeader(PDPageContentStream contentStream, float startpositionX) throws IOException {
		contentStream.beginText();
		contentStream.newLineAtOffset(startpositionX, pageHeight - distanceTopRight);
		contentStream.setFont(fontBold, 10);
		contentStream.showText("adesso AG");
		contentStream.setFont(fontLight, headerFontSize);
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("Agrippinawerft 26");
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("50678 Köln");
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("Telefon +49 221-27850-0");
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("Telefax +49 221-27850-500");
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("office-koeln@adesso.de");
		contentStream.newLineAtOffset(0, -lineHeight);
		contentStream.showText("www.adesso.de");
		contentStream.endText();
	}

	private void drawLogo(PDPageContentStream contentStream, PDDocument doc, float imgSize) throws IOException {
		String image = PdfFactory.class.getResource("/logo.jpg").getFile();
		PDImageXObject pdImage = PDImageXObject.createFromFile(image, doc);
		float imgWidth = mm2pt(37.5f), imgHeight = mm2pt(15);
		contentStream.drawImage(pdImage, pageWidth - distanceToRight - imgWidth * imgSize,
				pageHeight - distanceTopRight - imgHeight / 3 * imgSize, imgWidth * imgSize, imgHeight * imgSize);
	}

	private void drawDate(PDPageContentStream contentStream, LocalDate date, boolean showMailContent)
			throws IOException {
		contentStream.beginText();
		contentStream.newLineAtOffset(distanceToLeft, pageHeight - distanceTopRight * 0.5f);
		contentStream.setFont(fontLight, fontSize);
		if (showMailContent) {
			contentStream.showText("Abholbereit ab: " + date.format(DateTimeFormatter.ofPattern("d.M.yyyy")));

		} else {
			contentStream.showText("Abholdatum: " + date.format(DateTimeFormatter.ofPattern("d.M.yyyy")));
		}
		contentStream.endText();
	}

	private void drawPrices(PDPageContentStream contentStream, Collection<Category> categories) throws IOException {
		Category catArray[] = categories.toArray(new Category[categories.size()]);
		contentStream.beginText();
		contentStream.setFont(fontLight, fontSize);
		if (categories.size() <= 5) {
			contentStream.newLineAtOffset(distanceToLeft, pageHeight - distanceTopRight * 0.5f - lineHeight * 2);
			for (Category category : categories) {
				contentStream.showText(category.getName());
				contentStream.newLineAtOffset(0, -lineHeight);
			}
		} else {
			contentStream.newLineAtOffset(distanceToLeft, pageHeight - distanceTopRight * 0.5f - lineHeight * 2);
			for (int i = 0; i < 5; i++) {
				contentStream.showText(catArray[i].getName());
				contentStream.newLineAtOffset(0, -lineHeight);
			}
			contentStream.newLineAtOffset(distanceToLeft * 2, lineHeight * 5);
			for (int i = 5; i < categories.size(); i++) {
				contentStream.showText(catArray[i].getName());
				contentStream.newLineAtOffset(0, -lineHeight);
			}
		}
		contentStream.endText();
		contentStream.beginText();
		if (categories.size() <= 5) {
			contentStream.newLineAtOffset(distanceToLeft * 2, pageHeight - distanceTopRight * 0.5f - lineHeight * 2);
			for (Category category : categories) {
				contentStream.showText(category.getName());
				contentStream.newLineAtOffset(0, -lineHeight);
			}
		} else {
			contentStream.newLineAtOffset(distanceToLeft * 2, pageHeight - distanceTopRight * 0.5f - lineHeight * 2);
			for (int i = 0; i < 5; i++) {
				contentStream.showText(String.format(Locale.GERMAN, "%.2f", catArray[i].getPrice().floatValue()) + " €");
				contentStream.newLineAtOffset(0, -lineHeight);
			}
			contentStream.newLineAtOffset(distanceToLeft * 2, lineHeight * 5);
			for (int i = 5; i < categories.size(); i++) {
				contentStream.showText(String.format(Locale.GERMAN, "%.2f", catArray[i].getPrice().floatValue()) + " €");
				contentStream.newLineAtOffset(0, -lineHeight);
			}
		}
		contentStream.endText();
	}

	/**
	 * Draws the provided content on the given pdf page.
	 * 
	 * @param page          The PDPage to modify.
	 * @param contentStream The stream to modify the data with.
	 * @param y             The height of the table
	 * @param margin        The left and right margin of the page
	 * @param headers       The table headlines
	 * @param content       The table content
	 * @throws IOException
	 */
	private void drawTable(PDPageContentStream contentStream, float x, float y, float width, String[] headers,
			List<String[]> content) throws IOException {
		final int rows = content.size() + 1; // +1 for headline
		final int cols = headers.length;
		final float rowHeight = 20f;
		final float tableHeight = rowHeight * rows;

		final double nameColWidth = Math.min((width / cols) * 2, width * 0.5);
		final double otherColWidth = (width - nameColWidth) / (cols - 1);

		// draw the rows
		float nexty = y;
		for (int i = 0; i <= rows; i++) {
			contentStream.moveTo(x, nexty);
			contentStream.lineTo(x + width, nexty);
			contentStream.stroke();
			nexty -= rowHeight;
		}

		// draw the columns
		float nextx = x;
		for (int i = 0; i <= cols; i++) {
			contentStream.moveTo(nextx, y);
			contentStream.lineTo(nextx, y - tableHeight);
			contentStream.stroke();

			if (i == 0)
				nextx += nameColWidth;
			else
				nextx += otherColWidth;
		}

		final float cellMargin = 5.0f;
		float textx = x + cellMargin;
		float texty = y - 15.0f;

		// add the headline
		contentStream.setFont(fontNorm, fontSize);
		for (int i = 0; i < headers.length; i++) {
			contentStream.beginText();
			contentStream.newLineAtOffset(textx, texty);
			contentStream.showText(headers[i]);
			contentStream.endText();
			if (i == 0)
				textx += nameColWidth;
			else
				textx += otherColWidth;
		}

		textx = x + cellMargin;
		texty -= rowHeight;

		// now add the text
		contentStream.setFont(fontLight, fontSize);
		for (final String[] aContent : content) {
			for (int i = 0; i < aContent.length; i++) {
				contentStream.beginText();
				contentStream.newLineAtOffset(textx, texty);
				contentStream.showText(aContent[i]);
				contentStream.endText();
				if (i == 0)
					textx += nameColWidth;
				else
					textx += otherColWidth;
			}
			texty -= rowHeight;
			textx = x + cellMargin;
		}

	}

	private float mm2pt(float mm) {
		return 2.83465f * mm;
	}

}
