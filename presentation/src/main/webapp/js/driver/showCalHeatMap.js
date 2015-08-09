/**
 * Shows on page in #cal-heatmap tag result of 
 * plugin 'cal-heatmap'.
 */
$(document).ready(function() {
	
	$.ajax({
	    url: window.location.pathname + '/calendarHeatMapData'
	}).done(function (data) {
		var calendar = new CalHeatMap();
	    calendar.init({
	        data: data,
	        itemName: ["hour", "hours"],
	        range: 1, // Number of days to display
	        domain: "month", // Display days
	        subDomain: "day", // Split each day by hours
	        browsing: true, // Enable browsing
	        cellSize: 25,
	        tooltip: true,
	        afterLoadNextDomain: function (start, end) {
	            alert("You just loaded a new domain starting on " + start + " and ending on " + end);
	        }
	    });	

	});
});

