var mod = angular.module('ClientSearch', ['PersonServices','directives']);
addMocksToModule(mod);

var ClientSearchController = function(theScope,clientService) {
	theScope.clients= //clientService.query();
	
	[
		{name:"Dean",account:"1",number:"111",external:"E111"},
		{name:"Sean",account:"2",number:"222",external:"E112"},
		{name:"Aean",account:"3",number:"333",external:"E113"},
		{name:"Kean",account:"4",number:"444",external:"E114"},
		];
	
};
ClientSearchController.$inject = ['$scope','Client'];