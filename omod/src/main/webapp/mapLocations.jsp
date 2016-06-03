<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>
<%@ include file="./resources/js/js_css.jsp"%>
	
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

<tr>

<h3>
	<spring:message code="dhisreport.mappedLocations" />
</h3>
	<div class="box"
		style="padding-right: 2px; width: 260px; margin-left: 0px;">
		<table cellspacing="2" cellpadding="2">
			<tbody>
			<thead style="background-color: #1AAC9B; color: white; padding: 2px;">
				<td><spring:message code="dhisreport.OpenMRSLocations" /></td>
				<td><spring:message code="dhisreport.DHIS2OrgUnits" /></td>
			</thead>

			<c:forEach items="${map}" var="map" varStatus="varStatus">
				<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" }'>
					<td>${map.key}</td>
					<td>${map.value}</td>
				</tr>
			</c:forEach>

			</tbody>
		</table>
	</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>