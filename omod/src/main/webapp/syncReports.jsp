<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h3><td><spring:message code="dhisreport.syncReportsTitle" /></td></h3>

<table>
    <thead style="background-color: #1AAC9B; color: white; padding: 2px;">
        <tr>
            <td><spring:message code="dhisreport.action" /></td>
            <td><spring:message code="dhisreport.Link" /></td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td><spring:message code="dhisreport.fullSync" /></td>
            <td><a href="fullReportSync.form"><spring:message code="dhisreport.Link" /></a></td>
        </tr>
        <!--    
        <tr>
            <td><spring:message code="dhisreport.partReportSync" /></td>
            <td><a href="partReportSync.form"><spring:message code="dhisreport.Link" /></a></td>
        </tr>
    
         <tr>
            <td><spring:message code="dhisreport.newSync" /></td>
            <td><a href="newReportSync.form"><spring:message code="dhisreport.Link" /></a></td>
        </tr>
         -->
    </tbody>
</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>