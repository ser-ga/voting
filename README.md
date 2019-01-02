# API documentation

Requests to the API are made via the HTTP protocol with basic authorization. 
POST and PUT request's content must be transferred in JSON format, the Content-Type header must be set to "application/json".

## Sections

- [_Profile_](#profile)

- [_Restaurant_](#restaurant)

- [_Menu_](#menu)

- [_Vote_](#vote)

#### Basic authorization (Base64)

    "admin@yandex.ru:pass"          "YWRtaW5AeWFuZGV4LnJ1OnBhc3M="     
    "user@ya.ru:pass"               "dXNlckB5YS5ydTpwYXNz"
    "lisa@gmail.com:password"       "bGlzYTEyMkBnbWFpbC5jb206cGFzc3dvcmQ="

Authorization example for user admin@yandex.ru

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

[↑ sections](#sections)

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


    curl -s -X PUT -d '{"name": "Гусли", "city": "Александров", "description": "Ресторан русской кухни"}' -H 'Content-Type:application/json;charset=UTF-8' -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M="  http://{hostname}/rest/restaurants/{id}

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
| 204 | No Content | Normal status |
| 422 | Unprocessable Entity | Not found entity |

[↑ sections](#sections)

## Menu

#### Create menu
***

method: `POST`

path: `/rest/menus`

    curl -s -X POST -d '{"name": "Гусли", "city": "Александров", "description": "Ресторан русской кухни"}' -H 'Content-Type:application/json;charset=UTF-8' -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M="  http://{hostname}/rest/restaurants

Request:

    {
        "restaurantId": 10006,
        "added": "2019-01-01",
        "dishes": [
            {
                "name": "Ролл1",
                "price": 270.15
            },
            {
                "name": "Ролл2",
                "price": 180.55
            }
        ]
    }
    
Response:

    {
        "id": 10025,
        "added": "2019-01-01",
        "dishes": [
            {
                "id": 10026,
                "name": "Ролл1",
                "price": 270.15
            },
            {
                "id": 10027,
                "name": "Ролл2",
                "price": 180.55
            }
        ]
    }

Response headers:

    Location http://localhost:8080/rest/menus/10025

HTTP status codes:

| Code | Status | Description |
| --- | --- | --- |
| 201 | Created | Menu created with location
| 409 | Conflict | Menu for that date is exist
| 422 | Unprocessable Entity | Any fields is not valid

#### Get all menus
***

method: `GET`

path: `/rest/menus`

    curl -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M=" http://{hostname}/rest/menus

Response:

    [
        {
            "id": 10010,
            "restaurantId": 10006,
            "dishes": [
                {
                    "id": 10013,
                    "name": "Картошка",
                    "price": 70.15
                },
                {
                    "id": 10014,
                    "name": "Бургер куриный",
                    "price": 80.55
                },
                {
                    "id": 10015,
                    "name": "Салат",
                    "price": 100.35
                }
            ],
            "added": "2018-12-14"
        },
        
        ...
        
    ]
    
HTTP status codes:

| Code | HTTP status | Description |
| --- | --- | --- |
| 200 | OK | Normal status |
| 403 | Forbidden | Need admin role |

#### Get menu
***



method: `GET`

path: `/rest/menus/{id}`

    curl -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M=" http://{hostname}/rest/menus/{id}

Response:

    {
        "id": 10010,
        "added": "2018-12-14",
        "dishes": [
            {
                "id": 10013,
                "name": "Картошка",
                "price": 70.15
            },
            {
                "id": 10014,
                "name": "Бургер куриный",
                "price": 80.55
            },
            {
                "id": 10015,
                "name": "Салат",
                "price": 100.35
            }
        ]
    }
    
    
HTTP status codes:

| Code | HTTP status | Description |
| --- | --- | --- |
| 200 | OK | Normal status |
| 403 | Forbidden | Need admin role |

#### Get menu by restaurant ID and date(optional)
***

method: `GET`

path: `/rest/menus/by`



| Request param | type | required | example |
| --- | --- | --- | --- |
| **restaurantId** | Number | true | 10006 |
| **date** | Date ISO 8601 | false | 2018-12-14 |

    curl -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M=" http://{hostname}/rest/menus/by?restaurantId={id}&date={date}

Response:

    [
        {
            "id": 10010,
            "added": "2018-12-14",
            "dishes": [
                {
                    "id": 10013,
                    "name": "1Картошка",
                    "price": 70.15
                },
                {
                    "id": 10014,
                    "name": "2Бургер куриный",
                    "price": 80.55
                },
                {
                    "id": 10015,
                    "name": "3Салат",
                    "price": 100.35
                }
            ]
        },
        {
            "id": 10012,
            "added": "2019-01-01",
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
    
    
HTTP status codes:

| Code | HTTP status | Description |
| --- | --- | --- |
| 200 | OK | Normal status |
| 403 | Forbidden | Need admin role |

#### Update menu
***

method: `PUT`

path: `/rest/menus/{id}`


    curl -s -X PUT -d '{"id": "10010", "restaurantId": 10006, "added": "2018-12-14", "dishes": [{"name": "Ролл1", "price": 270.15},{"name": "Ролл2", "price": 280.55}]}' -H 'Content-Type:application/json;charset=UTF-8' -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M="  http://{hostname}/rest/menus/{id}

Request:

    {
        "id": 10010,
        "restaurantId": 10006,
        "added": "2018-12-14",
        "dishes": [
            {
                "name": "Ролл1",
                "price": 270.15
            },
            {
                "name": "Ролл2",
                "price": 280.55
            }
        ]
    }
        
HTTP status codes:

| Code | Status | Description |
| --- | --- | --- |
| 204 | No Content | Normal status
| 409 | Conflict | 
| 422 | Unprocessable Entity | Any fields is not valid


#### Delete menu
***

method: `DELETE`

path: `/rest/menus/{id}`

    curl -s -X DELETE -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M=" http://{hostname}/rest/menus/{id}

HTTP status codes:

| Code | Status | Description |
| --- | --- | --- |
| 204 | No Content | Normal status |
| 422 | Unprocessable Entity | Not found entity |

[↑ sections](#sections)

## Vote

#### Vote for restaurant
***

method: `POST`

path: `/rest/vote/{restaurantId}`

    curl -s -X POST -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M=" http://{hostname}/rest/vote/{restaurantId}

HTTP status codes:

| Code | Status | Description |
| --- | --- | --- |
| 204 | No Content | Normal status |
| 409 | Conflict | Restaurant not found |
| 412 | Precondition Failed | Vote time is expired |

#### Get all of auth User
***

method: `GET`

path: `/rest/votes`

    curl -H "Authorization: Basic YWRtaW5AeWFuZGV4LnJ1OnBhc3M=" http://{hostname}/rest/votes


response:

    [
        {
            "id": 10022,
            "date": "2019-01-02",
            "userEmail": "admin@yandex.ru",
            "restaurant": {
                "id": 10006,
                "name": "KFC3",
                "city": "Москва",
                "description": "Куриные бургеры и картошка",
                "added": "2019-01-02",
                "menus": null
            }
        },
        
        ...
        
    ]

[↑ sections](#sections)