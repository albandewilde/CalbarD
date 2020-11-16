# CalbarD

TP of android app

## Back

### API

- /pic/
    Route to manipulate one pictures, it support `PUT` and `DELETE` method.
    You should provide a body. Ex:
    ```json
    {
        "img": "other encoded image",
        "name": "other image name",
        "taken": "2009-11-10T23:00:00Z",
        "author": "not me"
    }
    ```

- /pics/
    Route to get all images on the server, it only support the `GET` method.
    It return a json like this:
    ```json
    [
        {
            "img": "encoded image",
            "name": "image name",
            "taken": "2009-11-10T23:00:00Z",
            "author": "me"
        },
        {
            "img": "other encoded image",
            "name": "other image name",
            "taken": "2009-11-10T23:00:00Z",
            "author": "not me"
        }
    ]
    ```

### Start the back

To start the backend you must have docker installed.
Then you can use the Makefile to start the project
