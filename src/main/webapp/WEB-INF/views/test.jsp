<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Test munic</title>
</head>
<body>

	<c:if test="${result != null }">
		<br>${result}
	</c:if>
	<h1>Post correct json-message</h1>
	<h1>${inject}</h1>

	<form method="POST">
		<!-- 		<input type="text" name="value" /> -->
		<textarea id="value" name="value" rows="20" cols="80">${json}</textarea>
		<br /> <input type="submit" value="Send" />
	</form>

<!-- 	<br /> -->
<!-- 	<form method="POST" action="reprocess"> -->
<!-- 		<input type="submit" value="ReProcess all items" /> -->
<!-- 	</form> -->
</body>
</html>
