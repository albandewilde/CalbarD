package main

import (
	"encoding/json"
	"io/ioutil"
	"sync"
	"time"
)

type image struct {
	Img    string    `json:"img"`    // Base64 encoded picture
	Name   string    `json:"name"`   // Name of the image
	Taken  time.Time `json:"taken"`  // Data when the image was taken
	Author string    `json:"author"` // Author of the image
}

func (img *image) valid() bool {
	return img.Img != "" || img.Name != "" || img.Taken != time.Time{} || img.Author != ""
}

type Images struct {
	filepath string
	mux      sync.Mutex
}

func New(fp string) *Images {
	return &Images{
		filepath: fp,
	}
}

func (imgs *Images) Read() ([]image, error) {
	// Read file content
	imgs.mux.Lock()
	content, err := ioutil.ReadFile(imgs.filepath)
	imgs.mux.Unlock()
	if err != nil {
		return nil, err
	}

	var data []image

	// Parse json content
	err = json.Unmarshal(content, &data)
	if err != nil {
		return nil, err
	}

	return data, nil
}

func (imgs *Images) Save(imagesList []image) error {
	// Convert content to json
	jsn, err := json.Marshal(imagesList)
	if err != nil {
		return err
	}

	// Write in file
	imgs.mux.Lock()
	err = ioutil.WriteFile(imgs.filepath, jsn, 0644)
	imgs.mux.Unlock()
	if err != nil {
		return err
	}

	return nil
}

func (imgs *Images) Add(img image) ([]image, error) {
	images, err := imgs.Read()
	if err != nil {
		return nil, err
	}

	images = append(images, img)

	return images, nil
}
