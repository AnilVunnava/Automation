angular.module('ticketmonster').controller(
		'EditMailConfigController',
		function($scope, $routeParams, $location, locationParser,
				MediaItemResource) {
			var self = this;
			$scope.disabled = false;
			$scope.$location = $location;

			$scope.get = function() {
				var successCallback = function(data) {
					self.original = data;
					$scope.config = new MediaItemResource(self.original);
					$scope.config.environment = self.original.environment;
					$scope.environmentSelection = $scope.config.environment;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				MediaItemResource.get({
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
				$scope.config.$update($scope.config, successCallback,
						errorCallback);
			};

			$scope.cancel = function() {
				$location.path("/MailConfig");
			};

			$scope.get();
		});
