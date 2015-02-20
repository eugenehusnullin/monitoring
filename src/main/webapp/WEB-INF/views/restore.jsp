<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Restore from table municrawdata</title>
</head>
<body>

	<c:if test="${result != null }">
		<br>${result}
	</c:if>
	<h1>Post correct dates</h1>
	<h1>${inject}</h1>

	<form method="POST">
		<input type="text" name="date1" value="dd-MM-yyyy HH:mm:ss"/>
		<input type="text" name="date2" value="dd-MM-yyyy HH:mm:ss"/>
		<br /> <input type="submit" value="try restore" />
	</form>

</body>
</html>
