angular.module('voodie.foodie.election').controller('FoodieElectionSearchCtrl', ['$scope', '$location', 'Voodie', 'User', 'districts',
    function($scope, $location, Voodie, User, districts){
        function formatDate(date){
            var formattedDate = date;
            var dd = date.getDate();
            var mm = date.getMonth()+1; //January is 0!
            var yyyy = formattedDate.getFullYear();
            if(dd<10){
				dd='0'+dd;
			} if(mm<10){
				mm='0'+mm;
			} 
			formattedDate = mm+'/'+dd+'/'+yyyy;
            return formattedDate;
        }

        $scope.goToElection = function(election){
            //TODO polite check to see if they are allowed to vote
            $location.path('election/' + election.id);
        };
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
        angular.forEach($scope.districts, function(mapValue, key){
            if(mapValue.name == User.district){
                $scope.search.district = mapValue;
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
            var isRendered = $scope.elections && $scope.elections.length === 0;
            isRendered &= !$scope.renderNoFiltersText();
            return Boolean(isRendered);
        };
        $scope.renderNoFiltersText = function(){
            var isRendered = !$scope.search.startDate || !$scope.search.endDate || !$scope.search.district;
            return Boolean(isRendered);
        };
    }
]);


angular.module('voodie.foodie.election').controller('FoodieElectionViewCtrl', ['$scope', '$routeParams', '$location', 'Voodie', 'election', '$rootScope',
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

angular.module('voodie.foodie.election').controller('VoteCtrl', ['$scope', '$routeParams', 'Voodie',
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
                for(var j=0;j<data.foodTrucks.length;j++){
                    for(var c=0;c<data.foodTrucks[j].categories.length; c++){
                        var cat = data.foodTrucks[j].categories[c];
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

angular.module('voodie.foodie.election').controller('CheckInCtrl', ['$scope', '$routeParams', '$location', 'Voodie', 'election',
    function($scope, $routeParams, $location, Voodie, election){
        $scope.election = election;
        $scope.checkedIn = false;
        $scope.checkIn = function(){
            Voodie.checkIn($scope.election.id, function(data){
    //            $location.path('elections');
                $scope.checkedIn = true;
            });
        };
    }
]);

