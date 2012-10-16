<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h3><spring:message code="dhisreport.reportDefinitionFor" /> ${reportDefinition.name}</h3>

<form action="executeReport.form" method="post">
    <table>
        <tr>
            <td><spring:message code="dhisreport.Location" /></td>
            <td>
                <select name="location"> 
                    <c:forEach var="location" items="${locations}" >
                        <option value="${fn:substring(location.name,fn:indexOf(location.name,"[") + 1,fn:indexOf(location.name,"]"))}" />${location.name}</option>
                    </c:forEach>
                </select>
            </td>
        <tr>
            <td><spring:message code="dhisreport.Date" /></td>
            <td><input type="text" name="date" /></td>
        </tr>
        <tr>                   
            <td /><td><select name="resultDestination">
                    <option value="preview"><spring:message code="dhisreport.Preview" /></option>
                    <c:if test="${not empty dhis2Server}">
                        <option value="post"><spring:message code="dhisreport.postToDHIS" /></option>
                    </c:if>
                </select>
            </td>
        </tr>
        <tr>
            <td />
            <td>
                <input type="submit" value="<spring:message code="dhisreport.Generate" />" />
            </td>
        </tr>

</table>
<input type="hidden" name="reportDefinition_id" value="${reportDefinition.id}" />
</form>


<%@ include file="/WEB-INF/template/footer.jsp"%>