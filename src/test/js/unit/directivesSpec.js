describe("The input directive", function() {
	var compile, scope;
	beforeEach(function(){
		module('voodie');
		inject(function($compile, $rootScope){
			compile = $compile;
			scope = $rootScope.$new();
		})
	});
	//it('replaces the element with the appropriate content', function(){
		//TODO compile method fails.  Refactor directive
		//var element = angular.element("<bsinput id='test'></bsinput");
		//compile(element)(scope);
		//scope.$digest();
		//expect(element.html()).toContain('input');
	//});

});

describe("The facebook share directive", function() {
    beforeEach(function(){
        module('voodie');
    });
    it('shows the correct url', inject(function($compile, $rootScope){
        var element = angular.element("<fbshare election='1'></fbshare>");
        $compile(element)($rootScope);
        $rootScope.$digest();
        expect(element.html()).toContain("data-href=\"http://localhost:9876/voodie/#/election/1\"");
    }));
});

describe("The twitter share directive", function(){
    beforeEach(function(){
        module('voodie');
    });
    it('shows the correct url', inject(function($compile, $rootScope){
        var element = angular.element("<tweetshare election='1'></fbshare>");
        $compile(element)($rootScope);
        $rootScope.$digest();
        expect(element.html()).toContain("data-url=\"http://localhost:9876/voodie/#/election/1\"");
    }));
});
