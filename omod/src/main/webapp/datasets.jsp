<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:require privilege="View Dhisreport" otherwise="/login.htm"
                 redirect="/module/dhisreport/datasets.form"/>
<%@ include file="template/localHeader.jsp" %>
<%@ include file="./resources/js/js_css.jsp" %>

<h3><spring:message code="dhisreport.importedDataSets"/></h3>
<table>
    <tr>
        <th>DataSet</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="dataset" items="${datasets}">
        <tr>
            <td>${dataset.name}</td>
            <td><a href="${pageContext.request.contextPath}/module/dhisreport/mapDataset.form?uid=${dataset.uid}">map</a></td>
            <td><a href="${pageContext.request.contextPath}/module/dhisreport/prepareDatasetToPost.form?uid=${dataset.uid}">post</a></td>
        </tr>
    </c:forEach>
</table>

<%@ include file="/WEB-INF/template/footer.jsp" %>
