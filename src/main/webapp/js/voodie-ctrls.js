
//TODO most of this logic should be moved to directive
angular.module('voodie').controller('NavbarCtrl', ['$scope', '$location', 'User',
    function($scope, $location, User){
        $scope.home = function() {
            return $location.path().indexOf('home') > 0;
        };
        $scope.about = function() {
            return $location.path().indexOf('about') > 0;
        };
        $scope.login = function() {
            return $location.path().indexOf('login') > 0;
        };
        $scope.loginOrRegister = function() {
            return $scope.login() || $scope.register();
        };
        $scope.register = function() {
            return $location.path().indexOf('foodtruck/registration') > 0
                || $location.path().indexOf('foodie/registration') > 0;
        };
        $scope.profile = function() {
            return $location.path().indexOf('foodtruck/profile') > 0
                || $location.path().indexOf('foodie/profile') > 0;
        };
        $scope.elections = function() {
            return $location.path().indexOf('foodtruck/elections') > 0;
        }
        $scope.currentUser = User;
        $scope.logout = function(){
            User.logout('login');
        }
}]);

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

angular.module('voodie').controller('ElectionsCtrl', ['$scope', '$location', 'Voodie', 'User',
    function($scope, $location, Voodie, User){
        function formatDate(date){
            var formattedDate = date;
            var dd = date.getDate();
            var mm = date.getMonth()+1; //January is 0!
            var yyyy = formattedDate.getFullYear();
            if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm} formattedDate = mm+'/'+dd+'/'+yyyy;
            return formattedDate;
        }

        //district filter
        $scope.districts = Voodie.getDistricts(function(data){
            angular.forEach(data, function(value, key){
                if(value.name == User.district){
                    $scope.district = value;
                }
            });
        });

        $scope.goToElection = function(election){
            //TODO polite check to see if they are allowed to vote
            $location.path('election/' + election.id);
        }
        $scope.$watch('district + startDate + endDate', function(){
            if(!$scope.renderNoFiltersText()){
                $scope.elections = Voodie.getAllElections($scope.district, $scope.startDate, $scope.endDate, function(data){
                    $scope.electionRows = [];
                    for( var i = 0; i < data.length; i = i + 2 ){
                        $scope.electionRows.push(i);
                    }
                });
            }
        }) ;
        // date filters
        $scope.startOpened = false;
        $scope.endOpened = false;
        $scope.startDate = formatDate(new Date());
        var endDate = new Date();
        endDate.setDate(endDate.getDate() + 7);
        $scope.endDate = formatDate(endDate);
        $scope.openStart = function() {
            $scope.startOpened = true;
        };
        $scope.openEnd = function() {
            $scope.endOpened = true;
        };
        // render rules
        $scope.renderNoElectionsText = function(){
            var isRendered = $scope.elections && $scope.elections.length == 0;
            isRendered &= !$scope.renderNoFiltersText();
            return Boolean(isRendered);
        };
        $scope.renderNoFiltersText = function(){
            var isRendered = !$scope.startDate || !$scope.endDate || !$scope.district;
            return Boolean(isRendered);
        };
    }
]);

angular.module('voodie').controller('ElectionCtrl', ['$scope', '$routeParams', '$location', 'Voodie',
    function($scope, $routeParams, $location, Voodie){
        $scope.election = Voodie.getElection($routeParams.e, function(data){
            var mapOptions = {
                center: new google.maps.LatLng(data.candidates[0].latitude, data.candidates[1].longitude),
                zoom: 8,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            $scope.map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
            for(var i=0;i<data.candidates.length; i++){
                var candidate = data.candidates[i];
                var latLng = new google.maps.LatLng(candidate.latitude,candidate.longitude);
                var marker = new google.maps.Marker({
                    position: latLng,
                    map: $scope.map,
                    title: candidate.displayName
                });
            }
        });
        $scope.candidateChoice = "";
        $scope.vote = function(){
            Voodie.vote($scope.candidateChoice, function(data){
                $location.path('/elections');
            })
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

angular.module('voodie').controller('FoodTruckRegistrationCtrl', ['$scope','Voodie', '$rootScope',
    function($scope, Voodie, $rootScope){
        $scope.districts = Voodie.getDistricts();
        $scope.submit = function(){
            var path = "foodtruck/elections";
            if($rootScope.captureRedirect){
                path = $rootScope.captureRedirect;
            }
            Voodie.registerTruck($scope, path);
        }
    }
]);

angular.module('voodie').controller('FoodieRegistrationCtrl', ['$scope', 'Voodie', '$rootScope',
    function($scope, Voodie, $rootScope){
        $scope.districts = Voodie.getDistricts();
        $scope.submit = function(){
            var path = "elections";
            if($rootScope.captureRedirect){
                path = $rootScope.captureRedirect;
            }
            Voodie.registerFoodie($scope, path);
        }
    }
]);

angular.module('voodie').controller('FoodTruckCreateElectionCtrl', ['$scope', '$location', 'Voodie', 'User', 'GoogleMaps', '$rootScope',
    function ($scope, $location, Voodie, User, GoogleMaps, $rootScope){
        function mapServingTime(servingDate, servingTime){
            new Date(servingDate.getFullYear(), servingDate.getMonth(),
                servingDate.getDate(), servingTime.getHours(), servingTime.getMinutes(), 0, 0);
        };
        function reset(){
            $scope.election = {
                //TODO smart defaults?
                servingDate : "",
                servingStartTime : new Date(),
                servingEndTime : new Date(),
                pollOpeningDate : "",
                pollClosingDate : "",
                candidates : []
            };
            $scope.candidate  = {};
        };
        reset();
        $scope.submit = function(){
            $scope.servingStartTime = mapServingTime($scope.election.servingDate, $scope.election.servingStartTime);
            $scope.servingEndTime = mapServingTime($scope.election.servingDate, $scope.election.servingEndTime);
            Voodie.createElection($scope.election, function(){
                $rootScope.clearAlerts = false;
                $location.path("foodtruck/elections");
            });
        };
        $scope.addCandidate = function(){
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
                            $scope.election.candidates.push(candidate);
                            $scope.candidate = {};
                        });
                    },function(){
                        $scope.$apply(function(){
                            $rootScope.alerts.push({type:'danger', message:'Invalid Location'});
                        });
                    }
                );
            }
        };
    }
]);

angular.module('voodie').controller('FoodTruckElectionCtrl', ['$scope', '$location', 'Voodie', 'User', 'GoogleMaps',
    function($scope, $location, Voodie, User, GoogleMaps){
        $scope.openElections = [];
        $scope.closedElections = [];
        Voodie.getElections(User.username, function(data){
            for(var i=0;i<data.length;i++){
                var election = data[i];
                if(election.status === "IN_PROGRESS"){
                    $scope.openElections.push(election);
                }else{
                    $scope.closedElections.push(election);
                }
            }
        });
        $scope.viewElection = function(election){
            $location.path("foodtruck/viewElection/" + election.id)
        }
    }
]);

angular.module('voodie').controller('FoodTruckViewElectionCtrl', ['$scope', '$routeParams', '$location', 'Voodie', '$modal',
    function($scope, $routeParams, $location, Voodie, $modal){
        $scope.candidatedSelected = false;
        $scope.candidateProgress = {};
        $scope.election = Voodie.getElectionForSelection($routeParams.e, function(data){
            for(var i=0;i<data.candidates.length;i++){
                var candidate = data.candidates[i];
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
            data.candidates.sort(function(a,b){
                if(a.percentagOfVotes > b.percentageOfVotes){
                    return -1;
                }else if(b.percentageOfVotes > a.percentageOfVotes){
                    return 1;
                }
                return 0;
            });
            $scope.candidateSelected = (data.status === "CLOSED");
        });

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

angular.module('voodie').controller('FoodTruckProfileCtrl', ['$scope', 'Voodie', 'User',
    function($scope, Voodie, User){
        $scope.foodTruck = Voodie.getFoodTruckProfile(User.username);
    }
]);

angular.module('voodie').controller('FoodieProfileCtrl', ['$scope', 'Voodie', 'User',
    function($scope, Voodie, User){
        $scope.foodie = Voodie.getFoodieProfile(User.username);
    }
]);

angular.module('voodie').controller('LoginCtrl', ['$scope', 'User', '$rootScope',
    function($scope, User, $rootScope){
        $scope.submit = function(){
            User.username = $scope.username;
            User.password = $scope.password;
            var path = "elections";
            if($rootScope.captureRedirect){
                path = $rootScope.captureRedirect;
            }
            User.login(path);
        }
    }
]);

angular.module('voodie').controller('CheckInCtrl', ['$scope', '$routeParams', '$location', 'Voodie',
    function($scope, $routeParams, $location, Voodie){
        $scope.election = Voodie.getElection($routeParams.e);
        $scope.checkedIn = false;
        $scope.checkIn = function(){
            Voodie.checkIn($scope.election.id, function(data){
    //            $location.path('elections');
                $scope.checkedIn = true;
            });
        }
    }
]);

angular.module('voodie').controller('FoodTruckCtrl', ['$scope', '$routeParams', 'EatingTime', 'Voodie', 'GoogleMaps',
    function($scope, $routeParams, EatingTime, Voodie, GoogleMaps) {
        $scope.eatingDate = EatingTime.roundedTime();
        $scope.eatingTime = EatingTime.roundedTime();
        //get food truck info
        $scope.foodTruck = Voodie.getFoodTruckInfo($routeParams.id, function(data){
            //get food truck coordinates
            GoogleMaps.geolocate(data.address,function(longitude, latitude){
                $scope.latitude = latitude;
                $scope.longitude = longitude;
                //init map based on coords
                var ll = new google.maps.LatLng($scope.latitude, $scope.longitude);
                $scope.mapOptions = {
                    center: ll,
                    zoom: 12,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                $scope.map = new google.maps.Map(document.getElementById("map-canvas"), $scope.mapOptions);
                //get voting data and create heat map
                $scope.eatingTimestamp = EatingTime.getEatingTime(),
                    Voodie.getVotes($routeParams.id, EatingTime.getEatingTime().getTime(), function(data){
                        var votingData = [];
                        var votes = data.votes;
                        for(var i=0;i<votes.length;i++){
                            votingData.push(new google.maps.LatLng(votes[i].latitude, votes[i].longitude))
                        }
                        $scope.pointArray = new google.maps.MVCArray(votingData);
                        $scope.heatmap = new google.maps.visualization.HeatmapLayer({
                            data: $scope.pointArray
                        });
                        $scope.heatmap.setMap($scope.map);
                    });
            });
        });
        $scope.updateMap = function(){
            EatingTime.setEatingTime(EatingTime.timeStamp($scope.eatingDate, $scope.eatingTime));
            //reset heat map
            if($scope.heatmap){
                $scope.heatmap.setMap(null);
            }
            //this should be moved to a promise?
            Voodie.getVotes($routeParams.id, EatingTime.getEatingTime().getTime(), function(data){
                var votingData = [];
                var votes = data.votes;
                for(var i=0;i<votes.length;i++){
                    votingData.push(new google.maps.LatLng(votes[i].latitude, votes[i].longitude))
                }
                $scope.pointArray = new google.maps.MVCArray(votingData);
                $scope.heatmap = new google.maps.visualization.HeatmapLayer({
                    data: $scope.pointArray
                });
                $scope.heatmap.setMap($scope.map);
            });
        };
    }
]);