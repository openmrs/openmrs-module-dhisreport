<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:require privilege="View Dhisreport" otherwise="/login.htm"
                 redirect="/module/dhisreport/datasets.form"/>
<%@ include file="template/localHeader.jsp" %>
<%@ include file="./resources/js/js_css.jsp" %>

<h3><spring:message code="dhisreport.importedDataSets"/></h3>
<table>
    <tr>
        <th>Code</th>
        <th>Name</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="dataset" items="${datasets}">
        <tr>
            <td>${dataset.code}</td>
            <td><a href="#">${dataset.name}</a></td>
            <td><a href="${pageContext.request.contextPath}/module/dhisreport/mapDataset.form?uid=${dataset.uid}">map</a></td>
        </tr>
    </c:forEach>
</table>

<%@ include file="/WEB-INF/template/footer.jsp" %>
