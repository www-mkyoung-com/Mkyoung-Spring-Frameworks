<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>

	<c:if test="${not empty exception.errCode}">
		<h1>${exception.errCode} : System Errors</h1>
	</c:if>
	
	<c:if test="${empty exception.errCode}">
		<h1>System Errors</h1>
	</c:if>

	<c:if test="${not empty exception.errMsg}">
		<h4>${exception.errMsg}</h4>
	</c:if>
	
</body>
</html>