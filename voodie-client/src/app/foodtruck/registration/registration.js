angular.module('voodie.foodtruck.registration').controller('FoodTruckRegistrationCtrl', ['$scope','Voodie', '$rootScope', 'districts',
    function($scope, Voodie, $rootScope, districts){
        $scope.registration = {};
        $scope.districts = districts;
        $scope.submit = function(){
            if($scope.foodTruckRegistrationForm.$valid){
                var path = "foodtruck/elections";
                if($rootScope.captureRedirect){
                    path = $rootScope.captureRedirect;
                }
                Voodie.registerTruck($scope.registration, path);
            }else{
                $scope.foodTruckRegistrationForm.submitted = true;
            }
        };
    }
]);
