Description of calorie counting program commands.
We take two entity: Meal and User and one DTO MealTo in JSON format.

Components:
    Meal:
    {
        "id": integer,
        "dateTime": "yyyy-MM-ddThh:mm:ss",
        "description": string,
        "calories": integer
    }

    MealTo:
    {
        "id": integer,
        "dateTime": "yyyy-MM-ddThh:mm:ss",
        "description": string,
        "calories": integer,
        "excess": boolean
    }

    User:
    {
    
    }


-**Get all** user's meals. User's meals comes in JSON format in array of MealTo:
    curl --location --request GET localhost:8080/topjava/rest/meals/

-**Get meal** We can take meal by id:
    curl --location --request GET localhost:8080/topjava/rest/meals/100005

-**Delete meal** To delete meal by id by DELETE method:
    curl --location --request DELETE localhost:8080/topjava/rest/meals/100003

-**Create meal** To create meal we send Meal object in body request by POST method:
    curl --location --request POST 'localhost:8080/topjava/rest/meals/' --data-raw '{"dateTime": "2020-01-30T10:00:00","description": "Tea","calories": 500 }'

curl --header "Content-Type: application/json" \
--request POST \
--data '{"dateTime":"2020-01-30T10:00:00","description":"Tea", "calories":"50"}' \
localhost:8080/topjava/rest/meals/
