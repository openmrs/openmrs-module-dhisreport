<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<table>
    <thead style="background-color: #1AAC9B; color: white; padding: 2px;">
        <tr>
            <td>Action</td>
            <td>Link</td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>Import/Export report definitions</td>
            <td><a href="loadReportDefinitions.form">link</a></td>
        </tr>
        <tr>
            <td>Configure DHIS2 connection</td>
            <td><a href="configureDhis2.form">link</a></td>
        </tr>
        <tr>
            <td>Reports</td>
            <td><a href="listDhis2Reports.form">link</a></td>
        </tr>
    </tbody>
</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>