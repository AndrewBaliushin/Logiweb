/**
 * Send request to remove Drivers and Truck from order.
 * @param order id
 * @author Andrey Baliushin
 */
function removeTruckAndDriverFromOrder(orderId) {
	$.ajax({
		type : "POST",
		url : "removeDriversAndTruckFromOrder",
		dataType : "json",
		data : {"orderId" : orderId},
		success : function(data) {
			location.reload(true);
		},
		error : function(data) {
			bootbox.alert(data.responseJSON.msg);
		}
	});
}