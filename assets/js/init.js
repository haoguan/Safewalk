function init(start, end) {
	//TODO: MOVE INIT TO AFTER THE USER CLICKS SUBMIT

	var amazonIP = "ec2-54-215-147-231.us-west-1.compute.amazonaws.com"
	var IP = "localhost"
	var requestURL = "http://" + amazonIP + ":5000/?"
	var d = new Date();
	time = d.getHours() + ":" + d.getMinutes();
	var city = SUPPORTED_CITIES[aUser.getCity()];
	if (city) {
		var parameters = "day=" + aUser.getToday() + "&city=" + city + "&time=" + time;
		console.log("start", start)
		console.log("end", end)
		requestURL = requestURL + parameters;
		console.log("requestURL", requestURL)
		console.log("+data processing");
		initData(requestURL, function() {
			runMap(start, end);
		});
		console.log("-data processing");
	} else
		runMap(start, end);
}
$(function () {
	start_app(Safewalk.getStartAddress(), Safewalk.getEndAddress());
});

function start_app(start, end){
	validRoute = true; //keeps track of valid routes
	validateCity(start, function(validRouteA) {
		console.log("Check start location");
		if (validRouteA) {
			console.log("Check end location");
			validateCity(end, function(validRouteB) {
				if (validRouteB) {
					console.log("THIS IS A VALID CITY")
					validRoute = true;
				} else {
					validRoute = false;
				}
				init(start, end);

			});
		} else {
			validRoute = false;
			init(start, end);
		}

	});

};

function runMap(start, end) {
	var lat = 0;
	var lng = 0;
	console.log("----------------------")
	if (!hasMapInit) {
		initMap(lat, lng, function() {
			hasMapInit = true;
			calcRoute(start, end);
		});
	} else {
		calcRoute(start, end);
	}
}

function initData(dataLocation, callback){
	$.ajax({
		type: "GET",
		url: dataLocation,
		dataType: "json",
		success: function(data) {
			console.log("-- REQUESTING CRIME DATA FROM SERVER --")
			database = data;
			var numCrimes = 0;
			for (var key in data){
				numCrimes = numCrimes + 1;
			}
			console.log("Crimes Retrieved from DB: ", numCrimes);
			callback();
		}
	});
}