/**
 * Created by miroslav.rusnac on 21/03/2016.
 */
'use strict';

App.controller('RapoarteController', ['$scope', 'RapoarteService', function($scope, RapoarteService) {
    var self = this;
    self.raport={denumire:''};
    self.rapoarte=[];

    self.test='ABRACADABRA';

    self.fetchAllReports = function(){
        RapoarteService.fetchAllReports()
            .then(
            function(d) {
                self.rapoarte = d;
            },
            function(errResponse){
                console.error('Error while fetching Reports');
            }
        );
    };

    self.download = function(fileName) {
        RapoarteService.download(fileName)
            .then(function(success) {
                console.log('success : ' + success);
            }, function(error) {
                console.log('error : ' + error);
            });
    };

    self.fetchAllReports();
}]);
