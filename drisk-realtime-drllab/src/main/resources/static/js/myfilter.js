//过滤器
var nullFilter=function(){
	return function(str){
		if(str==undefined||str==null||str.trim()=='null'||str.trim()==''){
			return '';
		}
		return str;
	}
}

//时间单位：年月日
var isperiod=function(){
	return function(str){
		 switch (str) {
		 case "0":
			 return '月';
		 case "1":
			 return '日';	
		 case "-1":
			 return '年';
		 default:
			return  '';
		 }
	}
}


angular
    .module('inspinia')
    .filter('nullFilter',nullFilter);
