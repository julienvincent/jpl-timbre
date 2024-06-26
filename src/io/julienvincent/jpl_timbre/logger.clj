(ns io.julienvincent.jpl-timbre.logger
  (:require
   [taoensso.timbre :as timbre])
  (:import
   [java.lang System$Logger$Level])
  (:gen-class
   :name io.julienvincent.jpl_timbre.Logger
   :implements [java.lang.System$Logger]
   :constructors {[String] []}
   :state state
   :init init))

(set! *warn-on-reflection* true)

(defn -init [name]
  [[] {:name name}])

(def ^:private system-level->timbre-level
  {System$Logger$Level/ALL :trace
   System$Logger$Level/TRACE :trace
   System$Logger$Level/DEBUG :debug
   System$Logger$Level/INFO :info
   System$Logger$Level/WARNING :warn
   System$Logger$Level/ERROR :error})

(defn -getName [^io.julienvincent.jpl_timbre.Logger this]
  (:name (.state this)))

(defn -isLoggable [^io.julienvincent.jpl_timbre.Logger this level]
  (let [name (-getName this)]
    (boolean
     (when-not (or (re-matches #"java\.lang\..*" name)
                   (re-matches #"jdk\..*" name))
       (timbre/may-log? (system-level->timbre-level level) name)))))

(defn -log [^io.julienvincent.jpl_timbre.Logger this & [level & args]]
  (timbre/log! (system-level->timbre-level level)
               :p args
               {:?ns-str (-getName this)}))
