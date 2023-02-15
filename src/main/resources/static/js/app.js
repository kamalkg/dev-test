(function(){

    var app = angular.module('notesApp',['ngRoute', 'ngMaterial']);

    var loggedUser = null;

    app.config(['$locationProvider', '$routeProvider',
        function ($locationProvider, $routeProvider) {

            $routeProvider
                .when('/', {
                    templateUrl: '/partials/notes-view.html',
                    controller: 'notesController'
                })
                .when('/login', {
                    templateUrl: '/partials/login.html',
                    controller: 'loginController',
                })
                .otherwise('/');
        }
    ]);

    app.run(['$rootScope', '$location', 'AuthService', function ($rootScope, $location, AuthService) {
        $rootScope.$on('$routeChangeStart', function (event) {

            if ($location.path() == "/login"){
                return;
            }

            if (!AuthService.isLoggedIn()) {
                event.preventDefault();
                $location.path('/login');
            } else {
                $rootScope.isAuthenticated = true;
                $location.path("/");

                console.log("Authenticated ")
            }
        });
    }]);


    app.service('AuthService', function($http){


        function login (username, password){
            return $http.post("api/login", {username: username, password: password}).then(function(user){
                loggedUser = user;
                console.log(loggedUser);
                saveToken(true, loggedUser);
            }, function(error){
                loggedUser = null;
            })
        }

        function saveToken(authenticated, loggedUser) {
            localStorage.setItem('authenticated', authenticated);
            localStorage.setItem("user", loggedUser);
        }

        function isLoggedIn() {
            return localStorage.getItem('authenticated') ;
        }

        function getLoggedUser() {
            return localStorage.getItem('loggedUser', loggedUser) ;
        }

        function userId() {
            console.log(loggedUser);
            if (loggedUser != null) {
                return loggedUser.id;
            }
            return null;
        }
        return {
            login : login,
            isLoggedIn: isLoggedIn,
            userId : userId()
        }
    });

    app.controller('loginController', function($scope, AuthService, $location, $rootScope){
        $scope.invalidCreds = false;
        $scope.formSubmitted = false;
        $scope.login = {
            username : null,
            password : null
        };

        if ($rootScope.isAuthenticated) {
            $location.path("/");
        }

        $scope.login = function(){
            console.log("inside login")
            $scope.formSubmitted = true;
            if ($scope.loginForm.$valid) {
                console.log("Authyenticating")
                AuthService.login($scope.login.username, $scope.login.password).then(function success(response) {
                        $rootScope.isAuthenticated = true;
                        $location.path("/");
                        console.log("Authenticated ")
                    },function(error){
                        console.log(error);
                    });
            }
            if (loggedUser == null) {
                $rootScope.isAuthenticated = false;
                $scope.invalidCreds = true;
            }
        };

        $scope.logout= function(){
            console.log("logout");
            AuthService.logout().then(function(){
                console.log("logout");
                $location.path("/login");
            }, function(error){
                console.log(error);
            });
            $scope.isAuthenticated = false;
        };
    });

    app.controller('headerController', function($scope, $location, AuthService){
        $scope.logout= function(){
            console.log("logout");
            AuthService.logout().then(function(){
                $location.path("/login");
            }, function(error){
                console.log(error);
            });
            $scope.isAuthenticated = false;
        };
    });


    app.controller('notesController', function($scope, $http, AuthService, $timeout) {
        $scope.notes = [];
        $scope.isEditCreateView = false;
        $scope.isCancelled = false;
        $scope.selectedNote = null;

        $scope.showSuccessMessage = false;
        $scope.showErrorMessage = false;

        $scope.showSuccessMessageDelete = false;
        $scope.showErrorMessageDelete = false;

        $scope.showSuccessMessageUpdate = false;
        $scope.showErrorMessageUpdate  = false;


        console.log()
        function loadNotes() {
            $http.get("api/v1/note/user/" + loggedUser.data.user.id)
                .then(function(response) {
                    if (response != null) {
                        $scope.notes = response.data;
                    }
                });
        }
        loadNotes();

        $scope.logout= function(){
            console.log("logout");
            AuthService.logout().then(function(){
                $location.path("/login");
            }, function(error){
                console.log(error);
            });
            $scope.isAuthenticated = false;
        };


        var currentNote = [];
        $scope.saveNote = function(note) {
            var postData = {
                userId: loggedUser.data.user.id,
                noteHeader: note.noteHeader,
                content: note.content
            };
            console.log(note);
            $http.post("http://localhost:8080/api/v1/note/user/" + loggedUser.data.user.id, postData)
                .then(function success(response) {
                    console.log("Note saved successfully");
                    console.log(response.data);
                    $scope.showSuccessMessage = true;
                    $timeout(function() {
                        $scope.showSuccessMessage = false;
                    }, 3000);
                    currentNote = response.data;
                    $scope.notes.push(response.data);
                    $scope.isEditCreateView = false;
                    $scope.isCancelled = false;
                    $scope.selectedNote = {};
                    $scope.selectedNote = null;

                }, function error(response) {
                    $scope.showErrorMessage = true;
                    $timeout(function() {
                        $scope.showErrorMessage  = false;
                    }, 3000);
                    console.log("Error saving the note");
                });
        };

        $scope.updateNote = function(note) {
            console.log(currentNote.noteId);
            var putData = {
                noteId: currentNote.noteId,
                userId: loggedUser.data.user.id,
                noteHeader: note.noteHeader,
                content: note.content
            };
            console.log(putData);
            var putIndex = $scope.notes.findIndex(function(note) {
                return note.id === currentNote.noteId;
            });

            $http.put("http://localhost:8080/api/v1/note/" + currentNote.noteId, putData )
                .then(function success(response) {
                    console.log("Note updated successfully");
                    $scope.notes.splice(putIndex, 1);
                    $scope.notes.push(response.data);
                    $scope.showSuccessMessageUpdate = true;
                    console.log($scope.showSuccessMessageUpdate);
                    $timeout(function() {
                        $scope.showSuccessMessageUpdate = false;
                    }, 3000);
                    console.log($scope.showSuccessMessageUpdate);
                }, function error(response) {
                    $scope.showErrorMessageUpdate = true;
                    $timeout(function() {
                        $scope.showErrorMessageUpdate  = false;
                    }, 3000);
                    console.log("Error while updating the note");
                });
        };

        $scope.deleteNote = function(note) {
            console.log(note);
            console.log(currentNote);
            console.log(currentNote.noteId);
            $http.delete("http://localhost:8080/api/v1/note/" + currentNote.noteId)
                .then(function success(response) {
                    console.log("Note deleted successfully");
                    var deleteIndex = $scope.notes.findIndex(function(note) {
                        return note.id === currentNote.noteId;
                    });
                    $scope.notes.splice(deleteIndex, 1);
                    $scope.showSuccessMessageDelete = true;
                    $timeout(function() {
                        $scope.showSuccessMessageDelete = false;
                    }, 3000);
                    currentNote = null;
                    $scope.isCancelled = true;
                    $scope.selectedNote = {};
                    $scope.selectedNote = null;
                }, function error(response) {
                    $scope.showErrorMessageDelete = true;
                    $timeout(function() {
                        $scope.showErrorMessageDelete  = false;
                    }, 3000);
                    console.log("Error deleting the note");
                });
        };


        $scope.createNote = function() {
            console.log("In CreateNote");
            $scope.isCancelled = false;
            $scope.isEditCreateView = true;
            $scope.selectedNote = null;
            $scope.noteHeader = "";
            $scope.content = "";
        };

        $scope.newNoteView = function() {
            console.log("In CreateNote");
            $scope.isCancelled = false;
            $scope.isEditCreateView = false;
            $scope.selectedNote = {};
        };

        $scope.editNote = function(note) {
            $scope.isEditCreateView = true;
            $scope.isCancelled = false;
            console.log(note);
            $scope.selectedNote = angular.copy(note);
            console.log($scope.selectedNote);
            currentNote = $scope.selectedNote;
        };

        $scope.cancelNote = function() {
            console.log("in cancel button");
            currentNote = [];
            $scope.isCancelled = true;
            $scope.selectedNote = null;
            $scope.noteHeader = "";
            $scope.content = "";
        };
    });
})();