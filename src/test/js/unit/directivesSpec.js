describe("The input directive", function() {
	var compile, scope;
	beforeEach(function(){
		module('voodie');
		inject(function($compile, $rootScope){
			compile = $compile;
			scope = $rootScope.$new();
		})
	});
	it('replaces the element with the appropriate content', function(){
		var element = angular.element("<bsinput id='test'></bsinput");
		compile(element)(scope);	
		scope.$digest();

		expect(element.html()).toContain('input');
	});

});
