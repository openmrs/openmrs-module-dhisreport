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
            <a href="${pageContext.request.contextPath}/module/reporting/reports/periodIndicatorReport.form?uuid=${currentReport.uuid}">
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
      const reportUuid = $("#reportSelector").val();
      $.post(
          "${pageContext.request.contextPath}/module/dhisreport/dataset/${dataset.uid}/updateReport.form",
          {reportUuid},
          function () {
            // Reload the page after the operation is success
            location.reload();
          }
      );
    }
</script>
<%@ include file="/WEB-INF/template/footer.jsp" %>
