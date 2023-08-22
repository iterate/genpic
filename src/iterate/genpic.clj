(ns iterate.genpic 
  (:require [babashka.cli :as cli]
            [iterate.genpic.prompt2 :as prompt]))


(defn branch-create [opts]
  (println "branch create"))

(defn branch-delete [opts]
  (println "Branch delete!!!!"))

(defn complete [opts]
  (println "Complete!!!"))

(defn ask [opts]
  (let [prompt (get-in opts [:opts :prompt])]
    (println (prompt/gpt-ask prompt (System/getenv "GENPIC_OPENAI_API_KEY")))
    )
  )

(defn help [opts]
  (println "Nyttig hjelpetekst her ..."))

(def subcommands [{:cmds ["branch" "create"] :fn branch-create}
                  {:cmds ["branch" "delete"] :fn branch-delete}
                  {:cmds ["complete"] :fn complete :args->opts [:prompt]}
                  {:cmds ["ask"] :fn ask :args->opts [:prompt]}
                  {:cmds [] :fn help}])

(defn -main [& args]
  (cli/dispatch subcommands args))
