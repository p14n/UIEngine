var addMocksToModule = function(module) {
	if(parent.mocks) {

		module.config(function($provide){
			$provide.decorator('$httpBackend', angular.mock.e2e.$httpBackendDecorator);
		});
		angular.forEach(parent.mocks,function(it){
			module.run(it);
		});
	}
}