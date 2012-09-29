<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h3>Import Summary</h3>

<c:if test="${not empty importSummary}">
    <table>
        <tr><td>Status: </td><td>${importSummary.status}</td></tr>
        <tr><td>Description: </td><td>${importSummary.description}</td></tr>
        <tr><td>DataValue count: </td><td>${importSummary.dataValueCount}</td></tr>
    </table>
    <c:forEach var="conflict" items="${importSummary.conflicts}">
        <p>Conflict: ${conflict.object}, Value: ${conflict.value}</p>
    </c:forEach>

</c:if>

<%@ include file="/WEB-INF/template/footer.jsp"%>