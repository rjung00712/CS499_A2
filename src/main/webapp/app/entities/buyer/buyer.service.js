(function() {
    'use strict';
    angular
        .module('myappApp')
        .factory('Buyer', Buyer);

    Buyer.$inject = ['$resource'];

    function Buyer ($resource) {
        var resourceUrl =  'api/buyers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
