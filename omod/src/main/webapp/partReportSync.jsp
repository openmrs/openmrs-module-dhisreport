<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>
<%@ include file="./resources/js/js_css.jsp" %>
<h3><spring:message code="dhisreport.reportDefinitions" /></h3>
<form method="post">
    <c:forEach var="reportDefinition" items="${reportDefinitions}">
        <tr>
        <input type="checkbox" name="reportids" value="${reportDefinition.id}"> ${reportDefinition.name}<br>
        </tr>
    </c:forEach>
    <c:if test="${ fn:length(reportDefinitions) == 0 }">
        <spring:message code="general.none" />
    </c:if>
    <input type="submit" value="Submit">
</form>


<%@ include file="/WEB-INF/template/footer.jsp"%>
