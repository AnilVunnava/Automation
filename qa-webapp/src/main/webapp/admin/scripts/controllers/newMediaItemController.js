angular.module('ticketmonster').controller(
		'NewMailConfigController',
		function($scope, $location, locationParser, MediaItemResource) {
			$scope.disabled = false;
			$scope.$location = $location;
			$scope.config = $scope.config || {};

			$scope.$watch("environmentSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.environment = {};
					$scope.config.environment = selection;
				}
			});

			$scope.save = function() {
				var successCallback = function(data, responseHeaders) {
					var id = locationParser(responseHeaders);
					$location.path('/MailConfig/edit/' + id);
					$scope.displayError = false;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				MediaItemResource.save($scope.config, successCallback,
						errorCallback);
			};

			$scope.cancel = function() {
				$location.path("/MailConfig");
			};
		});
