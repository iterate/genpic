(ns iterate.genpic 
  (:require [babashka.cli :as cli]
            [iterate.prompt :as prompt]))

prompt/config

(defn branch-create [opts]
  (println "branch create"))

(defn branch-delete [opts]
  (println "Branch delete!!!!"))

(defn complete [opts]
  (println "Complete!!!"))

(defn ask [opts]
  (let [prompt (get-in opts [:opts :prompt])]
    (prompt/gpt-cli-ask prompt))
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
