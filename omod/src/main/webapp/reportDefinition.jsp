<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h3>Report Definition for ${reportDefinition.name}</h3>

<form action="executeReport.form" method="post">
    <table>
        <tr>
            <td>Location</td>
            <td>
                <select name="location"> 
                    <c:forEach var="location" items="${locations}" >
                        <option value="${location.id}" />${location.name}</option>
                    </c:forEach>
                </select>
            </td>
        <tr>
            <td>Date (eg 2012-03-01)</td>
            <td><input type="text" name="date" /></td>
        </tr>
        <tr>                   
            <td /><td><input type="submit" value="Execute"></td>
        </tr>
    </table>
    <input type="hidden" name="reportDefinition_id" value="${reportDefinition.id}" />
</form>


<%@ include file="/WEB-INF/template/footer.jsp"%>