# CommunityConnectAPI
- The default timeslot array is variable in length although the program will run into issues is it is anything other than length 7.
	- If a day is left blank add a blank list to the array for that day, do not negate the day all together.
- Put requests are used when updating existing documents, use a post request when adding a new document.

Requests:

	Meetings:
		- /api/booking - Gets all meetings
		- Add /id or /name followed by the id or name to get/ delete meetings with that id/ name
		- /api/booking/memberID/{memberID} can be used with a get of delete request to find bookings associated with a given member
		- /api/booking/IDandDatetime/{memberID} can be used with a get of delete request to find bookings associated with a given member ID and a date + time
		- /api/booking/dateAndStatus/{date}/{status} can be used with a get or delete request to find bookings associated with a given date + status
		- /api/booking/status/{status} can be used with a put request to update the status of a meeting
		- /api/booking/clearAll To clear all bookings

	Members:
		- /api/member - Gets all members
		- Add /id or /name followed by the id or name to get members or meetings with that id/ name
		- /api/active/{id} can be used with a put or a get to find members by the active variable
		- /api/member/timeslots/id/{id} returns the timeslots associated with the member of the given ID
		- /meetingid/{id} is a put endpoint where a meeting id can be added manually to the list of meeting associated with a given member
		  This endpoint in fairly unnecesary at present as when a meeting is accepted this is done automatically
		- /api/member/clearAll To clear all members

	MemberLogin:
		- api/memberLogin/{username} added BasicAuth with the username and password received then a member is posted to access this get endpoint
		  It will return the id of the given member

- Put(Update) Correct use:
	- Use the same syntax of the get resuest to update existing documents with a put request.
	- Do not send a put request without the id end point of the document(s) you wish to alter.
	- Put requests do not accept the /name end point as there may be duplicates of that name.
	- Altering a member will not alter their timeslots, use the /timeslot endpoint for this.


Entity Blueprints:

- /member:
	- Below is an example JSON doc from the member collection:
{
    "position": "Councillor",
    "name": "rory",
    "defaultTimeslots": [
    [
        8.4,
        9.4,
        10.1,
        11.4,
        12.4,
        13.4,
        14.4,
        15.4,
        16.4,
        17.4,
        18.4,
        19.4
    ],
    [
        8.4,
        9.4,
        10.1,
        11.4,
        12.4,
        13.4,
        14.4,
        15.4,
        16.4,
        17.4,
        18.4,
        19.4
    ],
    [],
    [],
    [],
    [
        8.4,
        9.4,
        10.1,
        11.4,
        12.4,
        13.4,
        14.4,
        15.4,
        16.4,
        17.4,
        18.4,
        19.4
    ],
    [
        8.4,
        9.4,
        10.1,
        11.4,
        12.4,
        13.4,
        14.4,
        15.4,
        16.4,
        17.4,
        18.4,
        19.4
    ]
]
}

	- Timeslots represents the available times for each member.
	- It is stored as a map with key being the date and value being a Float list
	- The integer portion of the Floats represent the hour on the 24 hour clock the rep is available on.
	- The decimal portion of the Floats represent the type of meeting the member is available for
		(.0 -> booked, .1 -> In person, .2 -> phone call, .3 -> video call, .4 -> all)

- /booking (meetings):
	- Below is an example JSON doc from the booking collection:

	{
    		 "dateTime": "2020-10-04T12:00:00.000",
  		 "type": 1,
   		 "status": 3,
   		 "name": "Martin Flanagan",
  		 "subject": "Dangers of 5G Radiation",
   		 "email": "FlanTheMan@gmail.com",
   		 "location": "5 Flomp Road, Flompsville",
   		 "memberId": "5f6fc39445a7b7702495d514"
	}

	- memberId refers to the ID of the member with whom this meeting if with.
	- Type refers to the type of meeting (1->In Person; 2->Phone; 3->Video Call).
	- Status refers to the current status of this meeting (0->Unbooked; 1->Accepted; 2->Declined; 3->Pending; 4->Disabled).
	- Ensure dateTime is on the hour

- The ID is automatically generated for both document types.
- The ID will be visible in the JSON retrieved from a get request.
