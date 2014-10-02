angular.module('voodie.foodtruck.election').controller('FoodTruckElectionSearchCtrl', ['$scope', '$location', 'Voodie', 'User', 'GoogleMaps', 'elections',
     function($scope, $location, Voodie, User, GoogleMaps, elections){
         $scope.openElections = [];
         $scope.closedElections = [];
         $scope.elections = elections;
         for(var i=0;i<$scope.elections.length;i++){
             var election = $scope.elections[i];
             if(election.status === "IN_PROGRESS"){
                 $scope.openElections.push(election);
             }else{
                 $scope.closedElections.push(election);
             }
         }
         $scope.viewElection = function(election){
             $location.path("foodtruck/viewElection/" + election.id);
         };
     }
]);


angular.module('voodie.foodtruck.election').controller('FoodTruckEditElectionCtrl', ['$scope', '$routeParams', 'Voodie', 'GoogleMaps', '$rootScope', 'election',
    function($scope, $routeParams, Voodie, GoogleMaps, $rootScope, election){
        $scope.election = election;
        $scope.candidate = {};
        $scope.addCandidate = function(){
            if($scope.addCandidateForm.$valid){
                $rootScope.alerts = [];
                var address = $scope.candidate.address;
                if(address){
                    GoogleMaps.geolocate(address,
                        function(longitude, latitude){
                            $scope.$apply(function(){
                                var candidate = {
                                    "latitude" : latitude,
                                    "longitude" : longitude,
                                    "displayName": $scope.candidate.displayName
                                };
                                Voodie.addCandidate($routeParams.e, candidate, function(data){
                                    $scope.election.candidates.push(candidate);
                                });
                                $scope.candidate = {};
                                $scope.addCandidateForm.submitted = false;
                            });
                        },function(){
                            $scope.$apply(function(){
                                $rootScope.alerts.push({type:'danger', message:'Invalid Location'});
                            });
                        }
                    );
                }
            }else{
                $scope.addCandidateForm.submitted = true;
            }
        };
    }
]);
angular.module('voodie.foodtruck.election').controller('FoodTruckCreateElectionCtrl', ['$scope', '$location', 'Voodie', '$rootScope',
    function ($scope, $location, Voodie, $rootScope){
        function mapServingTime(servingDate, servingTime){
            return new Date(servingDate.getFullYear(), servingDate.getMonth(),
                servingDate.getDate(), servingTime.getHours(), servingTime.getMinutes(), 0, 0);
        }
        function reset(){
            $scope.election = {
                //TODO smart defaults?
                servingDate : "",
                servingStartTime : new Date(),
                servingEndTime : new Date(),
                pollOpeningDate : "",
                pollClosingDate : ""
            };
        }
        reset();
        $scope.submit = function(){
            if($scope.createElectionForm.$valid){
                $scope.election.servingStartTime = mapServingTime($scope.election.servingDate, $scope.election.servingStartTime);
                $scope.election.servingEndTime = mapServingTime($scope.election.servingDate, $scope.election.servingEndTime);
                Voodie.createElection($scope.election, function(data){
                    $rootScope.clearAlerts = false;
                    $location.path("foodtruck/editElection/" + data.id);
                });
            }else{
                $scope.createElectionForm.submitted = true;
            }
        };
    }
]);
angular.module('voodie.foodtruck.election').controller('FoodTruckViewElectionCtrl', ['$scope', '$routeParams', '$location', 'Voodie', '$modal', '$rootScope', 'election',
    function($scope, $routeParams, $location, Voodie, $modal, $rootScope, election){
        $scope.candidatedSelected = false;
        $scope.candidateProgress = {};
        $scope.election = election;
        for(var i=0;i<$scope.election.candidates.length;i++){
            var candidate = $scope.election.candidates[i];
            if(candidate.percentageOfVotes >= 75){
                $scope.candidateProgress[candidate.id] = {
                    type: 'success',
                    value: candidate.percentageOfVotes
                };
            }else if(candidate.percentageOfVotes >= 50){
                $scope.candidateProgress[candidate.id] = {
                    type: 'info',
                    value: candidate.percentageOfVotes
                };
            }else if(candidate.percentageOfVotes >= 25){
                $scope.candidateProgress[candidate.id] = {
                    type: 'warning',
                    value: candidate.percentageOfVotes
                };
            }else{
                $scope.candidateProgress[candidate.id] = {
                    type: 'danger',
                    value: candidate.percentageOfVotes
                };
            }
        }
        $scope.election.candidates.sort(function(a,b){
            if(a.percentagOfVotes > b.percentageOfVotes){
                return -1;
            }else if(b.percentageOfVotes > a.percentageOfVotes){
                return 1;
            }
            return 0;
        });
        $scope.candidateSelected = ($scope.election.status === "CLOSED");

        $scope.electionUrl = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/voodie/#/election/" + $scope.election.id;
        $scope.makeSelection = function(candidate){
            Voodie.selectCandidate(candidate, function(data){
                $rootScope.showSelectionMade = true;
                $scope.candidateSelected = true;
            });
        };
    }
]);
