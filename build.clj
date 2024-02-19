(ns build
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.tools.build.api :as b]
   [deps-deploy.deps-deploy :as deps-deploy]))

(def basis (delay (b/create-basis {})))

(def lib 'io.julienvincent/jpl-timbre)
(def version (str/replace (or (System/getenv "VERSION") "0.0.0") #"v" ""))
(def class-dir "target/classes")
(def jar-file "target/lib.jar")

(defn clean [_]
  (b/delete {:path "classes"})
  (b/delete {:path "target"}))

(defn prep [_]
  (.mkdir (io/file "classes"))

  (b/compile-clj {:basis @basis
                  :class-dir "classes"
                  :ns-compile ['io.julienvincent.jpl-timbre.logger
                               'io.julienvincent.jpl-timbre.logger-finder]
                  :filter-nses ['io.julienvincent]})

  (b/javac {:src-dirs ["src" "classes"]
            :class-dir "classes"
            :basis @basis
            :javac-opts ["-Xlint:-options"]}))

(defn build [_]
  (clean nil)

  (b/compile-clj {:basis @basis
                  :class-dir class-dir
                  :ns-compile ['io.julienvincent.jpl-timbre.logger
                               'io.julienvincent.jpl-timbre.logger-finder]
                  :filter-nses ['io.julienvincent]})

  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})

  (b/javac {:src-dirs [class-dir]
            :class-dir class-dir
            :basis @basis
            :javac-opts ["-Xlint:-options"]})

  (b/write-pom {:class-dir class-dir
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

  (b/jar {:class-dir class-dir
          :jar-file jar-file}))

(defn install [_]
  (b/install {:basis @basis
              :lib lib
              :version version
              :jar-file jar-file
              :class-dir class-dir}))

(defn release [_]
  (deps-deploy/deploy {:installer :remote
                       :artifact (b/resolve-path jar-file)
                       :pom-file (b/pom-path {:lib lib
                                              :class-dir class-dir})}))
