/**
 * Remove driver.
 * 
 * @param element --
 *            reference to element, that triggered this function (needed to
 *            remove row after succesful truck deletion)
 * @param id -
 *            of the driver
 * 
 * @author Andrey Baliushin
 */
function removeDriver(element, id) {
	bootbox.confirm("Delete driver?", function(result) { // bootboxjs.com
		if (result) {
			$.ajax({
				url : "deleteDriver",
				type : "POST",
				data : { driverId: id },
				dataType : "json",
				success : function(result) {
					$(element).closest("tr").fadeOut(1000, function() {
						$(this).remove();
					});
				},
				error : function(result) {
					bootbox.alert(result.responseJSON.msg);
				}
			});
		}
	});
}