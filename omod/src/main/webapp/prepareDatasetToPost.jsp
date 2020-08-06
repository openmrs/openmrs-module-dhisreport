<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:require privilege="View Dhisreport" otherwise="/login.htm"
                 redirect="/module/dhisreport/mapDatasets.form"/>
<%@ include file="template/localHeader.jsp" %>
<%@ include file="./resources/js/js_css.jsp" %>

<h3>Post data to DHIS2</h3>
<h4>Dataset: ${dataset.name}</h4>
<c:choose>
    <c:when test="${!isAllDataValuesTemplatesMapped}">
        <%-- Todo: display an error        --%>
        <div id='openmrs_error'>One or more DataValueTemplates not mapped with report indicators.
        </div>
    </c:when>
    <c:otherwise>
        <form id="form" action="postDataSet.form" method="post">
            <div>
                <label for="locationSelector">
                    Select the location: (Only the mapped locations are available)
                </label>
                <select id="locationSelector" name="locationUuid">
                    <c:forEach var="location" items="${mappedLocations}">
                        <option value="${location.uuid}">${location.name}</option>
                    </c:forEach>
                </select>
            </div>
            <c:choose>
                <c:when test="${dataset.periodType == 'Monthly'}">
                    <label for="monthSelector">Select the month:</label>
                    <input type="month" id="monthSelector" onchange="onMonthChange()">
                </c:when>
                <c:otherwise>
                    The period type isn't supported yet
                </c:otherwise>
            </c:choose>
            <input type="hidden" name="startDate" id="startDate"/>
            <input type="hidden" name="uid" value="${dataset.uid}"/>
            <button type="submit">
                Post Data
            </button>
        </form>
    </c:otherwise>
</c:choose>
<script>

  function onMonthChange() {
    let startDate = $('#monthSelector').val() + '-01';
    $('#startDate').val(startDate);
  }

  $('#form').submit(function (e) {
    if ($('#startDate').val() === "") {
      alert("Please select the period");
      e.preventDefault();
    }
  });

</script>
<%@ include file="/WEB-INF/template/footer.jsp" %>
