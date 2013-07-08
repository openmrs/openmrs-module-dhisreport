<%--
*  Copyright 2012 Society for Health Information Systems Programmes, India (HISP India)
*
*  This file is part of DHIS2 Reporting module.
*
*  DHIS2 Reporting module is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  (at your option) any later version.
*  DHIS2 Reporting module is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with Registration module.  If not, see <http://www.gnu.org/licenses/>.
*
--%> 

<script type="text/javascript">
	$ = jQuery.noConflict();
</script>

<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/moduleResources/dhisreport/dhisreport.css" />
<script type="text/javascript"  src="${pageContext.request.contextPath}/moduleResources/dhisreport/scripts/common-utils.js" ></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/moduleResources/dhisreport/scripts/paging.js" ></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/moduleResources/dhisreport/scripts/page-actions.js" ></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/moduleResources/dhisreport/scripts/page-utils.js" ></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/moduleResources/dhisreport/dhisreport.js" ></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/moduleResources/dhisreport/scripts/jquery/jquery.metadata.min.js" ></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/moduleResources/dhisreport/scripts/jquery/jquery.monthpicker.js" ></script>
<script type="text/javascript"  src="${pageContext.request.contextPath}/moduleResources/dhisreport/scripts/jquery/jquery.validate.min.js" ></script>



<script type="text/javascript">
    $ = jQuery.noConflict();

    // Get context path
    function getContextPath() {
        return "${pageContext.request.contextPath}";
    }

    REPORTDEFINITION = {
        edit: function(id) {
            jQuery("#reportDefinition_edit" + id).hide();

            if (jQuery("#reportDefinition_save" + id).html() == "") {
                jQuery("#reportDefinition_save" + id).append('<a onclick="REPORTDEFINITION.save(' + id + ')"><spring:message code="dhisreport.save" /></a>');

                var strQuery = jQuery("#reportDefinition_query" + id).text();
                jQuery("#reportDefinition_query" + id).html('<textarea id="textarea_query' + id + '">' + strQuery + '</textarea>');
            }
            else {
                jQuery("#reportDefinition_save" + id).show();
                var strQuery = jQuery("#reportDefinition_query" + id).text();
                jQuery("#reportDefinition_query" + id).html('<textarea id="textarea_query' + id + '">' + strQuery + '</textarea>');
            }
        },
        deleteReportDefinition: function(id) {
            if (confirm("<spring:message code="dhisreport.rus" />")) {
                var link = getContextPath() + "/module/dhisreport/deleteReportDefinition.htm?reportDefinition_id=" + id;
                if (opener != undefined) {
                    jQuery(opener.location).attr({href: link});
                } else {
                    jQuery(location).attr({href: link});
                }
            }
        },
        save: function(id) {
            var newQuery = jQuery("#textarea_query" + id).val();
            jQuery.ajax({
                type: "GET",
                url: getContextPath() + "/module/dhisreport/editDataValueTemplate.htm",
                data: ({
                    dataValueTemplate_id: id,
                    dataValueTemplate_query: newQuery
                }),
                success: function() {
                    //jQuery("#reportDefinition_query" + id).text(newQuery);
                    //jQuery("#reportDefinition_save" + id).hide();
                    //jQuery("#reportDefinition_edit" + id).show();
                    // to help the highlight function work
                    location.reload();
                },
                error: function(xhr, ajaxOptions, thrownError) {
                    alert(thrownError);
                }
            });
        }
    };
</script>
