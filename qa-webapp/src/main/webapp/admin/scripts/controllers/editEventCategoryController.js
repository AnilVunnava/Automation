angular.module('ticketmonster').controller(
		'EditMobileConfigController',
		function($scope, $routeParams, $location, EventCategoryResource) {
			var self = this;
			$scope.disabled = false;
			$scope.$location = $location;

			$scope.get = function() {
				var successCallback = function(data) {
					self.original = data;
					$scope.config = new EventCategoryResource(self.original);
					$scope.environmentSelection = $scope.config.environment;
					$scope.platformSelection = $scope.config.platform;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				EventCategoryResource.get({
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
				EventCategoryResource.$update(angular.toJson($scope.config),
						successCallback, errorCallback);
			};

			$scope.cancel = function() {
				$location.path("/MobileConfig");
			};

			$scope.remove = function() {
				var successCallback = function() {
					$location.path("/MobileConfig");
					$scope.displayError = false;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				$scope.eventCategory.$remove(successCallback, errorCallback);
			};

			$scope.get();
		});