<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>
<%@ include file="./resources/js/js_css.jsp" %>

<script type="text/javascript">

    window.onload = function(){

        var errormsg = '${errorMessage}';

        if(errormsg) {
            $("#menu").after("<div id='openmrs_error'>"+errormsg+"</div>");

        }
    }

$(function(){ 

	
	$('input:radio[name=frequency]').click(function() {
		options1 = {
		pattern: 'yyyy-mmm', // Default is 'mm/yyyy' and separator char is not mandatory
		};
		options2 = {
		pattern: 'yyyy-mmm', // Default is 'mm/yyyy' and separator char is not mandatory
		monthNames: ['W01', 'W02', 'W03', 'W04', 'W05', 'W06', 'W07', 'W08', 'W09', 'W10', 'W11', 'W12',
				 'W13', 'W14', 'W15', 'W16', 'W17', 'W18', 'W19', 'W20', 'W21', 'W22', 'W23', 'W24',
				 'W25', 'W26', 'W27', 'W28', 'W29', 'W30', 'W31', 'W32', 'W33', 'W34', 'W35', 'W36',
				 'W37', 'W38', 'W39', 'W40', 'W41', 'W42', 'W43', 'W44', 'W45', 'W46', 'W47', 'W48',
				 'W49', 'W50', 'W51', 'W52', 'W53']
		};
		
		var val = $('input:radio[name=frequency]:checked').val();
	
		if( val == 'daily'){
	
	 
			$('#monthpicker').monthpicker('destroy');
			$('#monthpicker').datepicker('destroy');
			$('#monthpicker').datepicker(); 
		}
		if( val == 'weekly'){
			$('#monthpicker').datepicker('destroy');
			//$('#monthpicker').datepicker("option", "disabled", true);
			$('#monthpicker').monthpicker('destroy');
			//$('#monthpicker').monthpicker(options2);
			//$( "#monthpicker" ).datepicker( "option", "calculateWeek", myWeekCalc );
			//$('#monthpicker' ).datepicker({ calculateWeek: myWeekCalc });
			$('#monthpicker')
      		.datepicker(
			{
			firstDay : 1,
			dateFormat: 'yy/mm/dd',
			showOtherMonths: true,
			selectOtherMonths: true,
            changeMonth : true, // provide option to select Month
            changeYear : true, // provide option to select year
           
           
				onClose : function(dateText, inst) {
                  if ($(this).val() == "") {
                        return;
                  }
           
                  // below code will calculate the monday and set it in input box
                  var fromDate = $("#monthpicker").datepicker("getDate");
				  //var fromDate = $("#monthpicker").datepicker("getWeek");
                  var date = fromDate.getDate();
                  var month = fromDate.getMonth() + 1;// +1 as jan is 0
                  var year = $(
                  "#ui-datepicker-div .ui-datepicker-year :selected").val();
     
                  if (fromDate.getDay() != 1) { // if not monday
                        if (fromDate.getDay() == 0) {// if sunday
                              date = date - 6;
                        } else {
                              date = date + (-fromDate.getDay() + 1);
                        }
                  }
				  date2 = date + 6;
				  if(date == 0){
					date = 31;
					month = 12;
					year = year - 1;
				  }
                  var dateStr = month + "/" + date + "/" + year;
				  var dateStr2 = month + "/" + date2 + "/" + year;
				  var setDate = dateStr + "-" + dateStr2;
				  var week = $.datepicker.iso8601Week(new Date(fromDate))
				  if(week < 10){
						var SetWeek = year+"-"+"W0"+week;
				  } else {
						var SetWeek = year+"-"+"W"+week;
				  }
				  //var dateStr = year + "-" + fromDate;
                  $(this).val(SetWeek);
                  $(this).blur();// remove focus input box
        },
            duration : 0,

            onChangeMonthYear : function() {
                  setTimeout("applyWeeklyHighlight()", 100);
                  },// applyWeeklyHighlight is below
                  beforeShow : function() {
                        setTimeout("applyWeeklyHighlight()", 100);
                  }
            });
			$('#monthpicker').datepicker("option", "disabled", true);

			
		}
		
		if( val == 'monthly'){
	
			$('#monthpicker').datepicker('destroy');
			//$('#monthpicker').monthpicker('destroy');
			$('#monthpicker').monthpicker(options1);
		}
	});	

	

});

/**
 * Set highhlight on the week on hover.
 */
function applyWeeklyHighlight() {

      $('.ui-datepicker-calendar tr').each(function() {

            if ($(this).parent().get(0).tagName == 'TBODY') {
                  $(this).mouseover(function() {
                        $(this).find('a').css({
                              'background' : '#ffffcc',
                              'border' : '1px solid #dddddd'
                        });
                        $(this).find('a').removeClass('ui-state-default');
                        $(this).css('background', '#ffffcc');
                  });
                 
                  $(this).mouseout(function() {
                        $(this).css('background', '#ffffff');
                        $(this).find('a').css('background', '');
                        $(this).find('a').addClass('ui-state-default');
                  });
            }

      });
}


</script>



<h3><spring:message code="dhisreport.reportDefinitionFor" /> ${reportDefinition.name}</h3>

<form action="executeReport.form" method="post">
    <table>
       <%--  <tr>
            <td><spring:message code="dhisreport.Location" /></td>
            <td>
                <select name="location"> 
                    <c:forEach var="location" items="${locations}" >
                        <option>${location.name}</option>
                    </c:forEach>
                </select>
            </td> --%>
           <tr>
				<td><spring:message code="dhisreport.Frequency" /></td>
				<td>
					
						<input type="radio" name="frequency" value="daily">Daily
						<input type="radio" name="frequency" value="weekly">Weekly
						<input type="radio" name="frequency" value="monthly">Monthly
					
				</td>
			</tr> 
        <tr>
            <td><spring:message code="dhisreport.Date" /></td>
            <td><input type="text" name="date" id="monthpicker"/></td>
        </tr>
           <tr>
               <td><spring:message code="dhisreport.selectMapping" /></td>
               <td>
                   <input type="radio" name="mappingType" value="Reporting">Reporting
                   <input type="radio" name="mappingType" value="SQL">SQL
               </td>
           </tr>
        <tr>                   
            <td /><td><select name="resultDestination">
                    <option value="preview"><spring:message code="dhisreport.Preview" /></option>
                    <c:if test="${not empty dhis2Server}">
                        <option value="post"><spring:message code="dhisreport.postToDHIS" /></option>
                    </c:if>
                </select>
            </td>
        </tr>
        <tr>
            <td />
            <td>
                <input type="submit" value="<spring:message code="dhisreport.Generate" />" />
            </td>
        </tr>

    </table>
    <input type="hidden" name="reportDefinition_id" value="${reportDefinition.id}" />
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>