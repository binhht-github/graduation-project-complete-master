package com.spring.service.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.repository.BookingRepository;

@Service
public class exportServices {
	@Autowired
	BookingRepository bookingRepository;

	public ResponseEntity<Object> test(Long idBooking) throws IOException, InvalidFormatException {
		List<Object[]> list1 = new ArrayList<>();
		List<Object[]> list2 = new ArrayList<>();

		list1 = bookingRepository.exportHoaDon(idBooking);
		list2 = bookingRepository.exportHoaDon2(idBooking);

		System.out.println(list1.size() + list2.size());

		URL url = new URL("https://character-text.herokuapp.com/convert/string-character");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setReadTimeout(10000);
		con.setConnectTimeout(15000);
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod("GET");
		List<NameValuePair> params = new ArrayList<>();
		System.out.println(list1.size());
		params.add(new BasicNameValuePair("string", (list1.size() <= 0 ? "Hóa Đơn" : list1.get(0)[0]) + ""));

		OutputStream os = con.getOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		writer.write(getQuery(params));
		writer.flush();
		writer.close();
		os.close();

		con.connect();

		// Create new document
		XWPFDocument document = new XWPFDocument();

		XWPFParagraph logo = document.createParagraph();
		XWPFRun logoRun = logo.createRun();
		String imgFile = "logo.png";
		FileInputStream is = new FileInputStream(imgFile);
		logoRun.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, imgFile, Units.toEMU(100), Units.toEMU(100)); // 200x200
											
		try {
			CTDrawing drawing = logoRun.getCTR().getDrawingArray(0);
			  CTGraphicalObject graphicalobject = drawing.getInlineArray(0).getGraphic();
			  CTAnchor anchor = getAnchorWithGraphic(graphicalobject, imgFile, 
			                                         Units.toEMU(120), Units.toEMU(100), 
			                                         Units.toEMU(20), Units.toEMU(10));
			  drawing.setAnchorArray(new CTAnchor[]{anchor});
			  drawing.removeInline(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		XWPFParagraph tenbv = document.createParagraph();
		XWPFRun ltenbvRun = tenbv.createRun();
		ltenbvRun.setText("NHA KHOA SMILE DENTAL");
		tenbv.setAlignment(ParagraphAlignment.RIGHT);
		ltenbvRun.setFontSize(25);
		ltenbvRun.setColor("0876C3");
		ltenbvRun.addBreak();
		XWPFRun daichi2 = tenbv.createRun();
		daichi2.setText("Địa chỉ số 1 Trịnh Văn Bô, Nam Từ Niêm, Hà Nội");
		daichi2.setColor("0876C3");
		daichi2.addBreak();
		daichi2.addBreak();
		daichi2.addBreak();
		tenbv.setAlignment(ParagraphAlignment.RIGHT);
		// Create new Paragraph
		XWPFParagraph tieude = document.createParagraph();
		XWPFRun tieudeRun = tieude.createRun();
		tieudeRun.setColor("0876C3");
		tieudeRun.setText("HÓA ĐƠN KHÁM BỆNH");
		tieudeRun.setFontSize(25);
		tieudeRun.setBold(true);
		tieude.setAlignment(ParagraphAlignment.CENTER);
		tieude.setSpacingAfter(1000);

		XWPFParagraph hoten = document.createParagraph();
		XWPFRun run = hoten.createRun();
		run = hoten.createRun();
//		run.setColor("0876C3");
		run.setText("Họ tên bệnh nhân: " + (list1.size() <= 0 ? "" : list1.get(0)[0] + ""));
		run.addBreak();
		run.setText("Số điện thoại:   " + (list1.size() <= 0 ? "" : list1.get(0)[1] + ""));
		run.addBreak();
		run.setText("Địa chỉ:  " + (list1.size() <= 0 ? "" : list1.get(0)[2] + ""));
		run.addBreak();
		run.addBreak();
		run.setText("Dịch vụ đã khám");

		XWPFTable tb = document.createTable();
		tb.setCellMargins(200, 200, 0, 0);
//		tb.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "0876C3");
//		tb.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "0876C3");
		CTTblWidth width = tb.getCTTbl().addNewTblPr().addNewTblW();
		width.setType(STTblWidth.DXA);
		width.setW(BigInteger.valueOf(9072));
		XWPFTableRow row = tb.getRow(0);
//		row.getCell(0).setColor(null)
//		row.getCell(0).setColor("0876C3");
//		row.getCell(0).setText("Version");
		tb.setColBandSize(250);
		row.setHeight(25);
		XWPFTableCell cell = row.getCell(0);
		cell.setText("Tên dịch vụ");

		cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
		XWPFTableCell cell1 = row.createCell();
		cell1.setText("Giá");

		if (list2.size() > 0 && list1.size() > 0) {
			for (Object[] obj : list2) {
				XWPFTableRow r = tb.createRow();
				XWPFTableCell c = r.getCell(0);
				c.setText(obj[0] + "");
				c.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
				XWPFTableCell c2 = r.getCell(1);
				c2.setText(obj[1] + "");
				c2.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
			}
		}

		XWPFParagraph paragraph4 = document.createParagraph();
		run = paragraph4.createRun();
//		run.setColor("0876C3");
		run.setText("Tổng tiền: " + (list1.size() <= 0 ? "" : list1.get(0)[3] + " VND"));

		XWPFParagraph ngaythangnam = document.createParagraph();
		ngaythangnam.setIndentationFirstLine(5000);
		run = ngaythangnam.createRun();
//		run.setColor("0876C3");
		run.setText("Ngày " + java.time.LocalDateTime.now().getDayOfMonth() + " tháng "
				+ java.time.LocalDateTime.now().getMonthValue() + " năm " + java.time.LocalDateTime.now().getYear());
		ngaythangnam.setAlignment(ParagraphAlignment.CENTER);

		XWPFParagraph chuky = document.createParagraph();
		chuky.setIndentationFirstLine(5000);
		chuky.setAlignment(ParagraphAlignment.CENTER);
		run = chuky.createRun();
//		run.setColor("0876C3");
		run.setText("Bác sĩ:");
		run.addBreak();
		XWPFParagraph chuky2 = document.createParagraph();
		chuky2.setIndentationFirstLine(5000);
		chuky2.setAlignment(ParagraphAlignment.CENTER);
		run = chuky2.createRun();
//		run.setColor("0876C3");
		run.setText((list1.size() <= 0 ? "" : list1.get(0)[4] + ""));
		
		

		// Write the Document in file system
		File f = new File("Hoa Don.docx");
		FileOutputStream out = new FileOutputStream(f);
		document.write(out);
		try {
			InputStreamResource resource = new InputStreamResource(new FileInputStream(f));
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"",
					(con.getHeaderField("string") != null ? con.getHeaderField("string") : "Hoa-Don") + ".docx"));
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(f.length())
					.contentType(MediaType.parseMediaType("application/txt")).body(resource);

			System.out.println("successfully");
			return responseEntity;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			out.close();
			document.close();
			con.disconnect();
		}
	}

	private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	private static CTAnchor getAnchorWithGraphic(CTGraphicalObject graphicalobject, String drawingDescr, int width,
			int height, int left, int top) throws Exception {

		String anchorXML = "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
				+ "simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"1\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
				+ "<wp:simplePos x=\"0\" y=\"0\"/>" + "<wp:positionH relativeFrom=\"column\"><wp:posOffset>" + left
				+ "</wp:posOffset></wp:positionH>" + "<wp:positionV relativeFrom=\"paragraph\"><wp:posOffset>" + top
				+ "</wp:posOffset></wp:positionV>" + "<wp:extent cx=\"" + width + "\" cy=\"" + height + "\"/>"
				+ "<wp:effectExtent l=\"0\" t=\"0\" r=\"0\" b=\"0\"/>" + "<wp:wrapTight wrapText=\"bothSides\">"
				+ "<wp:wrapPolygon edited=\"0\">" + "<wp:start x=\"0\" y=\"0\"/>" + "<wp:lineTo x=\"0\" y=\"21600\"/>"
				// Square// polygon// 21600// x// 21600// leads// to// wrap// points// in// fully// width// x// height
				+ "<wp:lineTo x=\"21600\" y=\"21600\"/>"// Why? I don't know. Try & error ;-).
				+ "<wp:lineTo x=\"21600\" y=\"0\"/>" + "<wp:lineTo x=\"0\" y=\"0\"/>" + "</wp:wrapPolygon>"
				+ "</wp:wrapTight>" + "<wp:docPr id=\"1\" name=\"Drawing 0\" descr=\"" + drawingDescr
				+ "\"/><wp:cNvGraphicFramePr/>" + "</wp:anchor>";

		CTDrawing drawing = CTDrawing.Factory.parse(anchorXML);
		CTAnchor anchor = drawing.getAnchorArray(0);
		anchor.setGraphic(graphicalobject);
		return anchor;
	}
}
