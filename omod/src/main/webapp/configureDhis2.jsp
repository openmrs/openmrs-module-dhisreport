<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<h3>DHIS2 Server</h3>

<form method="POST">
    <table>
        <thead style="background-color: #1AAC9B; color: white; padding: 2px;">
            <tr>
                <td>Parameter</td>
                <td>Value</td>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Base DHIS2 URL (eg: http://apps.dhis2/demo )</td>
                <td><input name="url" type="text" size="30" value="${dhis2Server.url}" /></td>
            </tr>
            <tr>
                <td>User name</td>
                <td><input name="username" type="text" size="20" value="${dhis2Server.username}" /></td>
            </tr>
            <tr>
                <td>Password</td>
                <td><input name="password" type="password" size="20" value="${dhis2Server.password}"/></td>
            </tr>
            <tr>
                <td />
                <td><input name="submit" type="submit" value="Save" /></td>
            </tr>
        </tbody>
    </table>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>