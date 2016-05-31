<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>


<h3>
	<spring:message code="dhisreport.mapLocationsWithDHIS2ORGUnits" />
</h3>

<form action="mapLocations.form" method="post">
	<table>
		<!-- openmrs:portlet url="globalProperties" parameters="title=${title}|propertyPrefix=dhisreport.|excludePrefix=dhisreport.started"/-->
		<thead style="background-color: #1AAC9B; color: white; padding: 2px;">
			<td><spring:message code="dhisreport.OpenMRSLocations" /></td>
			<td><spring:message code="dhisreport.DHIS2OrgUnits" /></td>
		</thead>
		<tbody>
			<tr>
				<td><select name="openmrsLocations">
						<c:forEach items="${locationList}" var="item">

							<option value="${item.name}">${item.name }</option>
						</c:forEach>
				</select></td>
				<td><select name="DHIS2OrgUnits">
						<c:forEach items="${orgunits}" var="item">

							<option value="${item.code}">${item.name }</option>
						</c:forEach>
				</select></td>
			<tr>
				<td />
				<td><input value="Map Locations" name="submit" type="submit"></td>
			</tr>
		</tbody>
	</table>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>