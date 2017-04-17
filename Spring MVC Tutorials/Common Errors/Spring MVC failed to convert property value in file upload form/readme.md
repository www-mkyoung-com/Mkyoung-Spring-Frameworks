## Problem

In Spring MVC application, while clicking on the file upload button, it hits the following property type conversion error?

_Failed to convert property value of type [org.springframework.web.multipart.commons.CommonsMultipartFile] to required type [byte[]] for property file; nested exception is java.lang.IllegalArgumentException: Cannot convert value of type [org.springframework.web.multipart.commons.CommonsMultipartFile] to required type [byte] for property file[0]: PropertyEditor [org.springframework.beans.propertyeditors.CustomNumberEditor] returned inappropriate value_

Here’s the SimpleFormController …

    public class FileUploadController extends SimpleFormController{

    	public FileUploadController(){
    		setCommandClass(FileUpload.class);
    		setCommandName("fileUploadForm");
    	}
    	//...

    public class FileUpload{

    	byte[] file;
    	//...
    }

## Solution

This is a common issue in handling the uploaded file in Spring MVC, which is unable to convert the uploaded file into byte arrays automatically. To make it work, you have to register a custom editor (**ByteArrayMultipartFileEditor**) in the SimpleFormController’s **initBinder()** method to guide Spring to convert the uploaded file into byte array.

    public class FileUploadController extends SimpleFormController{

    public FileUploadController(){
    	setCommandClass(FileUpload.class);
    	setCommandName("fileUploadForm");
    }

           @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
    	throws ServletException {

    	// Convert multipart object to byte[]
    	binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());

    }
    //...

## Problem

In Spring MVC application, while clicking on the file upload button, it hits the following property type conversion error?

_Failed to convert property value of type [org.springframework.web.multipart.commons.CommonsMultipartFile] to required type [byte[]] for property file; nested exception is java.lang.IllegalArgumentException: Cannot convert value of type [org.springframework.web.multipart.commons.CommonsMultipartFile] to required type [byte] for property file[0]: PropertyEditor [org.springframework.beans.propertyeditors.CustomNumberEditor] returned inappropriate value_

Here’s the SimpleFormController …

    public class FileUploadController extends SimpleFormController{

    	public FileUploadController(){
    		setCommandClass(FileUpload.class);
    		setCommandName("fileUploadForm");
    	}
    	//...

    public class FileUpload{

    	byte[] file;
    	//...
    }

## Solution

This is a common issue in handling the uploaded file in Spring MVC, which is unable to convert the uploaded file into byte arrays automatically. To make it work, you have to register a custom editor (**ByteArrayMultipartFileEditor**) in the SimpleFormController’s **initBinder()** method to guide Spring to convert the uploaded file into byte array.

    public class FileUploadController extends SimpleFormController{

    public FileUploadController(){
    	setCommandClass(FileUpload.class);
    	setCommandName("fileUploadForm");
    }

           @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
    	throws ServletException {

    	// Convert multipart object to byte[]
    	binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());

    }
    //...

[http://www.mkyong.com/spring-mvc/spring-mvc-failed-to-convert-property-value-in-file-upload-form/](http://www.mkyong.com/spring-mvc/spring-mvc-failed-to-convert-property-value-in-file-upload-form/)
