<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<script type="text/javascript">
    jQuery(document).ready(function($) {
        $("#editReportingReportMapping").click( function() {
            document.getElementById("reportinReportMappingValue").innerHTML =document.getElementById("reportingReportsList").innerHTML;
            $("#editReportingReportMapping").hide();
            $("#saveReportingReportMapping").show();
        });
        $("#saveReportingReportMapping").click( function() {
            $.post("${pageContext.request.contextPath}/module/dhisreport/mapReports.form",{reportDefinition_id: document.getElementById("reportDefinitionId").value,reportmoduledefinitions: document.getElementById("reportmoduledefinitions").value});
            setTimeout(function(){
                window.location.reload();
            }, 1000);
        }); 
        $(".mapper").click(function(){
            var $row = $(this).closest("tr");
            var $val = $row.find(".rep-list").val(); 
            var $repId = $row.find(".report_id").val();
            $.post("${pageContext.request.contextPath}/module/dhisreport/confirmReports.form",{reporting_report: $val,dhis_report_id: $repId}); 
            setTimeout(function(){ 
                window.location.reload();
                $("#openmrs_msg").show();
            }, 1000);
        });
    });
</script>

<form>
    <table style="width: 70%">
        <thead style="background-color: #1AAC9B; color: white; padding: 2px;">
        <tr>
            <td><spring:message code="dhisreport.mappingDHISHeader" /></td>
            <td><spring:message code="dhisreport.mappingReportingHeader" /></td>
            <td><spring:message code="dhisreport.action" /></td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <input id="reportDefinitionId" value="${reportDefinition.id}" hidden>
            <td>${reportDefinition.name}</td>
            <td>
                <span id="reportinReportMappingValue"> ${correspondingReportDefinition.name}</span>
            </td>
            <td><a
                    id="editReportingReportMapping" style="cursor: pointer;"><img
                    src="<c:url value='/images/edit.gif'/>" border="0"
                    title='<spring:message code="dhisreport.editReportMapping"/>'/></a><a
                    id="saveReportingReportMapping" style="display: none; cursor: pointer;"><spring:message code="dhisreport.dhis2saveButton" /></a>
                <span id="reportingReportsList" style="display: none;">
                <select id="reportmoduledefinitions" name="reportmoduledefinitions">
                    <c:forEach items="${definitionSummaries}" var="r">
                        <c:choose>
                            <c:when test="${r.uuid == correspondingReportDefinition.uuid}">
                                <option selected value="${r.uuid}"> ${r.name}
                                </option>
                            </c:when>
                            <c:otherwise>
                                <option value="${r.uuid}"> ${r.name}
                                </option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
    <br>
    <br>
    <c:if test="${not empty correspondingReportDefinition}">
        <div id="openmrs_msg" hidden><spring:message code="dhisreport.mappingReportMapping" /> </div>
    </c:if>
    <br>
    <br>
    <div  style="float: left; width: 49%;">
        <b class="boxHeader"><spring:message code="dhisreport.mappingDHISTableHeader" /></b>
        <div class="box">
            <table cellspacing="0" cellpadding="2" style="width: 100%;">
                <tbody>
                <tr>
                    <th>[<spring:message code="dhisreport.dataElementOrder" />]
                    </th>
                    <th>[<spring:message code="dhisreport.dataElement" />]
                    </th>

                    <th>[<spring:message code="dhisreport.disaggregationName" />]
                    </th>
                </tr>
                <c:forEach var="dataValueTemplate" varStatus="varStatus"
                           items="${reportDefinition.dataValueTemplates}">
                    <c:set var="count" value="0" scope="page"/>
                    <tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" }'>
                        <td>${varStatus.index +1}</td>
                        <td>${dataValueTemplate.dataelement.name}</td>

                        <td>${dataValueTemplate.disaggregation.name}</td>
                        
                        <td>
                            <c:choose>
                                <c:when test="${empty dataValueTemplate.query and empty dataValueTemplate.mappeddefinitionlabel}">
                                    <select style="width: 100px;background-color:#FE2E2E" class="rep-list">
                                </c:when>
                                <c:otherwise>
                                    <select style="width: 100px" class="rep-list">
                                </c:otherwise>
                            </c:choose>
                                <option value="${0}" selected>Default</option>
                            <c:set var="count" value="${0}" scope="page"/>
                        <c:forEach var="def" items="${reportingReportDefinitionReport.indicatorDataSetDefinition.columns}">
                            <c:set var="count" value="${count+1}" scope="page"/>
                            <c:choose>
                                <c:when test="${dataValueTemplate.mappeddefinitionlabel != def.label}">
                                    <option value="${count}">${def.label}</option> 
                                </c:when>
                                <c:otherwise>
                                    <option value="${count}" selected>${def.label}</option> 
                                </c:otherwise>
                            </c:choose>  
                        </c:forEach>
                        </select>
                        </td>
                        
                        <td>
                            <a class="mapper">Map</a>
                            <input class="report_id" value="${dataValueTemplate.id}" hidden>
                        </td>                       
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <c:if test="${not empty correspondingReportDefinition}">
        <div  style="float: Right; width: 49%;">
            <b class="boxHeader"><spring:message code="dhisreport.mappingReportingTableHeader" /></b>
            <div class="box" >
                <table cellspacing="0" cellpadding="2" style="width: 100%;">
                    <tbody>
                    <tr>
                        <th>[<spring:message code="dhisreport.indicatorOrder" />]
                        </th>
                        <th>[<spring:message code="dhisreport.label" />]
                        </th>

                        <th>[<spring:message code="dhisreport.indicator" />]
                        </th>
                    </tr>
                    <c:forEach var="col" varStatus="varStatus"
                               items="${reportingReportDefinitionReport.indicatorDataSetDefinition.columns}">
                        <tr class='${varStatus.index % 2 == 0 ? "oddRow" : "evenRow" }'>
                            <td>${varStatus.index +1}</td>
                            <td>${col.label}</td>
                            <td>${col.indicator.parameterizable.name}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>
</form>