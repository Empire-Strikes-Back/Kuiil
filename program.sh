#!/bin/bash

repl(){
  clj \
    -J-Dclojure.core.async.pool-size=1 \
    -X:repl Ripley.core/process \
    :main-ns Get-To-The-Ship.main
}

main(){
  clojure \
    -J-Dclojure.core.async.pool-size=1 \
    -M -m Get-To-The-Ship.main
}

uberjar(){

  clojure \
    -X:identicon Zazu.core/process \
    :word '"Get-To-The-Ship"' \
    :filename '"out/identicon/icon.png"' \
    :size 256

  clojure \
    -X:uberjar Genie.core/process \
    :main-ns Get-To-The-Ship.main \
    :filename '"out/Get-To-The-Ship.jar"' \
    :paths '["src" "out/identicon"]'
}

release(){
  uberjar
}

"$@"