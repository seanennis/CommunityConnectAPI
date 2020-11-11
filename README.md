# CommunityConnectAPI
- The timeslot array is variable in length although the program may run into issues is it is anything other than length 14.
	- If a day is left blank add a blank list to the array for that day, do not negate the day all together.
- Put requests are used when updating existing documents, use a post request when adding a new document.
- When a members timeslots are updated all meetings which are cancelled by this change are returned by the put request,
	- all meetings in timeslots which are booked, re book those time slots (ie, the value after the decimal place for those times is set to .0)
	- meetings will remain saved as a document until the date they are booked for has passed. If their time slot becomes available again they take
		precedence over that timeslot. If you want a booking to be removeds at any point to avoid this, simply use the delete booking end point


Requests:

- Get:
	/api/member - Gets all members
	/api/booking - Gets all meetings
	- Add /id or /name followed by the id or name to get members or meetings with that id/ name
	- Use /api/member/timeslots/id or /name to get only the timeslots field of a given member

- Put (Update):
	- Use the same syntax of the get resuest to update existing documents with a put request.
	- Do not send a put request without the id end point of the document(s) you wish to alter.
	- Put requests do not accept the /name end point as there may be duplicates of that name.
	- Altering a member will not alter their timeslots, use the /timeslot endpoint for this.

- Post (Insert):
	- Use /api/member or /api/booking with a post request to post a new document.

- Delete:

	- Follow the same instructions as the Put request.
	- use /clearAll endpoint to remove all members or meetings.


Entity Blueprints:


- /member:
	- Below is an example JSON doc from the member collection:
	{
    		"position": "Councillor",
    		"name": "Rory O'Connor",
    		"timeslots": [
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
            			10.4,
            			11.4,
            			12.4
        		],
			[],
			[],
			[],
			[],
			[],
			[],
			[],
			[],
			[],
			[],
			[],
			[]
    		]
	}

	- Timeslots represents the available times for each member.
	- It is stored as an array of Float lists, the index of the array represents the day of week (0->monday; 6->Sunday)
	- The integer portion of the Floats represent the hour on the 24 hour clock the rep is available on.
	- The decimal portion of the Floats represent the type of meeting the member is available for
		(.0 -> booked, .1 -> In person, .2 -> phone call, .3 -> video call, .4 -> all)

- /booking (meetings):
	- Below is an example JSON doc from the booking collection:

	{
    		 "dateTime": "2020-10-04T12:48:03.327",
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

- The ID is automatically generated for both document types.
- The ID will be visible in the JSON retrieved from a get request.
