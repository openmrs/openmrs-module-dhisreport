<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:require privilege="View Dhisreport" otherwise="/login.htm"
                 redirect="/module/dhisreport/mapDatasets.form"/>
<%@ include file="template/localHeader.jsp" %>
<%@ include file="./resources/js/js_css.jsp" %>

<h3>${dataset.name}</h3>
<p>
    Mapped Report:
</p>
<div id="currentReport">
    <c:choose>
        <c:when test="${currentReport != null}">
            <a href="${pageContext.request.contextPath}/module/reporting/reports/periodIndicatorReport.form?uuid=${currentReport.uuid}"
               target="_blank"
            >
                    ${currentReport.name}
            </a>
        </c:when>
        <c:otherwise>
            <i>Not mapped with a report yet</i>
        </c:otherwise>
    </c:choose>
    <button onclick="viewEditReportForm()">
        edit
    </button>
</div>
<div id="editReport" style="display: none;">
    <select id="reportSelector">
        <c:forEach items="${reports}" var="report">
            <c:choose>
                <c:when test="${report.uuid == currentReport.uuid}">
                    <option selected value="${report.uuid}">${report.name}</option>
                </c:when>
                <c:otherwise>
                    <option value="${report.uuid}"> ${report.name}
                    </option>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>
    <button onclick="updateReport()">
        save
    </button>
    <button onclick="closeEditReportForm()">
        cancel
    </button>
</div>
<div>
    <div class="boxHeader">
        <strong>
            Map Data Value Templates
        </strong>
    </div>
    <%-- Todo: Add an edit button to each Data Value Template --%>
    <div class="box">
        <table id="column-table" width="100%">
            <thead>
            <tr>
                <th>Data Element</th>
                <th>Category Option Combination</th>
                <th>Report Indicator</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${dataValueTemplates}" var="dataValueTemplate">
                <tr>
                    <td>${dataValueTemplate.dataElement.code}</td>
                    <td>${dataValueTemplate.dataElement.name}</td>
                    <td>
                        <c:forEach items="${dataValueTemplate.disaggregations}"
                                   var="disaggregation">
                            ${disaggregation.category.name}&nbsp;=&nbsp;${disaggregation.categoryOption.name},&nbsp;
                        </c:forEach>
                    </td>
                    <td>
                        <select id="dvt_${dataValueTemplate.id}">
                            <option disabled selected>Select a report indicator</option>
                            <c:forEach var="column"
                                       items="${currentReport.indicatorDataSetDefinition.columns}">
                                <option value="${column.label}"
                                        <c:if test="${column.label == dataValueTemplate.reportIndicatorLabel}">
                                            selected
                                        </c:if>
                                >
                                        ${column.label}
                                </option>
                            </c:forEach>
                        </select>
                        <button onclick="updateReportIndicator(${dataValueTemplate.id})">
                            save
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<script>
  function viewEditReportForm() {
    $("#currentReport").hide();
    $("#editReport").show();
  }

  function closeEditReportForm() {
    $("#currentReport").show();
    $("#editReport").hide();
  }

  function updateReport() {
    // Todo: Handle errors
    const reportUuid = $("#reportSelector").val();
    $.post(
        "${pageContext.request.contextPath}/module/dhisreport/dataset/${dataset.uuid}/updateReport.form",
        {reportUuid},
        function () {
          // Reload the page after the operation is success
          location.reload();
        }
    );
  }

  function updateReportIndicator(dataValueTemplateId) {
    const reportIndicatorUuid = $("#dvt_" + dataValueTemplateId).val();
    // Todo: Handle errors
    $.ajax({
          url: "${pageContext.request.contextPath}/module/dhisreport/dataValueTemplates/"
              + dataValueTemplateId + "/updateReportIndicator.form",
          type: "POST",
          data: {reportIndicatorUuid: reportIndicatorUuid},
          success: function () {
            // Display an alert after the operation is success
            alert("Saved!");
          }
        }
    );
  }
</script>
<%@ include file="/WEB-INF/template/footer.jsp" %>
