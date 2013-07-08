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
function changePageSize( baseLink )
{
	var pageSize = jQuery("#sizeOfPage").val();
	if(  !/^ *[0-9]+ *$/.test(pageSize) ){
		alert("Invalid number!");
	}else {
		window.location.href = baseLink +"pageSize=" + pageSize ;
	}
}

function jumpPage( baseLink )
{
	var pageSize = jQuery("#sizeOfPage").val();
	var currentPage = jQuery("#jumpToPage").val();
	if(  !/^ *[0-9]+ *$/.test(pageSize)  ||  !/^ *[0-9]+ *$/.test(currentPage) ){
		alert("Invalid number!");
	}else {
		window.location.href = baseLink +"pageSize=" + pageSize +"&currentPage=" +currentPage;
	}
}