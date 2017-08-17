<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h3><spring:message code="dhisreport.reportResult" /></h3>
<c:forEach var="aggregatedValues" items="${aggregatedList}">
<c:if test="${not empty aggregatedValues.dataValueSet}">
<h3>------------------------------------------------------------------------</h3>
    <div>
        <table>
            <tr><td><spring:message code="dhisreport.dataSet" />: </td><td>${aggregatedValues.dataValueSet.dataSet}</td></tr>
            <tr><td><spring:message code="dhisreport.orgUnit" />: </td><td>${aggregatedValues.dataValueSet.orgUnit}</td></tr>
            <tr><td><spring:message code="dhisreport.period" />: </td><td>${aggregatedValues.dataValueSet.period}</td></tr>
            <tr><td><spring:message code="dhisreport.reportName" />: </td>
                <c:if test="${empty resultUuid}">
                <td><div id='openmrs_error'><spring:message code="dhisreport.unmappedReport" /></div></td>
                </c:if>
                <c:if test="${not empty resultUuid}">
                <td><a href="${pageContext.request.contextPath}/module/reporting/reports/viewReport.form?uuid=${resultUuid}">View Report</a></td>
                </c:if>
            </tr>
        </table>
    </div>
    
    <!-- <c:forEach var="dv" items="${dataValueSet.dataValues}">
        <p><spring:message code="dhisreport.dataElement" />: Code: ${dv.dataElement}, Value: ${dv.value}</p>
    </c:forEach>
    
    <c:forEach var="dvm" items="${dataElementMap}">
        <p><spring:message code="dhisreport.dataElement" />: Name: ${dvm.key.name} Code: ${dvm.key.code} Value: ${dvm.value}</p>
    </c:forEach>
     -->
     <br>
<div>
	<table style="width:600px">
	<tr>
	<th>DataElement Name</th>
	<th>DataElement Code</th>		
	<th>Value</th>
	</tr>
	<tr>
    <c:forEach var="dvm" items="${aggregatedValues.dataElementMap}">
    <tr>
  		<td>${dvm.key.name}</td>
  		<td>${dvm.key.code}</td>		
  		<td>${dvm.value}</td>
  	</tr>
	<tr>
  	</c:forEach>
	</table>
	</div>
</c:if>
<c:if test="${not empty aggregatedValues.importSummary}">
    
    <div>
        <table>
            <tr><td><spring:message code="dhisreport.status" />: </td><td>${aggregatedValues.importSummary.status}</td></tr>
            <tr><td><spring:message code="dhisreport.description" />: </td><td>${aggregatedValues.importSummary.description}</td></tr>
            <tr><td><spring:message code="dhisreport.dataValueCount" />: </td><td>${aggregatedValues.importSummary.importCount}</td></tr>
        </table>
    </div>
</c:if>

    <c:if test="${not empty aggregatedValues.importSummaries}">
        <div>
            <table>
                <tr><td><spring:message code="dhisreport.imported" />: </td><td>${aggregatedValues.importSummaries.imported}</td></tr>
                <tr><td><spring:message code="dhisreport.updated" />: </td><td>${aggregatedValues.importSummaries.updated}</td></tr>
                <tr><td><spring:message code="dhisreport.deleted" />: </td><td>${aggregatedValues.importSummaries.deleted}</td></tr>
                <tr><td><spring:message code="dhisreport.ignored" />: </td><td>${aggregatedValues.importSummaries.ignored}</td></tr>
                <c:forEach var="importSummaryElement" items="${aggregatedValues.importSummaries.importSummaryList}">
                    <tr><td><spring:message code="dhisreport.status" />: </td><td>${importSummaryElement.status}</td></tr>
                    <tr><td><spring:message code="dhisreport.description" />: </td><td>${importSummaryElement.description}</td></tr>
                    <tr><td><spring:message code="dhisreport.dataValueCount" />: </td><td>${importSummaryElement.importCount}</td></tr>
                </c:forEach>
            </table>
        </div>
    </c:if>
</c:forEach>

<%@ include file="/WEB-INF/template/footer.jsp"%>