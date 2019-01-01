# API documentation

Запросы к API осуществляются через протокол HTTP с базовой авторизацией.

При наличии тела запроса (запросы типа POST и PUT), его содержимое должно быть передано в формате JSON, заголовок Content-Type должен быть установлен в application/json.

#### Basic authorization (Base64)

    "admin@yandex.ru:pass"          "YWRtaW5AeWFuZGV4LnJ1OnBhc3M="     
    "user@ya.ru:pass"               "dXNlckB5YS5ydTpwYXNz"
    "lisa@gmail.com:password"       "bGlzYTEyMkBnbWFpbC5jb206cGFzc3dvcmQ="

Пример авторизации для пользователя admin@yandex.ru

    "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M="

## Profile

#### Register user
***

method: `POST`

path: `/rest/profiles/register`

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

| Code | Status | Description |
| --- | --- | --- |
| 201 | Created |
| 409 | Conflict |
| 422 | Unprocessable Entity |

#### Get user profile
***

method: `GET`

path: `/rest/profiles`

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

| Code | Status | Description |
| --- | --- | --- |
| 200 | OK |
| 401 | Unauthorized |

#### Update user profile
***

method: `PUT`

path: `/rest/profiles`

    curl -s -X PUT -H "Authorization: Basic bGlzYUBnbWFpbC5jb206cGFzc3dvcmQ=" -d '{"id": 10021, "name": "Elisabeth", "email": "lisa@gmail.com", "password": "password"}' -H 'Content-Type:application/json;charset=UTF-8' http://{hostname}/rest/profiles

Request:

    {
        "id": 10021,
        "name": "Elisabeth",
        "email": "lisa@gmail.com",
        "password": "password"
    }
    
HTTP status codes:

| Code | Status | Description |
| --- | --- | --- |
| 204 | No Content |
| 401 | Unauthorized |
| 409 | Conflict |
| 422 | Unprocessable Entity |


#### Delete profile 
***

method: `DELETE`

path: `/rest/profiles`

    curl -s -X DELETE -H "Authorization: Basic bGlzYUBnbWFpbC5jb206cGFzc3dvcmQ=" http://{hostname}/rest/profiles

HTTP status codes:

| Code | Status | Description |
| --- | --- | --- |
| 204 | No Content |
| 401 | Unauthorized |

## Restaurant


#### Get all restaurants
***

method: `GET`

path: `/rest/restaurants`

    curl -H "Authorization: Basic dXNlckB5YS5ydTpwYXNz" http://{hostname}/rest/restaurants

Response:

    [
        {
            "id": 10006,
            "name": "KFC3",
            "city": "Москва",
            "description": "Куриные бургеры и картошка",
            "added": "2019-01-01",
            "menus": null
        },
        {
            "id": 10007,
            "name": "McDs1",
            "city": "Москва",
            "description": "Бургеры и картошка",
            "added": "2019-01-01",
            "menus": null
        },
        ...
    ]
HTTP status codes:

| Code | HTTP status | Description |
| --- | --- | --- |
| 200 | OK |


#### Get restaurant
***

method: `GET`

path: `/rest/restaurants/{id}`

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
HTTP status codes:

| Code | Status | Description |
| --- | --- | --- |
| 200 | OK |
| 422 | Unprocessable Entity |

#### Create restaurant
***

method: `POST`

path: `/rest/restaurants`

    curl -s -X POST -d '{"name": "Гусли", "city": "Александров", "description": "Ресторан русской кухни"}' -H 'Content-Type:application/json;charset=UTF-8' -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M="  http://{hostname}/rest/restaurants

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

HTTP status codes:

| Code | Status | Description |
| --- | --- | --- |
| 201 | Created |
| 409 | Conflict |
| 422 | Unprocessable Entity |


#### Update restaurant
***

method: `PUT`

path: `/rest/restaurants/{id}`


    curl -s -X PUT -d '{"name": "Гусли", "city": "Александров", "description": "Ресторан русской кухни"}' -H 'Content-Type:application/json;charset=UTF-8' -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M="  http://{hostname}/rest/restaurant/{id}

Request:

    {
        "id": 10022,
        "name": "Гусли",
        "city": "Александров",
        "description": "Ресторан традиционной русской кухни"
    }
HTTP status codes:

| Code | Status | Description |
| --- | --- | --- |
| 204 | No Content |
| 409 | Conflict |
| 422 | Unprocessable Entity |


#### Delete restaurant
***

method: `DELETE`

path: `/rest/restaurants/{id}`

    curl -s -X DELETE -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M=" http://{hostname}/rest/restaurants/{id}

HTTP status codes:

| Code | Status | Description |
| --- | --- | --- |
| 204 | No Content |
| 422 | Unprocessable Entity |
