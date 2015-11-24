angular.module('ticketmonster').controller(
		'NewEventController',
		function($scope, $location, locationParser, EventResource,
				BookingResource) {
			$scope.disabled = false;
			$scope.$location = $location;
			$scope.config = $scope.config || {};

			$scope.$watch("browserSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.browser = {};
					$scope.config.browser = selection;
				}
			});

			$scope.$watch("environmentSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.environment = {};
					$scope.config.environment = selection;
				}
			});

			$scope.save = function() {
				var successCallback = function(data, responseHeaders) {
					var id = locationParser(responseHeaders);
					$location.path('/DataConfig/edit/' + id);
					$scope.displayError = false;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				EventResource.save($scope.config, successCallback,
						errorCallback);
			};

			$scope.cancel = function() {
				$location.path("/DataConfig");
			};
		});
