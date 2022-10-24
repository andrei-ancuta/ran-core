<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Lista rapoarte</title>
    <style>
        .username.ng-valid {
            background-color: lightgreen;
        }
        .username.ng-dirty.ng-invalid-required {
            background-color: red;
        }
        .username.ng-dirty.ng-invalid-minlength {
            background-color: yellow;
        }

        .email.ng-valid {
            background-color: lightgreen;
        }
        .email.ng-dirty.ng-invalid-required {
            background-color: red;
        }
        .email.ng-dirty.ng-invalid-email {
            background-color: yellow;
        }

    </style>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css"/>
    <link href="${pageContext.request.contextPath}/assets/css/app.css" rel="stylesheet"/>
</head>
<body ng-app="myApp" class="ng-cloak">
<div class="generic-container" ng-controller="RapoarteController as ctrl">

    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Lista rapoarte</span></div>
        <div class="tablecontainer">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>Denumire raport</th>
                    <th width="20%"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="u in ctrl.rapoarte">
                    <td><span ng-bind="u.denumire"></span></td>
                    <td>
                        <button type="button" ng-click="ctrl.download(u.denumire)" class="btn btn-success custom-width">Descarca</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/lib/angular-1.1.4/angular.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
<script>
App.constant('contextPath', '${pageContext.request.contextPath}');
</script>
<script src="${pageContext.request.contextPath}/assets/js/service/rapoarte_service.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/controller/rapoarte_controller.js"></script>

</body>
</html>