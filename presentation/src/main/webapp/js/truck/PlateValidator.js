/**
 * Listner for changes in License plate input field.
 * Checks if plate is valid and change css to 'has-error' if not.
 * 
 * @author Andrey Baliushin
 */
$( document ).ready(function() {
	$("#licencePlate").on('keyup change', function() {
		var plate = $('#licencePlate').val();
		
		if (isPlateValid(plate)) {
			$(this).parents('div').addClass('has-success');
			$(this).parents('div').removeClass('has-error');
		} else {
			$(this).parents('div').addClass('has-error');
			$(this).parents('div').removeClass('has-success');
		}
	});
});

/**
 * Plate validator.
 * 
 * @author Andrey Baliushin
 */
function isPlateValid(plateNumber) {
	var sevenLettersAndNumbersRegExp = /^[A-Z0-9]{7}$/i;
	var twoLettersAnywhereRegExp = /^[0-9]*[A-Z]{1}[0-9]*[A-Z]{1}[0-9]*$/i;
		
	if (plateNumber.match(sevenLettersAndNumbersRegExp)
			&& plateNumber.match(twoLettersAnywhereRegExp)) {
		return true;
	} else {
		return false;		
	}
}