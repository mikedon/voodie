/**
 * directive that controls the ajax loading display
 */
angular.module('loading', [])

.directive("loading", ["$modal",
    function($modal){
        return {
            link: function(scope, element, attrs){
                var modalInstance;
                //this is a dummy controller because $modal needs it
                var ModalInstanceCtrl = function ($scope, $modalInstance) {
                };
                scope.$watch('loadingView', function(loadingView){
                    if(loadingView === false && modalInstance){
                        modalInstance.close();
                    }else if(loadingView === true){
                        modalInstance = $modal.open({
                            template: "<img src='css/img/ajax-loader.gif'>",
                            controller: ModalInstanceCtrl,
                            windowClass: "loading",
                            backdrop: "static",
                            keyboard: true
                        });
                    }
                });
            }
        };
    }
]);
