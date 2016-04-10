<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>
<%@ include file="./resources/js/js_css.jsp" %>
<script type="text/javascript">
      
    window.onload = function(){
        var errormsg = '${ConnectionErrorMessage}';
        if(errormsg=='DHIS2 Connection Not Established') {
            $("#menu").after("<div id='openmrs_error'>"+errormsg+"</div>");
        }
        
    }
</script>

<h3><spring:message code="dhisreport.dhis2" />DHIS2 Server</h3>

<form method="POST">
    <table>
    	<!-- openmrs:portlet url="globalProperties" parameters="title=${title}|propertyPrefix=dhisreport.|excludePrefix=dhisreport.started"/-->
        <thead style="background-color: #1AAC9B; color: white; padding: 2px;">
            <tr>
                <td><spring:message code="dhisreport.parameter" /></td>
                <td><spring:message code="dhisreport.value" /></td>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><spring:message code="dhisreport.dhis2URL" /></td>
                <td><input name="url" type="text" size="30" value="${dhis2Server.url}" /></td>
            </tr>
            <tr>
                <td><spring:message code="dhisreport.dhis2UserName" /></td>
                <td><input name="username" type="text" size="20" value="${dhis2Server.username}" /></td>
            </tr>
            <tr>
                <td><spring:message code="dhisreport.dhis2Password" /></td>
                <td><input name="password" type="password" size="20" value="${dhis2Server.password}"/></td>
            </tr>
            <tr>
                <td />
                <td><input name="submit" type="submit" value="<spring:message code="dhisreport.dhis2saveButton" />" /></td>
            </tr>
            
            <tr>
            <td><input value="Check Connection" name="checkConnection" type="submit" id="btnPrevious" class="submit_button"></td>
            </tr>

        </tbody>
    </table>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>