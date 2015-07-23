/**
 * Remove record by its id. (ex.: driver or truck)
 * 
 * @param element --
 *            reference to element, that triggered this function (needed to
 *            remove row after successful record deletion)
 * @param id of record
 * 
 * @author Andrey Baliushin
 */
function removeRecord(element, recordId) {
	bootbox.confirm("Delete record?", function(result) { // bootboxjs.com
		if (result) {
			$.ajax({
				url : window.location.pathname + "/" + recordId + "/remove",
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