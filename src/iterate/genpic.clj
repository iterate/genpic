(ns iterate.genpic 
  (:require [babashka.cli :as cli]
            [iterate.genpic.prompt2 :as prompt]))

(defn ask [opts]
  (let [prompt (get-in opts [:opts :prompt])]
    (println (prompt/gpt-ask prompt (System/getenv "GENPIC_OPENAI_API_KEY")))
    )
  )

(defn generate [opts]
  (prn opts)
  )

(defn help [opts]
  (println "Nyttig hjelpetekst her ..."))

(def subcommands [
                  {:cmds ["ask"] :fn ask :args->opts [:prompt]}
                  {:cmds ["generate"] :fn generate :args->opts [:prompt]}
                  {:cmds [] :fn help}])

(defn -main [& args]
  (cli/dispatch subcommands args))
 