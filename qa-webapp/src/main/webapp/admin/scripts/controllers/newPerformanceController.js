angular.module('ticketmonster').controller(
		'NewDataInputController',
		function($scope, $location, locationParser, PerformanceResource) {
			$scope.disabled = false;
			$scope.$location = $location;
			$scope.config = $scope.config || {};

			$scope.IsVisible = false;
			$scope.InoutVisible = false;
			$scope.HpdVisible = false;
			$scope.UdfVisible = false;
			$scope.hmyVisible = false;
			$scope.tmcVisible = false;
			$scope.hmyProjVisible = false;
			$scope.tmcConfVisible = false;
			$scope.tmcClientConfVisible = false;
			$scope.tmcGroupConfVisible = false;
			$scope.tmcRoleConfVisible = false;

			$scope.$watch("config.projectCount", function(value) {
				if (value > 0)
					$scope.hmyProjVisible = true;
				else
					$scope.hmyProjVisible = false;
			});

			$scope.$watch("addWebClientSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.addWebClient = {};
					$scope.config.addWebClient = selection;
				}
				if ($scope.config.addWebClient == 'yes') {
					$scope.tmcClientConfVisible = true;
				}
				if ($scope.config.addWebClient == 'no') {
					$scope.tmcClientConfVisible = false;
				}
				if (typeof selection == 'undefined') {
					$scope.tmcClientConfVisible = false;
				}
			});

			$scope.$watch("addBranchSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.addBranch = {};
					$scope.config.addBranch = selection;
				}
				if ($scope.config.addBranch == 'yes') {
					$scope.tmcGroupConfVisible = true;
				}
				if ($scope.config.addBranch == 'no') {
					$scope.tmcGroupConfVisible = false;
				}
				if (typeof selection == 'undefined') {
					$scope.tmcGroupConfVisible = false;
				}
			});

			$scope.$watch("addRoleSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.addRole = {};
					$scope.config.addRole = selection;
				}
				if ($scope.config.addRole == 'yes') {
					$scope.tmcRoleConfVisible = true;
				}
				if ($scope.config.addRole == 'no') {
					$scope.tmcRoleConfVisible = false;
				}
				if (typeof selection == 'undefined') {
					$scope.tmcRoleConfVisible = false;
				}
			});

			$scope.$watch("timeEntryMethodSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.timeEntryMethod = {};
					$scope.config.timeEntryMethod = selection;
				}
			});

			$scope.$watch("actionSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.action = {};
					$scope.config.action = selection;
				}
			});

			$scope.$watch("addEmployeeSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.addEmployee = {};
					$scope.config.addEmployee = selection;
				}
				if ($scope.config.addEmployee == 'yes')
					$scope.IsVisible = true;
				if ($scope.config.addEmployee == 'no')
					$scope.IsVisible = false;

				if (typeof selection == 'undefined') {
					$scope.IsVisible = false;
				}
			});

			$scope.$watch("processFromTMCSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.processFromTMC = {}
					$scope.config.processFromTMC = selection;
				}
				if ($scope.config.processFromTMC == 'yes') {
					$scope.hmyVisible = false;
					$scope.tmcVisible = true;
					$scope.config.processEmployee = 'no';
					$scope.tmcConfVisible = true;
				}
				if ($scope.config.processFromTMC == 'no') {
					$scope.tmcVisible = false;
					$scope.hmyVisible = false;
					$scope.tmcConfVisible = false;
					if ($scope.config.processEmployee == 'yes'
							&& $scope.config.processEmployee != '') {
						$scope.hmyVisible = true;
						$scope.tmcVisible = true;
					}
				}

				if (typeof selection == 'undefined') {
					$scope.hmyVisible = false;
					$scope.tmcVisible = false;
					$scope.tmcConfVisible = false;
				}

				if ($scope.config.processFromTMC == '') {
					$scope.tmcVisible = false;
					$scope.tmcConfVisible = false;
					if ($scope.config.processEmployee != 'yes'
							&& $scope.config.processEmployee != '') {
						$scope.tmcVisible = true;
						$scope.hmyVisible = true;
						$scope.tmcConfVisible = false;
					}
				}
			});

			$scope.$watch("processEmployeeSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.processEmployee = {}
					$scope.config.processEmployee = selection;
				}
				if ($scope.config.processEmployee == 'yes') {
					$scope.hmyVisible = true;
					$scope.tmcVisible = true;
					$scope.config.processFromTMC = 'no';
				}
				if ($scope.config.processEmployee == 'no') {
					$scope.hmyVisible = false;
					$scope.tmcVisible = false;
					if ($scope.config.processFromTMC == 'yes'
							&& $scope.config.processFromTMC != '') {
						$scope.hmyVisible = false;
						$scope.tmcVisible = true;
					}
				}

				if (typeof selection == 'undefined') {
					$scope.hmyVisible = false;
					$scope.tmcVisible = false;
				}

				if ($scope.config.processEmployee == '') {
					$scope.hmyVisible = false;
					if ($scope.config.processFromTMC != 'yes'
							&& $scope.config.processFromTMC != '') {
						$scope.tmcVisible = false;
					}
				}
			});

			$scope.$watch("empTypeSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.empType = {};
					$scope.config.empType = selection;
				}
				if ($scope.config.empType == 'inout') {
					$scope.InoutVisible = true;
					$scope.HpdVisible = false;
					$scope.UdfVisible = false;
				}
				if ($scope.config.empType == 'hpd') {
					$scope.InoutVisible = false;
					$scope.HpdVisible = true;
					$scope.UdfVisible = false;
				}

				if ($scope.config.empType == 'inout-udf') {
					$scope.InoutVisible = true;
					$scope.UdfVisible = true;
					$scope.HpdVisible = false;
				}
				if ($scope.config.empType == 'hpd-udf') {
					$scope.InoutVisible = false;
					$scope.UdfVisible = true;
					$scope.HpdVisible = true;
				}

				if (typeof selection == 'undefined') {
					$scope.InoutVisible = false;
					$scope.HpdVisible = false;
					$scope.UdfVisible = false;
				}
				if ($scope.config.empType == '') {
					$scope.InoutVisible = false;
					$scope.HpdVisible = false;
					$scope.UdfVisible = false;
				}
			});

			$scope.$watch("environmentSelection", function(selection) {
				if (typeof selection != 'undefined') {
					$scope.config.environment = selection;
				}
			});

			$scope.save = function() {
				var successCallback = function(data, responseHeaders) {
					var id = locationParser(responseHeaders);
					$location.path("/DataInput/edit/" + id);
					$scope.displayError = false;
				};
				var errorCallback = function() {
					$scope.displayError = true;
				};
				alert("JSON : " + angular.toJson($scope.config))
				PerformanceResource.save($scope.config,
						successCallback, errorCallback);
			};

			$scope.cancel = function() {
				$location.path("/DataInput");
			};
		});
