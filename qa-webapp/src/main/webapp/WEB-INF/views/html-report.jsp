<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html lang="en">
<head>
<meta charset="UTF-8" />
<title>AddEmployee-Input Test Inputs</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css" />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<style type="text/css">
.bs-example {
	margin: 20px;
}

thead {
	background-color: #00B4E9;
}

.fixed-table .header-fixed {
	position: absolute;
	top: 0px;
	z-index: 1020; /* 10 less than .navbar-fixed to prevent any overlap */
	border-bottom: 2px solid #d5d5d5;
	-webkit-border-radius: 0;
	-moz-border-radius: 0;
	border-radius: 0;
}

.fixed-table {
	display: block;
	position: relative;
}

.fixed-table th {
	padding: 8px;
	line-height: 18px;
	text-align: left;
}

.fixed-table .table-content {
	display: block;
	position: relative;
	height: 300px; /*FIX THE HEIGHT YOU NEED*/
	overflow-y: auto;
	/*border: 1px solid;*/
	text-align: center;
	padding-left: 5px;
}

.fixed-table .header-copy {
	position: absolute;
	top: 0;
	left: 0;
}

.fixed-table .header-copy th {
	background-color: #fff;
}
</style>
</head>
<body style="color: #2F6A79">
	<div class="container" style="border: 1px solid; height: 390px;">
		<div class="row"
			style="background-color: #00B4E9; border-bottom: 1px solid;">
			<h1 style="text-align: center;">
				<B>Elements in <a href="${url}">Test-Url</a></B>
			</h1>
		</div>
		<div class="row fixed-table" style="height: 330px; overflow-y: auto;">
			<div class="table-content">
				<p></p>
				<table
					class="table table-striped table-bordered table-hover table-fixed"
					style="border: 1px solid;">
					<thead>
						<tr>
							<th style="text-align: center;">Id</th>
							<th style="text-align: center;">Tag</th>
							<th style="text-align: center;">Test</th>
							<th style="text-align: center;">Name</th>
							<th style="text-align: center;">Type</th>
							<th style="text-align: center;">Value</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${elements}" var="element">
							<tr>
								<td>${element.id}</td>
								<td>${element.tag}</td>
								<td>${element.text}</td>
								<td>${element.name}</td>
								<td>${element.type}</td>
								<td>${element.value}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>