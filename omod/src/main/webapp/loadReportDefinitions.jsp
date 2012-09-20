<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<form action="loadReportDefinitions.form" method="post" enctype="multipart/form-data">
<div>
<b class="boxHeader"> Upload report </b>
    <div class="box">
		<span style="white-space: nowrap">
			<span> Select a reports definition file: </span>
			<input type="file" name="datafile" size="40" accept="application/xml" style="border: 1px solid cadetblue; padding: 1px;"/>
		</span>

		
		<div>
			<input type="submit" value="Upload">
		</div>
	</div>
	
</div>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>