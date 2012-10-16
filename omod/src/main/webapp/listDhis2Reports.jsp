<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>
<%@ include file="./resources/js/js_css.jsp" %>
<h3><spring:message code="dhisreport.reportDefinitions" /></h3>
<table>
    <c:forEach var="reportDefinition" items="${reportDefinitions}">
        <tr>
            <td>${reportDefinition.name}</td>
            <td><a href="setupReport.form?reportDefinition_id=${reportDefinition.id}"><spring:message code="dhisreport.Link" /></a> </td>
            <td><a href="editReportDefinition.form?reportDefinition_id=${reportDefinition.id}"><spring:message code="dhisreport.Edit" /></a>
            <td><a onclick="REPORTDEFINITION.deleteReportDefinition(${reportDefinition.id})"><spring:message code="dhisreport.Delete" /></a>
        </tr>
    </c:forEach>
    <c:if test="${ fn:length(reportDefinitions) == 0 }">
        <spring:message code="general.none" />
    </c:if>
</table>


<%@ include file="/WEB-INF/template/footer.jsp"%>
