/**
 * Remove 
 * @param element -- reference to element, that triggered this function
 * @param id - of the truck
 * 
 * @author Andrey Baliushin
 */
function removeTruck(element, id) {
	bootbox.confirm("Delete truck?", function(result) { //bootboxjs.com
		if (result) {
			
			$.post("deleteTruck", {
				truckId : id
			}).fail(function(data) {
				alert("Data Loaded: " + data);
			}).success(function() {
				$(element).closest("tr").fadeOut(1000, function() {
					$(this).remove();
				});
			});

		}
	});
}