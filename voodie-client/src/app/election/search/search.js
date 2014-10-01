angular.module('voodie.election').controller('ElectionsCtrl', ['$scope', '$location', 'Voodie', 'User', 'districts',
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
