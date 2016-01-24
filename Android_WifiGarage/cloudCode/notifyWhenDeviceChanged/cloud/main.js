// Twilio Credentials 
var accountSid = 'AC4882de10cb3dc57242b4e82d1d312465'; 
var authToken = '65f14b4342ec8b24a35187f4d4af1d0b'; 

//require the Twilio module and create a REST client 
var client = require('twilio')(accountSid, authToken); 

// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

//this will trigger everytime this class's objects has changed
Parse.Cloud.afterSave("Device",function(request) {
	//Do something
	console.log("Object has been added/updated "+request.object.id);

	Parse.Push.send({
	  channels: [ "Giants"],
	  data: {
	    alert: "The Giants won against the Mets 2-3."
	  }
	}, {
	  success: function() {
	    console.log("Push was successful");
	  },
	  error: function(error) {
	    console.log("error push");// Handle error
	  }
	});


	 
	// Send an SMS message
	/*client.sendSms({
	    to:'+12042962786',
	    from: '+12048088077',
	    body: 'This is your first bollywood notification from the one and only Mr Mandeep Bollywood Saini.'
	  }, function(err, responseData) {
	    if (err) {
	      console.log(err);
	    } else {
	      console.log(responseData.from);
	      console.log(responseData.body);
	    }
	  }
	);

		// Send an SMS message
	client.sendSms({
	    to:'+12042962786',
	    from: '+12048088077',
	    body: 'Told you i would get twilio working with parse cloud code.'
	  }, function(err, responseData) {
	    if (err) {
	      console.log(err);
	    } else {
	      console.log(responseData.from);
	      console.log(responseData.body);
	    }
	  }
	);
		// Send an SMS message
	client.sendSms({
	    to:'+12049953920',
	    from: '+12048088077',
	    body: 'This is your first bollywood notification from the one and only Mr Mandeep Bollywood Saini.'
	  }, function(err, responseData) {
	    if (err) {
	      console.log(err);
	    } else {
	      console.log(responseData.from);
	      console.log(responseData.body);
	    }
	  }
	);*/
});
