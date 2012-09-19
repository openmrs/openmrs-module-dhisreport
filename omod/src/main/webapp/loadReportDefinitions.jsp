<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<form action="loadReportDefinitions.form" method="post" enctype="multipart/form-data">
    <p>
        Select a reports definition file:<br>
        <input type="file" name="datafile" size="40" accept="application/xml" />
    </p>
    <div>
        <input type="submit" value="Upload">
    </div>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>