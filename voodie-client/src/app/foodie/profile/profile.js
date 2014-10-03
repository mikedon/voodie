angular.module('voodie').controller('FoodieProfileCtrl', ['$scope', 'Voodie', 'User', 'profile',
    function($scope, Voodie, User, profile){
        $scope.foodie = profile;
    }
]);
