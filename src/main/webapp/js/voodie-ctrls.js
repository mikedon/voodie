angular.module('voodie').controller('HomeCtrl', ['$scope', '$location', 'EatingTime', 'GoogleMaps',
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

angular.module('voodie').controller('ElectionsCtrl', ['$scope', '$location', 'Voodie', 'User', 'districts',
    function($scope, $location, Voodie, User, districts){
        function formatDate(date){
            var formattedDate = date;
            var dd = date.getDate();
            var mm = date.getMonth()+1; //January is 0!
            var yyyy = formattedDate.getFullYear();
            if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm} formattedDate = mm+'/'+dd+'/'+yyyy;
            return formattedDate;
        }



        $scope.goToElection = function(election){
            //TODO polite check to see if they are allowed to vote
            $location.path('election/' + election.id);
        }
        $scope.$watch('search.district + search.startDate + search.endDate', function(){
            if(!$scope.renderNoFiltersText()){
                $scope.elections = Voodie.getAllElections($scope.search.district.name, $scope.search.startDate, $scope.search.endDate, function(data){
                    $scope.electionRows = [];
                    for( var i = 0; i < data.length; i = i + 2 ){
                        $scope.electionRows.push(i);
                    }
                });
            }
        }) ;
        // date filters
        $scope.search = {};
        //district filter
        $scope.districts = districts;
        angular.forEach($scope.districts, function(value, key){
            if(value.name == User.district){
                $scope.search.district = value;
            }
        });
        $scope.search.startOpened = false;
        $scope.search.endOpened = false;
        $scope.search.startDate = formatDate(new Date());
        var endDate = new Date();
        endDate.setDate(endDate.getDate() + 7);
        $scope.search.endDate = formatDate(endDate);
        $scope.search.openStart = function() {
            $scope.search.startOpened = true;
        };
        $scope.openEnd = function() {
            $scope.search.endOpened = true;
        };
        // render rules
        $scope.renderNoElectionsText = function(){
            var isRendered = $scope.elections && $scope.elections.length == 0;
            isRendered &= !$scope.renderNoFiltersText();
            return Boolean(isRendered);
        };
        $scope.renderNoFiltersText = function(){
            var isRendered = !$scope.search.startDate || !$scope.search.endDate || !$scope.search.district;
            return Boolean(isRendered);
        };
    }
]);

angular.module('voodie').controller('ElectionCtrl', ['$scope', '$routeParams', '$location', 'Voodie', 'election', '$rootScope',
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
                })
            }
        }
    }
]);

angular.module('voodie').controller('VoteCtrl', ['$scope', '$routeParams', 'Voodie',
    function ($scope, $routeParams, Voodie) {
        $scope.currentPage = 1;
        $scope.categories = {};
        //pagination changes
        $scope.$watch('numPages + currentPage + maxSize', function(){
            $scope.foodTrucks = Voodie.getEntries($scope.currentPage, $routeParams.latitude, $routeParams.longitude, function(data){
                $scope.noOfPages = data.noOfPages;
                $scope.foodTruckRows = [];
                for( var i = 0; i < data.foodTrucks.length; i = i + 2 ){
                    $scope.foodTruckRows.push(i);
                }
                for(var i=0;i<data.foodTrucks.length;i++){
                    for(var c=0;c<data.foodTrucks[i].categories.length; c++){
                        var cat = data.foodTrucks[i].categories[c];
                        $scope.categories[cat] = cat;
                    }
                }
            });
        });

        $scope.vote = function(foodTruck){
            Voodie.vote(foodTruck.id, $routeParams.latitude, $routeParams.longitude, $routeParams.eatingTime);
            foodTruck.numberOfVotes++;
        };
    }
]);

angular.module('voodie').controller('FoodTruckRegistrationCtrl', ['$scope','Voodie', '$rootScope', 'districts',
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
        }
    }
]);

angular.module('voodie').controller('FoodieRegistrationCtrl', ['$scope', 'Voodie', '$rootScope', 'districts',
    function($scope, Voodie, $rootScope, districts){
        $scope.registration = {};
        $scope.districts = districts;
        $scope.submit = function(){
            if($scope.foodieRegistrationForm.$valid){
                var path = "elections";
                if($rootScope.captureRedirect){
                    path = $rootScope.captureRedirect;
                }
                Voodie.registerFoodie($scope.registration, path);
            }else{
                $scope.foodieRegistrationForm.submitted = true;
            }
        }
    }
]);

angular.module('voodie').controller('FoodTruckEditElectionCtrl', ['$scope', '$routeParams', 'Voodie', 'GoogleMaps', '$rootScope', 'election',
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
        }
    }
]);
angular.module('voodie').controller('FoodTruckCreateElectionCtrl', ['$scope', '$location', 'Voodie', '$rootScope',
    function ($scope, $location, Voodie, $rootScope){
        function mapServingTime(servingDate, servingTime){
            return new Date(servingDate.getFullYear(), servingDate.getMonth(),
                servingDate.getDate(), servingTime.getHours(), servingTime.getMinutes(), 0, 0);
        };
        function reset(){
            $scope.election = {
                //TODO smart defaults?
                servingDate : "",
                servingStartTime : new Date(),
                servingEndTime : new Date(),
                pollOpeningDate : "",
                pollClosingDate : ""
            };
        };
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

angular.module('voodie').controller('FoodTruckElectionCtrl', ['$scope', '$location', 'Voodie', 'User', 'GoogleMaps', 'elections',
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
            $location.path("foodtruck/viewElection/" + election.id)
        }
    }
]);

angular.module('voodie').controller('FoodTruckViewElectionCtrl', ['$scope', '$routeParams', '$location', 'Voodie', '$modal', 'election',
    function($scope, $routeParams, $location, Voodie, $modal, election){
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

        var SelectionMadeCtrl = function($scope, $modalInstance){
            $scope.ok = function () {
                $modalInstance.close();
            };
        };

        $scope.makeSelection = function(candidate){
            Voodie.selectCandidate(candidate, function(data){
                var modalInstance = $modal.open({
                    templateUrl: 'selectionMade.html',
                    controller: SelectionMadeCtrl
                });
                $scope.candidateSelected = true;
            });
        };
    }
]);

angular.module('voodie').controller('FoodTruckProfileCtrl', ['$scope', 'Voodie', 'User', 'profile',
    function($scope, Voodie, User, profile){
        $scope.foodTruck = profile;
    }
]);

angular.module('voodie').controller('FoodieProfileCtrl', ['$scope', 'Voodie', 'User', 'profile',
    function($scope, Voodie, User, profile){
        $scope.foodie = profile;
    }
]);

angular.module('voodie').controller('LoginCtrl', ['$scope', 'User', '$rootScope',
    function($scope, User, $rootScope){
        $scope.login = {};
        $scope.submit = function(){
            if($scope.loginForm.$valid){
                User.username = $scope.login.username;
                User.password = $scope.login.password;
                var path = "elections";
                if($rootScope.captureRedirect){
                    path = $rootScope.captureRedirect;
                }
                User.login(path);
            }else{
                $scope.loginForm.submitted = true;
            }
        }
    }
]);

angular.module('voodie').controller('CheckInCtrl', ['$scope', '$routeParams', '$location', 'Voodie', 'election',
    function($scope, $routeParams, $location, Voodie, election){
        $scope.election = election;
        $scope.checkedIn = false;
        $scope.checkIn = function(){
            Voodie.checkIn($scope.election.id, function(data){
    //            $location.path('elections');
                $scope.checkedIn = true;
            });
        }
    }
]);