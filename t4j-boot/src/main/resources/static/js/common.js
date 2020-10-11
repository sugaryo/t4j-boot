function copy(id) {
	var obj = document.getElementById(id);
	
	obj.select();
	document.execCommand("copy");
}