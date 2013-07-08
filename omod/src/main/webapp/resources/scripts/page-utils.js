/**
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of SDMXHDataExport module.
 *
 *  SDMXHDataExport module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  SDMXHDataExport module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with SDMXHDataExport module.  If not, see <http://www.gnu.org/licenses/>.
 *
*/
DHISReport = {
		setAddress : function(add){
			window.location.href = openmrsContextPath + "/module/sdmxhddataexport/"+add;
		},
		checkValue : function()
		{
			var form = jQuery("#form");
			if( jQuery("input[type='checkbox']:checked",form).length > 0 )
			{ 
				if(confirm("Are you sure?"))
				{
					form.submit();
				}
			}
			else
			{
				alert("Please choose objects for deleting");
				return false;
			}
		},
		
		
		
		checkUpload : function()
		{
			var form1 = jQuery("#uploadDataElements");
			
			if( jQuery("input[type='file']",form1).val()!=""  )
			{ 
				if(confirm("Are you sure?"))
				{
					form1.submit();
				}
			}
			else
			{
				alert("Please choose objects for Uploading");
				
				return false;
			}
		},
		
		
		select : function()
		{
			jQuery("#form :checkbox:not(:checked)").attr("checked", true);
			
				
			
			
		},
		
		whatUrl : function(id){
			//alert(window.location.href);
			jQuery('#url').val(window.location.href);
		},
		unselect : function()
		{
			jQuery("#form :checked").attr("checked", false);
				
			
			
		},
		
		
		search : function(url, value){
			ACT.go(url+"?"+value+"="+jQuery("#"+value).val());
		},
		onChangeReportDataElement : function(thiz){
			ACT.go("reportDataElement.form?reportId="+jQuery(thiz).val());
		},
		setOutputType : function(type){
			jQuery("#outputType").val(type);
		},
		showDialog : function(reportId){
			jQuery("#reportId").val(reportId);
			jQuery('#excecuteQuery').dialog('open');
		},
		upload : function(){
			jQuery('#uploadBox').dialog('open');
		},
		showQuery : function(queryId){
			jQuery("#queryId").val(queryId);
			jQuery('#excecuteQuery').dialog('open');
		},
		extractMonth: function(date, from){
			jQuery.ajax({
				type : "GET",				
				url : "extractMonth.form",
				data : ({
					date: date
				}),
				success : function(data) {
					if(from){
						jQuery("#fromMonth").html("<b>From month: </b>" + data);
					} else {
						jQuery("#toMonth").html("<b>To month:     </b>" + data);
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert("ERROR " + xhr);
				}
			});
		}
		
		
}


