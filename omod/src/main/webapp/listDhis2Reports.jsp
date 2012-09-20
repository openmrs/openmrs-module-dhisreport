<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>
<h3>Report Definitions</h3>
<table>
    <c:forEach var="reportDefinition" items="${reportDefinitions}">
        <tr>
            <td>${reportDefinition.name}</td>
            <td><a href="reportDefinition.form?reportDefinition_id=${reportDefinition.id}">Link</a>
        </tr>
    </c:forEach>
    <c:if test="${ fn:length(reportDefinitions) == 0 }">
        <spring:message code="general.none" />
    </c:if>
</table>


<%@ include file="/WEB-INF/template/footer.jsp"%>
