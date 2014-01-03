<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="View Dhisreport" otherwise="/login.htm" redirect="/module/dhisreport/listDhis2Reports.form" />

<%@ include file="template/localHeader.jsp"%>
<%@ include file="./resources/js/js_css.jsp" %>


<script type="text/javascript">
$(document).ready(function(){

	$("tr").each(function (index) { // traverse through all the rows
		
        if(index != 0) { // if the row is not the heading one
            $(this).find("td:first").html(index + "."); // set the index number in the first 'td' of the row

        }
    }); 
	
	$(".new").css({
		
		'background-color':'#FE2E2E',
		  'color':'#FAFAFA',

	})
	
	$(".old").css({
	
		'background-color':'',
		  'color':'',
	})
	
	
});
</script>


<h3><spring:message code="dhisreport.reportDefinitions" /></h3>
<table>

    <c:forEach var="reportDefinition" items="${reportDefinitions}">
    <c:set var="flag" value ="true"/>
    
   
    <c:forEach var="dataValueTemplate" items="${reportDefinition.dataValueTemplates}"> 
    
   
    	<c:if test='${flag == "true"}'>
    			<c:if test="${empty dataValueTemplate.query}"> 
    			   <c:set var="flag" value ="false"/>
    			
    			</c:if>
    	</c:if>
    
    	
    </c:forEach>
    
    <c:choose>
    <c:when test='${flag == "false"}'>
       <tr class="new">
        	<td id ="serial"></td>
            <td ><a  href="editReportDefinition.form?reportDefinition_id=${reportDefinition.id}" style="color:white" >${reportDefinition.name}</a>
         <td ><a href="setupReport.form?reportDefinition_id=${reportDefinition.id}" style="color:white"><spring:message code="dhisreport.Export" /></a> </td>     
            <td><a onclick="REPORTDEFINITION.deleteReportDefinition(${reportDefinition.id})" style="color:white"><spring:message code="dhisreport.Delete" /></a>
        </tr>
        </c:when>

      <c:otherwise>  
         <tr class="old">
        	<td id ="serial"></td>
            <td><a href="editReportDefinition.form?reportDefinition_id=${reportDefinition.id}">${reportDefinition.name}</a>
         <td><a href="setupReport.form?reportDefinition_id=${reportDefinition.id}"><spring:message code="dhisreport.Export" /></a> </td>     
            <td><a onclick="REPORTDEFINITION.deleteReportDefinition(${reportDefinition.id})"><spring:message code="dhisreport.Delete" /></a>
        </tr>
  </c:otherwise>
    
</c:choose>
    
    </c:forEach>
    <c:if test="${ fn:length(reportDefinitions) == 0 }">
        <spring:message code="general.none" />
    </c:if>
</table>


<%@ include file="/WEB-INF/template/footer.jsp" %>
