<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>
<%@ include file="./resources/js/js_css.jsp" %>

<div>
	<b class="boxHeader">Edit Report Definitions</b>
	<div class="box">
		<table cellspacing="0" cellpadding="2">
			<tbody>
				<tr>
					<th></th>
					<th>Name</th>
					<th>Code</th>
					<th>Data Element</th>
					<th>Query</th>
					<th>Action</th>
				</tr>
				<c:forEach var="dataValueTemplate" varStatus="varStatus"
					items="${reportDefinition.dataValueTemplates}">
					<tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" }'>
						<td><input type="checkbox"
							value="dataValueTemplate${dataValueTemplate.id }"
							name="dataValueTemplateId"></td>
						<td>${reportDefinition.name}</td>
						<td><b><u>${reportDefinition.code}</u></b></td>
						<th>${dataValueTemplate.dataelement.name}</th>

						<td><i><span id="reportDefinition_query${dataValueTemplate.id }">${dataValueTemplate.query}</span></i></td>

						<td><a onclick="REPORTDEFINITION.edit(${dataValueTemplate.id })"
							id="reportDefinition_edit${dataValueTemplate.id }">Edit</a> <span
							id="reportDefinition_save${dataValueTemplate.id }"></span></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
