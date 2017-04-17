<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<style>
.error {
	color: #ff0000;
}
.errorblock{
	color: #000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding:8px;
	margin:16px;
}
</style>
</head>

<body>
<h2>Spring's form select, option, options example</h2>

<form:form method="POST" commandName="customerForm">
<form:errors path="*" cssClass="errorblock" element="div"/>
<table>

<tr>
<td>Country : </td>
<td>
<form:select path="country">
	<form:option value="NONE" label="--- Select ---"/>
	<form:options items="${countryList}" />
</form:select>
</td>
<td><form:errors path="country" cssClass="error" /></td>
</tr>

<tr>
<td>Java Skills : </td>
<td>
<form:select path="javaSkills" items="${javaSkillsList}" multiple="true" />
</td>
<td><form:errors path="javaSkills" cssClass="error" /></td>
</tr>

<tr>
<td colspan="3"><input type="submit" /></td>
</tr>
</table>
</form:form>

</body>
</html>