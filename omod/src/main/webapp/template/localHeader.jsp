<spring:htmlEscape defaultHtmlEscape="true"/>
<h2><spring:message code="dhisreport.title"/></h2>
<ul id="menu">
    <li class="first">
        <a href="${pageContext.request.contextPath}/admin">
            <spring:message code="admin.title.short"/>
        </a>
    </li>
    <li
            <c:if test='<%= request.getRequestURI().contains("/configureDhis2") %>'>class="active"</c:if>
    >
        <a href="${pageContext.request.contextPath}/module/dhisreport/configureDhis2.form">
            <spring:message
                    code="dhisreport.configureDHIS2"/>
        </a>
    </li>
    <li
            <c:if test='<%= request.getRequestURI().contains("/metadata") %>'>class="active"</c:if>
    >
        <a href="${pageContext.request.contextPath}/module/dhisreport/metadata.form">
            <spring:message code="dhisreport.importDataset"/>
        </a>
    </li>
    <li
            <c:if test='<%=request.getRequestURI().contains("/mapLocations")%>'>class="active"</c:if>
    >
        <a href="${pageContext.request.contextPath}/module/dhisreport/mapLocations.form">
            <spring:message code="dhisreport.mapLocations"/>
        </a>
    </li>
    <li
            <c:if test='<%=request.getRequestURI().contains("/datasets")%>'>class="active"</c:if>
    >
        <a href="${pageContext.request.contextPath}/module/dhisreport/datasets.form">
            <spring:message code="dhisreport.manageDataSets"/>
        </a>
    </li>
    <!-- Add further links here -->
</ul>
