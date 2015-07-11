/**
 * Change status of the order to READY TO GO.
 * 
 * @author Andrey Baliushin
 */
function changeOrderStatusToReady(orderId) {
	bootbox.confirm("Status change can't be reversed. Are you sure?", function(result) { // bootboxjs.com
		if (result) {
			$.ajax({
				url : "changeOrderStatus",
				type : "POST",
				data : { orderId : orderId },
				dataType : "json",
				success : function(result) {
					location.reload(true);
				},
				error : function(result) {
					bootbox.alert(result.responseJSON.msg);
				}
			});
		}
	});
}