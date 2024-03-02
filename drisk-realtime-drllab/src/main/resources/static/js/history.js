var pageload = function() {
    $scope.histories = [];
    $scope.selectedHistory={};
    $scope.showHistoryDetail = function(item) {
$scope.selectedHistory=item;
    };

    $scope.getHistory = function() {
        $.ajax({
            "url": "/sysManage/getHistory",
            "type": "POST",
            "dataType": "json",
            "data": {
                "transId": $scope.transId,
                "serviceCode": $scope.serviceCode
            },
            "success": function(response) {
                $scope.histories = response.data;
                $scope.$apply();
            }
        });
    };

    $scope.getHistory();
}