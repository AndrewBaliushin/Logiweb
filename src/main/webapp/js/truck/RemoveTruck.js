/**
 * Remove truck.
 * 
 * @param element --
 *            reference to element, that triggered this function (needed to
 *            remove row after succesful truck deletion)
 * @param id -
 *            of the truck
 * 
 * @author Andrey Baliushin
 */
function removeTruck(element, id) {
	bootbox.confirm("Delete truck?", function(result) { // bootboxjs.com
		if (result) {
			$.ajax({
				url : "deleteTruck",
				type : "POST",
				data : {
					truckId : id
				},
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