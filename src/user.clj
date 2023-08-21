(ns user)

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn clerk-start!
  "Start Clerk og lytt etter filendringer
   
   Du må selv sette opp en tastatursvarvei for å evaluere i Clerk."
  []
  (let [clerk-serve (requiring-resolve 'nextjournal.clerk/serve!)
        clerk-port 7053]
    (clerk-serve {:browse? true :port clerk-port})))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn clerk-start-watch!
  "Start Clerk og lytt etter filendringer
   
   Clerk åpner filen du endret sist automatisk."
  []
  (let [clerk-serve (requiring-resolve 'nextjournal.clerk/serve!)
        clerk-port 7053]
    (clerk-serve {:browse? true
                  :port clerk-port
                  :watch-paths ["src"]})))
