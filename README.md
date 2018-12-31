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

path: "/rest/profiles"

    curl -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M=" http://{hostname}/rest/profiles

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

path: "/rest/profiles/register"

    curl -s -X POST -d '{"name": "Lisa", "email": "lisa@gmail.com", "password": "password"}' -H 'Content-Type:application/json;charset=UTF-8' http://{hostname}/rest/profiles/register

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

path: "/rest/profiles"

    curl -s -X POST -H "Authorization: Basic bGlzYUBnbWFpbC5jb206cGFzc3dvcmQ=" -d '{"id": 10021, "name": "Elisabeth", "email": "lisa@gmail.com", "password": "password"}' -H 'Content-Type:application/json;charset=UTF-8' http://{hostname}/rest/profiles

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

path: "/rest/profiles"

    curl -s -X DELETE -H "Authorization: Basic bGlzYUBnbWFpbC5jb206cGFzc3dvcmQ=" http://{hostname}/rest/profiles

HTTP status codes:

    HTTP status 204 - No Content

_**Restaurant**_
==

**Get restaurant**
***

method: "GET"

path: "/rest/restaurants/{id}"

>Returned restaurant with current menu of day

    curl -H "Authorization: Basic dXNlckB5YS5ydTpwYXNz" http://{hostname}/rest/restaurants/{id}

Response:

    {
        "id": 10006,
        "name": "KFC3",
        "city": "Москва",
        "description": "Куриные бургеры и картошка",
        "added": "2018-12-31",
        "menus": [
            {
                "id": 10012,
                "added": "2018-12-31",
                "dishes": [
                    {
                        "id": 10019,
                        "name": "1Пицца",
                        "price": 279.49
                    },
                    {
                        "id": 10020,
                        "name": "2Пицца",
                        "price": 279.49
                    }
                ]
            }
        ]
    }

**Create restaurant**
***

method: "POST"

path: "/rest/restaurants"

>Returned created restaurant with location URI in response header**

    curl -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M=" http://{hostname}/rest/restaurants

Request:

    {
        "name": "Гусли",
        "city": "Александров",
        "description": "Ресторан русской кухни"
    }

Response:

    {
        "id": 10022,
        "name": "Гусли",
        "city": "Александров",
        "description": "Ресторан русской кухни",
        "added": "2018-12-31",
        "menus": []
    }
    
Response headers:

    Location http://localhost:8080/rest/restaurants/10022

