<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
    <li class="first"><a
            href="${pageContext.request.contextPath}/admin"><spring:message
                code="admin.title.short" /></a></li>

    <li
        <c:if test='<%= request.getRequestURI().contains("/manage") %>'>class="active"</c:if>>
            <a
                href="${pageContext.request.contextPath}/module/dhisreport/manage.form"><spring:message
                code="dhisreport.manage" /></a>
    </li>
    
    <li
        <c:if test='<%= request.getRequestURI().contains("/listDhis2Reports") %>'>class="active"</c:if>>
            <a
                href="${pageContext.request.contextPath}/module/dhisreport/listDhis2Reports.form"><spring:message
                code="dhisreport.reporting" /></a>
    </li>
    

    <!-- Add further links here -->
</ul>
<h2>
    <spring:message code="dhisreport.title" />
</h2>
