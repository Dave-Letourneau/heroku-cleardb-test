
// Basic validation 
function validateForm() {
	//var w = document.getElementById("color").value;
	
    var x = document.getElementById("color").value;
    var y = document.getElementById("number").value;
    
    if (x == "" || y == "") {
        alert("Please fill out both color and number");
        return false;
    }
}