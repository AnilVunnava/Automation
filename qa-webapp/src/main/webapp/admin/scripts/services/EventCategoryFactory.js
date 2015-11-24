angular.module('ticketmonster').factory('EventCategoryResource',
		function($resource) {
			var resource = $resource('../rest/forge/mobileconfig/:id', {
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