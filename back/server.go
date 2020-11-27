package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"
)

var imgs *Images

// Manage one picture, actions can be:
// - Add one
// - Get one
// - Delete one
func pic(w http.ResponseWriter, r *http.Request) {
	if r.Method == "PUT" { // Put request
		// Check if the body isn't empty
		if r.ContentLength <= 0 {
			http.Error(w, "No body recieved", 400)
			return
		}

		b := make([]byte, r.ContentLength)
		// Read the body (ignore returned values)
		r.Body.Read(b)

		// Decode the image in the json
		var sendedImage image
		err := json.Unmarshal(b, &sendedImage)
		if err != nil {
			fmt.Println(err)
			http.Error(w, "Failed to parse the image json", 500)
			return
		}

		// Check if the image is valid
		if !sendedImage.valid() {
			http.Error(w, "The image is invalid", 400)
		}

		// Add the image
		images, err := imgs.Add(sendedImage)
		if err != nil {
			fmt.Println(err)
			http.Error(w, "Failed to add the image", 500)
			return
		}

		// Save the image on disk
		err = imgs.Save(images)
		if err != nil {
			fmt.Println(err)
			http.Error(w, "Failed to save the image", 500)
			return
		}

		fmt.Fprint(w, "Sucessfully saved")
		return
	} else if r.Method == "GET" { // Get request
		fmt.Fprint(w, "Don't use get method, this is non sens")
		return
	} else if r.Method == "DELETE" { // Delete request
		// Check if the body isn't empty
		if r.ContentLength <= 0 {
			http.Error(w, "No body recieved", 400)
			return
		}

		b := make([]byte, r.ContentLength)
		// Read the body (ignore returned values)
		r.Body.Read(b)

		// Decode the image in the json
		var sendedImage image
		err := json.Unmarshal(b, &sendedImage)
		if err != nil {
			fmt.Println(err)
			http.Error(w, "Failed to parse the image json", 500)
			return
		}

		// Check if the image is valid
		if !sendedImage.valid() {
			http.Error(w, "The image is invalid", 400)
		}

		images, err := imgs.Read()
		if err != nil {
			http.Error(w, "Failed to load images", 500)
		}

		for idx, img := range images {
			if sendedImage.equal(&img) {
				imgs.Remove(idx)
				break
			}
		}

		fmt.Fprint(w, "Sucessfully deleted")
		return
	} else {
		http.Error(w, http.StatusText(405), 405)
		return
	}
}

// Get the list of all pictures
// Only support GET http method
func pics(w http.ResponseWriter, r *http.Request) {
	if r.Method != "GET" {
		http.Error(w, http.StatusText(405), 405)
		return
	}
	content, err := imgs.Read()
	if err != nil {
		fmt.Println(err)
		http.Error(w, "Failed to read images", 500)
		return
	}

	jsn, err := json.Marshal(content)
	if err != nil {
		fmt.Println(err)
		http.Error(w, "Failed to jsonify images", 500)
		return
	}

	fmt.Fprint(w, string(jsn))
}

func logRequest(h http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		// Log the request
		fmt.Println(
			fmt.Sprintf(
				"%s - %s - %s %s", time.Now().Format("[2006-01-02 15:04:05]"),
				r.RemoteAddr,
				r.Host,
				r.URL,
			),
		)
		h(w, r)
	}
}

func init() {
	imgs = New("/data.json")
}

func main() {
	http.HandleFunc("/pic/", logRequest(pic))
	http.HandleFunc("/pics/", logRequest(pics))
	log.Fatal(http.ListenAndServe(":8080", nil))
}
