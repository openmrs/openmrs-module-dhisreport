<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<openmrs:require privilege="View Dhisreport" otherwise="/login.htm"
                 redirect="/module/dhisreport/mapDatasets.form"/>
<%@ include file="template/localHeader.jsp" %>
<%@ include file="./resources/js/js_css.jsp" %>

<h3>Post data to DHIS2</h3>
<h4>Dataset: ${dataset.name} | Period Type: ${dataset.periodType}</h4>
<div id="error" class="error" style="display: none"></div>
<c:choose>
    <c:when test="${!isAllDataValuesTemplatesMapped}">
        <%-- Todo: display an error        --%>
        <div class="openmrs_error">
            One or more DataValueTemplates not mapped with report indicators.
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
            <div id="monthSelectorSection" style="display: none">
                <label for="monthSelector">Select the month:</label>
                <input type="month" id="monthSelector" onchange="onMonthChange()">
            </div>
            <div id="weekSelectorSection" style="display: none">
                <label for="weekSelector">Select the start date of the week:</label>
                <input id="weekSelector">
            </div>
            <div id="quarterSelectorSection" style="display: none">
                <label for="quarterYearSelector">Year: </label>
                <input type="number" id="quarterYearSelector" min="1970" onChange="onQuarterYearChange()" />
                <label for="quarterSelector">quarter: </label>
                <input type="number" id="quarterSelector" min="1" onchange="updateQuarterRange()"/>
            </div>
            <div id="daySelectorSection" style="display: none">
                <label for="daySelector">Select the date:</label>
                <input id="daySelector">
            </div>
            <div id="yearSelectorSection" style="display: none">
                <label for="yearSelector">Select the year:</label>
                <input type="number" id="yearSelector" min="1970" onChange="onYearChange()" />
            </div>
            <p>
                <i>
                    <spring:message code="dhisreport.selectedPeriod"/>&nbsp;
                    <span id="fromDate"></span>&nbsp;-&nbsp;
                    <span id="toDate"></span>
                </i>
            </p>
            <input type="hidden" name="startDate" id="startDate"/>
            <input type="hidden" name="uid" value="${dataset.uid}"/>
            <button type="submit">
                Post Data
            </button>
        </form>
        <script>
          const periodType = "${dataset.periodType}";
          const errorMessageContainer = $('#error');
          $(function() {
            switch (periodType) {
              case "Daily":
                $('#daySelectorSection').show();
                const maxDate = new Date();
                maxDate.setDate(maxDate.getDate()-1);
                $("#daySelector").datepicker( { dateFormat: 'yy-mm-dd',
                  beforeShowDay: function(date) {
                        return [date <= maxDate];
                      },
                  onSelect: function(startDate) {
                    const startTime = moment(startDate, "YYYY-MM-DD");
                    $('#startDate').val(startDate);
                    $('#fromDate').text(startTime.format('YYYY-MM-DD H:mm:ss'));
                    $('#toDate').text(startTime.add(1, 'days').format('YYYY-MM-DD H:mm:ss'));
                  }});
                break;
              case "Monthly":
                $('#monthSelectorSection').show();
                $('#monthSelector').attr("max", moment().add(-1,'months').format("YYYY-MM"));
                break;
              case "BiMonthly":
                $('#monthSelectorSection').show();
                $('#monthSelector').attr("max", moment().add(-2,'months').format("YYYY-MM"));
                break;
              case "Yearly":
                const currentYear = moment().year();
                $('#yearSelectorSection').show();
                $('#yearSelector').attr("max", currentYear-1);
                $('#yearSelector').val(currentYear-1);
                onYearChange();
                break;
              case "Weekly":
              case "WeeklyWednesday":
              case "WeeklyThursday":
              case "WeeklySaturday":
              case "WeeklySunday":
              case "BiWeekly":
                let dayIndex = 1;
                // Number of dates in the duration
                let duration = 7;
                switch (periodType) {
                  case "WeeklySunday":
                    dayIndex = 0;
                    break;
                  case "WeeklyWednesday":
                    dayIndex = 3;
                    break;
                  case "WeeklyThursday":
                    dayIndex = 4;
                    break;
                  case "WeeklySaturday":
                    dayIndex = 6;
                    break;
                  case "BiWeekly":
                    duration = 14;
                }
                const possibleLastDate = new Date();
                possibleLastDate.setDate(possibleLastDate.getDate() - duration);
                $('#weekSelector').datepicker({
                  beforeShowDay: function(date) {
                    return [date < possibleLastDate && date.getDay() === dayIndex];
                  },
                  dateFormat: 'yy-mm-dd',
                  onSelect: function(startDate) {
                    $('#startDate').val(startDate);
                    $('#fromDate').text(startDate);
                    $('#toDate').text(moment(startDate, "YYYY-MM-DD").add(duration-1, 'days').format('YYYY-MM-DD'));
                  }
                });
                $('#weekSelectorSection').show();
                break;
                case "Quarterly":
                  let quarterSelector = $('#quarterSelector');
                  const quarterYearSelector = $('#quarterYearSelector');
                  const currentQuarter = moment().quarter();
                  let maxYear = getMaxQuarterYear();
                  quarterYearSelector.attr('max', maxYear);
                  quarterYearSelector.val(maxYear);
                  quarterSelector.attr('max', currentQuarter - 1);
                  quarterSelector.val(currentQuarter - 1);
                  $('#quarterSelectorSection').show();
                  updateQuarterRange();
                  break;
                default:
                  $('#form').hide();
                  errorMessageContainer.text('<spring:message code="dhisreport.unsupportedPeriodType" />');
                  errorMessageContainer.show();
            }
          });

          function onMonthChange() {
            let startDate = $('#monthSelector').val() + '-01';
            $('#startDate').val(startDate);
            $('#fromDate').text(startDate);
            let endMonth =  moment(startDate, "YYYY-MM-DD");
            if (periodType === "BiMonthly") {
              endMonth.add(1, 'month');
            }
            $('#toDate').text(endMonth.endOf('month').format('YYYY-MM-DD'))
          }

          function onQuarterYearChange() {
            const quarterYearSelector = $('#quarterYearSelector');
            let maxYear = getMaxQuarterYear();
            if (parseInt(quarterYearSelector.val()) > maxYear) {
              quarterYearSelector.val(maxYear);
            }
            const quarterSelector = $('#quarterSelector');
            if (parseInt(quarterYearSelector.val()) >= moment().year()) {
              const currentQuarter = moment().quarter();
              quarterSelector.attr('max', currentQuarter);
              if (quarterSelector.val() >= currentQuarter) {
                quarterSelector.val(currentQuarter - 1);
              }
            } else {
              quarterSelector.attr('max', 4);
            }
            updateQuarterRange();
          }

          function getMaxQuarterYear() {
            let maxYear = moment().year();
            if (moment().quarter() === 1) {
              maxYear--;
            }
            return maxYear;
          }

          function updateQuarterRange(){
            const year = $('#quarterYearSelector').val();
            const quarter = $('#quarterSelector').val();
            const startDate = moment(new Date(year, (quarter-1)*3, 1));
            const formattedStartDate = startDate.startOf('quarter').format('YYYY-MM-DD');
            const endDate = startDate.endOf('quarter');
            $('#startDate').val(formattedStartDate);
            $('#fromDate').text(formattedStartDate);
            $('#toDate').text(endDate.format('YYYY-MM-DD'));
          }

          function onYearChange(){
            const yearSelector = $('#yearSelector');
            let selectedYear = yearSelector.val();
            const currentYear = moment().year();
            if(selectedYear >= currentYear){
              yearSelector.val(currentYear - 1);
              selectedYear = currentYear -1;
            }
            const startDate = selectedYear + "-01-01";
            $('#startDate').val(startDate);
            $('#fromDate').text(startDate);
            $('#toDate').text(selectedYear + "-12-31");
          }

          $('#form').submit(function (e) {
            if ($('#startDate').val() === "") {
              alert("Please select the period");
              e.preventDefault();
            }
          });

        </script>
    </c:otherwise>
</c:choose>
<%@ include file="/WEB-INF/template/footer.jsp" %>
