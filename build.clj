(ns build
  (:require
   [clojure.string :as str]
   [clojure.tools.build.api :as b]
   [deps-deploy.deps-deploy :as deps-deploy]))

(def basis (delay (b/create-basis {})))

(def lib 'io.julienvincent/jpl-timbre)
(def version (str/replace (or (System/getenv "VERSION") "0.0.0") #"v" ""))
(def class-dir-build "target/build")
(def class-dir-release "target/release")
(def jar-file "target/lib.jar")

(defn clean [_]
  (b/delete {:path "target"}))

(defn compile-module-info [_]
  (b/javac {:src-dirs [class-dir-build]
            :class-dir class-dir-build
            :basis @basis
            :javac-opts ["-Xlint:-options"]}))

(defn build [_]
  (clean nil)

  (b/compile-clj {:basis @basis
                  :class-dir class-dir-build
                  :ns-compile ['io.julienvincent.jpl-timbre.logger
                               'io.julienvincent.jpl-timbre.logger-finder]})

  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir-build})

  (compile-module-info nil)

  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir-release})

  (b/copy-dir {:src-dirs [(str class-dir-build "/io/julienvincent")]
               :target-dir (str class-dir-release "/io/julienvincent")})

  (b/copy-file {:src (str class-dir-build "/module-info.class")
                :target (str class-dir-release "/module-info.class")})

  (b/write-pom {:class-dir class-dir-release
                :lib lib
                :version version
                :basis @basis
                :src-dirs ["src"]
                :pom-data [[:description "Java Platform Logging bridge for Timbre"]
                           [:url "https://github.com/julienvincent/jpl-timbre"]
                           [:licenses
                            [:license
                             [:name "MIT"]
                             [:url "https://opensource.org/license/mit"]]]]})

  (b/jar {:class-dir class-dir-release
          :jar-file jar-file}))

(defn install [_]
  (b/install {:basis @basis
              :lib lib
              :version version
              :jar-file jar-file
              :class-dir class-dir-release}))

(defn release [_]
  (deps-deploy/deploy {:installer :remote
                       :artifact (b/resolve-path jar-file)
                       :pom-file (b/pom-path {:lib lib
                                              :class-dir class-dir-release})}))
