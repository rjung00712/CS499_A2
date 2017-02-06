(function() {
    'use strict';
    angular
        .module('myappApp')
        .factory('Seller', Seller);

    Seller.$inject = ['$resource'];

    function Seller ($resource) {
        var resourceUrl =  'api/sellers/:id';

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
