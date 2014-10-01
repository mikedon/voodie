angular.module('voodie.election', [
	'ui.bootstrap'
])

.controller('ElectionCtrl', ['$scope', '$routeParams', '$location', 'Voodie', 'election', '$rootScope',
    function($scope, $routeParams, $location, Voodie, election, $rootScope){
        $scope.election = election;
        if($scope.election.candidates){
            var mapOptions = {
                center: new google.maps.LatLng($scope.election.candidates[0].latitude, $scope.election.candidates[0].longitude),
                zoom: 8,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            $scope.map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
            for(var i=0;i<$scope.election.candidates.length; i++){
                var candidate = $scope.election.candidates[i];
                var latLng = new google.maps.LatLng(candidate.latitude,candidate.longitude);
                var marker = new google.maps.Marker({
                    position: latLng,
                    map: $scope.map,
                    title: candidate.displayName
                });
            }
            $scope.candidateChoice = "";
            $scope.vote = function(){
                Voodie.vote($scope.candidateChoice, function(data){
                    $rootScope.showVoteSuccess = true;
                });
            };
        }
    }
]);
