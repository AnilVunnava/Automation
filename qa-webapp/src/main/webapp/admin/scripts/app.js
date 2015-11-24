'use strict';

angular.module('ticketmonster', [ 'ngRoute', 'ngResource' ]).config(
		[ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/', {
				templateUrl : 'views/landing.html',
				controller : 'LandingPageController'
			}).when('/Configuration', {
				templateUrl : 'views/Configuration/search.html',
				controller : 'SearchBookingController'
			}).when('/Configuration/new', {
				templateUrl : 'views/Configuration/detail.html',
				controller : 'NewBookingController'
			}).when('/Configuration/edit/:id', {
				templateUrl : 'views/Configuration/detail.html',
				controller : 'EditBookingController'
			}).when('/DataConfig', {
				templateUrl : 'views/DataConfig/search.html',
				controller : 'SearchEventController'
			}).when('/DataConfig/new', {
				templateUrl : 'views/DataConfig/detail.html',
				controller : 'NewEventController'
			}).when('/DataConfig/edit/:id', {
				templateUrl : 'views/DataConfig/detail.html',
				controller : 'EditEventController'
			}).when('/MailConfig', {
				templateUrl : 'views/MailConfig/search.html',
				controller : 'SearchMailConfigController'
			}).when('/MailConfig/new', {
				templateUrl : 'views/MailConfig/detail.html',
				controller : 'NewMailConfigController'
			}).when('/MailConfig/edit/:id', {
				templateUrl : 'views/MailConfig/detail.html',
				controller : 'EditMailConfigController'
			}).when('/MobileConfig', {
				templateUrl : 'views/MobileConfig/search.html',
				controller : 'SearchMobileConfigController'
			}).when('/MobileConfig/new', {
				templateUrl : 'views/MobileConfig/detail.html',
				controller : 'NewMobileConfigController'
			}).when('/MobileConfig/edit/:id', {
				templateUrl : 'views/MobileConfig/detail.html',
				controller : 'EditMobileConfigController'
			}).when('/DataInput', {
				templateUrl : 'views/DataInput/search.html',
				controller : 'SearchDataInputController'
			}).when('/DataInput/new', {
				templateUrl : 'views/DataInput/detail.html',
				controller : 'NewDataInputController'
			}).when('/DataInput/edit/:id', {
				templateUrl : 'views/DataInput/detail.html',
				controller : 'EditDataInputController'
			}).otherwise({
				redirectTo : '/'
			});
		} ]).controller('LandingPageController',
		function LandingPageController() {
		}).controller(
		'NavController',
		function NavController($scope, $location) {
			$scope.matchesRoute = function(route) {
				var path = $location.path();
				return (path === ("/" + route) || path.indexOf("/" + route
						+ "/") == 0);
			};
		});
