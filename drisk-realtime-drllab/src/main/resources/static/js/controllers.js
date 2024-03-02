function MainCtrl($scope,$location,$state,$http) {
    this.slideInterval = 5000;
};

//页面controller
var PageController = function($scope,$http,$location,$state,dateInit){
	load($scope, $http,$location,$state,this);
	try{pageload();}catch(e){console.info(e)}
}
//外部内容的Controller。菜单页，上下导航栏。
var ContentController = function($scope,$http,$state){

}

angular
    .module('inspinia')
    .controller("PageController",PageController)
    .controller("MainCtrl",MainCtrl)
    .controller("ContentController",ContentController)
    ;

