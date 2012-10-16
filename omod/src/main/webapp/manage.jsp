<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

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
            <td><spring:message code="dhisreport.reports" /></td>
            <td><a href="listDhis2Reports.form"><spring:message code="dhisreport.Link" /></a></td>
        </tr>
    </tbody>
</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>