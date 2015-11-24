angular.module('ticketmonster').controller(
		'EditDataInputController',
		function($scope, $routeParams, $location, PerformanceResource) {
			var self = this;
			$scope.disabled = false;
			$scope.$location = $location;

			$scope.get = function() {
				var successCallback = function(data) {
					self.original = data;
					$scope.config = new PerformanceResource(self.original);
					$scope.config.environment = self.original.environment;
					if ($scope.config.addEmployee == 'yes')
						$scope.IsVisible = true;
					else
						$scope.IsVisible = false;
					$scope.environmentSelection = $scope.config.environment;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				PerformanceResource.get({
					id : $routeParams.id
				}, successCallback, errorCallback);
			};

			$scope.isClean = function() {
				return angular.equals(self.original, $scope.config);
			};

			$scope.save = function() {
				var successCallback = function() {
					$scope.get();
					$scope.displayError = false;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				PerformanceResource.update(
						update(angular.toJson($scope.config)), successCallback,
						errorCallback);
			};

			$scope.cancel = function() {
				$location.path("/DataInput");
			};

			$scope.remove = function() {
				var successCallback = function() {
					$location.path("/DataInput");
					$scope.displayError = false;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				$scope.config.$remove(successCallback, errorCallback);
			};
			$scope.get();
		});
