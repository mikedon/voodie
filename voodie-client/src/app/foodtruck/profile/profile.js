angular.module('voodie.foodtruck.profile').controller('FoodTruckProfileCtrl', ['$scope', 'Voodie', 'User', 'profile',
    function($scope, Voodie, User, profile){
        $scope.foodTruck = profile;
    }
]);
