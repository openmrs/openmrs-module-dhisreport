<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<table>
    <thead>
        <tr>
            <td>Action</td>
            <td>Link</td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>Load report definitions</td>
            <td><a href="loadReportDefinitions.form">link</a></td>
        </tr>
        <tr>
            <td>Export report definitions</td>
            <td><a href="exportReportDefinitions">link</a></td>
        </tr>
        <tr>
            <td>Configure DHIS2 connection</td>
            <td><a href="configureDhis2.form">link</a></td>
        </tr>
        <tr>
            <td>Reports</td>
            <td><a href="listReports.form">link</a></td>
        </tr>
    </tbody>
</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>