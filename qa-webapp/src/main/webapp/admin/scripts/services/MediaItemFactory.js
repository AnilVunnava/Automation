angular.module('ticketmonster').factory('MediaItemResource',
		function($resource) {
			var resource = $resource('../rest/forge/mailconfig/:id', {
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