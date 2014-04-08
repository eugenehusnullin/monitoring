<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Utils munic</title>
</head>
<body>

	<c:if test="${result != null }">
		<br>${result}
	</c:if>
	<h1>Re send all old json-messages</h1>

	<form method="POST" action="">
<!-- 		<input type="text" name="value" /> -->
		<textarea id="value" name="value" rows="20" cols="80">${json}</textarea>
		 <br />
		<input type="submit" value="Send" />
	</form>
</body>
</html>
