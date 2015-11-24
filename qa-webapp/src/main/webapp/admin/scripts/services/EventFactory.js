angular.module('ticketmonster').factory('EventResource', function($resource) {
	var resource = $resource('../rest/forge/dataconfig/:id', {
		id : '@id'
	}, {
		'queryAll' : {
			method : 'GET',
			isArray : true
		},
		'query' : {
			method : 'GET',
			isArray : false
		},
		'update' : {
			method : 'PUT'
		}
	});
	return resource;
});