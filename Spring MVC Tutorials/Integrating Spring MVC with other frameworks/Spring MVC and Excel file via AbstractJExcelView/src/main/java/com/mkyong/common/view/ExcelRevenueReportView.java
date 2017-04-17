package com.mkyong.common.view;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.springframework.web.servlet.view.document.AbstractJExcelView;

public class ExcelRevenueReportView extends AbstractJExcelView{

	@Override
	protected void buildExcelDocument(Map model, WritableWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Map<String,String> revenueData = (Map<String,String>) model.get("revenueData");
		WritableSheet sheet = workbook.createSheet("Revenue Report", 0);
		
        sheet.addCell(new Label(0, 0, "Month"));
        sheet.addCell(new Label(1, 0, "Revenue"));

        int rowNum = 1;
		for (Map.Entry<String, String> entry : revenueData.entrySet()) {
			//create the row data
			sheet.addCell(new Label(0, rowNum, entry.getKey()));
		    sheet.addCell(new Label(1, rowNum, entry.getValue()));
		    rowNum++;
        }
        
	}
}