<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<%@ page import ="org.openmrs.GlobalProperty" %>
 <%@ page import= "org.openmrs.api.context.Context"%>


<style type="text/css">

.alert-box {
    color:#555;
    border-radius:10px;
    font-family:Tahoma,Geneva,Arial,sans-serif;font-size:13px;
    padding:10px 10px 10px 10px;
    margin:10px;
    float: left;
}

.alert-box span {
    font-weight:bold;
    text-transform:uppercase;
}

.notice {
    background:#fff8c4;
    border:1px solid #8ed9f6;
}

</style>


<table>
    <thead style="background-color: #1AAC9B; color: white; padding: 2px;">
        <tr>
            <td><spring:message code="dhisreport.action" /></td>
            <td><spring:message code="dhisreport.Link" /></td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td><spring:message code="dhisreport.importExport" /></td>
            <td><a href="loadReportDefinitions.form"><spring:message code="dhisreport.Link" /></a></td>
        </tr>
        <tr>
            <td><spring:message code="dhisreport.configureDHIS2" /></td>
            <td><a href="configureDhis2.form"><spring:message code="dhisreport.Link" /></a></td>
        </tr>
        <tr>
            <td><spring:message code="dhisreport.reporting" /></td>
            <td><a href="listDhis2Reports.form"><spring:message code="dhisreport.Link" /></a></td>
        </tr>
        <tr>
            <td><spring:message code="dhisreport.syncReports" /></td>
            <td><a href="syncReports.form"><spring:message code="dhisreport.Link" /></a></td>
        </tr>
             
    </tbody>
</table>
<br/>
<br/>
<span class="alert-box notice"><span>notice:</span> Last report sync Date: <openmrs:globalProperty key="dhisreport.dhis2SyncDate"/></span>


<%@ include file="/WEB-INF/template/footer.jsp"%>