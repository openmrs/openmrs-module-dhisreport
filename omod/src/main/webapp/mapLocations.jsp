<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="template/localHeader.jsp" %>
<%@ include file="./resources/js/js_css.jsp" %>

<h3><spring:message code="dhisreport.mapLocationsWithDHIS2ORGUnits"/></h3>
<form action="mapLocations.form" method="post">
    <table>
        <thead style="background-color: #1AAC9B; color: white; padding: 2px;">
        <tr>
            <td><spring:message code="dhisreport.OpenMRSLocation"/></td>
            <td><spring:message code="dhisreport.DHIS2OrgUnit"/></td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><select name="openmrsLocationUuid">
                <c:forEach items="${locations}" var="location">
                    <option value="${location.uuid}">${location.name}</option>
                </c:forEach>
            </select></td>
            <td>
                <select name="dhis2OrgUnitUid">
                    <c:forEach items="${organisationUnits}" var="organisationUnit">
                        <option value="${organisationUnit.key}">${organisationUnit.value.name}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td></td>
            <td><input value="Map Locations" name="submit" type="submit"></td>
        </tr>
        </tbody>
    </table>
</form>
<tr>
    <h3>
        <spring:message code="dhisreport.mappedLocations"/>
    </h3>
    <div class="box"
         style="padding-right: 2px; width: 260px; margin-left: 0px;">
        <table cellspacing="2" cellpadding="2">
            <tbody>
            <thead style="background-color: #1AAC9B; color: white; padding: 2px;">
            <tr>
                <td><spring:message code="dhisreport.OpenMRSLocation"/></td>
                <td><spring:message code="dhisreport.DHIS2OrgUnit"/></td>
            </tr>
            </thead>
            <c:forEach items="${mappedOrganisationUnits}" var="pair" varStatus="varStatus">
                <tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" }'>
                    <td>${pair.key.name}</td>
                    <td>${pair.value.name}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <%@ include file="/WEB-INF/template/footer.jsp" %>
