#!/bin/bash

repl(){
  clj \
    -J-Dclojure.core.async.pool-size=1 \
    -X:repl Ripley.core/process \
    :main-ns Kuiil.main
}

main(){
  clojure \
    -J-Dclojure.core.async.pool-size=1 \
    -M -m Kuiil.main
}

uberjar(){

  clojure \
    -X:identicon Zazu.core/process \
    :word '"Kuiil"' \
    :filename '"out/identicon/icon.png"' \
    :size 256

  clojure \
    -X:uberjar Genie.core/process \
    :main-ns Get-To-The-Ship.main \
    :filename '"out/Kuiil.jar"' \
    :paths '["src" "out/identicon"]'
}

release(){
  uberjar
}

"$@"