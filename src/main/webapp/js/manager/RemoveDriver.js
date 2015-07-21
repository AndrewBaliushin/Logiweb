/**
 * Remove driver.
 * 
 * @param element --
 *            reference to element, that triggered this function (needed to
 *            remove row after succesful truck deletion)
 * @param driverId -
 *            of the driver
 * 
 * @author Andrey Baliushin
 */
function removeDriver(element, driverId) {
	bootbox.confirm("Delete driver?", function(result) { // bootboxjs.com
		if (result) {
			$.ajax({
				url : window.location.pathname + "/" + driverId + "/remove",
				type : "POST",
				dataType : "text",
				success : function(result) {
					$(element).closest("tr").fadeOut(1000, function() {
						$(this).remove();
					});
				},
				error : function(result) {
					bootbox.alert(result.responseText);
				}
			});
		}
	});
}