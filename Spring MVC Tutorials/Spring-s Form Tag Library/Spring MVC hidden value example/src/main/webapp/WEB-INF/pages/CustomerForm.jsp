<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<body>
<h2>Spring's form hidden example</h2>

<form:form method="POST" commandName="customerForm">
<table>
<tr>
<td>Hidden value (view source to see it) : </td>
<td>
<form:hidden path="secretValue" />
</td>
</tr>

<tr>
<td><input type="submit" /></td>
</tr>
</table>
</form:form>

</body>
</html>