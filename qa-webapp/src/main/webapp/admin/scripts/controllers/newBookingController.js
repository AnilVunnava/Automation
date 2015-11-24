angular.module('ticketmonster').controller(
		'NewBookingController',
		function($scope, $location, locationParser, BookingResource) {
			$scope.disabled = false;
			$scope.$location = $location;
			$scope.config = $scope.config || {};

			$scope.IsVisible = false;
			$scope.ShowHide = function() {
				$scope.IsVisible = $scope.config.remoteDriver;
			}
			$scope.$watch("environmentSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.id.environment = {};
					$scope.config.id.environment = selection;
				}
			});

			$scope.save = function() {
				var successCallback = function(data, responseHeaders) {
					var id = locationParser(responseHeaders);
					$location.path('/Configuration/edit/' + id);
					$scope.displayError = false;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				BookingResource.save(angular.toJson($scope.config),
						successCallback, errorCallback);
			};

			$scope.cancel = function() {
				$location.path("/Configuration");
			};
		});
