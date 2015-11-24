angular.module('ticketmonster').factory('PerformanceResource',
		function($resource) {
			var resource = $resource('../rest/forge/datainput/:id', {
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