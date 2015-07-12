/**
* Limits number of checked checkboxes for Drivers
* assignment to Order.
* @author Andrey Baliushin
*/
$(document).ready(
		function() {
			maxDrivers = $("#assignDriverForm").find(
					'input:hidden[name=maxDriversToAssign]').val();
			if(maxDrivers !== undefined) {
				$('#assignDriverForm input[type=checkbox]').on('change', function(evt) {
					if($('#assignDriverForm input[type=checkbox]:checked').length >= maxDrivers) {
						$('#assignDriverForm input[type=checkbox]').not(":checked").attr("disabled", true);
					} else {
						$('#assignDriverForm input[type=checkbox]').not(":checked").attr("disabled", false);
					}
				});
			}
			
		});