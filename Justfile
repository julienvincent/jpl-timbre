clean:
    clojure -T:build clean 

prep:
    clojure -T:build prep

build:
    clojure -T:build build

build-and-install: build
    clojure -T:build install

release:
    clojure -T:build release
