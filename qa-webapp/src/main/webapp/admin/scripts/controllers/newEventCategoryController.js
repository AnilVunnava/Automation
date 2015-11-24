angular.module('ticketmonster').controller(
		'NewMobileConfigController',
		function($scope, $location, locationParser, EventCategoryResource) {
			$scope.disabled = false;
			$scope.$location = $location;
			$scope.config = $scope.config || {};

			$scope.$watch("platformSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.platform = {};
					$scope.config.platform = selection;
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
					$location.path('/MobileConfig/edit/' + id);
					$scope.displayError = false;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				EventCategoryResource.save($scope.config, successCallback,
						errorCallback);
			};

			$scope.cancel = function() {
				$location.path("/MobileConfig");
			};
		});