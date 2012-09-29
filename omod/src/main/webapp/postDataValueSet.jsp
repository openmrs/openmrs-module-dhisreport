<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h3>Report result</h3>

<c:if test="${not empty dataValueSet}">
    <table>
        <tr><td>DataSet: </td><td>${dataValueSet.dataSet}</td></tr>
        <tr><td>OrgUnit: </td><td>${dataValueSet.orgUnit}</td></tr>
        <tr><td>Period: </td><td>${dataValueSet.period}</td></tr>
    </table>
    <c:forEach var="dv" items="${dataValueSet.dataValues}">
        <p>DataElement: ${dv.dataElement}, Value: ${dv.value}</p>
    </c:forEach>

         <c:if test="$dhis2Server">
           <div>
             <p><a href="/postDataValueSet">Post</a> to DHIS2 server at ${dhisServer.url}</p>
           </div>
        </c:if>
</c:if>

<%@ include file="/WEB-INF/template/footer.jsp"%>