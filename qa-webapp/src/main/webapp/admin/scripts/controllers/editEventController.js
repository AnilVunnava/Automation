angular.module('ticketmonster').controller(
		'EditEventController',
		function($scope, $routeParams, $location, EventResource) {
			var self = this;
			$scope.disabled = false;
			$scope.$location = $location;

			$scope.get = function() {
				var successCallback = function(data) {
					self.original = data;
					$scope.config = new EventResource(self.original);
					$scope.config.environment = self.original.environment;
					$scope.environmentSelection = $scope.config.environment;
					$scope.browserSelection = $scope.config.browser;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				EventResource.get({
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
				EventResource.update(angular.toJson($scope.config),
						successCallback, errorCallback);
			};

			$scope.cancel = function() {
				$location.path("/DataConfig");
			};
			$scope.get();
		});
