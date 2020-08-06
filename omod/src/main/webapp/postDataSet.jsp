<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="template/localHeader.jsp" %>

<c:choose>
    <c:when test="${isError}">
        <div class="error">
                ${errorMessage}
        </div>
    </c:when>
    <c:otherwise>
        <h3><spring:message code="dhisreport.importSummary"/></h3>
        <table>
            <tr>
                <td><spring:message code="dhisreport.status"/>:</td>
                <td>${importSummary.status}</td>
            </tr>
            <tr>
                <td><spring:message code="dhisreport.description"/>:</td>
                <td>${importSummary.description}</td>
            </tr>
            <tr>
                <td><spring:message code="dhisreport.importCounts"/>:</td>
                <td>${importSummary.importCount}</td>
            </tr>
        </table>
        <c:forEach var="conflict" items="${importSummary.conflicts}">
            <p><spring:message code="dhisreport.conflict"/>: ${conflict.object}, <spring:message
                    code="dhisreport.value"/>: ${conflict.value}</p>
        </c:forEach>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/template/footer.jsp" %>
