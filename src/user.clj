(ns user)

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn clerk-start! []
  (let [clerk-serve (requiring-resolve 'nextjournal.clerk/serve!)
        clerk-port 7053]
    (clerk-serve {:browse? true :port clerk-port})))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn clerk-start-watch! []
  (let [clerk-serve (requiring-resolve 'nextjournal.clerk/serve!)
        clerk-port 7053]
    (clerk-serve {:browse? true
                  :port clerk-port
                  :watch-paths ["src"]})))
