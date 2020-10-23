FROM golang:1.15 as builder

WORKDIR /go/src/calbard

COPY . .

RUN CGO_ENABLED=0 go build -o /bin/calbard


FROM scratch

WORKDIR /bin/calbard

COPY --from=builder /bin/calbard .

CMD ["./calbard"]
