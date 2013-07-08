<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h3><spring:message code="dhisreport.importSumary" /></h3>

<c:if test="${not empty importSummary}">
    <table>
        <tr><td><spring:message code="dhisreport.status" />: </td><td>${importSummary.status}</td></tr>
        <tr><td><spring:message code="dhisreport.description" />: </td><td>${importSummary.description}</td></tr>
        <tr><td><spring:message code="dhisreport.dataValueCount" />: </td><td>${importSummary.dataValueCount}</td></tr>
    </table>
    <c:forEach var="conflict" items="${importSummary.conflicts}">
        <p><spring:message code="dhisreport.conflict" />: ${conflict.object}, <spring:message code="dhisreport.value" />: ${conflict.value}</p>
    </c:forEach>

</c:if>

<%@ include file="/WEB-INF/template/footer.jsp"%>