<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
    <li class="first"><a
            href="${pageContext.request.contextPath}/admin"><spring:message
                code="admin.title.short" /></a></li>

    <li
        <c:if test='<%= request.getRequestURI().contains("/loadReportDefinitions") %>'>class="active"</c:if>>
            <a
                href="${pageContext.request.contextPath}/module/dhisreport/loadReportDefinitions.form"><spring:message
                code="dhisreport.importExport" /></a>
    </li>

    <li
        <c:if test='<%= request.getRequestURI().contains("/configureDhis2") %>'>class="active"</c:if>>
            <a
                href="${pageContext.request.contextPath}/module/dhisreport/configureDhis2.form"><spring:message
                code="dhisreport.configureDHIS2" /></a>
    </li>

	<li
		<c:if test='<%=request.getRequestURI().contains("/mapLocations")%>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/dhisreport/mapLocations.form"><spring:message
				code="dhisreport.mapLocations" /></a>
	</li>

	<li
        <c:if test='<%=request.getRequestURI().contains("/listDhis2Reports")%>'>class="active"</c:if>>
            <a
                href="${pageContext.request.contextPath}/module/dhisreport/listDhis2Reports.form"><spring:message
                code="dhisreport.reporting" /></a>
    </li>

    <!--
     Sync feature is currently unavailable.
     <li
        <c:if test='<%= request.getRequestURI().contains("/syncReports") %>'>class="active"</c:if>>
            <a
                href="${pageContext.request.contextPath}/module/dhisreport/syncReports.form"><spring:message
                code="dhisreport.syncReports" /></a>
    </li>
     -->


    <!-- Add further links here -->
</ul>
<h2>
    <spring:message code="dhisreport.title" />
</h2>
