var module = angular.module('PersonServices',['ngResource']);

module.factory('Client', function($resource){
  return $resource('clients/:personId.json', {}, {
    query: {method:'GET', params:{personId:'current'}, isArray:true}
  });
});