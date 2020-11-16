.PHONY: all init build.container run.container

all: run.container

init:
	@echo Create daja json file if missing
	@if [ ! -f data.json ]; then echo "[]" > data.json; fi

build.container:
	docker-compose build --no-cache

run.container: init
	docker-compose up
