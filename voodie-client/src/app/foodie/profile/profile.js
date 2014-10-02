angular.module('voodie.foodie.profile').controller('FoodieProfileCtrl', ['$scope', 'Voodie', 'User', 'profile',
    function($scope, Voodie, User, profile){
        $scope.foodie = profile;
    }
]);
