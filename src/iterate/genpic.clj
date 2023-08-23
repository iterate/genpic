(ns iterate.genpic 
  (:require [babashka.cli :as cli]
            [iterate.genpic.prompt2 :as prompt]
            [iterate.genpic.create-image :as create-image]))

(defn ask [opts]
  (let [prompt (get-in opts [:opts :prompt])]
    (println (prompt/gpt-ask prompt (System/getenv "GENPIC_OPENAI_API_KEY")))
    )
  )

(defn generate [opts]
  (let [resp (create-image/generate (get-in opts [:opts :prompt]) (create-image/openai-key-from-env))]
    (println "\n")
    (doseq [url resp]
      (println (str url "\n" "\n")))
    ) 
  )
(defn help [opts]
  (println "Nyttig hjelpetekst her ..."))

(def subcommands [
                  {:cmds ["ask"] :fn ask :args->opts [:prompt]}
                  {:cmds ["generate"] :fn generate :args->opts [:prompt]}
                  {:cmds [] :fn help}])

(defn -main [& args]
  (cli/dispatch subcommands args))
 