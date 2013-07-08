<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h3><spring:message code="dhisreport.reportResult" /></h3>

<c:if test="${not empty dataValueSet}">
    <div>
        <table>
            <tr><td><spring:message code="dhisreport.dataSet" />: </td><td>${dataValueSet.dataSet}</td></tr>
            <tr><td><spring:message code="dhisreport.orgUnit" />: </td><td>${dataValueSet.orgUnit}</td></tr>
            <tr><td><spring:message code="dhisreport.period" />: </td><td>${dataValueSet.period}</td></tr>
        </table>
    </div>
    <!-- <c:forEach var="dv" items="${dataValueSet.dataValues}">
        <p><spring:message code="dhisreport.dataElement" />: Code: ${dv.dataElement}, Value: ${dv.value}</p>
    </c:forEach>
     -->
    <c:forEach var="dvm" items="${dataElementMap}">
        <p><spring:message code="dhisreport.dataElement" />: Name: ${dvm.key.name} Code: ${dvm.key.name} Value: ${dvm.value}</p>
    </c:forEach>
    
</c:if>
<c:if test="${not empty importSummary}">
    <div>
        <table>
            <tr><td><spring:message code="dhisreport.status" />; </td><td>${importSummary.status}</td></tr>
            <tr><td><spring:message code="dhisreport.description" />: </td><td>${importSummary.description}</td></tr>
            <tr><td><spring:message code="dhisreport.dataValueCount" />: </td><td>${importSummary.dataValueCount}</td></tr>
        </table>
    </div>
</c:if>


<%@ include file="/WEB-INF/template/footer.jsp"%>