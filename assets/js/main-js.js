function validateCity(address, cb) {
	/* REQUIRES STRING STREET ADDRESS LOCATION
	RETURNS TRUE IF CITY IS SUPPORTED OTHERWISE RETURN FALSE
	*/

	var validRoute = true;

	geocoder.geocode({
		'address': address
	}, function(results, status) {
		if (status == google.maps.GeocoderStatus.OK) {
			var city;
			for (var i = 0; i < results[0].address_components.length; i++) {
				if (results[0].address_components[i].types[0] == "locality") {
					city = results[0].address_components[i].long_name;
					aUser.setCity(city);
					console.log("User's city is: ", aUser.getCity())
					break;
				}
			};

			//if the city is not supported then this is not a valid route
			if (!SUPPORTED_CITIES[city]) {
				console.log("Not supported city");
				console.log("start city: " + city);
				validRoute = false;
			} else {
				console.log("supported city");
				console.log("start city: " + city);
				validRoute = true;
			}
			cb(validRoute);
		} else {
			console.log("Google API RESULT ERROR");
			console.log("Status" + google.maps.GeocoderStatus.OK);
		}

	})

}

function getBestRoute() {
	console.log("+getBestRoute");
	//sort routeCrimePts to ascending order
	routeCrimePts.sort(function(value1, value2) {
		return value1.totalCrimes - value2.totalCrimes;
	})
	console.log("-getBestRoute");

}

function chooseRoute(number) {
	var start = $(".start").val();
	var end = $(".end").val();
	if(validRoute)
	updateMarkers(number)
	updateRouteRenderer(Safewalk.getStartAddress(), Safewalk.getEndAddress(), number);
	currentRouteNum = number;
}
