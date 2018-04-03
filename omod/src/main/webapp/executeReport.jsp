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
            <tr><td><spring:message code="dhisreport.dataValueCount" />: </td><td>${fn:length(aggregatedValues.dataValueSet.dataValues)}</td></tr>
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
</c:if>
<c:if test="${not empty aggregatedValues.importSummary}">
    <h3>Import Summary</h3>
    <div>
        <table>
            <tr><td><spring:message code="dhisreport.status" />: </td><td>${aggregatedValues.importSummary.status}</td></tr>
            <tr><td><spring:message code="dhisreport.description" />: </td><td>${aggregatedValues.importSummary.description}</td></tr>
        </table>
    </div>
    <br />
    <div>
        <spring:message code="dhisreport.imported" /> ${aggregatedValues.importSummary.importCount.imported},
        <spring:message code="dhisreport.updated" /> ${aggregatedValues.importSummary.importCount.updated},
        <spring:message code="dhisreport.deleted" /> ${aggregatedValues.importSummary.importCount.deleted},
        <spring:message code="dhisreport.ignored" /> ${aggregatedValues.importSummary.importCount.ignored}
    </div>
    <c:if test="${not empty aggregatedValues.importSummary.conflicts}">
    <br />
    <b><spring:message code="dhisreport.conflicts" />:</b>
    <br />
    <ul>
    <c:forEach var="conflict" items="${aggregatedValues.importSummary.conflicts}">
        <li>${conflict.object != null ? "[" : ""}${conflict.object}${conflict.object != null ? "] " : ""} ${conflict.value}</li>
    </c:forEach>
    </ul>
    </c:if>
    <br />
    <h3>Data Elements</h3>
    <div>
        <table style="width:600px" border="1" cellspacing="0">
            <tr>
                <th style="text-align: center">Name</th>
                <th style="text-align: center">Code</th>
            </tr>
            <tr>
                <c:forEach var="de" items="${aggregatedValues.dataElements}">
            <tr>
                <td style="text-align: center">${de.name}</td>
                <td style="text-align: center">${de.code}</td>
            </tr>
            <tr>
                </c:forEach>
        </table>
    </div>
    <br />
    <h3>Data Values</h3>
    <div>
        <table style="width:600px" border="1" cellspacing="0">
            <tr>
                <th style="text-align: center">Data Element</th>
                <th style="text-align: center">Disaggregation(s)</th>
                <th style="text-align: center">Value</th>
            </tr>
            <tr>
                <c:forEach var="dv" items="${aggregatedValues.dataValueSet.dataValues}">
            <tr>
                <td style="text-align: center">${dv.dataElement}</td>
                <td style="text-align: center">${dv.categoryOptionCombo}</td>
                <td style="text-align: center">${dv.value}</td>
            </tr>
            <tr>
                </c:forEach>
        </table>
    </div>
</c:if>
</c:forEach>

<%@ include file="/WEB-INF/template/footer.jsp"%>