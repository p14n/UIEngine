describe('Client search tests', function() {

var myMock = function($httpBackend) {
	$httpBackend.whenGET('clients/current.json').respond(clientSearchTestData);
};

mocks = [myMock];	

	beforeEach(function() {
		browser().navigateTo(getTestUrl());
    });

    it('Should display "clients" model with 4 clients', function() {
    	expect(repeater('.clientrow').count()).toBe(4);
    });
    it('Should display 1 client when filtered for "Dean"', function() {
   	input('query').enter('Dean');
    	expect(repeater('.clientrow').count()).toBe(1);
    });
      
});