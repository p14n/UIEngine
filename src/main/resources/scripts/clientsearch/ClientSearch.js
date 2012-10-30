
var mod = angular.module('ClientSearch', ['PersonServices']);
addMocksToModule(mod);

var ClientSearchController = function(theScope,clientService) {
	theScope.clients=clientService.query();
};
ClientSearchController.$inject = ['$scope','Client'];