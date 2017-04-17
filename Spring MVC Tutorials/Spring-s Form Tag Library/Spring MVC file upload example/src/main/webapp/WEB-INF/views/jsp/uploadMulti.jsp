<html>

<body>
<h1>Spring MVC multi files upload example</h1>

<form method="POST" action="${pageContext.request.contextPath}/uploadMulti" enctype="multipart/form-data">
    <input type="file" name="files" /><br/>
    <input type="file" name="files" /><br/>
    <input type="file" name="files" /><br/><br/>
    <input type="submit" value="Submit" />
</form>

</body>
</html>