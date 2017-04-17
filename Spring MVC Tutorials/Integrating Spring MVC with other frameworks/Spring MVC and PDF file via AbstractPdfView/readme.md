Spring MVC comes with **AbstractPdfView** class to export data to pdf file via Bruno Lowagie’s **iText** library. In this tutorial, it show the use of **AbstractPdfView** class in Spring MVC application to export data to pdf file for download.

## 1\. iText

Get the [iText library](http://www.lowagie.com/iText/) to generate the pdf file.

    <!-- Pdf library -->
        <dependency>
    <groupId>com.lowagie</groupId>
    <artifactId>itext</artifactId>
    <version>2.1.7</version>
        </dependency>

## 2\. Controller

A controller class, generate dummy data for demonstration, and get the request parameter to determine which view to return. If the request parameter is equal to “**PDF**“, then return an Pdf view (**AbstractPdfView**).

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
    		revenueData.put("1/20/2010", "$100,000");
    		revenueData.put("1/21/2010", "$200,000");
    		revenueData.put("1/22/2010", "$300,000");
    		revenueData.put("1/23/2010", "$400,000");
    		revenueData.put("1/24/2010", "$500,000");

    		if(output ==null || "".equals(output)){
    		    //return normal view
    		    return new ModelAndView("RevenueSummary","revenueData",revenueData);

    		}else if("PDF".equals(output.toUpperCase())){
    		    //return excel view
    		    return new ModelAndView("PdfRevenueSummary","revenueData",revenueData);

    		}else{
    		    //return normal view
    		    return new ModelAndView("RevenueSummary","revenueData",revenueData);

    		}
    	}
    }

## 3\. PdfRevenueReportView

Create a pdf view by extends the **AbstractPdfView** class, override the **buildExcelDocument()** method to populate the data to pdf file. The **AbstractPdfView** is using the iText API to generate the pdf file.

_File : PdfRevenueReportView.java_

    package com.mkyong.common.view;

    import java.util.Map;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import org.springframework.web.servlet.view.document.AbstractPdfView;
    import com.lowagie.text.Document;
    import com.lowagie.text.Table;
    import com.lowagie.text.pdf.PdfWriter;

    public class PdfRevenueReportView extends AbstractPdfView{

    	@Override
    	protected void buildPdfDocument(Map model, Document document,
    		PdfWriter writer, HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    		Map<String,String> revenueData = (Map<String,String>) model.get("revenueData");

    		Table table = new Table(2);
    		table.addCell("Month");
    		table.addCell("Revenue");

    		for (Map.Entry<String, String> entry : revenueData.entrySet()) {
    			table.addCell(entry.getKey());
    			table.addCell(entry.getValue());
                    }

    		document.add(table);
    	}
    }

## 4\. Spring Configuration

Create a **XmlViewResolver** for the Pdf view.

    <beans ...>

     <bean
      class="org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping" />

        <bean class="com.mkyong.common.controller.RevenueReportController" />

        <bean class="org.springframework.web.servlet.view.XmlViewResolver">
           <property name="location">
              <value>/WEB-INF/spring-pdf-views.xml</value>
           </property>
        </bean>

    </beans>

_File : spring-pdf-views.xml_

    <beans ...">

       <bean id="PdfRevenueSummary"
       	class="com.mkyong.common.view.PdfRevenueReportView">
       </bean>

    </beans>

## 5\. Demo

URL : **http://localhost:8080/SpringMVC/revenuereport.htm?output=pdf**

It generates a pdf file for user to download.

![SpringMVC-PDF-Example](http://www.mkyong.com/wp-content/uploads/2010/08/SpringMVC-PDF-Example.jpg)

[http://www.mkyong.com/spring-mvc/spring-mvc-export-data-to-pdf-file-via-abstractpdfview/](http://www.mkyong.com/spring-mvc/spring-mvc-export-data-to-pdf-file-via-abstractpdfview/)
