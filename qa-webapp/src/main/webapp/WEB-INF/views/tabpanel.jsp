<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>

<head>
<title>Spring MVC Starter Application</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="resources/css/jQueryTab.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="resources/css/styles.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="resources/css/bootstrap.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="resources/css/bootstrap.min.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="resources/css/bootstrap-theme.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="resources/css/theme.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="resources/css/base.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="resources/css/bootstrap-theme.min.css"/>" />
<script type="text/javascript" src="resources/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="resources/js/bootstrap.js"></script>
<script type="text/javascript">
	$('#myTab a').click(function(e) {
		e.preventDefault();
		$(this).tab('show');
	});
	/* $('#myModal').on('shown.bs.modal', function() {
		$('#myInput').focus();
	}); */
</script>

</head>

<body>
	<div class="container">
		<div class="inner-page-outer">
			<div class="indexHome"
				style="width: 800px; margin: auto; background-color: #FFFFFF; padding: 20px; border-radius: 10px;">
				<ul id="myTab" class="nav nav-tabs">
					<li class="active"><a href="#configuration" data-toggle="tab">Selenium
							Test Properties</a></li>
					<li><a href="#testData" data-toggle="tab">Selenium Test
							Data</a></li>
					<li><a href="#reports" data-toggle="tab">Slenium Test
							Reports</a></li>
				</ul>
				<form:form commandName="config" id="config" action="register">
					<div id="tabContent" class="tab-content">
						<div id="configuration" class="tab-pane fade in active">
							<div class="join-frm clearfix">
								<div class="joinfrm-clm lgnbxfrm">
									<form:select path="browser" name="browsers" id="browser"
										class="form-control spl">
										<form:option value="ie" label="Internet Explorer" />
										<form:option value="chrome" label="Google Chrome" />
										<form:option value="firefox" label="Firefox" />
									</form:select>
								</div>
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="text" placeholder="Desired Browser Version"
										autocomplete="off" path="desiredBrowserVersion"
										class="form-control zip" required="true" />
									<form:errors class="errors" path="desiredBrowserVersion" />
								</div>
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="text"
										placeholder="Screenshots Directory Path" autocomplete="off"
										path="screenshotDirectory" class="form-control usr"
										required="true" />
									<form:errors class="errors" path="screenshotDirectory" />
								</div>
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="text" placeholder="Host" autocomplete="off"
										path="host" class="form-control usr" required="true" />
									<form:errors class="errors" path="host" />
								</div>
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="text" placeholder="Port" autocomplete="off"
										path="port" class="form-control zip" required="true" />
									<form:errors class="errors" path="port" />
								</div>
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="text" placeholder="Root Context of App"
										autocomplete="off" path="context" class="form-control usr"
										required="true" />
									<form:errors class="errors" path="context" />
								</div>
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="text" placeholder="Reports Directory Path"
										autocomplete="off" path="testResultsDir"
										class="form-control usr" required="true" />
									<form:errors class="errors" path="testResultsDir" />
								</div>
								<div class="joinfrm-clm lgnbxfrm">
									<label class="checkbox-inline"> <input type="checkbox"
										name="Enable Video" value="Yes" id="inlineCheckbox1" />
										Enable Video Recording ?
									</label>
								</div>
							</div>
						</div>
						<div id="testData" class="tab-pane fade">
							<div class="join-frm clearfix">
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="text" placeholder="Module Name"
										autocomplete="off" path="moduleName" class="form-control usr"
										required="true" />
									<form:errors class="errors" path="moduleName" />
								</div>
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="text" placeholder="Test URL"
										autocomplete="off" path="testUrl" class="form-control usr"
										required="true" />
									<form:errors class="errors" path="testUrl" />
								</div>
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="text" placeholder="Test Case"
										autocomplete="off" path="testCase" class="form-control usr"
										required="true" />
									<form:errors class="errors" path="testCase" />
								</div>
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="text" placeholder="Test Expected"
										autocomplete="off" path="testExpected"
										class="form-control zip" required="true" />
									<form:errors class="errors" path="testExpected" />
								</div>
								<div class="joinfrm-clm join-frm">
									<a class="btn btn-primary" data-toggle="modal" href="#addInput"
										data-target="#addInput">Add Input <span
										class="glyphicon glyphicon-chevron-right"></span>
									</a>
								</div>
							</div>
						</div>
						<div id="reports" class="tab-pane fade">
							<div class="join-frm clearfix">
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="email" placeholder="Email Address"
										autocomplete="off" path="email" class="form-control email"
										required="true" />
									<form:errors class="errors" path="email" />
								</div>
							</div>
						</div>
					</div>
					<div class="join-frm clearfix">
						<div class="joinfrm-clm join-frm">
							<button class="btn btn-primary" type="submit">
								Cancel <span class="glyphicon glyphicon-chevron-right"></span>
							</button>
							<button class="btn btn-primary" type="submit">
								Save <span class="glyphicon glyphicon-chevron-right"></span>
							</button>
						</div>
					</div>
				</form:form>
				<div class="modal fade" id="addInput" aria-hidden="true"
					style="display: none;">
					<div class="modal-header">
						<a href="#" class="btn btn-success pull-right"
							data-dismiss="modal">×</a>
						<h4>[Test] Add Input for the Module and Case</h4>
					</div>
					<div class="modal-body">
						<form:form commandName="testInput" id="testInput" action="input">
							<div class="join-frm clearfix">
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="text" placeholder="Module Name"
										autocomplete="off" path="testModule" class="form-control usr"
										required="true" />
									<form:errors class="errors" path="testModule" />
								</div>
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="text" placeholder="Test Case"
										autocomplete="off" path="testCase" class="form-control usr"
										required="true" />
									<form:errors class="errors" path="testCase" />
								</div>
								<div class="joinfrm-clm lgnbxfrm">
									<form:input type="text" placeholder="Test Case"
										autocomplete="off" path="testInput" class="form-control usr"
										required="true" />
									<form:errors class="errors" path="testInput" />
								</div>
								<div class="joinfrm-clm join-frm">
									<button class="btn btn-primary" type="submit">
										Cancel <span class="glyphicon glyphicon-chevron-right"></span>
									</button>
									<button class="btn btn-primary" type="submit">
										Save <span class="glyphicon glyphicon-chevron-right"></span>
									</button>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
