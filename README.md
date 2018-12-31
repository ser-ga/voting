**API documentation**
==

Запросы к API осуществляются через протокол HTTP.

При наличии тела запроса (запросы типа POST и PUT), его содержимое должно быть передано в формате JSON, заголовок Content-Type должен быть установлен в application/json.

**Basic authorization (Base64)**

    "admin@yandex.ru:pass"          "YWRtaW5AeWFuZGV4LnJ1OnBhc3M="     
    "user@ya.ru:pass"               "dXNlckB5YS5ydTpwYXNz"
    "lisa@gmail.com:password"       "bGlzYTEyMkBnbWFpbC5jb206cGFzc3dvcmQ="

Пример авторизации для пользователя admin@yandex.ru

    "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M="

_**Profile**_
==

**Get user profile**
***

method: "GET"

path: "/rest/profile"

    curl -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M=" http://{hostname}/rest/profile

Response:

    {
        "id": 10000,
        "name": "Sergey",
        "email": "admin@yandex.ru",
        "enabled": true,
        "roles": [
            "ROLE_ADMIN"
        ]
    }
 
HTTP status codes:

    HTTP status 200 - Ok
    HTTP Status 401 – Unauthorized
***

_**Register user**_ 
***

method: "POST"

path: "/rest/profile/register"

    curl -s -X POST -d '{"name": "Lisa", "email": "lisa@gmail.com", "password": "password"}' -H 'Content-Type:application/json;charset=UTF-8' http://{hostname}/rest/profile/register

Request:

    {
        "name": "Lisa",
        "email": "lisa@gmail.com",
        "password": "password"
    }
    
 Response:
 
    {
        "id":10021,
        "name":"Lisa",
        "email":"lisa@gmail.com",
        "enabled":true,
        "roles": [
            "ROLE_USER"
        ]
    }
    
HTTP status codes:

    HTTP status 201 - Created
    HTTP status 409 - Conflict
    HTTP Status 415 – Unsupported Media Type
    HTTP Status 422 - Unprocessable Entity
***
_**Update user profile**_ 
***

method: "PUT"

path: "/rest/profile"

    curl -s -X POST -H "Authorization: Basic bGlzYUBnbWFpbC5jb206cGFzc3dvcmQ=" -d '{"id": 10021, "name": "Elisabeth", "email": "lisa@gmail.com", "password": "password"}' -H 'Content-Type:application/json;charset=UTF-8' http://{hostname}/rest/profile

Request:

    {
        "id": 10021,
        "name": "Lisa",
        "email": "lisa@gmail.com",
        "password": "password"
    }
    
Response:
 
    {
        "id":10021,
        "name":"Elisabeth",
        "email":"lisa@gmail.com",
        "enabled":true,
        "roles": [
            "ROLE_USER"
        ]
    }
    
HTTP status codes:

    HTTP status 201 - Created
    HTTP status 409 - Conflict
    HTTP Status 422 - Unprocessable Entity

***
_**Delete profile**_ 
***

method: "DELETE"

path: "/rest/profile"

    curl -s -X DELETE -H "Authorization: Basic bGlzYUBnbWFpbC5jb206cGFzc3dvcmQ=" http://{hostname}/rest/profile

HTTP status codes:

    HTTP status 204 - No Content

_**Restaurant**_
==

**Get restaurant**
***

method: "GET"

path: "/rest/restaurant/{id}"

    curl -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M=" http://{hostname}/rest/restaurant/{id}

Response: