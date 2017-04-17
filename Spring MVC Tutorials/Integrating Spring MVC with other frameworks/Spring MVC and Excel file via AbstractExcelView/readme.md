Spring MVC comes with **AbstractExcelView** class to export data to Excel file via **Apache POI** library. In this tutorial, it show the use of **AbstractExcelView** class in Spring MVC application to export data to Excel file for download.

## 1\. Apache POI

Get the [Apache POI library](http://poi.apache.org/) to create the excel file.

    <!-- Excel library -->
       <dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>3.6</version>
       </dependency>

## 2\. Controller

A controller class, generate dummy data for demonstration, and get the request parameter to determine which view to return. If the request parameter is equal to “EXCEL”, then return an Excel view (**AbstractExcelView**).

_File : RevenueReportController.java_

    package com.mkyong.common.controller;

    import java.util.HashMap;
    import java.util.Map;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.web.bind.ServletRequestUtils;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.AbstractController;

    public class RevenueReportController extends AbstractController{

    	@Override
    	protected ModelAndView handleRequestInternal(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		String output =
    			ServletRequestUtils.getStringParameter(request, "output");

    		//dummy data
    		Map<String,String> revenueData = new HashMap<String,String>();
    		revenueData.put("Jan-2010", "$100,000,000");
    		revenueData.put("Feb-2010", "$110,000,000");
    		revenueData.put("Mar-2010", "$130,000,000");
    		revenueData.put("Apr-2010", "$140,000,000");
    		revenueData.put("May-2010", "$200,000,000");

    		if(output ==null || "".equals(output)){
    			//return normal view
    			return new ModelAndView("RevenueSummary","revenueData",revenueData);

    		}else if("EXCEL".equals(output.toUpperCase())){
    			//return excel view
    			return new ModelAndView("ExcelRevenueSummary","revenueData",revenueData);

    		}else{
    			//return normal view
    			return new ModelAndView("RevenueSummary","revenueData",revenueData);

    		}
    	}
    }

## 3\. AbstractExcelView

Create an Excel view by extends the **AbstractExcelView** class, and override the **buildExcelDocument()** method to populate the data to Excel file. The **AbstractExcelView** is using the Apache POI API to create the Excel file detail.

**Note**  
For detail about how to use the Apache POI , please refer to [Apache POI documentation](http://poi.apache.org/)

_File : ExcelRevenueReportView.java_

    package com.mkyong.common.view;

    import java.util.Map;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.apache.poi.hssf.usermodel.HSSFRow;
    import org.apache.poi.hssf.usermodel.HSSFSheet;
    import org.apache.poi.hssf.usermodel.HSSFWorkbook;
    import org.springframework.web.servlet.view.document.AbstractExcelView;

    public class ExcelRevenueReportView extends AbstractExcelView{

    	@Override
    	protected void buildExcelDocument(Map model, HSSFWorkbook workbook,
    		HttpServletRequest request, HttpServletResponse response)
    		throws Exception {

    		Map<String,String> revenueData = (Map<String,String>) model.get("revenueData");
    		//create a wordsheet
    		HSSFSheet sheet = workbook.createSheet("Revenue Report");

    		HSSFRow header = sheet.createRow(0);
    		header.createCell(0).setCellValue("Month");
    		header.createCell(1).setCellValue("Revenue");

    		int rowNum = 1;
    		for (Map.Entry<String, String> entry : revenueData.entrySet()) {
    			//create the row data
    			HSSFRow row = sheet.createRow(rowNum++);
    			row.createCell(0).setCellValue(entry.getKey());
    			row.createCell(1).setCellValue(entry.getValue());
                    }
    	}
    }

**Note**  
Alternatively, you can use the **AbstractJExcelView**, which is using the **JExcelAPI** to create the same Excel view, see this [AbstractJExcelView example](http://www.mkyong.com/spring-mvc/spring-mvc-export-data-to-excel-file-via-abstractjexcelview/).

## 4\. Spring Configuration

Create a **XmlViewResolver** for the Excel view.

    <beans ...>

      <bean
      class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

    	<bean class="com.mkyong.common.controller.RevenueReportController" />

    	<bean class="org.springframework.web.servlet.view.XmlViewResolver">
    		<property name="location">
    			<value>/WEB-INF/spring-excel-views.xml</value>
    		</property>
    	</bean>

    </beans>

_File : spring-excel-views.xml_

    <bean id="ExcelRevenueSummary"
    	class="com.mkyong.common.view.ExcelRevenueReportView">
    </bean>

## 5\. Demo

URL : **http://localhost:8080/SpringMVC/revenuereport.htm?output=excel**

It generates an Excel file for user to download.

![SpringMVC-ExcelFile-Example](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-ExcelFile-Example.jpg)

[http://www.mkyong.com/spring-mvc/spring-mvc-export-data-to-excel-file-via-abstractexcelview/](http://www.mkyong.com/spring-mvc/spring-mvc-export-data-to-excel-file-via-abstractexcelview/)
