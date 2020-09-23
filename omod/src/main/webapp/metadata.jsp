<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:require privilege="View Dhisreport" otherwise="/login.htm"
                 redirect="/module/dhisreport/mapDatasets.form"/>
<%@ include file="template/localHeader.jsp" %>

<form action="metadata.form" method="post" enctype="multipart/form-data">
    <div>
        <b class="boxHeader"><spring:message code="dhisreport.uploadReport"/></b>
        <div class="box">
            <div style="white-space: nowrap">
                <span><spring:message code="dhisreport.selectReport"/>:</span>
                <input type="file" name="datafile" size="40" accept="application/xml"
                       style="border: 1px solid cadetblue; padding: 1px;"/>
            </div>
            <div>
                <input type="submit" name="import"
                       value="<spring:message code="dhisreport.UploadButton" />">
            </div>
        </div>
    </div>
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>
