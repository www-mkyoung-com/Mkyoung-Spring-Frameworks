Spring MVC comes with **AbstractJExcelView** class to export data to Excel file via **JExcelAPI** library. In this tutorial, it show the use of **AbstractJExcelView** class in Spring MVC application to export data to Excel file for download.

## 1\. JExcelAPI

Get the [JExcelAPI library](http://jexcelapi.sourceforge.net/).

    <!-- JExcelAPI library -->
        <dependency>
    <groupId>net.sourceforge.jexcelapi</groupId>
    <artifactId>jxl</artifactId>
    <version>2.6.3</version>
        </dependency>

## 2\. Controller

A controller class, generate dummy data for demonstration, and get the request parameter to determine which view to return. If the request parameter is equal to “EXCEL”, then return an Excel view (**AbstractJExcelView**).

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

## 3\. AbstractJExcelView

Create an Excel view by extends the **AbstractJExcelView** class, and override the **buildExcelDocument()** method to populate the data to Excel file. The **AbstractJExcelView** is using the JExcelAPI to create the Excel file detail.

**Note**  
For detail about how to use the JExcelAPI , please refer to the [JExcelAPI documentation](http://jexcelapi.sourceforge.net/)

_File : ExcelRevenueReportView.java_

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

**Note**  
Alternatively, you can use the **AbstractExcelView**, which is using the **Apache POI** API to create the same Excel view, see this [AbstractExcelView example](http://www.mkyong.com/spring-mvc/spring-mvc-export-data-to-excel-file-via-abstractexcelview/).

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

    <beans ...">

       <bean id="ExcelRevenueSummary"
       	class="com.mkyong.common.view.ExcelRevenueReportView">
       </bean>

    </beans>

## 5\. Demo

URL : **http://localhost:8080/SpringMVC/revenuereport.htm?output=excel**

It generates an Excel file for user to download.

![SpringMVC-ExcelFile-Example](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-ExcelFile-Example.jpg)

[http://www.mkyong.com/spring-mvc/spring-mvc-export-data-to-excel-file-via-abstractjexcelview/](http://www.mkyong.com/spring-mvc/spring-mvc-export-data-to-excel-file-via-abstractjexcelview/)
