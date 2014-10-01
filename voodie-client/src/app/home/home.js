angular.module( 'voodie.home', [
  'ui.bootstrap'
])

.controller('HomeCtrl', ['$scope', '$location', 'EatingTime', 'GoogleMaps',
    function ($scope, $location, EatingTime, GoogleMaps) {
        $scope.eatingTimestamp = EatingTime.getEatingTime();
        $scope.submit = function(){
            //TODO check if valid
            if($scope.address){
                GoogleMaps.geolocate($scope.address,function(longitude, latitude){
                    $scope.$apply(function(){
                        $scope.latitude = latitude;
                        $scope.longitude = longitude;
                        $location.path('/votes/' + $scope.latitude + '+' + $scope.longitude + '/' + $scope.eatingTimestamp.getTime());
                    });
                });
            }
        };
}]);

