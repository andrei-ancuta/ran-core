'use strict';

App.factory('RapoarteService', ['$http', '$q','$timeout','$window','contextPath', function($http, $q, $timeout, $window,contextPath){

    return {

        fetchAllReports: function() {
            return $http.get(contextPath+'/rapoarte?json')
                .then(
                function(response){
                    return response.data;
                },
                function(errResponse){
                    console.error('Error while fetching users');
                    return $q.reject(errResponse);
                }
            );
        },

        download: function (name) {

            var defer = $q.defer();

            $timeout(function () {
                $window.location =contextPath+ '/rapoarte?download=' + name;

            }, 1000)
                .then(function () {
                    defer.resolve('success');
                }, function () {
                    defer.reject('error');
                });
            return defer.promise;
        }


    };

}]);