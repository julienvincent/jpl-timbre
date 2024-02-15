(ns io.julienvincent.jpl-timbre.logger-finder
  (:gen-class
   :name io.julienvincent.jpl_timbre.LoggerFinder
   :extends System$LoggerFinder))

(defn -getLogger [_ name _]
  (io.julienvincent.jpl_timbre.Logger. name))
