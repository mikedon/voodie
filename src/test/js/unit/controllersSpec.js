describe("The Navbar Controller", function() {
    var scope;
    var UserServiceMock;
    var location;
    beforeEach(function(){
        UserServiceMock = {
            currentUser : "test"
        };
        angular.module('voodie')
        inject(function($rootScope, $controller, $location){
            scope = $rootScope.$new();
            $controller('NavbarCtrl', {$scope: scope, User: UserServiceMock});
            location = $location;
        });
    });
    it('should report the correct route is in view', function() {

        location.path('/foodtruck/registration');
	    expect(scope.register()).toBe(true);
	    location.path('/foodie/registration');
	    expect(scope.register()).toBe(true);
	    location.path('/invalid/registration');
	    expect(scope.register()).toBe(false);

	    location.path('/home');
	    expect(scope.home()).toBe(true);

	    location.path('/about');
	    expect(scope.about()).toBe(true);

	    location.path('/elections');
	    expect(scope.elections()).toBe(false);
	    location.path('/foodtruck/elections');
	    expect(scope.elections()).toBe(true);

	    location.path('/login');
	    expect(scope.login()).toBe(true);

	    location.path('/foodtruck/profile');
	    expect(scope.profile()).toBe(true);
	    location.path('/foodie/profile');
	    expect(scope.profile()).toBe(true);
	    location.path('/invalid/profile');
	    expect(scope.profile()).toBe(false);
    });

});
