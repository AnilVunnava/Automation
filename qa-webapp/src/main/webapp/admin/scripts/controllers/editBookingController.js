angular.module('ticketmonster').controller(
		'EditBookingController',
		function($scope, $routeParams, $location, locationParser,
				BookingResource) {
			var self = this;
			$scope.disabled = false;
			$scope.$location = $location;
			
			$scope.ShowHide = function() {
				$scope.IsVisible = $scope.config.remoteDriver;
			}

			$scope.get = function() {
				var successCallback = function(data) {
					self.original = data;
					$scope.config = new BookingResource(self.original);
					$scope.config.id.environment = self.original.id.environment;
					$scope.IsVisible = $scope.config.remoteDriver;
					$scope.environmentSelection = $scope.config.id.environment;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				BookingResource.get({
					id : $routeParams.id
				}, successCallback, errorCallback);
			};

			$scope.isClean = function() {
				return angular.equals(self.original, $scope.config);
			};

			$scope.save = function() {
				var successCallback = function(responseHeaders) {
					$scope.get();
					$scope.displayError = false;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				BookingResource.update(angular.toJson($scope.config),
						successCallback, errorCallback);
			};

			$scope.cancel = function() {
				$location.path("/Configuration");
			};
			$scope.get();
		});