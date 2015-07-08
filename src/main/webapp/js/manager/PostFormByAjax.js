/**
 * Submit form by POST with its original action address.
 * Reloads page on success.
 * If failed will show error message from recieved json 'msg'.
 * @param formId selector in jquery format ('#formId')
 * @author Andrey Baliushin
 */
function postFormByAjax(formId) {
	$.ajax({
		type : "POST",
		url : $(formId).attr('action'),
		dataType : "json",
		data : $(formId).serialize(),
		success : function(data) {
			location.reload(true);
		},
		error : function(data) {
			bootbox.alert(data.responseJSON.msg);
		}
	});
}
