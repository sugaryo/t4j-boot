function copy($txt) {
	$txt.select();
	document.execCommand("copy");
}

function altenter(event, $txt, $btn) {
	
	// [Alt]+[Enter]
	if (event.altKey && event.keyCode == 13) {

		console.log("Alt+Enter");

		var text = $txt.val();
		console.log(text);

		$btn.click();
		return false;
	}
}

$(document).ready(function() {
	console.log("document.ready");
});