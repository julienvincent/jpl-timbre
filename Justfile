clean:
    clojure -T:build clean 

build:
    clojure -T:build build

build-and-install: build
    clojure -T:build install

release:
    clojure -T:build release
