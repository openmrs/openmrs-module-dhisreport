<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<form method="POST">
    <table>
        <thead style="background-color: #1AAC9B; color: white; padding: 2px;">
        <tr>
            <td>Dhis2 Report</td>
            <td>OpenMRS(Reporting) Report</td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${reportDefinition.name}</td>
            <td>
                <select name="colour">
                <c:forEach items="${definitionSummaries}" var="r">
                <option value="${r.uuid}"> ${r.name}
                </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>
            <td><input name="submit" type="submit" value="<spring:message code="dhisreport.dhis2saveButton" />" /></td>
        </tr>
        </tbody>
    </table>
</form>


<%@ include file="/WEB-INF/template/footer.jsp" %>